/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.certification.command;

import br.net.gvt.efika.customerAPI.dao.request.AssiaGponRequest;
import br.net.gvt.efika.customerAPI.dao.request.RequestFactory;
import br.net.gvt.efika.customerAPI.model.badpractice.assia.AssiaClearViewIn;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author G0041775
 */
public class AssiaClearViewRunnable implements Runnable {

    private final AssiaGponRequest dao = RequestFactory.assiaGponRequest();

    private String usuario, designador;

    public AssiaClearViewRunnable(String usuario, String designador) {
        this.usuario = usuario;
        this.designador = designador;
    }

    @Override
    public void run() {
        try {
            AssiaClearViewIn in = new AssiaClearViewIn();
            in.setExecutor(usuario);
            in.setDesignador(designador);
            dao.clearViewRealTime(in);
        } catch (Exception ex) {
            Logger.getLogger(AssiaClearViewRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDesignador() {
        return designador;
    }

    public void setDesignador(String designador) {
        this.designador = designador;
    }

}
