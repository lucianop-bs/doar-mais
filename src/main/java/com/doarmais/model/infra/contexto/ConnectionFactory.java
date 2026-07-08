package com.doarmais.model.infra.contexto;

import com.doarmais.model.infra.exception.PersistenciaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:postgresql://localhost:5432/doarmais_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao conectar ao banco de dados", e);
        }
    }
}
