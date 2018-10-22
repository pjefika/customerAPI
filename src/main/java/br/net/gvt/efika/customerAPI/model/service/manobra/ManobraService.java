/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.manobra;

import br.net.gvt.efika.customerAPI.model.entity.LogManobra;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;

import java.util.List;

/**
 *
 * @author G0042204
 */
public interface ManobraService {

    public List<LogManobra> findManobraByCustomer(EfikaCustomer cust) throws Exception;

}
