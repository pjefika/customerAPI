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
import br.net.gvt.efika.stealer.model.tv.DecoderTV;
import java.util.List;

public class CertifierHpnaCertificationImpl extends CertifierCertificationBlockGeneric {

    private List<DecoderTV> stbs;

    public CertifierHpnaCertificationImpl() {
    }

    public CertifierHpnaCertificationImpl(List<DecoderTV> stbs) {
        this.stbs = stbs;
    }

    @Override
    protected void process() {
        if (stbs != null) {
            for (CertificationAssertName value : getAsserts()) {
                System.out.println("nNome: " + value);
                new NonExceptionCommand() {
                    @Override
                    public void run() {
                        try {
                            CertifierHpnaCertificationImpl.this.getBlock().getAsserts().add(new HpnaCertificationAsserter().assertCertification(value, CertifierHpnaCertificationImpl.this.stbs));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
            }
            this.check();
        } else {
            getBlock().concluir(CertificationResult.FORWARDED_CO, "Diagnóstico HPNA NOK.");
        }

    }

    @Override
    public void definirAsserts() {
        this.asserts.add(CertificationAssertName.IS_DIAG_HPNA_OK);
    }

}
