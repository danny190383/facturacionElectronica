package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CatalogoInfoAdicionalDAO;
import com.jvc.factunet.entidades.CatalogoInfoAdicional;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CatalogoInfoAdicionalServicio {
    
    @EJB
    private CatalogoInfoAdicionalDAO catalogoInfoAdicionalDAO;

    public List<CatalogoInfoAdicional> listar() {
        return catalogoInfoAdicionalDAO.listar();
    }
}
