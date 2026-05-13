package com.doarmais.util;

import com.doarmais.model.dao.DistribuicaoDAO;
import com.doarmais.model.dao.DoacaoDAO;
import com.doarmais.model.dao.EstoqueDAO;
import com.doarmais.model.dao.TipoItemDAO;
import com.doarmais.model.dao.UsuarioDAO;
import com.doarmais.model.bo.DoacaoBO;
import com.doarmais.model.bo.TipoItemBO;
import com.doarmais.model.bo.UsuarioBO;


public class BOFactory {

    private static UsuarioDAO usuarioDAO;
    private static UsuarioBO usuarioBO;
    private static DoacaoDAO doacaoDAO;
    private static DoacaoBO doacaoBO;
    private static EstoqueDAO estoqueDAO;
    private static DistribuicaoDAO distribuicaoDAO;
    private static TipoItemDAO tipoItemDAO;
    private static TipoItemBO tipoItemBO;

    public static UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public static UsuarioBO getUsuarioBO() {
        if (usuarioBO == null) {
            usuarioBO = new UsuarioBO(getUsuarioDAO());
        }
        return usuarioBO;
    }

    public static DoacaoDAO getDoacaoDAO() {
        if (doacaoDAO == null) {
            doacaoDAO = new DoacaoDAO();
        }
        return doacaoDAO;
    }

    public static EstoqueDAO getEstoqueDAO() {
        if (estoqueDAO == null) {
            estoqueDAO = new EstoqueDAO();
        }
        return estoqueDAO;
    }

    public static DistribuicaoDAO getDistribuicaoDAO() {
        if (distribuicaoDAO == null) {
            distribuicaoDAO = new DistribuicaoDAO();
        }
        return distribuicaoDAO;
    }

    public static TipoItemDAO getTipoItemDAO() {
        if (tipoItemDAO == null) {
            tipoItemDAO = new TipoItemDAO();
        }
        return tipoItemDAO;
    }

    public static TipoItemBO getTipoItemBO() {
        if (tipoItemBO == null) {
            tipoItemBO = new TipoItemBO(getTipoItemDAO());
        }
        return tipoItemBO;
    }

    public static DoacaoBO getDoacaoBO() {
        if (doacaoBO == null) {
            doacaoBO = new DoacaoBO(getDoacaoDAO(), getEstoqueDAO(), getDistribuicaoDAO(), getTipoItemBO());
        }
        return doacaoBO;
    }
}
