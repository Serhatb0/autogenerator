package com.biricik.autogenerator.core.abstracts;

public interface FindBySQLGeneratorService {

    public String generateCode(String tableName, String findMethodName, String[] findFields, String className,
            String tablePrimaryKey);

}