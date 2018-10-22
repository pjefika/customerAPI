package br.net.gvt.efika.customerAPI.model.entity;

import br.net.gvt.efika.customer.model.manobra.enums.MotivoManobraEnum;
import br.net.gvt.efika.customerAPI.model.FinalizacaoManobra;
import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Tiago Henrique Iwamoto
 * tiago.iwamoto@telefonica.com
 * System Analyst
 * 41 9 9513-0230
 **/
@Entity(value = "manobra", noClassnameStored = false)
public class LogManobra {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String instancia;
    private String designador;
    private String designadorAcesso;
    private String executor;
    private EfikaCustomer customer;
    private FinalizacaoManobra analises;
    private Boolean manobrar;
    private MotivoManobraEnum motivoSaida;
    private MotivoManobraEnum motivoEntrada;
    private Date datahora;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getInstancia() {
        return instancia;
    }

    public void setInstancia(String instancia) {
        this.instancia = instancia;
    }

    public String getDesignador() {
        return designador;
    }

    public void setDesignador(String designador) {
        this.designador = designador;
    }

    public String getDesignadorAcesso() {
        return designadorAcesso;
    }

    public void setDesignadorAcesso(String designadorAcesso) {
        this.designadorAcesso = designadorAcesso;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public EfikaCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(EfikaCustomer customer) {
        this.customer = customer;
    }

    public FinalizacaoManobra getAnalise() {
        return analises;
    }

    public void setAnalise(FinalizacaoManobra analise) {
        this.analises = analise;
    }

    public Boolean getManobrar() {
        return manobrar;
    }

    public void setManobrar(Boolean manobrar) {
        this.manobrar = manobrar;
    }

    public MotivoManobraEnum getMotivoSaida() {
        return motivoSaida;
    }

    public void setMotivoSaida(MotivoManobraEnum motivoSaida) {
        this.motivoSaida = motivoSaida;
    }

    public MotivoManobraEnum getMotivoEntrada() {
        return motivoEntrada;
    }

    public void setMotivoEntrada(MotivoManobraEnum motivoEntrada) {
        this.motivoEntrada = motivoEntrada;
    }

    public Date getDataHora() {
        return datahora;
    }

    public void setDataHora(Date dataHora) {
        this.datahora = dataHora;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogManobra{");
        sb.append("id=").append(id);
        sb.append(", instancia='").append(instancia).append('\'');
        sb.append(", designador='").append(designador).append('\'');
        sb.append(", designadorAcesso='").append(designadorAcesso).append('\'');
        sb.append(", executor='").append(executor).append('\'');
        sb.append(", customer=").append(customer);
        sb.append(", analise=").append(analises);
        sb.append(", manobrar=").append(manobrar);
        sb.append(", motivoSaida=").append(motivoSaida);
        sb.append(", motivoEntrada=").append(motivoEntrada);
        sb.append(", dataHora=").append(datahora);
        sb.append('}');
        return sb.toString();
    }
}
