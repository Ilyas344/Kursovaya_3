package com.example.kursovaya_3.Service.Impl;

import com.example.kursovaya_3.Service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Value("${path.to.file}")
    private String filePath;
    @Value("${name.of.file}")
    private String socksFileName;
    @Value("${name.of.file.history}")
    private String socksFileNameHistory;

    @Override
    public boolean saveFile(String json,String name) {
        try {
            clearFile(name);
            Files.writeString(Path.of(filePath,name), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readFile() {
        try {
            return Files.readString(Path.of(filePath,socksFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String readFile2() {
        try {
            return Files.readString(Path.of(filePath,socksFileNameHistory));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public File getFromFile(String name) {
        return new File(filePath + "/" + name);
    }



    @Override
    public boolean clearFile(String name) {
        try {
            Files.deleteIfExists(Path.of(filePath, name));
            Files.createFile(Path.of(filePath, name));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}


