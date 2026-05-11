package com.doarmais.model.entities;

public class ItemEntity {
    private ItemDoacaoEntity nome;
    private Integer qtd;

    public ItemEntity(ItemDoacaoEntity nome, Integer qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public ItemDoacaoEntity getNome() {
        return nome;
    }

    public void setNome(ItemDoacaoEntity nome) {
        this.nome = nome;
    }
}


