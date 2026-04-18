package com.doarmais.Services;


import com.doarmais.Infra.Repositorios.DoacaoRepository;

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
