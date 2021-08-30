package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.RazaMascotaDAO;
import com.jvc.factunet.entidades.RazaMascota;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RazaMascotaServicio {
    
    private static final Logger LOG = Logger.getLogger(RazaMascotaServicio.class.getName());
    
    @EJB
    private RazaMascotaDAO razaMascotaDAO;
    
    public List<RazaMascota> listar() {
        return razaMascotaDAO.listar();
    }
    
    public List<RazaMascota> listarNombre(String nombre) {
        return razaMascotaDAO.listarNombre(nombre);
    }
    
    public void eliminar(RazaMascota parametro) throws Exception {
        this.razaMascotaDAO.eliminar(parametro);
    }
    
    public void insertar(RazaMascota parametro) throws Exception {
        this.razaMascotaDAO.insertar(parametro);
    }

    public RazaMascota actualizar(RazaMascota parametro) throws Exception {
        return (RazaMascota) this.razaMascotaDAO.actualizar(parametro);
    }
}
