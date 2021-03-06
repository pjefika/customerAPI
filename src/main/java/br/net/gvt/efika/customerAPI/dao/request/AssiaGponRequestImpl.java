/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.request;

import br.net.gvt.efika.customerAPI.model.badpractice.assia.AssiaClearViewIn;
import br.net.gvt.efika.util.dao.http.Urls;
import br.net.gvt.efika.util.dao.http.factory.FactoryHttpDAOAbstract;
import com.assia.dslo.napi.model.xsd.ResponseDataBean;

public class AssiaGponRequestImpl implements AssiaGponRequest {

    @Override
    public ResponseDataBean clearViewRealTime(AssiaClearViewIn in) throws Exception {
        FactoryHttpDAOAbstract<ResponseDataBean> h = new FactoryHttpDAOAbstract<>(ResponseDataBean.class);
        return h.createWithoutProxy().post(Urls.ASSIA_CLEARVIEW_STEALER.getUrl(), in);
    }

}
