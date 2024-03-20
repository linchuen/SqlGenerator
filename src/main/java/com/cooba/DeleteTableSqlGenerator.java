package com.cooba;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ");

        String name = clazz.getSimpleName().replace(tableSuffix, "");
        String tableName = Common.camelToSnake(name);
        stringBuilder.append(tableName).append("\n");

        String condition = Common.getWhereBlock(clazz);
        stringBuilder.append(condition);

        System.out.println(stringBuilder);
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }

}
