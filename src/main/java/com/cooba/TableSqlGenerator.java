package com.cooba;


public class TableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        CreateTableSqlGenerator.generateSql(tableSuffix,clazz);
        InsertTableSqlGenerator.generateSql(tableSuffix,clazz);
        UpdateTableSqlGenerator.generateSql(tableSuffix,clazz);
        DeleteTableSqlGenerator.generateSql(tableSuffix,clazz);
        SelectTableSqlGenerator.generateSql(tableSuffix,clazz);
    }
}
