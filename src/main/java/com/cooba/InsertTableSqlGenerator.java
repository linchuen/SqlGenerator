package com.cooba;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class InsertTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("INSERT INTO ");
        String tableName = Common.getTableName(tableSuffix, clazz);
        stringBuilder.append(tableName).append("\n");

        List<Field> validFields = Common.getValidFields(clazz);
        String column = validFields.stream()
                .map(field -> Common.camelToSnake(field.getName()))
                .collect(Collectors.joining(",", "(", ")\n"));
        stringBuilder.append(column);

        String valueBlock = getValueBlock(clazz);
        stringBuilder.append(valueBlock);

        System.out.println(stringBuilder);
    }

    private static String getValueBlock(Class<?> clazz) {
        List<Field> validFields = Common.getValidFields(clazz);
        return validFields.stream().map(field -> {
            String fieldName = field.getName();
            return "#{" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "VALUES\n(\n", "\n)\n"));
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }
}
