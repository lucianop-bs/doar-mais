package com.doarmais.model.service;


import com.doarmais.model.infra.repositorios.DoacaoRepository;

public class CriarCestaBasicaService {
    private DoacaoRepository doacaoRepository;

    public CriarCestaBasicaService(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    public Integer criarCesta()
    {
        var doacoes = doacaoRepository.getListaDoacao();
        for (var doacao : doacoes) {
            var items = doacao.getItemDoacao();
        }
        return doacaoRepository.getListaDoacao().size();
    }
}
