package com.doarmais.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Doacao {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private Item itemDoacao;
    private Usuario usuario;
    private LocalDate criadoEm;

    public Doacao(Item itemsDoacao, Usuario usuario) {
        this.itemDoacao = itemsDoacao;
        this.usuario = usuario;
        this.criadoEm = LocalDate.now();
    }
    public Doacao(){};

    public Item getItemDoacao() {
        return itemDoacao;
    }

    public void setItemDoacao(Item itemDoacao) {
        this.itemDoacao = itemDoacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }
}
