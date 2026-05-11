package com.doarmais.model.bo;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;

public class CriarUsuarioBO {
    private final UsuarioDAO usuarioDAO;

    public CriarUsuarioBO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean criar(String nome, String email, String senha) throws Exception {
        AuditLogger.logAction("criarUsuarioBO", email);
        try {
            UsuarioEntity user = usuarioDAO.buscarPorEmail(email);

            if(user != null) {
                throw new Exception("UsuarioEntity ja possui cadastro");
            }
            UsuarioEntity novoUsuario = new UsuarioEntity(nome,email,senha);

            usuarioDAO.salvar(novoUsuario);
            return true;
        } catch (Exception e) {
            Logger.logException("criarUsuarioBO", email, e);
            throw e;
        }
    }
}
