package com.doarmais.model.dao;


import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.contexto.DbContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO {
    private final Connection conexao;

    public UsuarioDAO() {
        this.conexao = new DbContext().conectar();
    }

    public List<UsuarioEntity> buscarTodos() {
        String sql = "SELECT * FROM usuarios";
        List<UsuarioEntity> usuarios = new ArrayList<>();

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAdmin(rs.getBoolean("isAdmin"));
                usuario.setCriadoEm(rs.getDate("criadoEm").toLocalDate());
                usuarios.add(usuario);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    public UsuarioEntity login(String email, String senha)
    {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";


        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAdmin(rs.getBoolean("isAdmin"));
                usuario.setCriadoEm(rs.getDate("criadoEm").toLocalDate());
                return usuario;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fazer login do usuário: " + e.getMessage());
        }

        return null;
    }

    public UsuarioEntity buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAdmin(rs.getBoolean("isAdmin"));
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

    public void salvar(UsuarioEntity usuario) {
        String sql = "INSERT INTO usuarios(nome,email,senha,isAdmin,criadoEm) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setBoolean(4, usuario.isAdmin());
            stmt.setDate(5, java.sql.Date.valueOf(usuario.getCriadoEm()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public void atualizar(UsuarioEntity usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, isAdmin = ? WHERE id = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setBoolean(3, usuario.isAdmin());
            stmt.setLong(4, usuario.getId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void remover(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário: " + e.getMessage());
        }
    }
}
