package com.doarmais.controller;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.model.bo.CriarUsuarioBO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastroController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CriarUsuarioBO criarUsuarioBO = new CriarUsuarioBO(usuarioDAO);

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblError;

    @FXML
    void onCadastrarClick(ActionEvent event) {

        lblError.setVisible(false);
        lblError.setText("");

        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        
        AuditLogger.logAction("onCadastrarClick", email);

        if(!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
        {
            lblError.setText("Email inválido");
            lblError.setVisible(true);
            return;
        }
        if(senha.length() < 6)
        {
            lblError.setText("Senha deve conter mais de 6 dígitos");
            lblError.setVisible(true);
            return;
        }
        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*1).+$"))
        {
            lblError.setText("A senha deve conter o digito 1");
            lblError.setVisible(true);
            return;
        }

        try{
            boolean sucesso = criarUsuarioBO.criar(nome, email, senha);
                if(sucesso){
                    abrirNovaTela("login.fxml", "Login");
                }
                else {
                    lblError.setText("Não foi possivel criar UsuarioEntity");
                }
        } catch (Exception e) {
            Logger.logException("criarUsuario", email, e);
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }

    }

    @FXML
    public void voltar(){
        AuditLogger.logAction("voltarParaLogin", "não autenticado");
        abrirNovaTela("login.fxml", "Login");
    }

    private void abrirNovaTela(String fxml, String nome) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxml));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) txtNome.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(nome);
            stage.show();

        } catch (IOException e) {
            Logger.logException("abrirNovaTela", txtEmail.getText(), e);
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

}


