/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.certification.operator;

import br.net.gvt.efika.customerAPI.dao.certification.CertificationDAO;
import br.net.gvt.efika.customerAPI.dao.mongo.FactoryDAO;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.enums.CertificationType;
import br.net.gvt.efika.customerAPI.model.service.certificator.CertifierCustomerCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.certificator.FkIdGenerator;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryEntitiy;
import java.util.Calendar;

public class CustomerCertificationOperator {

    private static CertificationDAO dao = FactoryDAO.newCertificationDAO();

    public static CustomerCertification start(GenericRequest req) throws Exception {
        CustomerCertification certification = FactoryEntitiy.createCustLogCertification();
        certification.setExecutor(req.getExecutor());
        certification.setTipo(req.getTipoCertificao());
        dao.save(certification);
        return certification;
    }

    public static CustomerCertification update(CustomerCertification certification) throws Exception {
        dao.save(certification);
        return certification;
    }

    public static CustomerCertification conclude(CustomerCertification certification) throws Exception {
        new CertifierCustomerCertificationImpl().certify(certification);
        certification.setDataFim(Calendar.getInstance().getTime());
        if(certification.getFkId() == null){
            certification.setFkId(FkIdGenerator.generate(certification));
        }
        dao.save(certification);
        return certification;
    }

    public static CustomerCertification run(CustomerCertification certification) throws Exception {
        switch (certification.getTipo()) {
            case BANDA :
                break;
            case TV:
                break;
            case VOZ:
                break;
            default:
                break;
        }
        return certification;
    }

}
