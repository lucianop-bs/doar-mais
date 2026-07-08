package com.doarmais.controller;

import com.doarmais.model.bo.NavigationBO;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;
import com.doarmais.util.UsuarioLogado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LogController implements Initializable {

    @FXML
    private TextArea txtAuditoria;
    @FXML
    private TextArea txtErros;
    @FXML
    private Label lblError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        if (user == null || !user.isAdmin()) {
            exibirErro("Acesso restrito: apenas administradores podem visualizar os logs.");
            txtAuditoria.setDisable(true);
            txtErros.setDisable(true);
            return;
        }
        carregar();
    }

    @FXML
    private void carregar() {
        txtAuditoria.setText(lerArquivo(AuditLogger.getFileName()));
        txtErros.setText(lerArquivo(Logger.getFileName()));
        limparErro();
    }

    private String lerArquivo(String nome) {
        Path path = Paths.get(nome);
        if (!Files.exists(path)) {
            return "(arquivo ainda não gerado nesta execução)";
        }
        try {
            String conteudo = Files.readString(path);
            return conteudo.isBlank() ? "(sem registros)" : conteudo;
        } catch (Exception e) {
            return "Erro ao ler o arquivo: " + e.getMessage();
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        NavigationBO.navegar("dashboard.fxml", "Dashboard");
    }

    private void exibirErro(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    private void limparErro() {
        lblError.setVisible(false);
    }
}
