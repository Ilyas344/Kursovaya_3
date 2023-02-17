package com.example.kursovaya_3.Exception;

/**
 * Исключения в случае не корректных данных
 */

public class SocksException extends RuntimeException {
    public SocksException(String error) {
        super("Произошла ошибка с " + error);
    }
}
