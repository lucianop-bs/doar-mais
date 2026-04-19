package com.doarmais.model.service;

import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.UsuarioRepository;

public class LoginService {
    private final UsuarioRepository usuarioRepository;

    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.login(email, senha)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos."));
        return true;
    }

}
