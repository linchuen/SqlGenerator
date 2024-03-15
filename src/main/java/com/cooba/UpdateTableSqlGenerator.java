package com.cooba;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ");

        String name = clazz.getSimpleName().replace(tableSuffix, "");
        String tableName = Common.camelToSnake(name);
        stringBuilder.append(tableName).append("\n");

        Field[] fields = clazz.getDeclaredFields();
        List<Field> validFields = Arrays.stream(fields)
                .filter(field -> !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());

        stringBuilder.append("SET \n");
        String value = validFields.stream().map(field -> {
            String fieldName = field.getName();
            String columnName = Common.camelToSnake(fieldName);
            return columnName + " = ${" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "", "\n"));
        stringBuilder.append(value);

        stringBuilder.append("WHERE \n");
        stringBuilder.append(value);
        System.out.println(stringBuilder);
    }
}
