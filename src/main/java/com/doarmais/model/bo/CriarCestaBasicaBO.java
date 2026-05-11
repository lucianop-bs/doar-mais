package com.doarmais.model.bo;

import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.ItemDoacaoEntity;
import com.doarmais.model.dao.DoacaoDAO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CriarCestaBasicaBO {
    private final DoacaoDAO doacaoDAO;

    public CriarCestaBasicaBO(DoacaoDAO doacaoDAO) {
        this.doacaoDAO = doacaoDAO;
    }


    public Map<ItemDoacaoEntity, Integer> calcularEstoqueAgrupado() {
        AuditLogger.logAction("calcularEstoqueAgrupadoBO", "sistema");
        try {
            var lista = doacaoDAO.buscarTodos();
            return lista.stream()
                    .collect(Collectors.groupingBy(
                            doacao -> doacao.getItemDoacao().getNome(),
                            Collectors.summingInt(doacao -> doacao.getItemDoacao().getQtd())
                    ));
        } catch (Exception e) {
            Logger.logException("calcularEstoqueAgrupadoBO", "sistema", e);
            throw e;
        }

    }

    public List<ItemEntity> obterListaDeEstoque() {
        AuditLogger.logAction("obterListaDeEstoqueBO", "sistema");
        try {
            var totaisNoEstoque = calcularEstoqueAgrupado();

            return totaisNoEstoque.entrySet().stream()
                    .map(entrada -> new ItemEntity(entrada.getKey(), entrada.getValue()))
                    .toList();
        } catch (Exception e) {
            Logger.logException("obterListaDeEstoqueBO", "sistema", e);
            throw e;
        }
    }

    public int criarCesta() {
        AuditLogger.logAction("criarCestaBO", "sistema");
        try {
            var totais = calcularEstoqueAgrupado();

            int totalDeCestas = 0;
            boolean consegueMontarMaisUma = true;

            while (consegueMontarMaisUma) {

                int testeDeQuantidade = totalDeCestas + 1;

                for (ItemDoacaoEntity tipo : ItemDoacaoEntity.values()) {

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
        } catch (Exception e) {
            Logger.logException("criarCestaBO", "sistema", e);
            throw e;
        }
    }
}
