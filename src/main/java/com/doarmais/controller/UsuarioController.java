package com.doarmais.controller;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.exception.NegocioException;
import com.doarmais.model.bo.UsuarioBO;
import com.doarmais.util.Logger;
import com.doarmais.model.bo.NavigationBO;
import com.doarmais.util.BOFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {

    private final UsuarioBO usuarioBO = BOFactory.getUsuarioBO();
    private final ObservableList<UsuarioEntity> listaUsuarios = FXCollections.observableArrayList();
    private UsuarioEntity usuarioSelecionado;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        atualizarTabela();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdmin.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().isAdmin() ? "Sim" : "Não"));

        tableUsuarios.setItems(listaUsuarios);
        tableUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) selecionarUsuario(novo);
        });
    }

    private void selecionarUsuario(UsuarioEntity usuario) {
        usuarioSelecionado = usuario;
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
        chkAdmin.setSelected(usuario.isAdmin());
    }

    @FXML
    private void atualizarTabela() {
        try {
            listaUsuarios.setAll(usuarioBO.listarTodos());
        } catch (Exception e) {
            Logger.logException("UsuarioController.atualizarTabela", "erro", e);
            exibirAlerta("Erro", "Erro ao carregar usuários.");
        }
    }

    @FXML
    private void onSalvar(ActionEvent event) {
        if (usuarioSelecionado == null) {
            exibirAlerta("Aviso", "Selecione um usuário para editar.");
            return;
        }

        try {
            usuarioSelecionado.setNome(txtNome.getText());
            usuarioSelecionado.setEmail(txtEmail.getText());
            usuarioSelecionado.setAdmin(chkAdmin.isSelected());

            // Nota: No UsuarioBO precisaremos de um método de atualizar se quisermos seguir o padrão.
            // Por enquanto vamos usar o DAO via factory ou adicionar o método no BO.
            BOFactory.getUsuarioDAO().atualizar(usuarioSelecionado); 
            
            atualizarTabela();
            limparCampos();
            exibirAlerta("Sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            Logger.logException("UsuarioController.onSalvar", txtEmail.getText(), e);
            exibirAlerta("Erro", "Erro ao atualizar usuário.");
        }
    }

    @FXML
    private void onRemover(ActionEvent event) {
        if (usuarioSelecionado == null) {
            exibirAlerta("Aviso", "Selecione um usuário para remover.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente remover o usuário " + usuarioSelecionado.getNome() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    usuarioBO.excluir(usuarioSelecionado.getId());
                    atualizarTabela();
                    limparCampos();
                    exibirAlerta("Sucesso", "Usuário removido com sucesso!");
                } catch (Exception e) {
                    exibirAlerta("Erro", "Erro ao remover usuário.");
                }
            }
        });
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        NavigationBO.navegar("dashboard.fxml", "Dashboard");
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
}
