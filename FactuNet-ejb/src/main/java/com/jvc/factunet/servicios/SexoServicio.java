package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.SexoDAO;
import com.jvc.factunet.entidades.Sexo;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SexoServicio {
    
    private static final Logger LOG = Logger.getLogger(SexoServicio.class.getName());
    
    @EJB
    private SexoDAO sexoDAO;
    
    public List<Sexo> listar() {
        return sexoDAO.listar();
    }
    
    public List<Sexo> listarNombre(String nombre) {
        return sexoDAO.listarNombre(nombre);
    }
    
    public void eliminar(Sexo parametro) throws Exception {
        this.sexoDAO.eliminar(parametro);
    }
    
    public void insertar(Sexo parametro) throws Exception {
        this.sexoDAO.insertar(parametro);
    }

    public Sexo actualizar(Sexo parametro) throws Exception {
        return (Sexo) this.sexoDAO.actualizar(parametro);
    }
}
