/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.certification;

import br.net.gvt.efika.customer.EfikaCustomer;
import java.util.List;
import model.entity.CustomerCertification;

/**
 *
 * @author g0042204
 */
public interface CertificationDAO {

    public List<CustomerCertification> findByCustomer(EfikaCustomer cust) throws Exception;

}