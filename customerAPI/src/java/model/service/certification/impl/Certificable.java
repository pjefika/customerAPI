/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service.certification.impl;

import model.entity.CertificationBlock;

/**
 *
 * @author G0041775
 * @param <T>
 */
public interface Certificable <T>{

    public CertificationBlock certify(T t);

}
