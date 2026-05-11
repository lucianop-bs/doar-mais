package com.doarmais.model.bo;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;

public class LoginBO {
    private final UsuarioDAO usuarioDAO;

    public LoginBO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public UsuarioEntity autenticar(String email, String senha) {
        AuditLogger.logAction("autenticarBO", email);
        try {
            UsuarioEntity user = usuarioDAO.login(email, senha);

            if (user == null) {
                throw new RuntimeException("Usuário ou senha incorretos");
            }
            return user;
        } catch (Exception e) {
            Logger.logException("autenticarBO", email, e);
            throw e;
        }
    }
}
