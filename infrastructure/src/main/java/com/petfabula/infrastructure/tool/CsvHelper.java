package com.petfabula.infrastructure.tool;

public class CsvHelper {

    public static String cleanCell(String val) {
        val = val.trim();
        if (val.startsWith("\"") && val.endsWith("\"")) {
            return val.substring(1, val.length() - 1);
        }
        return val;
    }
}
