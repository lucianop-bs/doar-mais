package com.doarmais.controller;

import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.UsuarioRepository;
import com.doarmais.model.service.LoginService;
import com.doarmais.model.utils.UsuarioLogado;
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
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final LoginService loginService = new LoginService(usuarioRepository);
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

        try {
            Usuario user = loginService.autenticar(usuario, senha);
            if (user != null) {
                abrirNovaTela("dashboard.fxml", "Dashboard");
                UsuarioLogado.setUsuarioLogado(user);
            }
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }

    }

    @FXML
    void onCadastrarClick(ActionEvent event) {
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
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

}
