package com.biricik.autogenerator.core.concretes;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;
import com.biricik.autogenerator.util.Utils;

import org.springframework.stereotype.Component;

@Component
public class InsertSQLGenerator implements SQLGenratorService {

    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder insertMethod = new StringBuilder();

        String insertSql = createInsertSql(tableName, fields);
        String preparedStatement = createPreparedStatement(fields);

        insertMethod
                .append(String.format(
                        "    public void save(Connection  con) throws SQLException {\nString sql = \"%s\";\n\n%s}",
                        insertSql, preparedStatement));

        return insertMethod.toString();
    }

    private static String createInsertSql(String tableName, String[] filed) {

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" ( ");

        for (int i = 0; i < filed.length; i++) {
            String sqlfieldName = filed[i].split(":")[1];
            sql.append(sqlfieldName);
            if (i < filed.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" ) ");
        sql.append(" VALUES ( ");
        for (int i = 0; i < filed.length; i++) {
            sql.append("?");
            if (i < filed.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" ) ");

        return sql.toString();
    }

    private static String createPreparedStatement(String[] fields) {
        StringBuilder preparedStatement = new StringBuilder("PreparedStatement stmt = null; \n");
        preparedStatement.append("try { \n");
        preparedStatement.append("stmt = con.prepareStatement(sql);\n ");
        preparedStatement.append("int idx = 1;");

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].split(":")[0];
            String fieldType = fields[i].split(":")[2];

            fieldName = Utils.getField(fieldName);

            preparedStatement.append(String.format(Utils.stmtSetFieldType(fieldType), fieldName));
        }

        preparedStatement.append("stmt.executeUpdate(); \n");
        preparedStatement.append(" } \n");
        preparedStatement.append(" finally { ");
        preparedStatement.append(" DBUtils.closeAutoCloseble(stmt); } \n");
        preparedStatement.append("ResultSet rs = null; \n");
        preparedStatement.append("try { \n");
        preparedStatement.append(
                String.format("stmt = con.prepareStatement(\"%s\"\n);", "SELECT LAST_INSERT_ID() AS last_inst_id"));
        preparedStatement.append("rs = stmt.executeQuery();\n");
        preparedStatement.append("rs.next();\n");
        preparedStatement.append(String.format("setId(rs.getInt(\"%s\"\n));", "last_inst_id"));
        preparedStatement.append(" } \n");
        preparedStatement.append(" finally { \n");
        preparedStatement.append("DBUtils.closeAutoCloseble(rs);\n DBUtils.closeAutoCloseble(stmt);");
        preparedStatement.append(" } \n");

        return preparedStatement.toString();

    }

}
