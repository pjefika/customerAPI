/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.request;

import br.net.gvt.efika.customerAPI.model.badpractice.assia.AssiaClearViewIn;
import com.assia.dslo.napi.model.xsd.ResponseDataBean;

/**
 *
 * @author G0041775
 */
public interface AssiaGponRequest {

    public ResponseDataBean clearViewRealTime(AssiaClearViewIn in) throws Exception;

}
