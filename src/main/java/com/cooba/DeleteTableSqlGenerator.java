package com.cooba;


public class DeleteTableSqlGenerator {

    public static void generateSql(Class<?> clazz) {
        generateSql("Entity", clazz);
    }

    public static void generateSql(String tableSuffix, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("DELETE FROM ");
        String tableName = Common.getTableName(tableSuffix, clazz);
        stringBuilder.append(tableName).append("\n");

        String condition = Common.getWhereBlock(clazz);
        stringBuilder.append(condition);

        System.out.println(stringBuilder);
    }

    public static void main(String[] args) {
        generateSql(TestEntity.class);
    }

}
