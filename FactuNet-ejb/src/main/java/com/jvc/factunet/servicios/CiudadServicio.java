package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CiudadDAO;
import com.jvc.factunet.entidades.Ciudad;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CiudadServicio {
    private static final Logger LOG = Logger.getLogger(CiudadServicio.class.getName());
    
    @EJB
    private CiudadDAO ciudadDAO;
    
    public List<Ciudad> listar() {
        return ciudadDAO.listar();
    }
    
    public List<Ciudad> listarNombre(String nombre) {
        return ciudadDAO.listarNombre(nombre);
    }
    
    public void eliminar(Ciudad parametro) throws Exception {
        this.ciudadDAO.eliminar(parametro);
    }
    
    public void insertar(Ciudad parametro) throws Exception {
        this.ciudadDAO.insertar(parametro);
    }

    public Ciudad actualizar(Ciudad parametro) throws Exception {
        return (Ciudad) this.ciudadDAO.actualizar(parametro);
    }
}
