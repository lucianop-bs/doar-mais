package com.doarmais.model.infra.contexto;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbContext {

    private static final String URL = "jdbc:postgresql://localhost:5432/doarmais_db";
    private static final String USER = "postgres"; // geralmente "postgres"
    private static final String PASSWORD = "admin";

    public Connection conectar() {
        try {

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
