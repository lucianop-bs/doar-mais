package com.doarmais.model.entities;

public class EstoqueEntity {
    private TipoItemEntity tipoItem;
    private Integer quantidade;

    public EstoqueEntity() {}

    public EstoqueEntity(TipoItemEntity tipoItem, Integer quantidade) {
        this.tipoItem = tipoItem;
        this.quantidade = quantidade;
    }

    public TipoItemEntity getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItemEntity tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
