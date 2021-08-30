package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.MotivoDAO;
import com.jvc.factunet.entidades.Motivo;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class MotivoServicio {
    private static final Logger LOG = Logger.getLogger(MotivoServicio.class.getName());
    
    @EJB
    private MotivoDAO sexoDAO;
    
    public List<Motivo> listar() {
        return sexoDAO.listar();
    }
    
    public List<Motivo> listarNombre(String nombre) {
        return sexoDAO.listarNombre(nombre);
    }
    
    public void eliminar(Motivo parametro) throws Exception {
        this.sexoDAO.eliminar(parametro);
    }
    
    public void insertar(Motivo parametro) throws Exception {
        this.sexoDAO.insertar(parametro);
    }

    public Motivo actualizar(Motivo parametro) throws Exception {
        return (Motivo) this.sexoDAO.actualizar(parametro);
    }
}
