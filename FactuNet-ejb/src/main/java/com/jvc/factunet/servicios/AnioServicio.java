package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.AnioDAO;
import com.jvc.factunet.entidades.Anio;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class AnioServicio {
    private static final Logger LOG = Logger.getLogger(BancoServicio.class.getName());
    
    @EJB
    private AnioDAO anioDAO;
    
    public List<Anio> listar() {
        return anioDAO.listar();
    }
}
