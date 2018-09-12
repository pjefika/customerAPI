/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.model.entity;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author G0041775
 */
@Entity(value = "closure", noClassnameStored = false)
public class WorkOrderClosure {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private EfikaCustomer customer;

    private Date dataSolicitacao, dataEncerramento;

    private String solicitante;

    private String atendente;

}
