package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ReporteTabletDAO;
import com.jvc.factunet.entidades.ReporteTablet;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ReporteTabletServicio {
    private static final Logger LOG = Logger.getLogger(ReporteTabletServicio.class.getName());
    
    @EJB
    private ReporteTabletDAO impresoraDAO;
    
    public List<ReporteTablet> listar() {
        return impresoraDAO.listar();
    }
}
