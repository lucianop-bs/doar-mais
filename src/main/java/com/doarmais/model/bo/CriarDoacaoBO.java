package com.doarmais.model.bo;

import com.doarmais.model.entities.DoacaoEntity;
import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.dao.DoacaoDAO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;


public class CriarDoacaoBO {

    private final DoacaoDAO doacaoDAO;

    public CriarDoacaoBO( DoacaoDAO doacaoDAO) {
        this.doacaoDAO = doacaoDAO;
    }

    public void doar(ItemEntity itemsDoacao, UsuarioEntity usuario) {
        String username = (usuario != null) ? usuario.getNome() : "desconhecido";
        AuditLogger.logAction("doarBO", username);
        try {
            DoacaoEntity doacao = new DoacaoEntity(itemsDoacao, usuario);
            doacaoDAO.salvar(doacao);
        } catch (Exception e) {
            Logger.logException("doarBO", username, e);
            throw e;
        }
    }
}
