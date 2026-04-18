package com.doarmais.Services;
import com.doarmais.Domain.Doacao;
import com.doarmais.Domain.Item;
import com.doarmais.Domain.Usuario;
import com.doarmais.Infra.Repositorios.DoacaoRepository;
import com.doarmais.Infra.Repositorios.ItemRepository;
import com.doarmais.Infra.Repositorios.UsuarioRepository;

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
