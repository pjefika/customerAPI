package br.net.gvt.efika.customerAPI.model.service.certification.impl;

import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.dao.certification.CertificationDAO;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.customerAPI.dao.mongo.FactoryDAO;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.net.gvt.efika.customerAPI.model.entity.ExceptionLog;
import br.net.gvt.efika.customerAPI.model.entity.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certification.command.AssiaClearViewRunnable;
import br.net.gvt.efika.customerAPI.model.service.certification.command.LogCommand;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierCadastroCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierConectividadeCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierPerformanceCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierServicosCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryCertificationBlock;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryService;
import br.net.gvt.efika.customerAPI.model.service.finder.CustomerFinder;
import br.net.gvt.efika.efika_customer.model.customer.enums.TipoRede;
import br.net.gvt.efika.fulltest.model.fulltest.FullTest;
import br.net.gvt.efika.fulltest.model.fulltest.FulltestRequest;
import br.net.gvt.efika.fulltest.model.fulltest.SetOntToOltRequest;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.fulltest.service.factory.FactoryFulltestService;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.SerialOntGpon;
import br.net.gvt.efika.fulltest.service.fulltest.FulltestService;
import br.net.gvt.efika.fulltest.service.config_porta.ConfigPortaService;
import br.net.gvt.efika.util.thread.EfikaThread;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CertificationServiceImpl implements CertificationService {

    private final CustomerFinder finder = FactoryService.customerFinder();
    private CustomerCertification certification;
    private final FulltestService ftDAO = FactoryFulltestService.newFulltestService();
    private final ConfigPortaService confPortaDAO = FactoryFulltestService.newConfigPortaService();
    private final CertificationDAO dao = FactoryDAO.newCertificationDAO();

    private EfikaCustomer cust;

    @Override
    public CustomerCertification certificationByParam(GenericRequest req) throws Exception {
        certification = CustomerCertificationOperator.start(req);
        Thread certThread = new EfikaThread(new LogCommand(certification) {
            @Override
            public void run() {
                try {
                    if (req.getCustomer() == null) {
                        cust = FactoryService.customerFinder().getCustomer(req);
                    } else {
                        cust = req.getCustomer();
                    }
                    if (cust.getDesignador() != null && cust.getRede().getTipo() == TipoRede.GPON) {
                        Thread assiaGpon = new Thread(new AssiaClearViewRunnable(req.getExecutor(), cust.getDesignador()));
                        assiaGpon.start();
                    }
                } catch (Exception e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                }
                certification.setCustomer(cust);
                try {
                    CertificationBlock cadastro = FactoryCertificationBlock.createBlockByName(CertificationBlockName.CADASTRO);
                    new CertifierCadastroCertificationImpl(cust).certify(cadastro);
                    certification.getBlocks().add(cadastro);

                    if (cadastro.getResultado() == CertificationResult.OK) {
                        FullTest fulltest = ftDAO.fulltest(new FulltestRequest(cust, req.getExecutor()));
                        certification.setFulltest(fulltest);
                        CustomerCertificationOperator.update(certification);
//                        Thread.sleep(50000);
                        for (int i = 0; i < 15; i++) {
                            certification.setFulltest(ftDAO.getById(fulltest.getOwner()));
                            CustomerCertificationOperator.update(certification);
                            if (certification.getFulltest().getDataFim() != null) {
                                break;
                            }
                            Thread.sleep(10000);
                        }

                        Thread threadPerf = new Thread(new LogCommand(certification) {
                            @Override
                            public void run() {
                                try {
                                    CertificationBlock perfBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.PERFORMANCE);
                                    new CertifierPerformanceCertificationImpl(certification.getFulltest()).certify(perfBlock);
                                    certification.getBlocks().add(perfBlock);
                                } catch (Exception e) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                                }
                            }
                        });

                        Thread threadConnect = new Thread(new LogCommand(certification) {
                            @Override
                            public void run() {
                                try {
                                    CertificationBlock connectBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.CONECTIVIDADE);
                                    new CertifierConectividadeCertificationImpl(certification.getFulltest()).certify(connectBlock);
                                    certification.getBlocks().add(connectBlock);
                                } catch (Exception e) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                                }
                            }
                        });

                        Thread threadServ = new Thread(new LogCommand(certification) {
                            @Override
                            public void run() {
                                try {
                                    CertificationBlock servBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.SERVICOS);
                                    new CertifierServicosCertificationImpl(certification.getFulltest()).certify(servBlock);
                                    certification.getBlocks().add(servBlock);
                                } catch (Exception e) {
                                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                                }
                            }
                        });

                        threadPerf.start();
                        threadConnect.start();
                        threadServ.start();

                        threadPerf.join();
                        threadConnect.join();
                        threadServ.join();
                    }
                    certification = CustomerCertificationOperator.conclude(certification);
                } catch (Exception e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        });
        if (certification.getTipo() == null) {
            certThread.join();
        }
        return certification;
    }

    @Override
    public List<CustomerCertification> findByCustomer(EfikaCustomer cust) throws Exception {
        try {
            return FactoryDAO.newCertificationDAO().findByCustomer(cust);
        } catch (Exception e) {
            FactoryDAO.newExceptionLogDAO().save(new ExceptionLog(e));
            throw new Exception("Falha ao buscar histórico de execuções.");
        }
    }
    private ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
            true);

    @Override
    public ValidacaoResult certifyRede(GenericRequest req) throws Exception {
        if (req.getCustomer() == null) {
            cust = finder.getCustomer(req);
        } else {
            cust = req.getCustomer();
        }
        ValidacaoResult confRede = confPortaDAO.confiabilidadeRede(new FulltestRequest(cust, req.getExecutor()));

        return confRede;
    }

    @Override
    public List<SerialOntGpon> ontsDisp(GenericRequest req) throws Exception {
        if (req.getCustomer() == null) {
            cust = finder.getCustomer(req);
        } else {
            cust = req.getCustomer();
        }
        List<SerialOntGpon> ontsDisp = mapper.convertValue(confPortaDAO.ontsDisponiveis(new FulltestRequest(cust, req.getExecutor())), new TypeReference<List<SerialOntGpon>>() {
        });

        return ontsDisp;
    }

    @Override
    public ValidacaoResult setOntToOlt(GenericRequest req) throws Exception {
        if (req.getCustomer() == null) {
            cust = finder.getCustomer(req);
        } else {
            cust = req.getCustomer();
        }

        ValidacaoResult settedOnt = confPortaDAO.setOntToOlt(new SetOntToOltRequest(cust, req.getExecutor(), new SerialOntGpon(req.getParameter())));

        return settedOnt;
    }

    @Override
    public CustomerCertification findById(String id) throws Exception {
        certification = dao.read(id);
        if (certification.getFulltest() != null) {
            if (certification.getFulltest().getDataFim() == null) {
                certification.setFulltest(ftDAO.getById(certification.getFulltest().getOwner()));
            }
        }

        return certification;
    }

}
