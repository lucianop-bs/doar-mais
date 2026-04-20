package com.doarmais.model.service;

import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.ItemDoacao;
import com.doarmais.model.infra.repositorios.ItemRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;
import java.util.stream.Collectors;

public class CriarCestaBasicaService {

    private static final ObservableList<Item> listaTotal = FXCollections.observableArrayList();
    private final ItemRepository itemRepository;

    public CriarCestaBasicaService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Map<ItemDoacao, Integer> criarMapTotalDeCestas() {
        return itemRepository.getListaItems().stream()
                .collect(Collectors.groupingBy(
                        Item::getNome,
                        Collectors.summingInt(Item::getQtd)
                ));

    }

    public void criarListaTotalDeCestas() {
        var totaisNoEstoque = criarMapTotalDeCestas();
        var resultado = totaisNoEstoque.entrySet().stream()
                .map(entrada -> new Item(entrada.getKey(), entrada.getValue()))
                .toList();
        itemRepository.adicionarTotal(resultado);
    }

    public int criarCesta() {

        var totais = criarMapTotalDeCestas();

        int totalDeCestas = 0;
        boolean consegueMontarMaisUma = true;

        while (consegueMontarMaisUma) {

            int testeDeQuantidade = totalDeCestas + 1;

            for (ItemDoacao tipo : ItemDoacao.values()) {

                int quantidadeQueTemos = totais.getOrDefault(tipo, 0);

                if (quantidadeQueTemos < testeDeQuantidade) {
                    consegueMontarMaisUma = false;
                    break;
                }

            }

            if (consegueMontarMaisUma) {
                totalDeCestas++;
            }

        }
        return totalDeCestas;
    }
}