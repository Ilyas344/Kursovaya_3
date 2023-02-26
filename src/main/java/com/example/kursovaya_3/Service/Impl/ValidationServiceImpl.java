package com.example.kursovaya_3.Service.Impl;

import com.example.kursovaya_3.Model.Socks;
import com.example.kursovaya_3.Service.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

import static com.example.kursovaya_3.Service.Impl.SocksServiceImpl.key;


@Service
public class ValidationServiceImpl implements ValidationService {


    /**
     * Проверка наличия на складе и количество хлопка
     */
    @Override
    public boolean isQuantity(Integer checkQuantity) {
        return checkQuantity >= 0;
    }

    @Override
    public boolean isComposition(Integer checkComposition) {
        return checkComposition >= 0 && checkComposition <= 100;
    }

    /**
     * Проверка на соответсвие ключа мапы
     */
    @Override
    public boolean isPresence(Socks socks, Map<String, Socks> map) {

        return !map.containsKey(key(socks));

    }

    /**
     * Проверка Доступности на складе
     */



    /**
     * Проверка соответсвия мапы к ключу
     * Ключ соотвествовал записи
     */
    @Override
    public Map<String, Socks> checkComposition(Map<String, Socks> map) {
        Map<String, Socks> mapTemp = new TreeMap<>();
        for (Socks socks : map.values()) {
            mapTemp.put(key(socks), socks);
        }
        return mapTemp;
    }

}
