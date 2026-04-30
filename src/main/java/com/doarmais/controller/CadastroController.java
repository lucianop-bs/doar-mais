package com.doarmais.controller;

import com.doarmais.model.infra.repositorios.UsuarioRepository;
import com.doarmais.model.service.CriarUsuarioService;
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

public class CadastroController {
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final CriarUsuarioService criarUsuarioService = new CriarUsuarioService(usuarioRepository);

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
        if(!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
        {
            lblError.setText("Email inválido");
            lblError.setVisible(true);
            return;
        }
        try{
            boolean sucesso = criarUsuarioService.criar(nome, email, senha);
                if(sucesso){
                    abrirNovaTela("login.fxml", "Login");
                }
                else {
                    lblError.setText("Não foi possivel criar usuario");
                }
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }

    }

    @FXML
    public void voltar(){
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
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

}
