package br.net.gvt.efika.customerAPI.dao.manobra;

import br.net.gvt.efika.customerAPI.model.entity.LogManobra;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;

import java.util.List;

/**
 * Tiago Henrique Iwamoto
 * tiago.iwamoto@telefonica.com
 * System Analyst
 * 41 9 9513-0230
 **/
public interface ManobraDao {

    public List<LogManobra> findManobraByCustomer(EfikaCustomer cust) throws Exception;

}
