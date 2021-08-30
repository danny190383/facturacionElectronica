package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoIdentificacionDAO;
import com.jvc.factunet.entidades.TipoIdentificacion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoIdentificacionServicio {
    
    @EJB
    private  TipoIdentificacionDAO tipoIdentificacionDAO;
    
    public List<TipoIdentificacion> listar() {
        return tipoIdentificacionDAO.listar();
    }
    
    public List<TipoIdentificacion> listarNombre(String nombre) {
        return tipoIdentificacionDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoIdentificacion parametro) throws Exception {
        this.tipoIdentificacionDAO.eliminar(parametro);
    }
    
    public void insertar(TipoIdentificacion parametro) throws Exception {
        this.tipoIdentificacionDAO.insertar(parametro);
    }

    public TipoIdentificacion actualizar(TipoIdentificacion parametro) throws Exception {
        return (TipoIdentificacion) this.tipoIdentificacionDAO.actualizar(parametro);
    }
}
