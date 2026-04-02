package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ImpresoraDAO;
import com.jvc.factunet.entidades.Impresora;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ImpresoraServicio {
    private static final Logger LOG = Logger.getLogger(ImpresoraServicio.class.getName());
    
    @EJB
    private ImpresoraDAO impresoraDAO;
    
    public List<Impresora> listar() {
        return impresoraDAO.listar();
    }
    
    public List<Impresora> listar(Integer empresa) {
        return impresoraDAO.listar(empresa);
    }
}
