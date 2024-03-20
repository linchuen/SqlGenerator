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

        String name = clazz.getSimpleName().replace(tableSuffix, "");
        String tableName = Common.camelToSnake(name);
        stringBuilder.append(tableName).append("\n");

        List<Field> validFields = Common.getValidFields(clazz);

        String value = validFields.stream().map(field -> {
            String fieldName = field.getName();
            String columnName = Common.camelToSnake(fieldName);
            return columnName + " = #{" + fieldName + "}";
        }).collect(Collectors.joining(",\n", "SET\n", "\n"));
        stringBuilder.append(value);

        String condition = Common.getWhereBlock(clazz);
        stringBuilder.append(condition);
        System.out.println(stringBuilder);
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }
}
