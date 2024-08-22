package com.fmatheus.app.controller.exception.handler;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldsResponseHandler {

    private String field;
    private String message;
}