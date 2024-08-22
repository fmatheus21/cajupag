package com.fmatheus.app.controller.exception.handler;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fmatheus.app.controller.enumerable.MessagesEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponseHandler {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int statusCode;
    private String statusDescription;
    private String cause;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldsResponseHandler> fields;


    public MessageResponseHandler(MessagesEnum messagesEnum, String cause, String message) {
        this.statusCode = messagesEnum.getHttpSttus().value();
        this.statusDescription = messagesEnum.getHttpSttus().getReasonPhrase();
        this.cause = cause;
        this.message = message;
    }

}