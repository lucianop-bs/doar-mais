package com.doarmais.model.dao;

import com.doarmais.model.entities.EstoqueEntity;
import com.doarmais.model.entities.TipoItemEntity;
import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    public List<EstoqueEntity> listarTudo() {
        List<EstoqueEntity> lista = new ArrayList<>();
        String sql = "SELECT e.*, t.descricao FROM estoque e " +
                     "INNER JOIN tipos_item t ON e.nome_item = t.nome " +
                     "ORDER BY t.descricao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoItemEntity tipo = new TipoItemEntity(
                        rs.getString("nome_item"),
                        rs.getString("descricao")
                );
                lista.add(new EstoqueEntity(tipo, rs.getInt("quantidade")));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar estoque", e);
        }
        return lista;
    }

    public void atualizarQuantidade(String nomeItem, int delta) {
        String sql = "UPDATE estoque SET quantidade = quantidade + ? WHERE nome_item = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, delta);
            stmt.setString(2, nomeItem);
            
            int rows = stmt.executeUpdate();
            if (rows == 0 && delta > 0) {
                // Se o item não existir e for entrada, tenta inserir
                inserirNovoItem(nomeItem, delta);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar estoque", e);
        }
    }

    private void inserirNovoItem(String nomeItem, int quantidade) throws SQLException {
        String sql = "INSERT INTO estoque (nome_item, quantidade) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeItem);
            stmt.setInt(2, Math.max(0, quantidade));
            stmt.executeUpdate();
        }
    }
}
