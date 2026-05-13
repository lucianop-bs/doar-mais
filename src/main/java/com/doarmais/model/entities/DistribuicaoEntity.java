package com.doarmais.model.entities;

import java.time.LocalDate;

public class DistribuicaoEntity {
    private Long id;
    private String beneficiario;
    private Integer quantidadeCestas;
    private UsuarioEntity usuario;
    private LocalDate dataDistribuicao;

    public DistribuicaoEntity() {}

    public DistribuicaoEntity(String beneficiario, Integer quantidadeCestas, UsuarioEntity usuario) {
        this.beneficiario = beneficiario;
        this.quantidadeCestas = quantidadeCestas;
        this.usuario = usuario;
        this.dataDistribuicao = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Integer getQuantidadeCestas() {
        return quantidadeCestas;
    }

    public void setQuantidadeCestas(Integer quantidadeCestas) {
        this.quantidadeCestas = quantidadeCestas;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataDistribuicao() {
        return dataDistribuicao;
    }

    public void setDataDistribuicao(LocalDate dataDistribuicao) {
        this.dataDistribuicao = dataDistribuicao;
    }
}
