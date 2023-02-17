package com.example.kursovaya_3.Model;

public enum ColorSocks {
    YELLOW(" жёлтые."),
    GREEN(" зелёные."),
    BLUE(" синие."),
    BROWN(" коричневые."),
    WHITE(" белые."),
    RED(" красные."),
    ORANGE(" оранжевые."),
    PINK(" розовые."),
    GRAY(" серые."),
    BLACK(" чёрные.");
    String color;

    ColorSocks(String color) {
        this.color = color;
    }

}
