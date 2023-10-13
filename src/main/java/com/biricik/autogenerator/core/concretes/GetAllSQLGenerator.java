package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;
import com.biricik.autogenerator.util.Utils;

@Component
public class GetAllSQLGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder builder = new StringBuilder();

        String lowerCamelCaseClassName = Utils.toLowerCamelCase(className);

        StringBuilder createVector = new StringBuilder();
        createVector.append(String.format("Vector %s = new Vector();\n", lowerCamelCaseClassName + "s"));

        String getAllSql = generateGetlAllSql(tablePrimaryKey, tableName);
        String getAllPreparedStatement = generateGetAllPreparedStatement(className, lowerCamelCaseClassName,
                tablePrimaryKey);

        builder
                .append(String.format(
                        "    public static Vector<%s> getAll(Connection  con) throws Exception {%s\n\n\n String sql = \" %s\";\n%s}",
                        className, createVector,
                        getAllSql, getAllPreparedStatement));

        return builder.toString();
    }

    private String generateGetlAllSql(String tablePrimaryKey, String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("SELECT %s FROM %s", tablePrimaryKey, tableName));
        return builder.toString();

    }

    private String generateGetAllPreparedStatement(String className, String lowerCamelCaseClassName,
            String tablePrimaryKey) {
        StringBuilder builder = new StringBuilder();

        builder.append(" PreparedStatement stmt = null;\nResultSet rs = null;\n");
        builder.append(" try{");
        builder.append("  stmt = con.prepareStatement(sql);\n");
        builder.append(" rs = stmt.executeQuery();\n");
        builder.append(" while (rs.next()) { \n");
        builder.append(String.format("%s %s = new %s(con, rs.getInt(\"%s\"));\n", className, lowerCamelCaseClassName,
                className, tablePrimaryKey));
        builder.append(String.format("%s.add(%s);\n", lowerCamelCaseClassName + "s", lowerCamelCaseClassName));
        builder.append("}\n");
        builder.append(String.format("  return %s;\n", lowerCamelCaseClassName + "s"));
        builder.append(" }\n");
        builder.append(" finally {\n");
        builder.append("DBUtils.closeAutoCloseble(rs);\nBUtils.closeAutoCloseble(stmt);\n");
        builder.append("}\n");

        return builder.toString();

    }

}
