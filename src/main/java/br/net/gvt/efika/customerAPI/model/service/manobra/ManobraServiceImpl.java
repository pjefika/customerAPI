package br.net.gvt.efika.customerAPI.model.service.manobra;

import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.dao.certification.CertificationDAO;
import br.net.gvt.efika.customerAPI.dao.manobra.ManobraDao;
import br.net.gvt.efika.customerAPI.dao.mongo.FactoryDAO;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.entity.ExceptionLog;
import br.net.gvt.efika.customerAPI.model.entity.ManobraCertification;
import br.net.gvt.efika.customerAPI.model.enums.CertificationType;
import br.net.gvt.efika.customerAPI.model.service.certification.command.AssiaClearViewRunnable;
import br.net.gvt.efika.customerAPI.model.service.certification.command.LogCommand;
import br.net.gvt.efika.customerAPI.model.service.certification.impl.CertificationService;
import br.net.gvt.efika.customerAPI.model.service.certification.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.*;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryCertificationBlock;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryService;
import br.net.gvt.efika.customerAPI.model.service.finder.CustomerFinder;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.efika_customer.model.customer.enums.TipoRede;
import br.net.gvt.efika.fulltest.model.fulltest.FullTest;
import br.net.gvt.efika.fulltest.model.fulltest.FulltestRequest;
import br.net.gvt.efika.fulltest.model.fulltest.SetOntToOltRequest;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.SerialOntGpon;
import br.net.gvt.efika.fulltest.service.config_porta.ConfigPortaService;
import br.net.gvt.efika.fulltest.service.factory.FactoryFulltestService;
import br.net.gvt.efika.fulltest.service.fulltest.FulltestService;
import br.net.gvt.efika.stealer.model.TesteHpna;
import br.net.gvt.efika.stealer.model.tv.request.DiagnosticoHpnaIn;
import br.net.gvt.efika.stealer.service.conf_online.TVService;
import br.net.gvt.efika.stealer.service.factory.FactoryStealerService;
import br.net.gvt.efika.util.thread.EfikaThread;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManobraServiceImpl implements ManobraService{


    private final ManobraDao manobraDao = FactoryDAO.newManobraDao();

    private EfikaCustomer cust;

    @Override
    public List<ManobraCertification> findManobraByCustomer(EfikaCustomer cust) throws Exception {
        try {
            return FactoryDAO.newManobraDao().findManobraByCustomer(cust);
        } catch (Exception e) {
            FactoryDAO.newExceptionLogDAO().save(new ExceptionLog(e));
            throw new Exception("Falha ao buscar histórico de execuções.");
        }
    }
}
