package br.net.gvt.efika.customerAPI.rest;

import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.service.certification.command.LogCommand;
import br.net.gvt.efika.customerAPI.model.service.certification.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierHpnaCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryCertificationBlock;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryService;
import br.net.gvt.efika.customerAPI.rest.factories.CustomerApiServiceFactory;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.stealer.model.tv.DecoderTV;
import br.net.gvt.efika.stealer.model.tv.request.DiagnosticoHpnaIn;
import br.net.gvt.efika.stealer.service.conf_online.TVService;
import br.net.gvt.efika.stealer.service.factory.FactoryStealerService;
import br.net.gvt.efika.util.thread.EfikaThread;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("/customer")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-01-04T13:39:04.668Z")
public class CustomerApi {

    private final CustomerApiService delegate = CustomerApiServiceFactory.getCustomerApi();

    @POST
    @Path("/findByParameter")
    @Produces({"application/json", "application/xml"})
    public Response findByParameter(GenericRequest body, @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.findByParameter(body, securityContext);
    }

    @POST
    @Path("/pack")
    @Produces({"application/json", "application/xml"})
    public Response findBeta(GenericRequest body, @Context SecurityContext securityContext){
        EfikaCustomer cust = null;
        CustomerCertification certification;
        TVService tvDAO = FactoryStealerService.tvService();
        try {
            certification = CustomerCertificationOperator.start(body);
            if (body.getCustomer() == null) {
                cust = FactoryService.customerFinder().getCustomer(body);
            } else {
                cust = body.getCustomer();
            }
            certification.setCustomer(cust);

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


                        DiagnosticoHpnaIn diagnosticoHpnaIn = nN;
                        List<DecoderTV> mM = tvDAO.diagnosticoHpna(diagnosticoHpnaIn);


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
            threadHpna.join();

        }catch (Exception e){
            e.printStackTrace();
        }




        return null;
    }
}
