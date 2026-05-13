package com.doarmais.model.dao;

import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.*;
import java.util.Optional;

public class UsuarioDAO extends BaseDAO<UsuarioEntity, Long> {

    @Override
    protected String getTableName() {
        return "usuarios";
    }

    @Override
    protected UsuarioEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setAdmin(rs.getBoolean("isAdmin"));
        usuario.setCriadoEm(rs.getDate("criadoEm").toLocalDate());
        return usuario;
    }

    public Optional<UsuarioEntity> login(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao realizar login", e);
        }

        return Optional.empty();
    }

    public Optional<UsuarioEntity> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por email", e);
        }

        return Optional.empty();
    }

    public void salvar(UsuarioEntity usuario) {
        String sql = "INSERT INTO usuarios(nome, email, senha, isAdmin, criadoEm) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setBoolean(4, usuario.isAdmin());
            stmt.setDate(5, Date.valueOf(usuario.getCriadoEm()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar usuário", e);
        }
    }

    public void atualizar(UsuarioEntity usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, isAdmin = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setBoolean(3, usuario.isAdmin());
            stmt.setLong(4, usuario.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar usuário", e);
        }
    }
}

