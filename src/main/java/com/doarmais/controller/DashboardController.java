package com.doarmais.controller;

import com.doarmais.model.entities.DoacaoEntity;
import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.ItemDoacaoEntity;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.contexto.DbContext;
import com.doarmais.model.dao.DoacaoDAO;
import com.doarmais.model.bo.CriarCestaBasicaBO;
import com.doarmais.model.bo.CriarDoacaoBO;
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
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private final ObservableList<DoacaoEntity> listaObservavel = FXCollections.observableArrayList();

    private final ObservableList<ItemEntity> listaObservavelItens = FXCollections.observableArrayList();

    private final DbContext conexao = new DbContext();
    private final Connection connection = conexao.conectar();
    private final DoacaoDAO doacaoDAO = new DoacaoDAO(connection);
    private final CriarDoacaoBO criarDoacaoBO = new CriarDoacaoBO( doacaoDAO);
    private final CriarCestaBasicaBO criarCestaBasicaBO = new CriarCestaBasicaBO(doacaoDAO);

    @FXML
    private ComboBox<ItemDoacaoEntity> cbItemDoacao;
    @FXML
    private Spinner<Integer> spnQtd = new Spinner<>(1,10000,1);

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnGerenciarUsuarios;

    @FXML
    private TableView<DoacaoEntity> tableDoacoes;
    @FXML
    private TableColumn<DoacaoEntity, String> colItem;
    @FXML
    private TableColumn<DoacaoEntity, String> colQtd;
    @FXML
    private TableColumn<DoacaoEntity, String> colUsuario;
    @FXML
    private TableColumn<DoacaoEntity, String> colData;
    @FXML
    private TableView<ItemEntity> tableItem;
    @FXML
    private TableColumn<ItemEntity, String> colItemNome;
    @FXML
    private TableColumn<ItemEntity, Integer> colQtdTotal;

    @FXML
    private Label lblError;

    private DoacaoEntity doacaoSelecionada;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colItem.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getItemDoacao().getNome().getDescricao()));
        colUsuario.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getUsuario().getNome()));
        colQtd.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getItemDoacao().getQtd().toString()));
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colData.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getCriadoEm().format(formatador)));

        colItemNome.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getNome().getDescricao()));
        colQtdTotal.setCellValueFactory(new PropertyValueFactory<>("qtd"));

        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        spnQtd.setValueFactory(valores);

        cbItemDoacao.setItems(FXCollections.observableArrayList(ItemDoacaoEntity.values()));
        cbItemDoacao.getSelectionModel().selectFirst();

        tableItem.setItems(listaObservavelItens);
        tableDoacoes.setItems(listaObservavel);

        tableDoacoes.getSelectionModel().selectedItemProperty().addListener((observable, antigaDoacao, novaDoacao) -> {
            doacaoSelecionada = novaDoacao;
        });

        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        if (user != null) {
            btnGerenciarUsuarios.setVisible(user.isAdmin());
        }

        atualizar();
    }

    @FXML
    void onClickDoacao(ActionEvent event) {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";
        AuditLogger.logAction("onClickDoacao", username);
        try {
            ItemDoacaoEntity item = cbItemDoacao.getValue();
            Integer qtd = spnQtd.getValue();
            ItemEntity itemDoacao = new ItemEntity(item, qtd);
            criarDoacaoBO.doar(itemDoacao, user);
            atualizar();
        } catch (Exception e) {
            Logger.logException("onClickDoacao", username, e);
            lblError.setText("Erro ao registrar a doação.");
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    void irParaUsuarios(ActionEvent event) {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        if (user != null && user.isAdmin()) {
            abrirNovaTela("usuarios.fxml", "Gerenciamento de Usuários");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acesso Negado");
            alert.setHeaderText(null);
            alert.setContentText("Você não tem permissão para acessar esta área.");
            alert.showAndWait();
        }
    }

    @FXML
    void atualizar() {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";
        AuditLogger.logAction("atualizarDashboard", username);
        try {
            List<DoacaoEntity> doacoesDoBanco = doacaoDAO.buscarTodos();
            listaObservavel.setAll(doacoesDoBanco);
            List<ItemEntity> totais = criarCestaBasicaBO.obterListaDeEstoque();

            listaObservavelItens.setAll(totais);

            int totalDeCestas = criarCestaBasicaBO.criarCesta();

            lblTotal.setText(String.valueOf(totalDeCestas));
            lblTotal.setVisible(true);
        } catch (Exception e) {
            Logger.logException("atualizarDashboard", username, e);
            lblError.setText("Erro ao atualizar dados.");
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void voltar() {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";
        AuditLogger.logAction("logout", username);
        UsuarioLogado.setUsuarioLogado(null);
        abrirNovaTela("login.fxml", "Login");
    }

    private void abrirNovaTela(String fxml, String nome) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxml));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) spnQtd.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle(nome);
            stage.show();

        } catch (IOException e) {
            UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
            String username = (user != null) ? user.getNome() : "desconhecido";
            Logger.logException("abrirNovaTela", username, e);
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    private void onRemover(ActionEvent event) {
        if (doacaoSelecionada == null) {
            exibirAlerta("Aviso", "Selecione uma doação para remover.");
            return;
        }

        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        String username = (user != null) ? user.getNome() : "desconhecido";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Deseja realmente remover o Doação de " + doacaoSelecionada.getItemDoacao().getNome() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AuditLogger.logAction("removerDoacao", username);
            try {
                doacaoDAO.remover(doacaoSelecionada.getId());
                atualizar();
                exibirAlerta("Sucesso", "doação removida com sucesso!");
            } catch (Exception e) {
                Logger.logException("removerUsuario", username, e);
                exibirAlerta("Erro", "Erro ao remover doação: " + e.getMessage());
            }
        }
    }
    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}


