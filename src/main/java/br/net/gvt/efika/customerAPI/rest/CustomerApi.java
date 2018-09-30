package br.net.gvt.efika.customerAPI.rest;

import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.service.certification.command.LogCommand;
import br.net.gvt.efika.customerAPI.model.service.certification.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierHpnaCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryCertificationBlock;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryService;
import br.net.gvt.efika.customerAPI.rest.factories.CertificationApiServiceFactory;
import br.net.gvt.efika.customerAPI.rest.factories.CustomerApiServiceFactory;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.stealer.model.TesteHpna;
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
    private final CertificationApiService nDelegate = CertificationApiServiceFactory.getCertificationApi();

    @POST
    @Path("/findByParameter")
    @Produces({"application/json", "application/xml"})
    public Response findByParameter(GenericRequest body, @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.findByParameter(body, securityContext);
    }

    @POST
    @Path("/findByParameterTv")
    @Produces({"application/json", "application/xml"})
    public Response findByParameterTv(GenericRequest body, @Context SecurityContext securityContext){
        System.out.println("Oi");
        CustomerCertification customerCertification = null;
        Response response = null;
        try{
            response = nDelegate.getCertificationById(body.getParameter(), securityContext);
            customerCertification = (CustomerCertification) response.getEntity();
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok(e.getMessage()).build();
        }

        EfikaCustomer cust = null;
        CustomerCertification certification = customerCertification;
        TVService tvDAO = FactoryStealerService.tvService();
        try {

            EfikaThread threadHpna = new EfikaThread(new LogCommand(certification) {
                @Override
                public void run() {
                    System.out.println("Thread");
                    CertificationBlock hpnaBlock = null;
                    TesteHpna testeHpna = null;
                    try {
                        hpnaBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.HPNA);

                        DiagnosticoHpnaIn nN = new DiagnosticoHpnaIn(certification.getCustomer(), certification.getExecutor());
                        DiagnosticoHpnaIn diagnosticoHpnaIn = nN;
                        testeHpna = tvDAO.diagnosticoHpna(diagnosticoHpnaIn);
                        new CertifierHpnaCertificationImpl(testeHpna.getStbs()).certify(hpnaBlock);
                        //certification.getBlocks().clear();
                        certification.getBlocks().add(hpnaBlock);
                    } catch (Exception e) {
                        new CertifierHpnaCertificationImpl(testeHpna.getStbs()).certify(hpnaBlock);
                        //certification.getBlocks().clear();
                        certification.getBlocks().add(hpnaBlock);
                        //List<DecoderTV> mM = new ArrayList<>();
                        //new CertifierHpnaCertificationImpl(mM).certify(hpnaBlock);
                        //certification.getBlocks().add(hpnaBlock);
                        //Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                    }
                }
            });
            threadHpna.join();
            customerCertification = CustomerCertificationOperator.conclude(certification);
            return Response.ok(customerCertification).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.ok(e.getMessage()).build();
        }
    }
}
