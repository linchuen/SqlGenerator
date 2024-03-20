package com.cooba;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class SelectTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();

        String selectBlock = getSelectBlock(clazz);
        stringBuilder.append(selectBlock);

        stringBuilder.append("FROM ");
        String tableName = Common.getTableName(tableSuffix, clazz);
        stringBuilder.append(tableName).append("\n");

        String whereBlock = Common.getWhereBlock(clazz);
        stringBuilder.append(whereBlock);

        System.out.println(stringBuilder);
    }

    private static String getSelectBlock(Class<?> clazz) {
        List<Field> validFields = Common.getValidFields(clazz);

        return validFields.stream()
                .map(field -> Common.camelToSnake(field.getName()))
                .collect(Collectors.joining(", ", "SELECT ", "\n"));
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }
}
