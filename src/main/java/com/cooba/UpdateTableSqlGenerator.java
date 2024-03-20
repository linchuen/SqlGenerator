package com.cooba;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("UPDATE ");
        String tableName = Common.getTableName(tableSuffix, clazz);
        stringBuilder.append(tableName).append("\n");

        String setBlock = getSetBlock(clazz);
        stringBuilder.append(setBlock);

        String whereBlock = Common.getWhereBlock(clazz);
        stringBuilder.append(whereBlock);

        System.out.println(stringBuilder);
    }

    private static String getSetBlock(Class<?> clazz) {
        List<Field> validFields = Common.getValidFields(clazz);
        return validFields.stream().map(field -> {
            String fieldName = field.getName();
            String columnName = Common.camelToSnake(fieldName);
            return columnName + " = #{" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "SET\n", "\n"));
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }
}
