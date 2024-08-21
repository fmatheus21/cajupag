package com.fmatheus.app.controller.util;


abstract class CapitalizeUtil {

    CapitalizeUtil() {
        throw new IllegalStateException(getClass().getName());
    }

    public static String capitalizeFully(final String str) {
        return capitalizeFully(str, (char[]) null);
    }

    public static String capitalizeFully(String str, final char... delimiters) {
        if (isNullOrEmpty(str) || isNullOrEmpty(delimiters)) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str, delimiters);
    }

    private static String capitalize(final String str, final char... delimiters) {
        if (isNullOrEmpty(str) || isNullOrEmpty(delimiters)) {
            return str;
        }

        final var buffer = str.toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }

        return String.valueOf(buffer);
    }

    private static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        return new String(delimiters).indexOf(ch) >= 0;
    }

    private static boolean isNullOrEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    private static boolean isNullOrEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

}