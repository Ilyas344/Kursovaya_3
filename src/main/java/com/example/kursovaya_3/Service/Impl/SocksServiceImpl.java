package com.example.kursovaya_3.Service.Impl;

import com.example.kursovaya_3.Exception.SocksException;
import com.example.kursovaya_3.Model.ColorSocks;
import com.example.kursovaya_3.Model.SizeSocks;
import com.example.kursovaya_3.Model.Socks;
import com.example.kursovaya_3.Service.SocksService;
import com.example.kursovaya_3.Service.ValidationService;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.TreeMap;


@Service
public class SocksServiceImpl implements SocksService {
    private static final Map<String, Socks> SOCKS_MAP = new TreeMap<>();
    private final ValidationService validationService;


    public SocksServiceImpl(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Создание ключа для мапы
     */
    public static String key(Socks socks) {
        return socks.getSizeSocks().toString() + " "
                + socks.getColorSocks().toString() + " "
                + socks.getComposition();
    }

    /**
     * Добавление записи в мапу,
     * Если записи нет, объект в мапе создаётся
     * Если запись есть, добавляется количество носков
     */
    @Override
    public Socks addSocks(Socks socks) {
        String key = key(socks);
        if (!validationService.isComposition(socks.getComposition())
                && !validationService.isQuantity(socks.getQuantity())) {
            throw new SocksException(socks.toString());
        }
        if (validationService.isPresence(socks, SOCKS_MAP)) {
            SOCKS_MAP.put(key, socks);

        } else {
            socks.setQuantity(SOCKS_MAP.get(key).getQuantity() + socks.getQuantity());
            SOCKS_MAP.replace(key, socks);
        }
        return socks;
    }

    /**
     * Выдача носков со склада
     * Если количество носков хватает, то из значения мапы вычитается количество из объекта
     * Если не хватает, устанавливается 0(все носки, что были выданы) по крайней мере так задумывалось
     */
    @Override
    public Socks giveSocks(Socks socks) {
        if (validationService.isStockAvailability(socks, SOCKS_MAP)) {
            socks.setQuantity(SOCKS_MAP.get(key(socks)).getQuantity() - socks.getQuantity());
            SOCKS_MAP.replace(key(socks), socks);
        } else {
            socks.setQuantity(0);

            SOCKS_MAP.replace(key(socks), socks);
        }
        return socks;
    }

    /**
     * Проверка всех носков на складе
     *
     * @return выдача всех значений
     */
    @Override
    public Map<String, Socks> getSocks() {
        return SOCKS_MAP;
    }

    /**
     * Удаление записи
     *
     * @param socks объект носки
     * @return удаление из мапы
     */
    @Override
    public Socks delete(Socks socks) {
        if (!SOCKS_MAP.containsKey(key(socks))) {
            throw new RuntimeException();
        }
        return SOCKS_MAP.remove(key(socks));
    }
}





