package com.doarmais.model.infra.repositorios;


import com.doarmais.model.domain.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private List<Usuario> usuarios = new ArrayList<>();
    {
        usuarios.add(new Usuario("admin","admin","admin"));
    }

    public void salvar(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<Usuario> login(String email, String senha) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha))
                .findFirst();
    }

}
