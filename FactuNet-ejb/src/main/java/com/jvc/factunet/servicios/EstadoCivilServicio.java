package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.EstadoCivilDAO;
import com.jvc.factunet.entidades.EstadoCivil;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class EstadoCivilServicio {
      private static final Logger LOG = Logger.getLogger(EstadoCivilServicio.class.getName());
    
    @EJB
    private EstadoCivilDAO estadocivilDAO;
    
    public List<EstadoCivil> listar() {
        return estadocivilDAO.listar();
    }
    
    public List<EstadoCivil> listarNombre(String nombre) {
        return estadocivilDAO.listarNombre(nombre);
    }
    
    public void eliminar(EstadoCivil parametro) throws Exception {
        this.estadocivilDAO.eliminar(parametro);
    }
    
    public void insertar(EstadoCivil parametro) throws Exception {
        this.estadocivilDAO.insertar(parametro);
    }

    public EstadoCivil actualizar(EstadoCivil parametro) throws Exception {
        return (EstadoCivil) this.estadocivilDAO.actualizar(parametro);
    }
}
