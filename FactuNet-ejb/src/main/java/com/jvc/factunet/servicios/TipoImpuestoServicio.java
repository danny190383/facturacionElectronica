package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoImpuestoDAO;
import com.jvc.factunet.entidades.Impuesto;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoImpuestoServicio {
    private static final Logger LOG = Logger.getLogger(TipoImpuestoServicio.class.getName());
    
    @EJB
    private TipoImpuestoDAO tipoImpuestoDAO;

    
    public List<Impuesto> listar() {
        return tipoImpuestoDAO.listar();
    }
    
    public List<Impuesto> listarNombre(String nombre) {
        return tipoImpuestoDAO.listarNombre(nombre);
    }
    
    public void eliminar(Impuesto parametro) throws Exception {
        this.tipoImpuestoDAO.eliminar(parametro);
    }
    
    public void insertar(Impuesto parametro) throws Exception {
        this.tipoImpuestoDAO.insertar(parametro);
    }

    public Impuesto actualizar(Impuesto parametro) throws Exception {
        return (Impuesto) this.tipoImpuestoDAO.actualizar(parametro);
    }  
}
