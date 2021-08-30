package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.FormaPagoDAO;
import com.jvc.factunet.entidades.FormaPago;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class FormaPagoServicio {
    private static final Logger LOG = Logger.getLogger(FormaPagoServicio.class.getName());
    
    @EJB
    private FormaPagoDAO formaPagoDAO;
    
    public FormaPago buscar(Integer codigo) {
        return formaPagoDAO.buscar(codigo);
    }
    
    public List<FormaPago> listar() {
        return formaPagoDAO.listar();
    }
    
    public List<FormaPago> listarTipo(Integer tipo) {
        return formaPagoDAO.listarTipo(tipo);
    }
    
    public List<FormaPago> listarNombre(String nombre) {
        return formaPagoDAO.listarNombre(nombre);
    }
    
    public void eliminar(FormaPago parametro) throws Exception {
        this.formaPagoDAO.eliminar(parametro);
    }
    
    public void insertar(FormaPago parametro) throws Exception {
        this.formaPagoDAO.insertar(parametro);
    }

    public FormaPago actualizar(FormaPago parametro) throws Exception {
        return (FormaPago) this.formaPagoDAO.actualizar(parametro);
    }
}
