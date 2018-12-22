package br.net.gvt.efika.customerAPI.rest;

import br.net.gvt.efika.customer.model.certification.CertificationBlock;
import br.net.gvt.efika.customer.model.certification.enums.CertificationBlockName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.service.certification.operator.CustomerCertificationOperator;
import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierHpnaCertificationImpl;
import br.net.gvt.efika.customerAPI.model.service.factory.FactoryCertificationBlock;
import br.net.gvt.efika.customerAPI.rest.factories.CertificationApiServiceFactory;
import br.net.gvt.efika.customerAPI.rest.factories.CustomerApiServiceFactory;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.fulltest.model.fulltest.ConfirmaLeituraInput;
import br.net.gvt.efika.stealer.model.TesteHpna;
import br.net.gvt.efika.customerAPI.rest.badpractice.DiagnosticoHpnaIn;
import br.net.gvt.efika.stealer.service.conf_online.TVService;
import br.net.gvt.efika.stealer.service.factory.FactoryStealerService;
import br.net.gvt.efika.util.dao.http.Urls;
import br.net.gvt.efika.util.dao.http.factory.FactoryHttpDAOAbstract;
import br.net.gvt.efika.util.json.JacksonMapper;
import java.util.HashMap;

import br.net.gvt.efika.util.thread.EfikaThread;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

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
    @Path("/confirmaleitura")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmaLeitura(ConfirmaLeituraInput confirmaLeituraInput, @Context SecurityContext securityContext) throws Exception {
        Response response = null;
        CustomerCertification customerCertification = null;
        try {
            response = nDelegate.getCertificationById(confirmaLeituraInput.getFulltest(), securityContext);
            customerCertification = (CustomerCertification) response.getEntity();
            customerCertification.getFulltest().setLeituraConfirmada(true);
            CustomerCertification certification = customerCertification;
            //Salvar no banco
            customerCertification = CustomerCertificationOperator.conclude(certification);
            return Response.ok(customerCertification).build();
        } catch (Exception e) {
            return Response.ok(null).build();
        }
    }

    @POST
    @Path("/findByParameterTv")
    @Produces({"application/json", "application/xml"})
    public Response findByParameterTv(GenericRequest body, @Context SecurityContext securityContext) {
        System.out.println("Oi");
        CustomerCertification customerCertification = null;
        Response response = null;
        try {
            response = nDelegate.getCertificationById(body.getParameter(), securityContext);
            customerCertification = (CustomerCertification) response.getEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).build();
        }

        EfikaCustomer cust = null;
        CustomerCertification certification = customerCertification;
        TVService tvDAO = FactoryStealerService.tvService();
        try {

            System.out.println("EraThread");
            CertificationBlock hpnaBlock = null;
            TesteHpna testeHpna = null;
            try {
                hpnaBlock = FactoryCertificationBlock.createBlockByName(CertificationBlockName.HPNA);

                DiagnosticoHpnaIn diagnosticoHpnaIn = new DiagnosticoHpnaIn(certification.getCustomer(), certification.getExecutor());

                FactoryHttpDAOAbstract<TesteHpna> h = new FactoryHttpDAOAbstract(TesteHpna.class);

                testeHpna = (TesteHpna) h.createWithoutProxy().post(Urls.DIAGNOSTICO_HPNA_STEALER.getUrl(), diagnosticoHpnaIn);
                hpnaBlock.setResultado(CertificationResult.OK);
                new CertifierHpnaCertificationImpl(testeHpna).certify(hpnaBlock);
                certification.getBlocks().add(hpnaBlock);
            } catch (Exception e) {
                e.printStackTrace();
                hpnaBlock.setResultado(CertificationResult.OK);
                testeHpna = new TesteHpna();
                testeHpna.setSituacao("NOK");
                testeHpna.setMensagem("Não foi possível executar certificação! (Não houve retorno do COL)");
                new CertifierHpnaCertificationImpl(testeHpna).certify(hpnaBlock);
                certification.getBlocks().add(hpnaBlock);
            }
            System.out.println("testehpna -> " + new JacksonMapper(TesteHpna.class).serialize(testeHpna));
            System.out.println("hpnaBlock -> " + new JacksonMapper(CertificationBlock.class).serialize(hpnaBlock));
            customerCertification = CustomerCertificationOperator.conclude(certification);
            return Response.ok(customerCertification).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).build();
        }
    }
}
