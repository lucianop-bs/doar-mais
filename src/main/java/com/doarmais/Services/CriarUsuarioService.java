package com.doarmais.Services;


import com.doarmais.Domain.Usuario;
import com.doarmais.Infra.Repositorios.UsuarioRepository;

public class CriarUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public CriarUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void executar(String nome, String email, String senha) {
        if (usuarioRepository.buscarPorEmail(email) != null) {
            throw new RuntimeException("Usuário já cadastrado com este e-mail.");
        }

        Usuario novoUsuario = new Usuario(nome,email,senha);

        usuarioRepository.salvar(novoUsuario);
    }
}
