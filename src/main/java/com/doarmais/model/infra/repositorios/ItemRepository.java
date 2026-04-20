package com.doarmais.model.infra.repositorios;

import com.doarmais.model.domain.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;


public class ItemRepository {
    private static final ObservableList<Item> listaItems = FXCollections.observableArrayList();
    private final ObservableList<Item> listaTotal = FXCollections.observableArrayList();

    public ObservableList<Item> getListaTotal() {
        return listaTotal;
    }

    public void adicionarTotal(List<Item> item) {
        listaTotal.setAll(item);
    }

    public void adicionarItem(Item item) {
        listaItems.add(item);
    }
    public void removerItem(Item item) {
        listaItems.remove(item);
    }

    public ObservableList<Item> getListaItems() {
        return listaItems;
    }


}
