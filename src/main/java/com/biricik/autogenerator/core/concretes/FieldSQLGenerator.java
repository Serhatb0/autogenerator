package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;
import com.biricik.autogenerator.util.Utils;

@Component
public class FieldSQLGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {
        StringBuilder fieldDeclarations = new StringBuilder();
        fieldDeclarations.append("    private int id = 0;\n");
        for (String field : fields) {
            String fieldName = field.split(":")[0];
            String fieldType = field.split(":")[2];
            fieldDeclarations.append(String.format(Utils.setFieldDeclaration(fieldType), fieldName));
        }
        return fieldDeclarations.toString();

    }

}
