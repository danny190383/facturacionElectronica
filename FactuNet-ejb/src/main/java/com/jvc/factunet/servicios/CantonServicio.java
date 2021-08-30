package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CantonDAO;
import com.jvc.factunet.entidades.Canton;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CantonServicio {
    
    private static final Logger LOG = Logger.getLogger(CantonServicio.class.getName());

    @EJB
    private CantonDAO cantonDAO;
    
    public List<Canton> listarCantonesPorProvincia(Integer codigoProvincia) {
        return cantonDAO.listarCantonesProvincia(codigoProvincia);
    }
}
