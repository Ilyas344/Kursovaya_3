package com.example.kursovaya_3.Service;

import com.example.kursovaya_3.Model.ColorSocks;
import com.example.kursovaya_3.Model.SizeSocks;
import com.example.kursovaya_3.Model.Socks;

import java.util.Map;

public interface SocksService {


    Socks addSocks(Socks socks);

    Socks giveSocks(Socks socks);


    public Map<String, Socks> getSocks();

    Socks delete(Socks socks);
}
