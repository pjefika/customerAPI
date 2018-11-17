/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.rest.badpractice;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;

/**
 *
 * @author G0041775
 */
public class DiagnosticoHpnaIn {

    private EfikaCustomer ec;

    private String executor;

    public DiagnosticoHpnaIn(EfikaCustomer ec, String executor) {
        this.ec = ec;
        this.executor = executor;
    }

    public EfikaCustomer getEc() {
        return ec;
    }

    public void setEc(EfikaCustomer ec) {
        this.ec = ec;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

}
