package com.doarmais.model.service;

import com.doarmais.model.domain.Doacao;
import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.DoacaoRepository;
import com.doarmais.model.infra.repositorios.ItemRepository;
import com.doarmais.model.infra.repositorios.UsuarioRepository;

import java.util.List;

public class CriarDoacaoService {
    private ItemRepository itemRepository;

    private UsuarioRepository usuarioRepository;
    private DoacaoRepository doacaoRepository;

    public CriarDoacaoService(ItemRepository itemRepository, UsuarioRepository usuarioRepository, DoacaoRepository doacaoRepository) {
        this.itemRepository = itemRepository;
        this.usuarioRepository = usuarioRepository;
        this.doacaoRepository = doacaoRepository;
    }

    public void executar(List<Item> itemsDoacao, Usuario usuario) {
        for (Item item : itemsDoacao) {
            Doacao doacao = new Doacao(item, usuario);
            doacaoRepository.adicionarDoacao(doacao);
            itemRepository.adicionarItem(item);
        }

    }
}
