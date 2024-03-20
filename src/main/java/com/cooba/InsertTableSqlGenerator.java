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
        String tableName = Common.camelToSnake(name);
        stringBuilder.append(tableName).append("\n");

        List<Field> validFields = Common.getValidFields(clazz);

        String column = validFields.stream()
                .map(field -> Common.camelToSnake(field.getName()))
                .collect(Collectors.joining(",", "(", ")\n"));
        stringBuilder.append(column);

        stringBuilder.append("VALUES");
        String value = validFields.stream().map(field -> {
            String fieldName = field.getName();
            return "#{" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "\n(\n", "\n)\n"));
        stringBuilder.append(value);
        System.out.println(stringBuilder);
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }
}
