package com.doarmais.model.entities;

public enum ItemDoacaoEntity {
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

    ItemDoacaoEntity(String descricao) {
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

