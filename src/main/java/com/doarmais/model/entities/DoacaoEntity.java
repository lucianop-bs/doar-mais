package com.doarmais.model.entities;

import java.time.LocalDate;

public class DoacaoEntity {
    private long id;
    private ItemEntity itemDoacao;
    private UsuarioEntity usuario;
    private LocalDate criadoEm;

    public DoacaoEntity(ItemEntity itemDoacao, UsuarioEntity usuario) {
        this.itemDoacao = itemDoacao;
        this.usuario = usuario;
        this.criadoEm = LocalDate.now();
    }

    public DoacaoEntity() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ItemEntity getItemDoacao() {
        return itemDoacao;
    }

    public void setItemDoacao(ItemEntity itemDoacao) {
        this.itemDoacao = itemDoacao;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }
}
