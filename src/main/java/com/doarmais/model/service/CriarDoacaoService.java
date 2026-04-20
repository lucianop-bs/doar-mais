package com.doarmais.model.service;

import com.doarmais.model.domain.Doacao;
import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.DoacaoRepository;
import com.doarmais.model.infra.repositorios.ItemRepository;

public class CriarDoacaoService {
    private final ItemRepository itemRepository;

    private final DoacaoRepository doacaoRepository;

    public CriarDoacaoService(ItemRepository itemRepository, DoacaoRepository doacaoRepository) {
        this.itemRepository = itemRepository;
        this.doacaoRepository = doacaoRepository;
    }

    public void doar(Item itemsDoacao, Usuario usuario) {
        Doacao doacao = new Doacao(itemsDoacao, usuario);
            doacaoRepository.adicionarDoacao(doacao);
        itemRepository.adicionarItem(itemsDoacao);

    }
}
