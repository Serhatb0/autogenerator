package com.biricik.autogenerator.core.abstracts;

public interface SQLGenratorService {

    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className);
}
