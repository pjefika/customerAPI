package br.net.gvt.efika.customerAPI.model.service.factory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;

/**
 *
 * @author G0041775
 */
public class FactoryEntitiy {

    public static CustomerCertification createCustLogCertification() {
        return new CustomerCertification();
    }

}
