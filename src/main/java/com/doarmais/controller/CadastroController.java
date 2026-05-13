package com.doarmais.controller;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.exception.NegocioException;
import com.doarmais.model.bo.UsuarioBO;
import com.doarmais.util.Logger;
import com.doarmais.model.bo.NavigationBO;
import com.doarmais.util.BOFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CadastroController {

    private final UsuarioBO usuarioBO = BOFactory.getUsuarioBO();

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
        limparErro();

        try {
            UsuarioEntity novoUsuario = new UsuarioEntity(
                    txtNome.getText(),
                    txtEmail.getText(),
                    txtSenha.getText()
            );

            usuarioBO.cadastrar(novoUsuario);
            NavigationBO.navegar("login.fxml", "Login");

        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            Logger.logException("CadastroController.onCadastrarClick", txtEmail.getText(), e);
            exibirErro("Ocorreu um erro inesperado ao realizar o cadastro.");
        }
    }

    @FXML
    public void voltar() {
        NavigationBO.navegar("login.fxml", "Login");
    }

    private void exibirErro(String mensagem) {
        lblError.setText(mensagem);
        lblError.setVisible(true);
    }

    private void limparErro() {
        lblError.setVisible(false);
        lblError.setText("");
    }
}
