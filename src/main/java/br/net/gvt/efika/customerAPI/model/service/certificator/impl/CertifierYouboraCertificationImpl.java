/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.service.certificator.impl;

import br.net.gvt.efika.customer.model.certification.enums.CertificationAssertName;
import br.net.gvt.efika.customer.model.certification.enums.CertificationResult;
import br.net.gvt.efika.customerAPI.model.service.assertations.HpnaCertificationAsserter;
import br.net.gvt.efika.customerAPI.model.service.certification.command.NonExceptionCommand;
import br.net.gvt.efika.stealer.model.TesteHpna;
import br.net.gvt.efika.stealer.model.tv.DecoderTV;

import java.util.List;

public class CertifierYouboraCertificationImpl extends CertifierCertificationBlockGeneric {

    private List<DecoderTV> stbs;
    private TesteHpna testeHpna;

    public CertifierYouboraCertificationImpl() {
    }

    public CertifierYouboraCertificationImpl(TesteHpna testeHpna) {
        this.testeHpna = testeHpna;
    }

    @Override
    protected void process() {
        if (stbs != null) {
            for (CertificationAssertName value : getAsserts()) {
                new NonExceptionCommand() {
                    @Override
                    public void run() throws Exception {
                        CertifierYouboraCertificationImpl.this.getBlock().getAsserts().add(new HpnaCertificationAsserter().assertCertification(value, CertifierYouboraCertificationImpl.this.testeHpna));
                    }
                };
            }
            this.check();
        } else {
            getBlock().concluir(CertificationResult.FORWARDED_CO, "Diagnóstico Youbora NOK.");
        }

    }

    @Override
    public void definirAsserts() {
        this.asserts.add(CertificationAssertName.IS_REDE_BANDA_OK);
    }

}
