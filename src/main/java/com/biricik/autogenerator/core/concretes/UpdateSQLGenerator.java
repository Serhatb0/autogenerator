package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;
import com.biricik.autogenerator.util.Utils;

@Component
public class UpdateSQLGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder updateMethod = new StringBuilder();

        String updateSql = updateSql(tableName, fields, tablePrimaryKey);
        String preparedStatement = createPreparedStatement(fields);

        updateMethod
                .append(String.format(
                        "    public void update(Connection  con) throws SQLException {\nString sql = \"%s\";\n\n%s}",
                        updateSql, preparedStatement));

        return updateMethod.toString();
    }

    private String updateSql(String tableName, String[] fields, String tablePrimaryKey) {
        StringBuilder updaStringBuilder = new StringBuilder();
        updaStringBuilder.append(String.format("UPDATE %s", tableName) + " SET ");
        for (int i = 0; i < fields.length; i++) {
            String sqlFieldName = fields[i].split(":")[1];

            updaStringBuilder.append(sqlFieldName + " = ? ");
            if (i < fields.length - 1) {
                updaStringBuilder.append(" , ");
            }

        }

        updaStringBuilder.append(String.format("WHERE  %s = ? ", tablePrimaryKey));

        return updaStringBuilder.toString();
    }

    private String createPreparedStatement(String[] fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nPreparedStatement stmt = null;\n");
        builder.append(String.format("try {%s}\n", generateUpdateTry(fields)));

        builder.append("finally {\n");
        builder.append("DBUtils.closeAutoCloseble(stmt);\n");
        builder.append("}\n");

        return builder.toString();
    }

    private String generateUpdateTry(String[] fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nstmt = con.prepareStatement(sql);\n");
        builder.append("int idx = 1;\n");

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].split(":")[0];
            String fieldType = fields[i].split(":")[2];

            builder.append(String.format(Utils.stmtSetFieldType(fieldType), Utils.getField(fieldName)) + "\n");
        }

        builder.append("stmt.setInt(idx++, getId());\n");
        builder.append("stmt.executeUpdate();");

        return builder.toString();

    }

}
