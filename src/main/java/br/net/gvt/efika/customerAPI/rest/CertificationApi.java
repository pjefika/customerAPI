package br.net.gvt.efika.customerAPI.rest;

import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.customerAPI.rest.factories.CertificationApiServiceFactory;
import br.net.gvt.efika.customerAPI.model.CertificationResponse;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/certification")
public class CertificationApi {

    private final CertificationApiService delegate = CertificationApiServiceFactory.getCertificationApi();

    @POST
    @Path("/execByParam")
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response certification(GenericRequest body, @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.certification(body, securityContext);
    }

    @POST
    @Path("/findByCustomer")
    @Produces({"application/json", "application/xml"})
    public Response findByCustomer(EfikaCustomer body, @Context SecurityContext securityContext)
            throws Exception {
        return delegate.findByCustomer(body, securityContext);
    }

    @POST
    @Path("/findManobraByCustomer")
    @Produces({"application/json", "application/xml"})
    public Response findMonobraByCustomer(EfikaCustomer body, @Context SecurityContext securityContext)
            throws Exception {
        return delegate.findManobraByCustomer(body, securityContext);
    }

    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    public Response getCertificationById(@PathParam("id") String id, @Context SecurityContext securityContext)
            throws NotFoundException {
        System.out.println("-----------------------------------------------------");
        CustomerCertification cC = (CustomerCertification) delegate.getCertificationById(id, securityContext).getEntity();
        System.out.println(cC.getExecutor());
        System.out.println("-----------------------------------------------------");
        return delegate.getCertificationById(id, securityContext);
    }

    @PUT
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response updateCertification(CertificationResponse body, @Context SecurityContext securityContext)
            throws NotFoundException {
        return delegate.updateCertification(body, securityContext);
    }

    @POST
    @Path("/confRede")
    @Produces({"application/json", "application/xml"})
    public Response confiabilidadeRede(GenericRequest body, SecurityContext securityContext) throws NotFoundException {
        return delegate.confiabilidadeRede(body, securityContext);
    }

    @POST
    @Path("/ontsDisp")
    @Produces({"application/json", "application/xml"})
    public Response ontsDisponiveis(GenericRequest body, SecurityContext securityContext) throws NotFoundException {
        return delegate.ontsDisponiveis(body, securityContext);
    }

    @POST
    @Path("/setOntToOlt")
    @Produces({"application/json", "application/xml"})
    public Response setOntToOlt(GenericRequest body, SecurityContext securityContext) throws NotFoundException {
        return delegate.setOntToOlt(body, securityContext);
    }

}
