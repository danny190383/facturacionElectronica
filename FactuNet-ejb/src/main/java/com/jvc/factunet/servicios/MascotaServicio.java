package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.MascotaDAO;
import com.jvc.factunet.entidades.Mascota;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class MascotaServicio {
    private static final Logger LOG = Logger.getLogger(MascotaServicio.class.getName());
    
    @EJB
    private MascotaDAO mascotaDAO;
    
    public void eliminar(Mascota parametro) throws Exception {
        this.mascotaDAO.eliminar(parametro);
    }
    
    public void insertar(Mascota parametro) throws Exception {
        this.mascotaDAO.insertar(parametro);
    }

    public Mascota actualizar(Mascota parametro) throws Exception {
        return (Mascota) this.mascotaDAO.actualizar(parametro);
    }
    
    public List<Mascota> listar(Integer persona) {
        return mascotaDAO.listar(persona);
    }
}
