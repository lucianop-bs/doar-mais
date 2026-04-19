package com.doarmais.model.infra.repositorios;

import com.doarmais.model.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private static final List<Item> listaItems = new ArrayList<>();

    public void adicionarItem(Item item) {
        listaItems.add(item);
    }
    public void removerItem(Item item) {
        listaItems.remove(item);
    }
    public List<Item> getListaItems() {
        return listaItems;
    }

}
