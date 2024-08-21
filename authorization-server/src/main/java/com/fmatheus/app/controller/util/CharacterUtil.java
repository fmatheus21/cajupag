package com.fmatheus.app.controller.util;


import java.util.Objects;

public class CharacterUtil extends CapitalizeUtil {

    private CharacterUtil() {
        super();
    }

    public static String removeSpecialCharacters(String value) {
        return Objects.nonNull(value) ? value.replaceAll("[^a-zA-Z0-9]", "") : null;
    }

    public static String maskCardNumber(String cardNumber) {
        var cardNumberReplace = cardNumber.replaceAll("\\D", "");

        if (cardNumberReplace.length() != 16) {
            throw new IllegalArgumentException("O número do cartão deve ter 16 dígitos.");
        }

        var lastDigits = cardNumberReplace.substring(12);
        var digits = "*".repeat(12);

        return formatCard(digits + lastDigits);
    }

    private static String formatCard(String texto) {
        var size = 4;
        var formatado = new StringBuilder();

        for (var i = 0; i < texto.length(); i += size) {
            formatado.append(texto, i, Math.min(i + size, texto.length()));

            if (i + size < texto.length()) {
                formatado.append(" ");
            }
        }

        return formatado.toString();
    }


}
