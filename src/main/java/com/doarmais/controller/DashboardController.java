package com.doarmais.controller;

import com.doarmais.model.entities.DistribuicaoEntity;
import com.doarmais.model.entities.EstoqueEntity;
import com.doarmais.model.entities.DoacaoEntity;
import com.doarmais.model.entities.TipoItemEntity;
import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.exception.NegocioException;
import com.doarmais.model.bo.DoacaoBO;
import com.doarmais.model.bo.TipoItemBO;
import com.doarmais.util.UsuarioLogado;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private final DoacaoBO doacaoBO = BOFactory.getDoacaoBO();
    private final TipoItemBO tipoItemBO = BOFactory.getTipoItemBO();
    private final ObservableList<DoacaoEntity> listaDoacoes = FXCollections.observableArrayList();
    private final ObservableList<EstoqueEntity> listaEstoque = FXCollections.observableArrayList();
    private final ObservableList<DistribuicaoEntity> listaDistribuicoes = FXCollections.observableArrayList();
    private final ObservableList<ItemEntity> listaPendentes = FXCollections.observableArrayList();

    @FXML
    private ComboBox<TipoItemEntity> cbItemDoacao;
    @FXML
    private Spinner<Integer> spnQtd;
    @FXML
    private Spinner<Integer> spnQtdDoacao;
    @FXML
    private TextField txtBeneficiario;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblUsuarioLogado;
    @FXML
    private Label lblError;
    @FXML
    private Button btnGerenciarUsuarios;
    @FXML
    private javafx.scene.layout.HBox paneDistribuir;

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
    private TableView<EstoqueEntity> tableItem;
    @FXML
    private TableColumn<EstoqueEntity, String> colItemNome;
    @FXML
    private TableColumn<EstoqueEntity, Integer> colQtdTotal;

    @FXML
    private TableView<DistribuicaoEntity> tableDistribuicoes;
    @FXML
    private TableColumn<DistribuicaoEntity, String> colBenNome;
    @FXML
    private TableColumn<DistribuicaoEntity, Integer> colBenQtd;
    @FXML
    private TableColumn<DistribuicaoEntity, String> colBenData;
    @FXML
    private TableColumn<DistribuicaoEntity, String> colBenUser;

    @FXML
    private TableView<ItemEntity> tablePendentes;
    @FXML
    private TableColumn<ItemEntity, String> colPenItem;
    @FXML
    private TableColumn<ItemEntity, Integer> colPenQtd;

    private DoacaoEntity doacaoSelecionada;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabelas();
        configurarSpinners();
        
        carregarTiposDeItens();

        tableDoacoes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> doacaoSelecionada = novo);

        verificarPermissoes();
        atualizar();
    }

    private void carregarTiposDeItens() {
        try {
            cbItemDoacao.setItems(FXCollections.observableArrayList(tipoItemBO.listarTodos()));
            cbItemDoacao.getSelectionModel().selectFirst();
        } catch (Exception e) {
            exibirErro("Erro ao carregar catálogo de itens.");
        }
    }

    private void configurarTabelas() {
        var test =  new UsuarioEntity();
        // Tabela de Doações
        colItem.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getItemDoacao().getNome().getDescricao()));
        colUsuario.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUsuario().getNome()));
        colQtd.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getItemDoacao().getQtd().toString()));
        colData.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCriadoEm().format(formatter)));
        tableDoacoes.setItems(listaDoacoes);

        // Tabela de Estoque
        colItemNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getTipoItem().getDescricao()));
        colQtdTotal.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableItem.setItems(listaEstoque);

        // Tabela de Distribuições (Beneficiários)
        colBenNome.setCellValueFactory(new PropertyValueFactory<>("beneficiario"));
        colBenQtd.setCellValueFactory(new PropertyValueFactory<>("quantidadeCestas"));
        colBenData.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDataDistribuicao().format(formatter)));
        colBenUser.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUsuario().getNome()));
        tableDistribuicoes.setItems(listaDistribuicoes);

        // Tabela de Itens Pendentes (Carrinho)
        colPenItem.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome().getDescricao()));
        colPenQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tablePendentes.setItems(listaPendentes);
    }

    private void configurarSpinners() {
        spnQtd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
        spnQtdDoacao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }

    private void verificarPermissoes() {
        UsuarioEntity user = UsuarioLogado.getUsuarioLogado();
        boolean isAdmin = user != null && user.isAdmin();
        
        if (user != null) {
            lblUsuarioLogado.setText(user.getNome());
        }
        
        btnGerenciarUsuarios.setVisible(isAdmin);
        paneDistribuir.setVisible(isAdmin);
        paneDistribuir.setManaged(isAdmin);
    }

    @FXML
    void onAdicionarPendente(ActionEvent event) {
        limparErro();
        TipoItemEntity tipo = cbItemDoacao.getValue();
        if (tipo == null) return;
        int qtd = spnQtd.getValue();


        boolean existe = false;
        for (ItemEntity item : listaPendentes) {
            if (item.getNome().getNome().equals(tipo.getNome())) {
                item.setQtd(item.getQtd() + qtd);
                existe = true;
                break;
            }
        }

        if (!existe) {
            listaPendentes.add(new ItemEntity(tipo, qtd));
        }
        tablePendentes.refresh();
    }

    @FXML
    void onAdicionarCestaCompleta(ActionEvent event) {
        limparErro();
        try {
            for (TipoItemEntity tipo : tipoItemBO.listarTodos()) {
                boolean existe = false;
                for (ItemEntity item : listaPendentes) {
                    if (item.getNome().getNome().equals(tipo.getNome())) {
                        item.setQtd(item.getQtd() + 1);
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    listaPendentes.add(new ItemEntity(tipo, 1));
                }
            }
            tablePendentes.refresh();
        } catch (Exception e) {
            exibirErro("Erro ao montar cesta completa.");
        }
    }

    @FXML
    void onLimparLista(ActionEvent event) {
        listaPendentes.clear();
    }

    @FXML
    void onFinalizarRecebimento(ActionEvent event) {
        limparErro();
        if (listaPendentes.isEmpty()) {
            exibirErro("A lista de entrada está vazia.");
            return;
        }

        try {
            doacaoBO.doarLote(new ArrayList<>(listaPendentes), UsuarioLogado.getUsuarioLogado());
            listaPendentes.clear();
            atualizar();
        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            Logger.logException("Dashboard.onFinalizarRecebimento", "erro", e);
            exibirErro("Erro ao salvar doações.");
        }
    }

    @FXML
    void onDoar(ActionEvent event) {
        limparErro();
        try {
            doacaoBO.distribuirCestas(spnQtdDoacao.getValue(), txtBeneficiario.getText(), UsuarioLogado.getUsuarioLogado());
            txtBeneficiario.clear();
            atualizar();
        } catch (NegocioException e) {
            exibirErro(e.getMessage());
        } catch (Exception e) {
            Logger.logException("Dashboard.onDoar", "erro", e);
            exibirErro("Erro ao distribuir cestas.");
        }
    }

    @FXML
    void onRemover(ActionEvent event) {
        if (doacaoSelecionada == null) {
            exibirErro("Selecione uma doação para remover.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente remover esta doação?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    doacaoBO.removerDoacao(doacaoSelecionada.getId());
                    atualizar();
                } catch (Exception e) {
                    exibirErro("Erro ao remover doação.");
                }
            }
        });
    }

    @FXML
    void atualizar() {
        try {
            listaDoacoes.setAll(doacaoBO.listarTodas());
            listaEstoque.setAll(doacaoBO.obterEstoqueAtual());
            listaDistribuicoes.setAll(doacaoBO.listarDistribuicoes());
            lblTotal.setText(String.valueOf(doacaoBO.calcularCestasBasicas()));
        } catch (Exception e) {
            exibirErro("Erro ao atualizar dados.");
        }
    }

    @FXML
    void irParaUsuarios(ActionEvent event) {
        NavigationBO.navegar("usuarios.fxml", "Gerenciamento de Usuários");
    }

    @FXML
    void voltar() {
        UsuarioLogado.setUsuarioLogado(null);
        NavigationBO.navegar("login.fxml", "Login");
    }

    private void exibirErro(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    private void limparErro() {
        lblError.setVisible(false);
    }
}
