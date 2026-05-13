package com.doarmais.model.dao;

import com.doarmais.model.entities.DistribuicaoEntity;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribuicaoDAO extends BaseDAO<DistribuicaoEntity, Long> {

    @Override
    protected String getTableName() {
        return "distribuicoes";
    }

    @Override
    protected DistribuicaoEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        DistribuicaoEntity d = new DistribuicaoEntity();
        d.setId(rs.getLong("id"));
        d.setBeneficiario(rs.getString("beneficiario"));
        d.setQuantidadeCestas(rs.getInt("quantidade_cestas"));
        d.setDataDistribuicao(rs.getDate("data_distribuicao").toLocalDate());
        
        UsuarioEntity u = new UsuarioEntity();
        u.setId(rs.getLong("usuario_id"));
        try {
            int nomeIdx = rs.findColumn("usuario_nome");
            if (nomeIdx > 0) {
                u.setNome(rs.getString("usuario_nome"));
            }
        } catch (SQLException ignored) {}
        d.setUsuario(u);
        
        return d;
    }

    @Override
    public List<DistribuicaoEntity> buscarTodos() {
        List<DistribuicaoEntity> lista = new ArrayList<>();
        String sql = "SELECT d.*, u.nome as usuario_nome FROM distribuicoes d " +
                     "INNER JOIN usuarios u ON d.usuario_id = u.id " +
                     "ORDER BY d.data_distribuicao DESC, d.id DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar distribuições", e);
        }
        return lista;
    }

    public void salvar(DistribuicaoEntity d) {
        String sql = "INSERT INTO distribuicoes (beneficiario, quantidade_cestas, usuario_id, data_distribuicao) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, d.getBeneficiario());
            stmt.setInt(2, d.getQuantidadeCestas());
            stmt.setLong(3, d.getUsuario().getId());
            stmt.setDate(4, Date.valueOf(d.getDataDistribuicao()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar distribuição", e);
        }
    }
}
