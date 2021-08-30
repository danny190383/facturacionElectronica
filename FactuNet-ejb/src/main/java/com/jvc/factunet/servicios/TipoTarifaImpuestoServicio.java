package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoTarifaImpuestoDAO;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoTarifaImpuestoServicio {
    private static final Logger LOG = Logger.getLogger(TipoTarifaImpuestoServicio.class.getName());
    
    @EJB
    private TipoTarifaImpuestoDAO tipoTarifaImpuestoDAO;

    
    public List<ImpuestoTarifa> listar() {
        return tipoTarifaImpuestoDAO.listar();
    }
    
    public List<ImpuestoTarifa> listarNombre(String nombre) {
        return tipoTarifaImpuestoDAO.listarNombre(nombre);
    }
    
    public List<ImpuestoTarifa> listarImpuesto(Integer impuesto) {
        return tipoTarifaImpuestoDAO.listarImpuesto(impuesto);
    }
    
    public void eliminar(ImpuestoTarifa parametro) throws Exception {
        this.tipoTarifaImpuestoDAO.eliminar(parametro);
    }
    
    public void insertar(ImpuestoTarifa parametro) throws Exception {
        this.tipoTarifaImpuestoDAO.insertar(parametro);
    }

    public ImpuestoTarifa actualizar(ImpuestoTarifa parametro) throws Exception {
        return (ImpuestoTarifa) this.tipoTarifaImpuestoDAO.actualizar(parametro);
    }  
}
