package com.biricik.autogenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ClassInfo {

    private String className;
    private String tableName;
    private String findMethodName;
    private String[] fields;
    private String[] findFields;
    private String tablePrimaryKey;
    private boolean loadMethod;
    private boolean saveMethod;
    private boolean updateMethod;
    private boolean deleteMethod;
    private boolean findMethod;

}