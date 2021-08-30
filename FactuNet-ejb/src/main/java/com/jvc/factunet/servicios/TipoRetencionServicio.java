package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoRetencionDAO;
import com.jvc.factunet.entidades.TipoRetencion;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoRetencionServicio {
    
  
    private static final Logger LOG = Logger.getLogger(TipoRetencionServicio.class.getName());
    
    @EJB
    private TipoRetencionDAO tiporetencionDAO;

    public List<TipoRetencion> listar() {
        return tiporetencionDAO.listar();
    }
    
    public List<TipoRetencion> listarNombre(String nombre) {
        return tiporetencionDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoRetencion parametro) throws Exception {
        this.tiporetencionDAO.eliminar(parametro);
    }
    
    public void insertar(TipoRetencion parametro) throws Exception {
        this.tiporetencionDAO.insertar(parametro);
    }

    public TipoRetencion actualizar(TipoRetencion parametro) throws Exception {
        return (TipoRetencion) this.tiporetencionDAO.actualizar(parametro);
    }
}
