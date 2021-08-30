package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ParroquiaDAO;
import com.jvc.factunet.entidades.Parroquia;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ParroquiaServicio {
    
    private static final Logger LOG = Logger.getLogger(ParroquiaServicio.class.getName());

    @EJB
    private ParroquiaDAO parroquiaDAO;

    public List<Parroquia> listarParroquiasPorCanton(Integer codigoProvincia, Integer codigoCanton) {
        return this.parroquiaDAO.listarParroquiasCanton(codigoProvincia, codigoCanton);
    }
}
