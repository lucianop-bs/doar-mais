package com.doarmais.model.service;

import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.ItemDoacao;
import com.doarmais.model.infra.repositorios.DoacaoRepository;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CriarCestaBasicaService {
    private final DoacaoRepository doacaoRepository;

    public CriarCestaBasicaService(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }


    public Map<ItemDoacao, Integer> calcularEstoqueAgrupado() {

        var lista = doacaoRepository.buscarTodos();
        return lista.stream()
                .collect(Collectors.groupingBy(
                        doacao -> doacao.getItemDoacao().getNome(),
                        Collectors.summingInt(doacao -> doacao.getItemDoacao().getQtd())
                ));

    }

    public List<Item> obterListaDeEstoque() {
        var totaisNoEstoque = calcularEstoqueAgrupado();

        return totaisNoEstoque.entrySet().stream()
                .map(entrada -> new Item(entrada.getKey(), entrada.getValue()))
                .toList();
    }

    public int criarCesta() {

        var totais = calcularEstoqueAgrupado();

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