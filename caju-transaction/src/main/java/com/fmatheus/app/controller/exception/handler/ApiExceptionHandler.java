package com.fmatheus.app.controller.exception.handler;


import com.fmatheus.app.controller.enumerable.MessagesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private String message;

    private String cause;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var erros = this.createErros(ex);
        return handleExceptionInternal(ex, erros, headers, status, request);
    }


    private MessageResponseHandler createErros(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        this.cause = "MethodArgumentNotValidException";
        var messagesEnum = MessagesEnum.ERROR_INVALID_ARGUMENTS;
        var newMessage = this.messageSource.getMessage(messagesEnum.getMessage(), null, LocaleContextHolder.getLocale());
        var messegeResponse = new MessageResponseHandler(messagesEnum, this.cause, newMessage);
        List<FieldsResponseHandler> fields = new ArrayList<>();
        result.getFieldErrors().forEach(fieldErro -> {
            this.message = this.messageSource.getMessage(fieldErro, LocaleContextHolder.getLocale());
            var field = FieldsResponseHandler.builder()
                    .field(fieldErro.getField())
                    .message(this.message)
                    .build();
            fields.add(field);
        });
        messegeResponse.setFields(fields);
        return messegeResponse;
    }


}