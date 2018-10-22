package br.net.gvt.efika.customerAPI.model.service.manobra;

import br.net.gvt.efika.customerAPI.dao.manobra.ManobraDao;
import br.net.gvt.efika.customerAPI.dao.mongo.FactoryDAO;
import br.net.gvt.efika.customerAPI.model.entity.ExceptionLog;
import br.net.gvt.efika.customerAPI.model.entity.LogManobra;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;

import java.util.List;

public class ManobraServiceImpl implements ManobraService{


    private final ManobraDao manobraDao = FactoryDAO.newManobraDao();

    private EfikaCustomer cust;

    @Override
    public List<LogManobra> findManobraByCustomer(EfikaCustomer cust) throws Exception {
        try {
            return FactoryDAO.newManobraDao().findManobraByCustomer(cust);
        } catch (Exception e) {
            FactoryDAO.newExceptionLogDAO().save(new ExceptionLog(e));
            throw new Exception("Falha ao buscar histórico de execuções.");
        }
    }
}
