package com.cooba;


import com.cooba.annotation.Comment;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE `");

        String name = clazz.getSimpleName().replace(tableSuffix, "");
        stringBuilder.append(Common.camelToSnake(name));
        stringBuilder.append("` (\n");

        Field[] fields = clazz.getDeclaredFields();
        String column = Arrays.stream(fields).map(field -> {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) return "";

            String snake = "`" + Common.camelToSnake(field.getName()) + "`";
            String javaType = field.getType().getName();
            boolean isPk = snake.equals("`id`");
            String columnInfo = createColumn(javaType, isPk);

            Comment comment = field.getAnnotation(Comment.class);
            if (comment != null) {
                String description = comment.description();
                return snake + " " + columnInfo + " " + "COMMENT '" + description + "',\n";
            }
            return snake + " " + columnInfo + ",\n";
        }).collect(Collectors.joining());
        stringBuilder.append(column);

        stringBuilder.append("PRIMARY KEY (`id`)");
        String uniqueKeyName = getUniqueKeyName(fields);
        if (!uniqueKeyName.isBlank()) {
            stringBuilder.append(",\n UNIQUE KEY `uk` (").append(uniqueKeyName).append(")");
        }
        stringBuilder.append("\n);\n");

        System.out.println(stringBuilder);
    }

    private static String createColumn(String javaType, boolean isPk) {
        switch (javaType) {
            case "java.lang.Long":
                return isPk ? "bigint(20) NOT NULL AUTO_INCREMENT" : "bigint(20) DEFAULT NULL";
            case "long":
                return "bigint(20) NOT NULL";
            case "java.lang.Integer":
                return "int(11) DEFAULT NULL";
            case "int":
                return "int(11) NOT NULL";
            case "java.math.BigDecimal":
                return "decimal(28,16) DEFAULT NULL";
            case "java.lang.Boolean":
                return "tinyint(1) DEFAULT NULL";
            case "boolean":
                return "tinyint(1) DEFAULT '0'";
            case "java.lang.String":
                return "varchar(50) DEFAULT NULL";
            case "java.util.Date":
            case "java.time.LocalDateTime":
                return "datetime DEFAULT NULL";
            default:
                return "";
        }
    }

    private static String getUniqueKeyName(Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.getAnnotation(Comment.class) != null)
                .filter(field -> field.getAnnotation(Comment.class).isUniqueKey())
                .map(field -> "`" + Common.camelToSnake(field.getName()) + "`")
                .collect(Collectors.joining(","));
    }
}
