package com.doarmais.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String FILE_NAME = "log_de_erros.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getFileName() {
        return FILE_NAME;
    }

    public static void reset() {
        try {
            Files.write(Paths.get(FILE_NAME), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            System.err.println("Falha ao limpar o arquivo de log: " + ex.getMessage());
        }
    }

    public static void logException(String acao, String UsuarioEntity, Exception e) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logMessage = String.format("Data/Hora: %s, Usuário: %s, Ação: %s, Exceção: %s%n",
                    timestamp, UsuarioEntity, acao, e.toString());
            Files.write(Paths.get(FILE_NAME), logMessage.getBytes(java.nio.charset.StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.err.println("Falha ao escrever no arquivo de log: " + ex.getMessage());
        }
    }
}


