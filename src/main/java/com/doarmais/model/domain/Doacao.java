package com.doarmais.model.domain;

import java.time.LocalDateTime;

public class Doacao {
    private Item itemDoacao;
    private Usuario usuario;
    private LocalDateTime criadoEm;

    public Doacao(Item itemsDoacao, Usuario usuario) {
        this.itemDoacao = itemsDoacao;
        this.usuario = usuario;
        this.criadoEm = LocalDateTime.now();
    }

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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
