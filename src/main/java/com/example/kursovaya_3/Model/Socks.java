package com.example.kursovaya_3.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socks {
    private ColorSocks colorSocks; //цвет
    private SizeSocks sizeSocks; //размер
    private int composition; //состав
    private int quantity;//количество на складе

    @Override
    public String toString() {
        return "Носки" + sizeSocks +
                colorSocks +
                ", содержание хлопка:" + composition + "%" +
                ", количество на складе:" + quantity +
                " пар";
    }
}
