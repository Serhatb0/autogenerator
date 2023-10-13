package com.biricik.autogenerator.core.concretes;

import org.springframework.stereotype.Component;

import com.biricik.autogenerator.core.abstracts.SQLGenratorService;

@Component
public class LoadConstructorGenerator implements SQLGenratorService {

    @Override
    public String generateCode(String tableName, String tablePrimaryKey, String[] fields, String className) {

        StringBuilder builder = new StringBuilder();

        StringBuilder constructorBuilder = new StringBuilder();

        constructorBuilder.append(" setId( id );\nload(con);");

        builder.append(String.format("\npublic %s(Connection con, int id ) throws SQLException {%s}\n", className,
                constructorBuilder));

        return builder.toString();
    }

}
