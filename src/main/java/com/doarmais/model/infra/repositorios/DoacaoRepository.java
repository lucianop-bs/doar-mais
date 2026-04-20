package com.doarmais.model.infra.repositorios;

import com.doarmais.model.domain.Doacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DoacaoRepository {
    private static final ObservableList<Doacao> listaDoacao = FXCollections.observableArrayList();

    public ObservableList<Doacao> getListaDoacao() {
        return listaDoacao;
    }

    public void adicionarDoacao(Doacao doacao) {
        listaDoacao.add(doacao);
    }
    public void removerDoacao(Doacao doacao) {
        listaDoacao.remove(doacao);
    }
}
