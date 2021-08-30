package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TarjetaEmpresaDAO;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TarjetaEmpresaServicio {
    private static final Logger LOG = Logger.getLogger(TarjetaEmpresaServicio.class.getName());
    
    @EJB
    private TarjetaEmpresaDAO tarjetaEmpresaDAO;
    
    public List<TarjetaEmpresa> listar(Integer empresa) {
        return this.tarjetaEmpresaDAO.listar(empresa);
    }
}
