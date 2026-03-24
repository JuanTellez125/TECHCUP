package edu.dosw.TECHCUP.core.util;

import java.util.UUID;

public class IdGeneratorUtil {

    private IdGeneratorUtil() {}

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

}