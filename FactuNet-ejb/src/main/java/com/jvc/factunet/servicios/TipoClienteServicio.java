package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoClienteDAO;
import com.jvc.factunet.entidades.TipoCliente;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoClienteServicio {
  
    private static final Logger LOG = Logger.getLogger(TipoClienteServicio.class.getName());
    
    @EJB
    private TipoClienteDAO tipoclienteDAO;
    
    public List<TipoCliente> listar() {
        return tipoclienteDAO.listar();
    }
    
    public List<TipoCliente> listarNombre(String nombre) {
        return tipoclienteDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoCliente parametro) throws Exception {
        this.tipoclienteDAO.eliminar(parametro);
    }
    
    public void insertar(TipoCliente parametro) throws Exception {
        this.tipoclienteDAO.insertar(parametro);
    }

    public TipoCliente actualizar(TipoCliente parametro) throws Exception {
        return (TipoCliente) this.tipoclienteDAO.actualizar(parametro);
    }
}
