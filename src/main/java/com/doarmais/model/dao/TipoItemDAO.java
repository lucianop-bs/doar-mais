package com.doarmais.model.dao;

import com.doarmais.model.entities.TipoItemEntity;
import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoItemDAO extends BaseDAO<TipoItemEntity, Integer> {

    @Override
    protected String getTableName() {
        return "tipos_item";
    }

    @Override
    protected TipoItemEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new TipoItemEntity(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao")
        );
    }

    @Override
    public List<TipoItemEntity> buscarTodos() {
        List<TipoItemEntity> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_item ORDER BY descricao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar tipos de itens", e);
        }
        return lista;
    }

    public Optional<TipoItemEntity> buscarPorNome(String nome) {
        String sql = "SELECT * FROM tipos_item WHERE nome = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar tipo de item por nome", e);
        }
        return Optional.empty();
    }

    public void salvar(TipoItemEntity tipo) {
        String sql = "INSERT INTO tipos_item (nome, descricao) VALUES (?, ?) ON CONFLICT (nome) DO UPDATE SET descricao = EXCLUDED.descricao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getNome());
            stmt.setString(2, tipo.getDescricao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar tipo de item", e);
        }
    }
}
