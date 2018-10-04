/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.manobra;

import br.net.gvt.efika.customerAPI.dao.certification.CertificationDAO;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.entity.ManobraCertification;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.mongo.dao.AbstractMongoDAO;
import br.net.gvt.efika.mongo.dao.MongoEndpointEnum;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

/**
 *
 * @author G0041775
 */
public class ManobraDAOImpl extends AbstractMongoDAO<ManobraCertification> implements ManobraDao {

    public ManobraDAOImpl() {
        super(MongoEndpointEnum.MONGO.getIp(), "fulltestAPI", ManobraCertification.class);
    }
    
    @Override
    public List<ManobraCertification> findManobraByCustomer(EfikaCustomer cust) throws Exception {
        return getDatastore().createQuery(ManobraCertification.class)
                .field("customer.instancia")
                .equal(cust.getInstancia())
                .order("datahora")
                .asList(new FindOptions()
                        .limit(10));
    }
    
}
