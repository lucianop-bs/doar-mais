package com.doarmais.model.infra.repositorios;

import com.doarmais.model.domain.Doacao;

import java.util.ArrayList;
import java.util.List;

public class DoacaoRepository {
    private final List<Doacao> listaDoacao = new ArrayList<>();

    public List<Doacao> getListaDoacao() {
        return listaDoacao;
    }

    public void adicionarDoacao(Doacao doacao) {
        listaDoacao.add(doacao);
    }
    public void removerDoacao(Doacao doacao) {
        listaDoacao.remove(doacao);
    }
}
