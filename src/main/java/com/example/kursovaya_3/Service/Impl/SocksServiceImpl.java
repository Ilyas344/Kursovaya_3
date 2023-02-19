package com.example.kursovaya_3.Service.Impl;

import com.example.kursovaya_3.Exception.SocksException;
import com.example.kursovaya_3.Model.ColorSocks;
import com.example.kursovaya_3.Model.SizeSocks;
import com.example.kursovaya_3.Model.Socks;
import com.example.kursovaya_3.Service.FileService;
import com.example.kursovaya_3.Service.SocksService;
import com.example.kursovaya_3.Service.ValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class SocksServiceImpl implements SocksService {
    private static Map<String, Socks> socksMap = new TreeMap<>();
    private final ValidationService validationService;
    private final FileService fileService;
    private static long idSocks = 1;
    @Value("${name.of.file}")
    private String socksFileName;
    @Value("${name.of.file.history}")
    private String socksFileNameHistory;

    private static Map<Long, String> socksMapHistory = new TreeMap<>();


    public SocksServiceImpl(ValidationService validationService, FileService fileService) {
        this.validationService = validationService;
        this.fileService = fileService;
    }

    /**
     * Создание ключа для мапы
     */
    public static String key(Socks socks) {
        return socks.getSizeSocks() + " "
                + socks.getColorSocks() + " "
                + socks.getComposition() + "%";
    }

    /**
     * Добавление записи в мапу,
     * Если записи нет, объект в мапе создаётся
     * Если запись есть, добавляется количество носков
     */
    @Override
    public Socks addSocks(Socks socks) {
        String type = "приемка";
        String key = key(socks);
        if (!validationService.isComposition(socks.getComposition())
                && !validationService.isQuantity(socks.getQuantity())) {
            throw new SocksException();
        }
        if (validationService.isPresence(socks, socksMap)) {
            socksMap.put(key, socks);

        } else {
            socks.setQuantity(socksMap.get(key).getQuantity() + socks.getQuantity());
            socksMap.replace(key, socks);
        }
        saveSocksHistory(type, socks);
        saveFile();
        return socks;
    }

    /**
     * Выдача носков со склада
     * Если количество носков хватает, то из значения мапы вычитается количество из объекта
     * Если не хватает, устанавливается 0(все носки, что были выданы) по крайней мере так задумывалось
     */
    @Override
    public Socks giveSocks(Socks socks) {
        String type = "выдача";
        if (isStockAvailability(socks, socksMap)) {
            socks.setQuantity(socksMap.get(key(socks)).getQuantity() - socks.getQuantity());
            socksMap.replace(key(socks), socks);
        } else {
            socks.setQuantity(0);

            socksMap.replace(key(socks), socks);
        }
        saveSocksHistory(type, socks);
        saveFile();

        return socks;
    }

    /**
     * Проверка всех носков на складе
     *
     * @return выдача всех значений
     */
    @Override
    public Socks getSocks(ColorSocks colorSocks, SizeSocks sizeSocks, int minCotton, int maxCotton) {
        for (Socks socks : socksMap.values()) {
            if ((socks.getColorSocks().equals(colorSocks)) &&
                    (socks.getSizeSocks().equals(sizeSocks)) &&
                    (socks.getComposition() <= (maxCotton)) &&
                    (socks.getComposition() >= (minCotton))){
                return socks;
            }
        }

        return null;
    }

    /**
     * Удаление записи
     *
     * @param socks объект носки
     * @return удаление из мапы
     */
    @Override
    public Socks delete(Socks socks) {
        String type = "списание";
        if (!socksMap.containsKey(key(socks))) {
            throw new SocksException();
        }
        socksMap.remove(key(socks));
        saveSocksHistory(type, socks);
        saveFile();
        return socks;
    }

    private void saveFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMap);
            fileService.saveFile(json, socksFileName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    private void readFile() {
        try {
            String json = fileService.readFile();
            String json2 = fileService.readFile2();
            socksMap = new ObjectMapper().readValue(json, new TypeReference<TreeMap<String, Socks>>() {
            });
            socksMapHistory = new ObjectMapper().readValue(json2, new TypeReference<TreeMap<Long, String>>() {
            });
            socksMap = validationService.checkComposition(socksMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveSocksHistory(String text, Socks socks) {


        JsonObject rootObject = new JsonObject();
        Gson gson = new GsonBuilder().create();

        rootObject.addProperty("Тип", text);

        rootObject.addProperty("Дата и время:", LocalDateTime.now().toString());
        rootObject.addProperty("Количество носков", socks.getQuantity());
        rootObject.addProperty("Размер", socks.getSizeSocks().toString());
        rootObject.addProperty("Содержание хлопка", socks.getComposition());
        rootObject.addProperty("Цвет", socks.getColorSocks().toString());
        String json = gson.toJson(rootObject);
        socksMapHistory.put(idSocks++, json);
        saveFileHistory();
    }

    private void saveFileHistory() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMapHistory);
            fileService.saveFile(json, socksFileNameHistory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private boolean isStockAvailability(Socks socks, Map<String, Socks> map) {
        return ((map.get(key(socks)).getQuantity() - socks.getQuantity()) >= 0);
    }

}





