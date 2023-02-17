package com.example.kursovaya_3.Service;

import com.example.kursovaya_3.Model.Socks;

import java.util.Map;

public interface ValidationService {
    boolean isQuantity(Integer checkQuantity);

    boolean isComposition(Integer checkComposition);

    boolean isPresence(Socks socks, Map<String, Socks> map);

    boolean isStockAvailability(Socks socks, Map<String, Socks> map);
}
