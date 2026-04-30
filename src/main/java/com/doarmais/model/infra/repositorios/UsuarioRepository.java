package com.doarmais.model.infra.repositorios;


import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.contexto.DbContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioRepository {
    private final Connection conexao;

    public UsuarioRepository() {
        this.conexao = new DbContext().conectar();
    }

    public Usuario login(String email, String senha)
    {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        Usuario usuario = new Usuario();

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCriadoEm(rs.getDate("criadoEm").toLocalDate());
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fazer login do usuário: " + e.getMessage());
        }

        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCriadoEm(rs.getDate("criadoEm").toLocalDate());
                return usuario;
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nome,email,senha) VALUES (?,?,?)";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
