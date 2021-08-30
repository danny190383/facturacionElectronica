package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ReportesDAO;
import com.jvc.factunet.entidades.Reportes;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ReportesServicio {
    private static final Logger LOG = Logger.getLogger(ReportesServicio.class.getName());
    
    @EJB
    private ReportesDAO reportesDAO;
    
    public List<Reportes> listarPadre(String padre) {
        return reportesDAO.listar(padre);
    }
}
