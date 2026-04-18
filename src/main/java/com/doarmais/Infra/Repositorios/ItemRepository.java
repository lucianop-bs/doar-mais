package com.doarmais.Infra.Repositorios;
import com.doarmais.Domain.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private final List<Item> listaItems = new ArrayList<>();

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
