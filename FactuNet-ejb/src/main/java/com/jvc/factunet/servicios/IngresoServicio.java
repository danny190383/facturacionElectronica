package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.IngresoDAO;
import com.jvc.factunet.entidades.GarantiaCabecera;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class IngresoServicio {
    private static final Logger LOG = Logger.getLogger(IngresoServicio.class.getName());
    
    @EJB
    private IngresoDAO ingresoDAO;
    
    public List<GarantiaCabecera> listarIngresosFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.ingresoDAO.listarIngresosFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
}
