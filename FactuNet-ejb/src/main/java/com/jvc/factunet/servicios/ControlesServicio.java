package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ControlesDAO;
import com.jvc.factunet.entidades.Controles;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ControlesServicio {
    private static final Logger LOG = Logger.getLogger(ControlesServicio.class.getName());
    
    @EJB
    private ControlesDAO controlesDAO;
    
    public Controles buscarControl(Integer control, Integer empresa) {
        return controlesDAO.buscarControl(control, empresa);
    }
}
