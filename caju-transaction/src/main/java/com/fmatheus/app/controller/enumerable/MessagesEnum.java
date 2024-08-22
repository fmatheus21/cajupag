package com.fmatheus.app.controller.enumerable;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MessagesEnum {
    ERROR_INVALID_ARGUMENTS(HttpStatus.BAD_REQUEST, "message.error.invalid-arguments");


    private final HttpStatus httpSttus;
    private final String message;

    MessagesEnum(HttpStatus httpSttus, String message) {
        this.httpSttus = httpSttus;
        this.message = message;
    }

}
