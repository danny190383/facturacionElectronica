package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoDocumentoDAO;
import com.jvc.factunet.entidades.TipoDocumento;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoDocumentoServicio {
    
    private static final Logger LOG = Logger.getLogger(TipoDocumentoServicio.class.getName());
    
    @EJB
    private TipoDocumentoDAO tipodocumentoDAO;

    
    public List<TipoDocumento> listar() {
        return tipodocumentoDAO.listar();
    }
    
    public List<TipoDocumento> listarNombre(String nombre) {
        return tipodocumentoDAO.listarNombre(nombre);
    }
    
    public void eliminar(TipoDocumento parametro) throws Exception {
        this.tipodocumentoDAO.eliminar(parametro);
    }
    
    public void insertar(TipoDocumento parametro) throws Exception {
        this.tipodocumentoDAO.insertar(parametro);
    }

    public TipoDocumento actualizar(TipoDocumento parametro) throws Exception {
        return (TipoDocumento) this.tipodocumentoDAO.actualizar(parametro);
    }  
}
