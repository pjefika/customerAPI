/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.assertations;

import br.net.gvt.efika.customer.model.certification.CertificationAssert;
import br.net.gvt.efika.customer.model.certification.enums.CertificationAssertName;
import static br.net.gvt.efika.customer.model.certification.enums.CertificationAssertName.*;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.model.service.assertations.exception.AssertNaoImpl;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.stealer.model.tv.DecoderTV;
import java.util.List;

public class HpnaCertificationAsserter extends CertificationAsserterAbs<List<DecoderTV>> {

    private transient ValidacaoResult v;

    @Override
    public CertificationAssert assertCertification(CertificationAssertName name, List<DecoderTV> stbs) throws Exception {
        try {
            certName = name;
            switch (name) {
                case IS_DIAG_HPNA_OK:
                    Double maxAtn = stbs.size() > 1 ? new Double("-6") : new Double("0");
                    Double minAtn;
                    switch (stbs.size()) {
                        case 1:
                            minAtn = new Double("-5");
                            break;
                        case 2:
                            minAtn = new Double("-11.5");
                            break;
                        case 3:
                            minAtn = new Double("-19.5");
                            break;
                        case 4:
                            minAtn = new Double("-28.0");
                            break;
                        case 5:
                            minAtn = new Double("-36.5");
                            break;
                        default:
                            minAtn = new Double("-40");
                            break;
                    }
                    String badStbs = "";
                    for (DecoderTV stb : stbs) {
                        Boolean r;
                        r = stb.getPacketsReceived().compareTo(1000l) >= 0;
                        r = stb.getAttenuation().compareTo(maxAtn) <= 0 && stb.getAttenuation().compareTo(minAtn) >= 0;
                        r = stb.getSnr().compareTo(new Double("35")) >= 0;
                        r = stb.getPacketsLost().compareTo(new Double("0")) <= 0;
                        r = new Long(stb.getBaudRate().split("\\d+")[0]).compareTo(191l) >= 0;

                        if (!r) {
                            badStbs = badStbs.length() > 0 ? badStbs + ", " + stb.getSerial() : stb.getSerial();
                        }
                    }
                    if (stbs.size() <= 0) {
                        orientacao = "Não foi identificado equipamento aprovisionado.";
                        result = CertificationResult.FORWARDED_CO;
                    } else {
                        orientacao = badStbs.length() > 1 ? "Diagnóstico HPNA OK" : "Diagnóstico HPNA NOK no(s) STB(s) " + badStbs;
                        result = badStbs.length() > 1 ? CertificationResult.OK : CertificationResult.FISICAL;
                    }

                    break;

                default:
                    throw new AssertNaoImpl();
            }
            return new CertificationAssert(certName, result, orientacao);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertNaoImpl();
        }
    }

// ExceptionLog
}
