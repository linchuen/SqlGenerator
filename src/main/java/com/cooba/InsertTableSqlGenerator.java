package com.cooba;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InsertTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");

        String name = clazz.getSimpleName().replace(tableSuffix, "");
        stringBuilder.append(Common.camelToSnake(name));

        Field[] fields = clazz.getDeclaredFields();
        List<Field> validFields = Arrays.stream(fields)
                .filter(field -> !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());

        String column = validFields.stream()
                .map(field -> Common.camelToSnake(field.getName()))
                .collect(Collectors.joining(",", "\n(", ")\n"));
        stringBuilder.append(column);

        stringBuilder.append("VALUES");
        String value = validFields.stream().map(field -> {
            String fieldName = field.getName();
            String columnName = Common.camelToSnake(fieldName);
            return columnName + " = ${" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "\n(\n", "\n)\n"));
        stringBuilder.append(value);
        System.out.println(stringBuilder);
    }
}
