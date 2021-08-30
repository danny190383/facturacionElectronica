package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoValidacionDAO;
import com.jvc.factunet.entidades.TipoValidacion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoValidacionServicio {
    @EJB
    private  TipoValidacionDAO tipoValidacionDAO;
    
    public List<TipoValidacion> listar() {
        return tipoValidacionDAO.listar();
    }
    
    public List<TipoValidacion> listarNombre(String nombre) {
        return tipoValidacionDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoValidacion parametro) throws Exception {
        this.tipoValidacionDAO.eliminar(parametro);
    }
    
    public void insertar(TipoValidacion parametro) throws Exception {
        this.tipoValidacionDAO.insertar(parametro);
    }

    public TipoValidacion actualizar(TipoValidacion parametro) throws Exception {
        return (TipoValidacion) this.tipoValidacionDAO.actualizar(parametro);
    }
}
