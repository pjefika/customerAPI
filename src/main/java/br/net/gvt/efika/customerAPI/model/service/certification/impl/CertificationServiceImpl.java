package br.net.gvt.efika.customerAPI.model.service.certification.impl;

import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.dao.certification.CertificationDAO;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.*;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.customerAPI.dao.mongo.FactoryDAO;
import br.net.gvt.efika.customerAPI.model.GenericRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.net.gvt.efika.customerAPI.model.entity.ExceptionLog;
import br.net.gvt.efika.customerAPI.model.enums.CertificationType;
import br.net.gvt.efika.customerAPI.model.service.certification.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certification.command.AssiaClearViewRunnable;
import br.net.gvt.efika.customerAPI.model.service.certification.command.LogCommand;
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
import br.net.gvt.efika.stealer.model.tv.DecoderTV;
import br.net.gvt.efika.stealer.model.tv.request.DiagnosticoHpnaIn;
import br.net.gvt.efika.stealer.service.conf_online.TVService;
import br.net.gvt.efika.stealer.service.factory.FactoryStealerService;
import br.net.gvt.efika.util.thread.EfikaThread;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpHost;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.JsonObject;
import java.util.Calendar;

public class CertificationServiceImpl implements CertificationService {

    private final CustomerFinder finder = FactoryService.customerFinder();
    private CustomerCertification certification;
    private final FulltestService ftDAO = FactoryFulltestService.newFulltestService();
    private final TVService tvDAO = FactoryStealerService.tvService();
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

                    System.out.println("Resultado: " + cadastro.getResultado());
                    System.out.println("Tipo: " + certification.getTipo());
                    if (cadastro.getResultado() == CertificationResult.OK) {
                        if (certification.getTipo() == CertificationType.TV) {
                            try {
                                System.out.println("AAAAAA");
                                Calendar c = Calendar.getInstance();
                                c.add(Calendar.MINUTE, -10);
                                CustomerCertification lastCert = dao.findByCustomer(cust).get(0);
                                if (lastCert.getDataFim().after(c.getTime()) && lastCert.getFulltest().getResultado()) {
                                    System.out.println("BBBBBB");
                                    certification.setFulltest(lastCert.getFulltest());
                                } else {
                                    System.out.println("CCCCCC");
                                    certification = execFulltest(certification);
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                certification = execFulltest(certification);
                            }
                            EfikaThread threadHpna = new EfikaThread(new LogCommand(certification) {
                                @Override
                                public void run() {
                                    System.out.println("Thread");
                                    CertificationBlock hpnaBlock = null;
                                    try {
                                        hpnaBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.HPNA);
                                        System.out.println("HPNA BLOCK");

                                        DiagnosticoHpnaIn nN = new DiagnosticoHpnaIn(certification.getCustomer(), certification.getExecutor());
                                        System.out.println("Diag: " + nN.toString());
                                        //nN.getEc().setAsserts(new ArrayList<>());

                                        //region TEST UNIREST

                                        //Unirest.setProxy(new HttpHost("192.168.25.89", 8080));

//                                        Gson gson = new GsonBuilder()
//                                                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//
//                                        String nJson = gson.toJson(nN);
//                                        JSONArray oJson = new JSONArray(nJson);
//                                        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:7173/stealerAPI/confOnline/diagnosticoHpna")
//                                                .header("accept", "application/json")
//                                                .header("content-type", "application/json")
//                                                .body(oJson)
//                                                .asJson();
//                                        System.out.println(jsonResponse.getBody());
                                        //endregion
                                        DiagnosticoHpnaIn diagnosticoHpnaIn = nN;
                                        List<DecoderTV> mM = tvDAO.diagnosticoHpna(diagnosticoHpnaIn);
                                        //Object mM = tvDAO.diagnosticoHpna(diagnosticoHpnaIn);
                                        //List<DecoderTV> mM = null;
                                        //System.out.println("SIZE: " + mM.size());
                                        //System.out.println("Dec" + mM.toString());
//                                        if(mM == null){
//                                            mM = new ArrayList<>();
//                                        }
                                        new CertifierHpnaCertificationImpl(mM).certify(hpnaBlock);
                                        certification.getBlocks().add(hpnaBlock);
                                    } catch (Exception e) {
                                        List<DecoderTV> mM = new ArrayList<>();
                                        new CertifierHpnaCertificationImpl(mM).certify(hpnaBlock);
                                        certification.getBlocks().add(hpnaBlock);
                                        //System.out.println("ERRO: " + e.getMessage());
                                        System.out.println("---------------------------------------------");
                                        e.printStackTrace();
                                        System.out.println("---------------------------------------------");
                                        //Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                                    }
                                }
                            });
                            /**
                             * abrir thread para adicionar bloco de
                             * certification do youbora
                             *  PENDENTE ADICIONAR NA STEALER
                             */


                            EfikaThread threadYoubora = new EfikaThread(new LogCommand(certification) {
                                @Override
                                public void run() {
                                    try{
                                        CertificationBlock youboraBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.YOUBORA);
                                        String url =
                                                String.format("https://qos-commands-api.youbora.com/%s/certification/execute_certifications/%s",
                                                        "ob-brazil",
                                                        certification.getCustomer().getInstancia());
                                        Unirest.setProxy(new HttpHost("192.168.25.89", 8080));
                                        new CertifierYouboraCertificationImpl().certify(youboraBlock);
                                        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                                                .header("accept", "application/json")
                                                .header("content-type", "application/json")
                                                .asJson();
                                        System.out.println(jsonResponse.getBody());
                                        certification.getBlocks().add(youboraBlock);
                                    }catch (Exception e){
                                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                                    }
                                }
                            });
                            threadYoubora.join();

                            threadHpna.join();
                        } else {
                            certification = execFulltest(certification);
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

    private CustomerCertification execFulltest(CustomerCertification certification) throws Exception {
        FullTest fulltest = ftDAO.fulltest(new FulltestRequest(certification.getCustomer(), certification.getExecutor()));
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
        return certification;
    }

}
