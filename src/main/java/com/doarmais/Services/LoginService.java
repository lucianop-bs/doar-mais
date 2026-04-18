package com.doarmais.Services;
import com.doarmais.Domain.Usuario;
import com.doarmais.Infra.Repositorios.UsuarioRepository;

public class LoginService {
    private  UsuarioRepository usuarioRepository;

    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean autenticar(String email) {
      Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuário ou senha inválidos.");
        }
        return true;
    }

}
