package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.PuntoVentaDAO;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PuntoVentaServicio {
    private static final Logger LOG = Logger.getLogger(PuntoVentaServicio.class.getName());
    
    @EJB
    private PuntoVentaDAO puntoVentaDAO;
    
    public SecuenciaDocumento numeroActual(Integer codigo) {
        return puntoVentaDAO.secuencia(codigo);
    }
    
    public SecuenciaDocumento numeroActual(Integer documento, Integer empresa, Integer punto) {
        return puntoVentaDAO.secuencia(documento, empresa, punto);
    }
    
    public SecuenciaDocumento actualizar(SecuenciaDocumento parametro){
        return (SecuenciaDocumento) this.puntoVentaDAO.actualizar(parametro);
    }
    
    public List<PuntoVenta> listarPuntoVenta(Integer empresa){
        return puntoVentaDAO.listarPuntoVenta(empresa);
    }
}
