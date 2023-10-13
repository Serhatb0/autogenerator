package com.biricik.autogenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.FindBySQLGeneratorService;
import com.biricik.autogenerator.core.abstracts.SQLGenratorService;

@Component
public class JavaClassGenerator {

    @Autowired
    @Qualifier("insertSQLGenerator")
    private SQLGenratorService insertSqlGenratorService;
    @Autowired
    @Qualifier("fieldSQLGenerator")
    private SQLGenratorService fieldGenratorService;

    @Autowired
    @Qualifier("loadSQLGenerator")
    private SQLGenratorService loadSqlGenratorService;

    @Autowired
    @Qualifier("updateSQLGenerator")
    private SQLGenratorService updateSqlGenratorService;

    @Autowired
    @Qualifier("deleteSQLGenerator")
    private SQLGenratorService deleteSqlGenratorService;

    @Autowired
    @Qualifier("findBySQLGenerator")
    private FindBySQLGeneratorService findBySQLGeneratorService;

    @Autowired
    @Qualifier("loadConstructorGenerator")
    private SQLGenratorService loadConstructorGeneratorService;

    @Autowired
    @Qualifier("getAllSQLGenerator")
    private SQLGenratorService getAllSQLGeneratorService;

    public byte[] generateJavaClass(String className, String[] fields, String tableName, String tablePrimaryKey,
            boolean loadMethod, boolean saveMethod, boolean updateMethod, boolean deleteMethod, boolean findMethod,
            String[] findFields, String findMethodName) {
        StringBuilder classImport = new StringBuilder();
        classImport.append("package com.vitgusa.vedi.bean;\n");
        classImport.append("import java.sql.Connection;\n");
        classImport.append("import java.sql.PreparedStatement;\n");
        classImport.append("import java.sql.ResultSet;\n");
        classImport.append("import java.sql.SQLException;\n");
        classImport.append("import com.vitgusa.vedi.util.DBUtils;\n");
        classImport.append("import com.vitgusa.vedi.util.StringUtils;\n");
        classImport.append("import lombok.Getter;\n");
        classImport.append("import lombok.Setter;\n");
        classImport.append("import java.util.Vector;\n");

        String classTemplate = "%s\n\n@Getter\n@Setter\n";
        classTemplate += "public class %s {\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s}";

        String loadConstructor = "";
        String loadSql = "";
        String insertSql = "";
        String updateSql = "";
        String deleteSql = "";
        String findSql = "";
        String getAllSql = "";

        if (loadMethod) {
            loadSql = loadSqlGenratorService.generateCode(tableName, tablePrimaryKey, fields, className);
            loadConstructor = loadConstructorGeneratorService.generateCode(tableName, tablePrimaryKey, fields,
                    className);
            getAllSql = getAllSQLGeneratorService.generateCode(tableName, tablePrimaryKey, findFields, className);

        }
        if (saveMethod) {
            insertSql = insertSqlGenratorService.generateCode(tableName, tablePrimaryKey, fields, className);
        }
        if (updateMethod) {
            updateSql = updateSqlGenratorService.generateCode(tableName, tablePrimaryKey, fields, className);
        }
        if (deleteMethod) {
            deleteSql = deleteSqlGenratorService.generateCode(tableName, tablePrimaryKey, fields, className);
        }
        if (findMethod) {
            findSql = findBySQLGeneratorService.generateCode(tableName, findMethodName, findFields, className,
                    tablePrimaryKey);
        }

        String javaClass = String.format(classTemplate, classImport, className,
                fieldGenratorService.generateCode(tableName, tablePrimaryKey, fields, className),
                loadConstructor,
                loadSql,
                getAllSql,
                insertSql,
                updateSql,
                deleteSql,
                findSql);

        return javaClass.getBytes();
    }

}
