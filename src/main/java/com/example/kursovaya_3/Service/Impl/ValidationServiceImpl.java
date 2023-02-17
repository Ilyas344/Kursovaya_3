package com.example.kursovaya_3.Service.Impl;

import com.example.kursovaya_3.Model.Socks;
import com.example.kursovaya_3.Service.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Map;


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

    @Override
    public boolean isPresence(Socks socks, Map<String, Socks> map) {

        return !map.containsKey(SocksServiceImpl.key(socks));

    }

    @Override
    public boolean isStockAvailability(Socks socks, Map<String, Socks> map) {
        return ((map.get(SocksServiceImpl.key(socks)).getQuantity() - socks.getQuantity()) >= 0);
    }


}
