package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.UnidadMedidaDAO;
import com.jvc.factunet.entidades.UnidadMedida;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean

public class UnidadMedidaServicio {
    
    
    private static final Logger LOG = Logger.getLogger(UnidadMedidaServicio.class.getName());
    
    @EJB
    private UnidadMedidaDAO unidadmedidaDAO;

    
    public List<UnidadMedida> listar() {
        return unidadmedidaDAO.listar();
    }
    
    public List<UnidadMedida> listarNombre(String nombre) {
        return unidadmedidaDAO.listarNombre(nombre);
    }
    
    public void eliminar(UnidadMedida parametro) throws Exception {
        this.unidadmedidaDAO.eliminar(parametro);
    }
    
    public void insertar(UnidadMedida parametro) throws Exception {
        this.unidadmedidaDAO.insertar(parametro);
    }

    public UnidadMedida actualizar(UnidadMedida parametro) throws Exception {
        return (UnidadMedida) this.unidadmedidaDAO.actualizar(parametro);
    }
    
    
    
}
