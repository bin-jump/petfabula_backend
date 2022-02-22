package com.petfabula.domain.common.util;

public abstract class ValueUtil {

    public static String trimContent(String text) {
        if (text == null) {
            return text;
        }

        return text.trim();
    }
}
