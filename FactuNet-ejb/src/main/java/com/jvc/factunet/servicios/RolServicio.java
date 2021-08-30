package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.RolDAO;
import com.jvc.factunet.entidades.Rol;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RolServicio {
    
    private static final Logger LOG = Logger.getLogger(RolServicio.class.getName());
    
    @EJB
    private RolDAO rolDAO;
    
    public List<Rol> listar() {
        return rolDAO.listar();
    }
    
    public List<Rol> listarNombre(String nombre) {
        return rolDAO.listarNombre(nombre);
    }
    
    public void eliminar(Rol parametro) throws Exception {
        this.rolDAO.eliminar(parametro);
    }
    
    public void insertar(Rol parametro) throws Exception {
        this.rolDAO.insertar(parametro);
    }

    public Rol actualizar(Rol parametro) throws Exception {
        return (Rol) this.rolDAO.actualizar(parametro);
    }
    
}
