package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProvinciaDAO;
import com.jvc.factunet.entidades.Provincia;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ProvinciaServicio {
    private static final Logger LOG = Logger.getLogger(ProvinciaServicio.class.getName());

    @EJB
    private ProvinciaDAO provinciaController;
    
    public List<Provincia> listarProvincias() {
        return provinciaController.listar();
    }
}
