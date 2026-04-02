package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ReporteImpresoraDAO;
import com.jvc.factunet.entidades.ReporteImpresora;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ReporteImpresoraServicio {
    
    private static final Logger LOG = Logger.getLogger(ReporteImpresoraServicio.class.getName());
    
    @EJB
    private ReporteImpresoraDAO impresoraDAO;

    public List<ReporteImpresora> listar(Integer empresa, Integer reporte) {
        return impresoraDAO.listar(empresa, reporte);
    }
}
