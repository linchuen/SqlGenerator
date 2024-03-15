package com.cooba;

import java.util.HashMap;
import java.util.Map;

public class Common {
    private static final Map<String, String> cacheMap = new HashMap<>();

    public static String camelToSnake(String str) {
        String cache = cacheMap.get(str);
        if (cache != null) return cache;

        StringBuilder result = new StringBuilder();
        char c = str.charAt(0);
        result.append(Character.toLowerCase(c));

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        String resultString = result.toString();
        cacheMap.put(str, resultString);
        return resultString;
    }
}