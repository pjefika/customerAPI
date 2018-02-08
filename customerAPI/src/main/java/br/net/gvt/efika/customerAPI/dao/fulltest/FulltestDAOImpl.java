/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.net.gvt.efika.customerAPI.dao.fulltest;

import br.net.gvt.efika.customerAPI.dao.factory.FactoryDAO;
import br.net.gvt.efika.customerAPI.dao.http.Urlss;
import fulltest.FullTest;
import fulltest.FulltestRequest;

public class FulltestDAOImpl implements FulltestDAO {

    @Override
    public FullTest fulltest(FulltestRequest request) throws Exception {
        return (FullTest) FactoryDAO.createHttpFulltestDAO().post(Urlss.FULLTEST.getUrl(),
                new FulltestRequest(request.getCust(), request.getExecutor()));
    }

}
