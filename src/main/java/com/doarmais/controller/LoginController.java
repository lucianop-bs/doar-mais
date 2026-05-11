package com.doarmais.controller;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.model.bo.LoginBO;
import com.doarmais.model.utils.UsuarioLogado;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LoginBO loginBO = new LoginBO(usuarioDAO);

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblError;

    @FXML
    void onEntrarClick(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        AuditLogger.logAction("onEntrarClick", usuario);

        try {
            UsuarioEntity user = loginBO.autenticar(usuario, senha);

            if (user != null) {
                UsuarioLogado.setUsuarioLogado(user);
                abrirNovaTela("dashboard.fxml", "Dashboard");
            }
        } catch (Exception e) {
            Logger.logException("autenticar", usuario, e);
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }

    }

    @FXML
    void onCadastrarClick(ActionEvent event) {
        AuditLogger.logAction("onCadastrarClick", "não autenticado");
        abrirNovaTela("cadastro.fxml", "Cadastro");
    }

    private void abrirNovaTela(String fxml, String nome) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxml));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(nome);
            stage.show();

        } catch (IOException e) {
            Logger.logException("abrirNovaTela", txtUsuario.getText(), e);
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

}


