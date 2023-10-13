package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;

@Component
public class DeleteSQLGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder builder = new StringBuilder();

        String sql = generateDeleteSql(tableName, tablePrimaryKey);

        builder.append(String.format("    public void delete(Connection  con) throws SQLException {\n%s\n}", sql));

        return builder.toString();
    }

    private String generateDeleteSql(String tableName, String tablePrimaryKey) {
        StringBuilder builder = new StringBuilder();

        builder.append("PreparedStatement stmt = null;\n");
        builder.append(String.format("try {%s}\n", generateDeleteTry(tableName, tablePrimaryKey)));

        builder.append("finally {\n");
        builder.append("DBUtils.closeAutoCloseble(stmt);\n");
        builder.append("}");

        return builder.toString();
    }

    private String generateDeleteTry(String tableName, String tablePrimaryKey) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("stmt = con.prepareStatement(\"DELETE FROM %s WHERE %s = ? \"\n);", tableName,
                tablePrimaryKey));
        builder.append("int idx = 1;\n");
        builder.append("stmt.setInt(idx++, getId());\n");
        builder.append("stmt.executeUpdate();\n");

        return builder.toString();

    }

}
