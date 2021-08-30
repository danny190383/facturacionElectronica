package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.NotaMedicaVeterinariaDAO;
import com.jvc.factunet.entidades.MascotaNotaMedica;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class NotaMedicaVeterinariaServicio {
    private static final Logger LOG = Logger.getLogger(NotaMedicaVeterinariaServicio.class.getName());
    
    @EJB
    private NotaMedicaVeterinariaDAO notaMedicaVeterinariaDAO;
    
    public void eliminar(MascotaNotaMedica parametro) throws Exception {
        this.notaMedicaVeterinariaDAO.eliminar(parametro);
    }
    
    public void insertar(MascotaNotaMedica parametro) throws Exception {
        this.notaMedicaVeterinariaDAO.insertar(parametro);
    }

    public MascotaNotaMedica actualizar(MascotaNotaMedica parametro) throws Exception {
        return (MascotaNotaMedica) this.notaMedicaVeterinariaDAO.actualizar(parametro);
    }
    
    public List<MascotaNotaMedica> listarNotasMedicas(Integer empresa){
        return this.notaMedicaVeterinariaDAO.listarNotasMedicas(empresa);
    }
    
    public List<MascotaNotaMedica> listarNotasMedicasMascota(Integer mascota){
        return this.notaMedicaVeterinariaDAO.listarNotasMedicasMascota(mascota);
    }
    
    public List<MascotaNotaMedica> listarNotasMedicasFiltro(Integer empresa, Date fecha, String proveedor, String cedula, String mascota, Date fechaC){
        return this.notaMedicaVeterinariaDAO.listarNotasMedicasFiltro(empresa, fecha, proveedor, cedula, mascota, fechaC);
    }
}
