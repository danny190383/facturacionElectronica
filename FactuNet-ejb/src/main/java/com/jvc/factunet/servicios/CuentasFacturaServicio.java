package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CuentasFacturaDAO;
import com.jvc.factunet.entidades.CuentaFactura;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CuentasFacturaServicio {
    
    private static final Logger LOG = Logger.getLogger(CuentasFacturaServicio.class.getName());
    
    @EJB
    private CuentasFacturaDAO cuentasFacturaDAO;
    
    public List<CuentaFactura> buscarPendientesCliente(Integer persona, Integer empresa){
        return this.cuentasFacturaDAO.buscarPendientesCliente(persona, empresa);
    }
    
    public List<CuentaFactura> buscarPendientesProveedor(Integer persona, Integer empresa){
        return this.cuentasFacturaDAO.buscarPendientesProveedor(persona, empresa);
    }
    
    public void eliminar(CuentaFactura parametro) throws Exception {
        this.cuentasFacturaDAO.eliminar(parametro);
    }
    
    public void insertar(CuentaFactura parametro) throws Exception {
        this.cuentasFacturaDAO.insertar(parametro);
    }

    public CuentaFactura actualizar(CuentaFactura parametro) throws Exception {
        return (CuentaFactura) this.cuentasFacturaDAO.actualizar(parametro);
    }
}
