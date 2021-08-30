package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoCuentaDAO;
import com.jvc.factunet.entidades.TipoCuenta;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoCuentaServicio {
    private static final Logger LOG = Logger.getLogger(TipoCuentaServicio.class.getName());
    
    @EJB
    private TipoCuentaDAO tipoCuentaDAO;
    
    public List<TipoCuenta> listar() {
        return tipoCuentaDAO.listar();
    }
    
    public List<TipoCuenta> listarNombre(String nombre) {
        return tipoCuentaDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoCuenta parametro) throws Exception {
        this.tipoCuentaDAO.eliminar(parametro);
    }
    
    public void insertar(TipoCuenta parametro) throws Exception {
        this.tipoCuentaDAO.insertar(parametro);
    }

    public TipoCuenta actualizar(TipoCuenta parametro) throws Exception {
        return (TipoCuenta) this.tipoCuentaDAO.actualizar(parametro);
    }
}
