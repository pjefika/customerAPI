/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.service_inventory;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;

/**
 *
 * @author G0042204
 */
public interface ServiceInventoryDAO {

    public EfikaCustomer consultar(String instancia) throws Exception;

}
