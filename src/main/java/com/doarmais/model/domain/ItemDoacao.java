package com.doarmais.model.domain;

public enum ItemDoacao {
    ARROZ("Arroz"),
    FEIJAO("Feijão"),
    MACARRAO("Macarrão"),
    CUSCUZ("Flocão de Milho / Cuscuz"),
    OLEO("Óleo de Soja"),
    CAFE("Café"),
    ACUCAR("Açúcar"),
    SAL("Sal"),
    LEITE("Leite"),
    BISCOITO("Biscoito");

    private final String descricao;

    ItemDoacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}