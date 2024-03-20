package com.cooba;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static String getTableName(String tableSuffix, Class<?> clazz) {
        String name = clazz.getSimpleName().replace(tableSuffix, "");
        return Common.camelToSnake(name);
    }

    public static List<Field> getValidFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    public static String getWhereBlock(Class<?> clazz) {
        List<Field> validFields = getValidFields(clazz);

        return validFields.stream().map(field -> {
            String fieldName = field.getName();
            String columnName = Common.camelToSnake(fieldName);
            return columnName + " = #{" + fieldName + "}";
        }).collect(Collectors.joining(" and\n", "WHERE\n", "\n"));
    }
}