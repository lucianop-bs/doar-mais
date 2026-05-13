package com.doarmais.model.dao;

import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO<T, ID> {
    protected abstract String getTableName();
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    public List<T> buscarTodos() {
        String sql = "SELECT * FROM " + getTableName();
        List<T> entities = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar registros de " + getTableName(), e);
        }

        return entities;
    }

    public Optional<T> buscarPorId(ID id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar registro por ID em " + getTableName(), e);
        }
        
        return Optional.empty();
    }

    public void remover(ID id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover registro de " + getTableName(), e);
        }
    }
}
