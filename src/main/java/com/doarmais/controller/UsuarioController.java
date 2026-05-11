package com.doarmais.controller;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.model.utils.UsuarioLogado;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {

    @FXML
    private TableView<UsuarioEntity> tableUsuarios;
    @FXML
    private TableColumn<UsuarioEntity, Long> colId;
    @FXML
    private TableColumn<UsuarioEntity, String> colNome;
    @FXML
    private TableColumn<UsuarioEntity, String> colEmail;
    @FXML
    private TableColumn<UsuarioEntity, String> colAdmin;

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private CheckBox chkAdmin;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<UsuarioEntity> listaUsuarios = FXCollections.observableArrayList();
    private UsuarioEntity usuarioSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdmin.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().isAdmin() ? "Sim" : "Não"));

        tableUsuarios.setItems(listaUsuarios);

        tableUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                selecionarUsuario(novo);
            }
        });

        atualizarTabela();
    }

    private void selecionarUsuario(UsuarioEntity usuario) {
        usuarioSelecionado = usuario;
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
        chkAdmin.setSelected(usuario.isAdmin());
    }

    @FXML
    private void atualizarTabela() {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";
        AuditLogger.logAction("listarUsuarios", username);
        
        try {
            List<UsuarioEntity> usuarios = usuarioDAO.buscarTodos();
            listaUsuarios.setAll(usuarios);
        } catch (Exception e) {
            Logger.logException("listarUsuarios", username, e);
            exibirAlerta("Erro", "Erro ao carregar usuários: " + e.getMessage());
        }
    }

    @FXML
    private void onSalvar(ActionEvent event) {
        if (usuarioSelecionado == null) {
            exibirAlerta("Aviso", "Selecione um usuário para editar.");
            return;
        }

        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";
        AuditLogger.logAction("editarUsuario", username);

        usuarioSelecionado.setNome(txtNome.getText());
        usuarioSelecionado.setEmail(txtEmail.getText());
        usuarioSelecionado.setAdmin(chkAdmin.isSelected());

        try {
            usuarioDAO.atualizar(usuarioSelecionado);
            atualizarTabela();
            limparCampos();
            exibirAlerta("Sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            Logger.logException("editarUsuario", username, e);
            exibirAlerta("Erro", "Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @FXML
    private void onRemover(ActionEvent event) {
        if (usuarioSelecionado == null) {
            exibirAlerta("Aviso", "Selecione um usuário para remover.");
            return;
        }

        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Deseja realmente remover o usuário " + usuarioSelecionado.getNome() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AuditLogger.logAction("removerUsuario", username);
            try {
                usuarioDAO.remover(usuarioSelecionado.getId());
                atualizarTabela();
                limparCampos();
                exibirAlerta("Sucesso", "Usuário removido com sucesso!");
            } catch (Exception e) {
                Logger.logException("removerUsuario", username, e);
                exibirAlerta("Erro", "Erro ao remover usuário: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        abrirNovaTela("dashboard.fxml", "Dashboard");
    }

    private void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        chkAdmin.setSelected(false);
        usuarioSelecionado = null;
        tableUsuarios.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void abrirNovaTela(String fxml, String nome) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxml));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) tableUsuarios.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(nome);
            stage.show();

        } catch (IOException e) {
            UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
            String username = (user != null) ? user.getNome() : "desconhecido";
            Logger.logException("abrirNovaTela", username, e);
            e.printStackTrace();
        }
    }
}


