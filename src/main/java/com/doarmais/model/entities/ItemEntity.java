package com.doarmais.model.entities;

public class ItemEntity {
    private TipoItemEntity nome;
    private Integer qtd;

    public ItemEntity(TipoItemEntity nome, Integer qtd) {
        this.nome = nome;
        this.qtd = qtd;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public TipoItemEntity getNome() {
        return nome;
    }

    public void setNome(TipoItemEntity nome) {
        this.nome = nome;
    }
}
