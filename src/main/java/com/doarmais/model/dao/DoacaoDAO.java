package com.doarmais.model.dao;

import com.doarmais.model.entities.DoacaoEntity;
import com.doarmais.model.entities.ItemEntity;
import com.doarmais.model.entities.ItemDoacaoEntity;
import com.doarmais.model.entities.UsuarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DoacaoDAO {

    private Connection conexao;

    public DoacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void salvar(DoacaoEntity doacao) {
        String sql = "INSERT INTO doacoes(usuario_id, nome_item, quantidade, data_doacao) VALUES (?,?,?,?)";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setLong(1, doacao.getUsuario().getId());
            stmt.setString(2, doacao.getItemDoacao().getNome().name());
            stmt.setInt(3, doacao.getItemDoacao().getQtd());
            stmt.setDate(4, Date.valueOf(doacao.getCriadoEm()));

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar doação: " + e.getMessage());
        }
    }

    public List<DoacaoEntity> buscarTodos() {
        List<DoacaoEntity> lista = new ArrayList<>();

        String sql = "SELECT d.id as id_doacao, d.data_doacao, d.nome_item, d.quantidade, " +
                "u.id as doador_id, u.nome as doador_nome " +
                "FROM doacoes d " +
                "INNER JOIN usuarios u ON d.usuario_id = u.id " +
                "ORDER BY d.data_doacao DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioEntity usuario = new UsuarioEntity();
                usuario.setId(rs.getLong("doador_id"));
                usuario.setNome(rs.getString("doador_nome"));

                String nomeItem = rs.getString("nome_item");
                ItemDoacaoEntity tipoEnum = ItemDoacaoEntity.valueOf(nomeItem);
                ItemEntity item = new ItemEntity(tipoEnum, rs.getInt("quantidade"));

                DoacaoEntity doacao = new DoacaoEntity();
                doacao.setId(rs.getLong("id_doacao"));
                doacao.setUsuario(usuario);
                doacao.setItemDoacao(item);
                
                Date data = rs.getDate("data_doacao");
                if (data != null) {
                    doacao.setCriadoEm(data.toLocalDate());
                }

                lista.add(doacao);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar doações: " + e.getMessage());
        }
    }

    public void remover(Long id) {
        String sql = "DELETE FROM doacoes WHERE id = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover doação: " + e.getMessage());
        }
    }
}
