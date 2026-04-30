package com.doarmais.model.service;

import com.doarmais.model.domain.Doacao;
import com.doarmais.model.domain.Item;
import com.doarmais.model.domain.Usuario;
import com.doarmais.model.infra.repositorios.DoacaoRepository;


public class CriarDoacaoService {

    private final DoacaoRepository doacaoRepository;

    public CriarDoacaoService( DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    public void doar(Item itemsDoacao, Usuario usuario) {
        Doacao doacao = new Doacao(itemsDoacao, usuario);
            doacaoRepository.salvar(doacao);
    }
}
