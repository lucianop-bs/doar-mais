package com.doarmais.model.bo;

import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.exception.NegocioException;
import com.doarmais.util.AuditLogger;

import java.util.List;
import java.util.Optional;

public class UsuarioBO {

    private final UsuarioDAO usuarioDAO;

    public UsuarioBO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void cadastrar(UsuarioEntity usuario) {
        validarUsuario(usuario);
        
        Optional<UsuarioEntity> existente = usuarioDAO.buscarPorEmail(usuario.getEmail());
        if (existente.isPresent()) {
            throw new NegocioException("Já existe um usuário cadastrado com este e-mail.");
        }

        usuarioDAO.salvar(usuario);
        AuditLogger.logAction("UsuarioBO.cadastrar", usuario.getEmail());
    }

    public UsuarioEntity autenticar(String email, String senha) {
        return usuarioDAO.login(email, senha)
                .orElseThrow(() -> new NegocioException("E-mail ou senha inválidos."));
    }

    public List<UsuarioEntity> listarTodos() {
        return usuarioDAO.buscarTodos();
    }

    public void excluir(Long id) {
        usuarioDAO.remover(id);
    }

    private void validarUsuario(UsuarioEntity usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new NegocioException("Nome é obrigatório.");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new NegocioException("E-mail inválido.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new NegocioException("A senha deve conter pelo menos 6 caracteres.");
        }
        if (!usuario.getSenha().matches(".*1.*")) {
             throw new NegocioException("A senha deve conter o dígito 1.");
        }
    }
}
