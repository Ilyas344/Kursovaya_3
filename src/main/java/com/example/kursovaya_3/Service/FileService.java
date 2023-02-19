package com.example.kursovaya_3.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {


        boolean saveFile(String json,String name);
        String readFile();
        String readFile2();
        File getFromFile(String name);
        boolean clearFile(String name);

}

