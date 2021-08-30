package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoContactoDAO;
import com.jvc.factunet.entidades.TipoContacto;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoContactoServicio {
    
    @EJB
    private  TipoContactoDAO tipoContactoDAO;
    
    public List<TipoContacto> listar() {
        return tipoContactoDAO.listar();
    }
    
    public List<TipoContacto> listarNombre(String nombre) {
        return tipoContactoDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoContacto parametro) throws Exception {
        this.tipoContactoDAO.eliminar(parametro);
    }
    
    public void insertar(TipoContacto parametro) throws Exception {
        this.tipoContactoDAO.insertar(parametro);
    }

    public TipoContacto actualizar(TipoContacto parametro) throws Exception {
        return (TipoContacto) this.tipoContactoDAO.actualizar(parametro);
    }
}
