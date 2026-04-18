package com.doarmais.Domain;

public class Item {
    private ItemDoacao nome;
    private Integer qtd;

    public Item(ItemDoacao nome, Integer qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public ItemDoacao getNome() {
        return nome;
    }

    public void setNome(ItemDoacao nome) {
        this.nome = nome;
    }
}
