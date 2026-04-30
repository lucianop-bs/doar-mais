package com.doarmais.model.service;


import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.UsuarioRepository;

import java.lang.reflect.Executable;
import java.util.Optional;

public class CriarUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public CriarUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean criar(String nome, String email, String senha) throws Exception {

        Usuario user = usuarioRepository.buscarPorEmail(email);

        if(user != null) {
            throw new Exception("Usuario ja possui cadastro");
        }
        Usuario novoUsuario = new Usuario(nome,email,senha);

        usuarioRepository.salvar(novoUsuario);
        return true;
    }
}
