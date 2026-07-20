package com.util;

import java.time.Year;

public class IdGeneratorUtil {

    public static String generateId(String prefix, long count) {
        String year = String.valueOf(Year.now().getValue());
        String padded = String.format("%04d", count + 1);
        return prefix + year + padded;
    }

    public static String generateStudentId(long count) {
        return generateId("STU", count);
    }

    public static String generateTeacherId(long count) {
        return generateId("TEA", count);
    }

    public static String generateParentId(long count) {
        return generateId("PAR", count);
    }
}
