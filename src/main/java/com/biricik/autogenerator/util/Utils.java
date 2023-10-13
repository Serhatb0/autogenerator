package com.biricik.autogenerator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getField(String fieldName) {
        if (fieldName != null && fieldName.length() > 0) {
            fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            fieldName = "get" + fieldName + "()";
        }

        return fieldName;
    }

    public static String setField(String fieldName) {
        if (fieldName != null && fieldName.length() > 0) {
            fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            fieldName = "set" + fieldName + "(%s);";
        }

        return fieldName;
    }

    public static String stmtSetFieldType(String fieldType) {
        switch (fieldType) {
            case "int":
                return "stmt.setInt(idx++, %s );\n";
            case "String":
                return "stmt.setString(idx++, %s );\n";
            default:
                return "stmt.setString(idx++, %s );\n";
        }
    }

    public static String resulSetGetType(String fieldType) {
        switch (fieldType) {
            case "int":
                return "rs.getInt(\" %s \")";
            case "String":
                return "rs.getString(\" %s \")";
            default:
                return "rs.getString(\" %s \")";
        }
    }

    public static String findType(String fieldType) {
        switch (fieldType) {
            case "int":
                return "int";
            case "String":
                return "String";
            default:
                return "String";
        }
    }

    public static String generateIfStatement(String fieldType, int index) {
        String result = "";
        switch (fieldType) {
            case "int":
                result = (index == 0) ? "if (%s > 0) {sql += \"  %s = ? \";}\n"
                        : "if (%s > 0) {sql += \" and %s = ? \";}\n";
                return result;
            case "String":
                result = (index == 0) ? "if (%s != null) {sql += \"  %s = ? \";}\n"
                        : "if (%s != null) {sql += \" and %s = ? \";}\n";
                return result;
            default:
                result = (index == 0) ? "if (%s != null) {sql += \"  %s = ? \";}\n"
                        : "if (%s != null) {sql += \" and %s = ? \";}\n";
                return result;
        }
    }

    public static String generateIfStatementForPreparedStatement(String fieldType) {
        switch (fieldType) {
            case "int":
                return " if(%s > 0){ stmt.setInt(index++, %s);}\n";
            case "String":
                return " if(%s != null){ stmt.setString(index++, %s);}\n";
            default:
                return " if(%s != null){ stmt.setString(index++, %s);}\n";
        }
    }

    public static String setFieldDeclaration(String fieldType) {
        switch (fieldType) {
            case "int":
                return "    private int %s = 0;\n";
            case "String":
                return "    private String %s = \"\";\n";
            default:
                return "    private String %s = \"\";\n";
        }
    }

    public static String toLowerCamelCase(String input) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]*");
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String word = matcher.group();

            if (result.length() == 0) {
                result.append(word.toLowerCase());
            } else {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

}
