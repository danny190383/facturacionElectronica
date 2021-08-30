package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.SexoMascotaDAO;
import com.jvc.factunet.entidades.SexoMascota;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SexoMascotaServicio {
    private static final Logger LOG = Logger.getLogger(SexoMascotaServicio.class.getName());
    
    @EJB
    private SexoMascotaDAO sexoMascotaDAO;
    
    public List<SexoMascota> listar() {
        return sexoMascotaDAO.listar();
    }
    
    public List<SexoMascota> listarNombre(String nombre) {
        return sexoMascotaDAO.listarNombre(nombre);
    }
    
    public void eliminar(SexoMascota parametro) throws Exception {
        this.sexoMascotaDAO.eliminar(parametro);
    }
    
    public void insertar(SexoMascota parametro) throws Exception {
        this.sexoMascotaDAO.insertar(parametro);
    }

    public SexoMascota actualizar(SexoMascota parametro) throws Exception {
        return (SexoMascota) this.sexoMascotaDAO.actualizar(parametro);
    }
}
