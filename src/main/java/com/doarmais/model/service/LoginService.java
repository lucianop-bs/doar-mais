package com.doarmais.model.service;

import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.UsuarioRepository;

public class LoginService {
    private final UsuarioRepository usuarioRepository;

    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario autenticar(String email, String senha) {
        Usuario user = usuarioRepository.login(email, senha);

        if (user == null) {
            throw new RuntimeException("Usuário ou senha incorretos");
        }
        return usuarioRepository.login(email, senha);

    }

}
