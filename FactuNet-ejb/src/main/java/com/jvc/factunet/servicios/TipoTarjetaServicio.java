package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoTarjetaDAO;
import com.jvc.factunet.entidades.TipoTarjeta;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoTarjetaServicio {
    private static final Logger LOG = Logger.getLogger(TipoTarjetaServicio.class.getName());
    
    @EJB
    private TipoTarjetaDAO tipoTarjetaDAO;
    
    public List<TipoTarjeta> listar() {
        return tipoTarjetaDAO.listar();
    }
    
    public List<TipoTarjeta> listarNombre(String nombre) {
        return tipoTarjetaDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoTarjeta parametro) throws Exception {
        this.tipoTarjetaDAO.eliminar(parametro);
    }
    
    public void insertar(TipoTarjeta parametro) throws Exception {
        this.tipoTarjetaDAO.insertar(parametro);
    }

    public TipoTarjeta actualizar(TipoTarjeta parametro) throws Exception {
        return (TipoTarjeta) this.tipoTarjetaDAO.actualizar(parametro);
    }
}
