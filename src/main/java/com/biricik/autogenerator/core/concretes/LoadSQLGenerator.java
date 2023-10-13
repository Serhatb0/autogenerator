package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;
import com.biricik.autogenerator.util.Utils;

@Component
public class LoadSQLGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder loadBuilder = new StringBuilder();

        String loadSql = generateLoadSql(tableName, fields, tablePrimaryKey);

        loadBuilder
                .append(String.format(
                        "    public void load(Connection  con) throws SQLException {\n%s\n}",
                        loadSql));

        return loadBuilder.toString();
    }

    public String generateLoadSql(String tableName, String[] fields, String tablePrimaryKey) {

        StringBuilder loadSqlBuilder = new StringBuilder();
        loadSqlBuilder.append("PreparedStatement stmt = null;\n");
        loadSqlBuilder.append("ResultSet rs = null;\n");
        loadSqlBuilder.append(String.format("try {%s}\n", generateLoadTry(tableName, fields, tablePrimaryKey)));

        loadSqlBuilder.append("finally { \n");
        loadSqlBuilder.append("DBUtils.closeAutoCloseble(rs);\nDBUtils.closeAutoCloseble(stmt);\n");
        loadSqlBuilder.append("}");

        return loadSqlBuilder.toString();
    }

    private String generateLoadTry(String tableName, String[] fields, String tablePrimaryKey) {
        StringBuilder loadTryBuilder = new StringBuilder();
        loadTryBuilder.append(
                String.format("stmt = con.prepareStatement(\"SELECT * FROM %s WHERE %s = ? \"\n);", tableName,
                        tablePrimaryKey));
        loadTryBuilder.append("stmt.setInt(1, getId());\n");
        loadTryBuilder.append("rs = stmt.executeQuery();\n");
        loadTryBuilder.append(
                String.format("if (rs.next()) {%s}\n", generateLoadIfStatement(tableName, fields, tablePrimaryKey)));
        return loadTryBuilder.toString();
    }

    private String generateLoadIfStatement(String tableName, String[] fields, String tablePrimaryKey) {

        StringBuilder loadIfBuilder = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].split(":")[0];
            String sqlFieldName = fields[i].split(":")[1];
            String fieldType = fields[i].split(":")[2];

            fieldName = Utils.setField(fieldName);

            loadIfBuilder.append(
                    String.format(fieldName, String.format(Utils.resulSetGetType(fieldType), sqlFieldName)) + "\n");

        }

        return loadIfBuilder.toString();
    }

}
