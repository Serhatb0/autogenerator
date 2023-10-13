package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.FindBySQLGeneratorService;
import com.biricik.autogenerator.util.Utils;

@Component
public class FindBySQLGenerator implements FindBySQLGeneratorService {

    @Override
    public String generateCode(String tableName, String findMethodName, String[] findFields, String className,
            String tablePrimaryKey) {
        StringBuilder builder = new StringBuilder();
        String lowerCamelCaseClassName = Utils.toLowerCamelCase(className) + "s";
        String findByMethotParemeters = generateFindByParameters(findFields);
        String findBySql = generateFindBySql(tableName, findFields);
        String whereSql = generateWhereSql(findFields);
        String preparedStatement = createPreparedStatement(className, findFields, tablePrimaryKey,
                lowerCamelCaseClassName);

        StringBuilder createVector = new StringBuilder();
        createVector.append(String.format("Vector %s = new Vector();\n", lowerCamelCaseClassName));

        builder
                .append(String.format(
                        "    public static Vector %s(Connection  con , %s) throws Exception {%s\n\n\n String sql = \" %s\";\n\n%s\n\n%s}",
                        findMethodName,
                        findByMethotParemeters, createVector.toString(), findBySql, whereSql, preparedStatement));

        return builder.toString();
    }

    private String createPreparedStatement(String className, String[] findFields, String tablePrimaryKey,
            String lowerCamelCaseClassName) {
        StringBuilder builder = new StringBuilder();

        builder.append("PreparedStatement stmt = null;\n");
        builder.append("ResultSet rs = null;\n");
        builder.append(String.format("try {%s}\n",
                generateFindByTry(className, findFields, tablePrimaryKey, lowerCamelCaseClassName)));

        builder.append("finally {\n");
        builder.append("DBUtils.closeAutoCloseble(rs);\nDBUtils.closeAutoCloseble(stmt);\n");
        builder.append("}");

        return builder.toString();

    }

    private String generateFindByTry(String className, String[] findFields, String tablePrimaryKey,
            String lowerCamelCaseClassName) {
        StringBuilder builder = new StringBuilder();

        builder.append("stmt = con.prepareStatement(sql);\n");
        builder.append("int index = 1;");

        for (int i = 0; i < findFields.length; i++) {
            String methodParametersName = findFields[i].split(":")[0];
            String fieldType = findFields[i].split(":")[2];

            builder.append(String.format(Utils.generateIfStatementForPreparedStatement(fieldType), methodParametersName,
                    methodParametersName));

        }

        builder.append("rs = stmt.executeQuery();\n");

        builder.append("while (rs.next()) {\n");
        builder.append(String.format("%s %s = new %s(con, rs.getInt(\"%s\"));\n", className,
                Utils.toLowerCamelCase(className), className,
                tablePrimaryKey));
        builder.append(String.format(lowerCamelCaseClassName + ".add(%s);\n", Utils.toLowerCamelCase(className)));
        builder.append("}\n");

        builder.append(String.format(" return %s;\n", lowerCamelCaseClassName));

        return builder.toString();
    }

    private String generateFindByParameters(String[] findFields) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < findFields.length; i++) {
            String methodParametersName = findFields[i].split(":")[0];
            String fieldType = findFields[i].split(":")[2];

            builder.append(String.format(" %s  %s ", Utils.findType(fieldType), methodParametersName));

            if (i < findFields.length - 1) {
                builder.append(" , ");
            }

        }

        return builder.toString();
    }

    private String generateFindBySql(String tableName, String[] findFields) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("SELECT * FROM %s WHERE ", tableName));

        return builder.toString();
    }

    private String generateWhereSql(String[] findFields) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < findFields.length; i++) {
            String methodParametersName = findFields[i].split(":")[0];
            String sqlFieldName = findFields[i].split(":")[1];
            String fieldType = findFields[i].split(":")[2];

            builder.append(String.format(Utils.generateIfStatement(fieldType, i), methodParametersName, sqlFieldName));

        }
        return builder.toString();
    }

}
