package com.doarmais.controller;

import com.doarmais.model.domain.Doacao;
import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.ItemDoacao;
import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.DoacaoRepository;
import com.doarmais.model.infra.repositorios.ItemRepository;
import com.doarmais.model.service.CriarCestaBasicaService;
import com.doarmais.model.service.CriarDoacaoService;
import com.doarmais.model.utils.UsuarioLogado;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private final ItemRepository itemRepository = new ItemRepository();
    private final DoacaoRepository doacaoRepository = new DoacaoRepository();
    private final CriarDoacaoService criarDoacaoService = new CriarDoacaoService(itemRepository, doacaoRepository);
    private final CriarCestaBasicaService criarCestaBasicaService = new CriarCestaBasicaService(itemRepository);

    @FXML
    private ComboBox<ItemDoacao> cbItemDoacao;
    @FXML
    private Spinner<Integer> spnQtd;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Doacao> tableDoacoes;
    @FXML
    private TableColumn<Doacao, String> colItem;
    @FXML
    private TableColumn<Doacao, String> colQtd;
    @FXML
    private TableColumn<Doacao, String> colUsuario;
    @FXML
    private TableColumn<Doacao, String> colData;

    @FXML
    private TableView<Item> tableItem;
    @FXML
    private TableColumn<Item, String> colItemNome;
    @FXML
    private TableColumn<Item, Integer> colQtdTotal;

    @FXML
    private Label lblError;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colItem.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getItemDoacao().getNome().getDescricao()));
        colUsuario.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getUsuario().getNome()));
        colQtd.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getItemDoacao().getQtd().toString()));
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colData.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getCriadoEm().format(formatador)));

        colItemNome.setCellValueFactory(celula -> new ReadOnlyStringWrapper(celula.getValue().getNome().getDescricao()));
        colQtdTotal.setCellValueFactory(new PropertyValueFactory<>("qtd"));

        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        spnQtd.setValueFactory(valores);

        cbItemDoacao.setItems(FXCollections.observableArrayList(ItemDoacao.values()));
        cbItemDoacao.getSelectionModel().selectFirst();
        atualizar();
        tableItem.setItems(itemRepository.getListaTotal());
        tableDoacoes.setItems(doacaoRepository.getListaDoacao());
    }

    @FXML
    void onClickDoacao(ActionEvent event) {
        ItemDoacao item = cbItemDoacao.getValue();
        Integer qtd = spnQtd.getValue();
        Item itemDoacao = new Item(item, qtd);
        Usuario user = UsuarioLogado.getUsuarioLogado();
        criarDoacaoService.doar(itemDoacao, user);
        atualizar();
    }

    @FXML
    void atualizar() {
        criarCestaBasicaService.criarListaTotalDeCestas();
        int totalDeCestas = criarCestaBasicaService.criarCesta();
        lblTotal.setText(String.valueOf(totalDeCestas));
        lblTotal.setVisible(true);
    }

    @FXML
    public void voltar() {
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
            lblError.setText("Erro ao carregar a tela: " + fxml);
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }
}
