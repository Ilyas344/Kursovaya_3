package com.example.kursovaya_3.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключения в случае не корректных данных
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SocksException extends RuntimeException {

}
