/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.manobra;

import br.net.gvt.efika.customerAPI.model.entity.LogManobra;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.mongo.dao.AbstractMongoDAO;
import br.net.gvt.efika.mongo.dao.MongoEndpointEnum;
import org.mongodb.morphia.query.FindOptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G0041775
 */
public class ManobraDAOImpl extends AbstractMongoDAO<LogManobra> implements ManobraDao {

    public ManobraDAOImpl() {
        super(MongoEndpointEnum.MONGO.getIp(), "fulltestAPI", LogManobra.class);
    }
    
    @Override
    public List<LogManobra> findManobraByCustomer(EfikaCustomer cust) {
        try{

            String res = getDatastore().find(LogManobra.class)
                    .disableValidation()
                    .field("customer.instancia")
                    .equal(cust.getInstancia())
                    .order("datahora")
                    .asList(new FindOptions()
                            .limit(10)).toString();

//            String res = getDatastore().createQuery(LogManobra.class)
//                    .disableValidation()
//                    .field("customer.instancia")
//                    .equal(cust.getInstancia())
//                    .order("datahora")
//                    .asList(new FindOptions()
//                            .limit(10)).toString();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
    
}
