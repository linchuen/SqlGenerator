package com.cooba;


import lombok.Builder;
import lombok.Data;

import java.util.stream.Collectors;

public class CreateTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");

        String tableName = Common.getTableName(tableSuffix, clazz);
        stringBuilder.append("`");
        stringBuilder.append(tableName);
        stringBuilder.append("` (\n");

        String columnBlock = getColumnBlock(clazz);
        stringBuilder.append(columnBlock);

        stringBuilder.append("\nPRIMARY KEY (`id`)");
        stringBuilder.append("\n);\n");

        System.out.println(stringBuilder);
    }

    private static String createColumn(Column column) {
        int length = column.length == null ? 50 : column.length;
        int precision = column.precision == null ? 28 : column.precision;
        int scale = column.scale == null ? 16 : column.scale;

        switch (column.getJavaType()) {
            case "java.lang.Long":
                return column.isPrimaryKey()
                        ? column.name + " bigint NOT NULL AUTO_INCREMENT"
                        : column.name + " bigint DEFAULT NULL";
            case "long":
                return column.name + " bigint NOT NULL";
            case "java.lang.Integer":
                return column.name + " int DEFAULT NULL";
            case "int":
                return column.name + " int NOT NULL";
            case "java.math.BigDecimal":
                return column.name + " decimal(" + precision + "," + scale + ") DEFAULT NULL";
            case "java.lang.Boolean":
                return column.name + " tinyint DEFAULT NULL";
            case "boolean":
                return column.name + " tinyint DEFAULT '0'";
            case "java.lang.String":
                return column.name + " varchar(" + length + ") DEFAULT NULL";
            case "java.util.Date":
            case "java.time.LocalDateTime":
                return column.name + " datetime DEFAULT NULL";
            default:
                return "";
        }
    }

    private static String getColumnBlock(Class<?> clazz) {
        return Common.getValidFields(clazz).stream().map(field -> {
            String fieldName = "`" + Common.camelToSnake(field.getName()) + "`";
            Column column = Column.builder()
                    .name(fieldName)
                    .javaType(field.getType().getName())
                    .isPrimaryKey(fieldName.equals("`id`"))
                    .build();
            return createColumn(column);
        }).collect(Collectors.joining(",\n"));
    }

    @Data
    @Builder
    private static class Column {
        private String name;
        private String javaType;
        private boolean isPrimaryKey;
        private Integer length;
        private Integer precision;
        private Integer scale;
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }

}
