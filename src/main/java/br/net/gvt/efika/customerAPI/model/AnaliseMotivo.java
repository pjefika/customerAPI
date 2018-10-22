package br.net.gvt.efika.customerAPI.model;

import br.net.gvt.efika.customer.model.manobra.enums.ConclusaoManobraEnum;
import br.net.gvt.efika.customer.model.manobra.enums.MotivoManobraEnum;

/**
 *
 * @author G0042204
 */
public class AnaliseMotivo {

    private MotivoManobraEnum motivo;

    private ConclusaoManobraEnum conclusao;

    private String beautyMotivo;

    private String beautyConclusao;

    public AnaliseMotivo() {
    }

    public AnaliseMotivo(MotivoManobraEnum motivo, ConclusaoManobraEnum conclusao) {
        this.motivo = motivo;
        this.conclusao = conclusao;
    }

    public MotivoManobraEnum getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoManobraEnum motivo) {
        this.motivo = motivo;
    }

    public ConclusaoManobraEnum getConclusao() {
        return conclusao;
    }

    public void setConclusao(ConclusaoManobraEnum conclusao) {
        this.conclusao = conclusao;
    }

    public String getBeautyMotivo() {
        if(motivo.getMotivo() != null) {
            return motivo.getMotivo();
        }else{
            return beautyMotivo;
        }
    }

    public void setBeautyMotivo(String beautyMotivo) {
        this.beautyMotivo = beautyMotivo;
    }

    public String getBeautyConclusao() {
        return conclusao.getNome();
    }

    public void setBeautyConclusao(String beautyConclusao) {
        this.beautyConclusao = beautyConclusao;
    }
}
