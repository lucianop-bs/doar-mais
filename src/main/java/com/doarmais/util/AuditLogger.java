package com.doarmais.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private static final String FILE_NAME = "log_de_auditoria.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logAction(String acao, String UsuarioEntity) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logMessage = String.format("Data/Hora: %s, Usuário: %s, Ação: %s%n",
                    timestamp, UsuarioEntity, acao);
            Files.write(Paths.get(FILE_NAME), logMessage.getBytes(java.nio.charset.StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.err.println("Falha ao escrever no arquivo de log de auditoria: " + ex.getMessage());
        }
    }
}


