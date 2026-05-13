package com.doarmais.model.bo;

import com.doarmais.model.dao.DistribuicaoDAO;
import com.doarmais.model.dao.DoacaoDAO;
import com.doarmais.model.dao.EstoqueDAO;
import com.doarmais.model.entities.*;
import com.doarmais.model.infra.exception.NegocioException;

import java.util.List;

public class DoacaoBO {

    private final DoacaoDAO doacaoDAO;
    private final EstoqueDAO estoqueDAO;
    private final DistribuicaoDAO distribuicaoDAO;
    private final TipoItemBO tipoItemBO;

    public DoacaoBO(DoacaoDAO doacaoDAO, EstoqueDAO estoqueDAO, DistribuicaoDAO distribuicaoDAO, TipoItemBO tipoItemBO) {
        this.doacaoDAO = doacaoDAO;
        this.estoqueDAO = estoqueDAO;
        this.distribuicaoDAO = distribuicaoDAO;
        this.tipoItemBO = tipoItemBO;
    }

    public void doar(ItemEntity item, UsuarioEntity usuario) {
        if (item.getQtd() <= 0) {
            throw new NegocioException("A quantidade deve ser maior que zero.");
        }
        if (usuario == null) {
            throw new NegocioException("Usuário não autenticado.");
        }

        DoacaoEntity doacao = new DoacaoEntity();
        doacao.setUsuario(usuario);
        doacao.setItemDoacao(item);
        doacao.setCriadoEm(java.time.LocalDate.now());

        doacaoDAO.salvar(doacao);
        estoqueDAO.atualizarQuantidade(item.getNome().getNome(), item.getQtd());
    }

    public List<DoacaoEntity> listarTodas() {
        return doacaoDAO.buscarTodos();
    }

    public List<DistribuicaoEntity> listarDistribuicoes() {
        return distribuicaoDAO.buscarTodos();
    }

    public void doarLote(List<ItemEntity> itens, UsuarioEntity usuario) {
        if (itens == null || itens.isEmpty()) {
            throw new NegocioException("A lista de doação está vazia.");
        }
        for (ItemEntity item : itens) {
            doar(item, usuario);
        }
    }

    public void removerDoacao(Long id) {
        DoacaoEntity doacao = doacaoDAO.buscarPorId(id)
                .orElseThrow(() -> new NegocioException("Doação não encontrada."));

        // Validação: Não permitir remover se o item já foi usado em uma cesta (estoque ficaria negativo)
        List<EstoqueEntity> estoqueAtual = obterEstoqueAtual();
        int qtdEstoque = estoqueAtual.stream()
                .filter(e -> e.getTipoItem().getNome().equals(doacao.getItemDoacao().getNome().getNome()))
                .mapToInt(EstoqueEntity::getQuantidade)
                .findFirst()
                .orElse(0);

        if (doacao.getItemDoacao().getQtd() > qtdEstoque) {
            throw new NegocioException("Não é possível remover esta doação pois os itens já foram distribuídos em cestas.");
        }

        estoqueDAO.atualizarQuantidade(doacao.getItemDoacao().getNome().getNome(), -doacao.getItemDoacao().getQtd());
        doacaoDAO.remover(id);
    }

    public List<EstoqueEntity> obterEstoqueAtual() {
        return estoqueDAO.listarTudo();
    }

    public int calcularCestasBasicas() {
        List<EstoqueEntity> estoque = obterEstoqueAtual();
        List<TipoItemEntity> todosTipos = tipoItemBO.listarTodos();
        
        if (estoque.size() < todosTipos.size()) return 0;

        int menorQuantidade = Integer.MAX_VALUE;
        for (EstoqueEntity item : estoque) {
            if (item.getQuantidade() < menorQuantidade) {
                menorQuantidade = item.getQuantidade();
            }
        }
        return (menorQuantidade == Integer.MAX_VALUE) ? 0 : menorQuantidade;
    }

    public void distribuirCestas(int quantidade, String beneficiario, UsuarioEntity usuario) {
        if (quantidade <= 0) throw new NegocioException("Quantidade de cestas deve ser maior que zero.");
        if (beneficiario == null || beneficiario.isBlank()) throw new NegocioException("Nome do beneficiário é obrigatório.");
        
        int cestasPossiveis = calcularCestasBasicas();
        if (quantidade > cestasPossiveis) {
            throw new NegocioException("Estoque insuficiente para distribuir " + quantidade + " cestas.");
        }

        List<TipoItemEntity> todosTipos = tipoItemBO.listarTodos();
        for (TipoItemEntity tipo : todosTipos) {
            estoqueDAO.atualizarQuantidade(tipo.getNome(), -quantidade);
        }
        
        distribuicaoDAO.salvar(new DistribuicaoEntity(beneficiario, quantidade, usuario));
    }
}
