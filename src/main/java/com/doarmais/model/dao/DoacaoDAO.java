package com.doarmais.model.dao;

import com.doarmais.model.entities.DoacaoEntity;
import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.TipoItemEntity;
import com.doarmais.model.entities.UsuarioEntity;
import com.doarmais.model.infra.contexto.ConnectionFactory;
import com.doarmais.model.infra.exception.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoacaoDAO extends BaseDAO<DoacaoEntity, Long> {

    @Override
    protected String getTableName() {
        return "doacoes";
    }

    @Override
    protected DoacaoEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(rs.getLong("usuario_id"));
        
        try {
            int doadorNomeIdx = rs.findColumn("doador_nome");
            if (doadorNomeIdx > 0) {
                usuario.setNome(rs.getString("doador_nome"));
            }
        } catch (SQLException ignored) {}

        String nomeItem = rs.getString("nome_item");
        String descItem = "";
        try {
            int descIdx = rs.findColumn("item_descricao");
            if (descIdx > 0) descItem = rs.getString("item_descricao");
        } catch (SQLException ignored) {}
        
        TipoItemEntity tipo = new TipoItemEntity(nomeItem, descItem);
        ItemEntity item = new ItemEntity(tipo, rs.getInt("quantidade"));

        DoacaoEntity doacao = new DoacaoEntity();
        doacao.setId(rs.getLong("id"));
        doacao.setUsuario(usuario);
        doacao.setItemDoacao(item);
        
        Date data = rs.getDate("data_doacao");
        if (data != null) {
            doacao.setCriadoEm(data.toLocalDate());
        }

        return doacao;
    }

    @Override
    public List<DoacaoEntity> buscarTodos() {
        List<DoacaoEntity> lista = new ArrayList<>();

        String sql = "SELECT d.id, d.data_doacao, d.nome_item, d.quantidade, d.usuario_id, " +
                "u.nome as doador_nome, t.descricao as item_descricao " +
                "FROM doacoes d " +
                "INNER JOIN usuarios u ON d.usuario_id = u.id " +
                "INNER JOIN tipos_item t ON d.nome_item = t.nome " +
                "ORDER BY d.data_doacao DESC, d.id DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToEntity(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar doações", e);
        }
    }

    public void salvar(DoacaoEntity doacao) {
        String sql = "INSERT INTO doacoes(usuario_id, nome_item, quantidade, data_doacao) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, doacao.getUsuario().getId());
            stmt.setString(2, doacao.getItemDoacao().getNome().getNome());
            stmt.setInt(3, doacao.getItemDoacao().getQtd());
            stmt.setDate(4, Date.valueOf(doacao.getCriadoEm()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar doação", e);
        }
    }
}
