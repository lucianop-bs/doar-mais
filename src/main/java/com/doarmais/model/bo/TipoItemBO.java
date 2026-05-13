package com.doarmais.model.bo;

import com.doarmais.model.dao.TipoItemDAO;
import com.doarmais.model.entities.TipoItemEntity;

import java.util.List;
import java.util.Optional;

public class TipoItemBO {

    private final TipoItemDAO tipoItemDAO;

    public TipoItemBO(TipoItemDAO tipoItemDAO) {
        this.tipoItemDAO = tipoItemDAO;
    }

    public List<TipoItemEntity> listarTodos() {
        return tipoItemDAO.buscarTodos();
    }

    public Optional<TipoItemEntity> buscarPorNome(String nome) {
        return tipoItemDAO.buscarPorNome(nome);
    }

    public void salvar(TipoItemEntity tipo) {
        if (tipo.getNome() == null || tipo.getNome().isBlank()) {
            throw new com.doarmais.model.infra.exception.NegocioException("Nome do item é obrigatório.");
        }
        tipoItemDAO.salvar(tipo);
    }
}
