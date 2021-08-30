package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.EspecieMascotaDAO;
import com.jvc.factunet.entidades.EspecieMascota;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class EspecieMascotaServicio {
    
    private static final Logger LOG = Logger.getLogger(EspecieMascotaServicio.class.getName());
    
    @EJB
    private EspecieMascotaDAO especieMascotaDAO;
    
    public List<EspecieMascota> listar() {
        return especieMascotaDAO.listar();
    }
    
    public List<EspecieMascota> listarNombre(String nombre) {
        return especieMascotaDAO.listarNombre(nombre);
    }
    
    public void eliminar(EspecieMascota parametro) throws Exception {
        this.especieMascotaDAO.eliminar(parametro);
    }
    
    public void insertar(EspecieMascota parametro) throws Exception {
        this.especieMascotaDAO.insertar(parametro);
    }

    public EspecieMascota actualizar(EspecieMascota parametro) throws Exception {
        return (EspecieMascota) this.especieMascotaDAO.actualizar(parametro);
    }
}
