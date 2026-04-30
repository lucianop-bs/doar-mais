package com.doarmais.model.infra.repositorios;

import com.doarmais.model.domain.Doacao;
import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.ItemDoacao;
import com.doarmais.model.domain.Usuario;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoacaoRepository {

    private Connection conexao;

    public DoacaoRepository(Connection conexao) {
        this.conexao = conexao;
    }

    public void salvar(Doacao doacao) {
        String sql = "INSERT INTO doacoes(usuario_id,nome_item,quantidade) VALUES (?,?,?)";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setLong(1, doacao.getUsuario().getId());
            stmt.setString(2, doacao.getItemDoacao().getNome().name());
            stmt.setInt(3,doacao.getItemDoacao().getQtd());

            stmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public List<Doacao> buscarTodos() {
        List<Doacao> lista = new ArrayList<>();

        String sql = "SELECT d.id as id_doacao,d.data_doacao,d.nome_item,d.quantidade," +
                "u.id as doador_id,u.nome as doador_nome " +
                "FROM doacoes d " +
                "Inner Join usuarios u on d.usuario_id = u.id";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next())
            {

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id_doacao"));
                usuario.setNome(rs.getString("nome_item"));

                ItemDoacao tipoEnum = ItemDoacao.valueOf(rs.getString("nome_item"));
                Item item = new Item(tipoEnum, rs.getInt("quantidade"));

                Doacao doacao = new Doacao();

                doacao.setId(rs.getLong("id_doacao"));
                doacao.setUsuario(usuario);
                doacao.setItemDoacao(item);
                doacao.setCriadoEm(rs.getDate("data_doacao").toLocalDate());

                lista.add(doacao);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
