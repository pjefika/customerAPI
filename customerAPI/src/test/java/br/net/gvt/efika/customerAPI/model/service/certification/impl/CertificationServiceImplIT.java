/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.certification.impl;

import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.model.entity.CustomerCertification;
import br.net.gvt.efika.customerAPI.model.GenericRequest;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.SerialOntGpon;
import br.net.gvt.efika.util.json.JacksonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author G0041775
 */
public class CertificationServiceImplIT {

    public CertificationServiceImplIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private GenericRequest req = new GenericRequest("4133335556", "TESTE");

    private CertificationServiceImpl instance = new CertificationServiceImpl();

    /**
     * Test of fulltestByParam method, of class CertificationServiceImpl.
     */
    @Test
    public void testCertificationByParam() throws Exception {
        try {
            System.out.println("certificationByParam");
//            GenericRequest req = new GenericRequest("4130886762", "G0041775");
//            CertificationServiceImpl instance = new CertificationServiceImpl();
            CustomerCertification result = instance.certificationByParam(req);
            System.out.println(new JacksonMapper(CustomerCertification.class).serialize(result));
            assertTrue(result.getResultado() == CertificationResult.OK);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * Test of certifyRede method, of class CertificationServiceImpl.
     */
    @Test
    public void testCertifyRede() throws Exception {
        System.out.println("certifyRede");
        ValidacaoResult result = instance.certifyRede(req);
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));

    }

    /**
     * Test of findByCustomer method, of class CertificationServiceImpl.
     */
    @Test
    public void testFindByCustomer() throws Exception {
        System.out.println("findByCustomer");
        EfikaCustomer cust = null;
        CertificationServiceImpl instance = new CertificationServiceImpl();
        List<CustomerCertification> expResult = null;
        List<CustomerCertification> result = instance.findByCustomer(cust);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ontsDisp method, of class CertificationServiceImpl.
     */
    @Test
    public void testOntsDisp() throws Exception {
        System.out.println("ontsDisp");
        List<SerialOntGpon> result = instance.ontsDisp(req);
        System.out.println(new JacksonMapper(new TypeReference<List<SerialOntGpon>>() {
        }).serialize(result));
    }

    /**
     * Test of setOntToOlt method, of class CertificationServiceImpl.
     */
    @Test
    public void testSetOntToOlt() throws Exception {
        System.out.println("setOntToOlt");
        GenericRequest req = null;
        CertificationServiceImpl instance = new CertificationServiceImpl();
        ValidacaoResult expResult = null;
        ValidacaoResult result = instance.setOntToOlt(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class CertificationServiceImpl.
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        String id = "5b81c7a61df2a936a81638e6";
        
        CustomerCertification result = instance.findById(id);
        System.out.println(new JacksonMapper(CustomerCertification.class).serialize(result));
    }

}
