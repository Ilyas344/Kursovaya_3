package com.example.kursovaya_3.Service;

import com.example.kursovaya_3.Model.ColorSocks;
import com.example.kursovaya_3.Model.SizeSocks;
import com.example.kursovaya_3.Model.Socks;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public interface SocksService {


    Socks addSocks(Socks socks);

    Socks giveSocks(Socks socks);


    Socks getSocks(ColorSocks colorSocks, SizeSocks sizeSocks, int minCotton, int maxCotton);

    Socks delete(Socks socks);

    void saveSocksHistory(String text, Socks socks);
}
