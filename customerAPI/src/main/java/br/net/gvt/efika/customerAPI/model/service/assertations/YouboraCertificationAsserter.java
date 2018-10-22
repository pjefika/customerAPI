/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.assertations;

import br.net.gvt.efika.customer.model.certification.CertificationAssert;
import br.net.gvt.efika.customer.model.certification.enums.CertificationAssertName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.model.service.assertations.exception.AssertNaoImpl;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.stealer.model.tv.DecoderTV;

import java.util.List;

@SuppressWarnings("all")
public class YouboraCertificationAsserter extends CertificationAsserterAbs<List<DecoderTV>> {

    private transient ValidacaoResult v;

    @Override
    public CertificationAssert assertCertification(CertificationAssertName name, List<DecoderTV> stbs) throws Exception {
        try {
            certName = name;
            String badStbs = "";
            if (stbs.size() <= 0) {
                orientacao = "Não foi identificado equipamento aprovisionado.";
                result = CertificationResult.FORWARDED_CO;
            } else {
                orientacao = badStbs.length() > 1 ? "Diagnóstico HPNA OK" : "Diagnóstico Youbora NOK no(s) STB(s) " + badStbs;
                result = badStbs.length() > 1 ? CertificationResult.OK : CertificationResult.FISICAL;
            }
            return new CertificationAssert(certName, result, orientacao);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertNaoImpl();
        }
    }

// ExceptionLog
}
