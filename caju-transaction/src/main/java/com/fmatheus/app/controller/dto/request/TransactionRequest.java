package com.fmatheus.app.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @NotBlank
    private String mcc;

    @NotBlank
    private String merchant;
}
