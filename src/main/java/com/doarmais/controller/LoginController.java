package com.doarmais.controller;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.exception.NegocioException;
import com.doarmais.model.bo.UsuarioBO;
import com.doarmais.util.UsuarioLogado;
import com.doarmais.util.Logger;
import com.doarmais.model.bo.NavigationBO;
import com.doarmais.util.BOFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private final UsuarioBO usuarioBO = BOFactory.getUsuarioBO();

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblError;

    @FXML
    void onEntrarClick(ActionEvent event) {
        limparErro();
        String email = txtUsuario.getText();
        String senha = txtSenha.getText();

        try {
            UsuarioEntity usuario = usuarioBO.autenticar(email, senha);
            UsuarioLogado.setUsuarioLogado(usuario);
            NavigationBO.navegar("dashboard.fxml", "Dashboard");

        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            Logger.logException("LoginController.onEntrarClick", email, e);
            exibirErro("Erro ao realizar login.");
        }
    }

    @FXML
    void onCadastrarClick(ActionEvent event) {
        NavigationBO.navegar("cadastro.fxml", "Cadastro");
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
