package com.doarmais.model.service;


import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.UsuarioRepository;

public class CriarUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public CriarUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void executar(String nome, String email, String senha) {
        usuarioRepository.buscarPorEmail(email).ifPresent(u -> {
            throw new RuntimeException("Usuário já cadastrado com este e-mail.");
        });

        Usuario novoUsuario = new Usuario(nome,email,senha);

        usuarioRepository.salvar(novoUsuario);
    }
}
