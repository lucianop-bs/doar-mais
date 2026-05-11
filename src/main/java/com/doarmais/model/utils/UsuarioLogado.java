package com.doarmais.model.utils;

import com.doarmais.model.entities.UsuarioEntity;

public class UsuarioLogado {

    public static UsuarioEntity usuarioLogado;

    public static UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(UsuarioEntity usuarioLogado) {
        UsuarioLogado.usuarioLogado = usuarioLogado;
    }
}


