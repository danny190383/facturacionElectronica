package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TransportistaDAO;
import com.jvc.factunet.entidades.Transportista;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TransportistaServicio {
    
    private static final Logger LOG = Logger.getLogger(TransportistaServicio.class.getName());
    
    @EJB
    private TransportistaDAO transportistaDAO;
    
    public List<Transportista> listar(Integer empresa, int maxResults, int firstResult) {
        return transportistaDAO.listar(empresa, maxResults, firstResult);
    }
    
    public List<Transportista> listar(Integer empresa) {
        return transportistaDAO.listar(empresa);
    }
    
    public Transportista buscar(Integer codigo) {
        return transportistaDAO.buscar(codigo);
    }
    
    public Transportista buscarCedula(String cedula, Integer empresa) {
        return transportistaDAO.buscarCedula(cedula, empresa);
    }
    
    public List<Transportista> listarBuscar(String nombre, String ciudad, String ruc, Integer empresa, int maxResults, int firstResult) {
        return transportistaDAO.listarBuscar(nombre, ciudad, ruc, empresa, maxResults, firstResult);
    }
    
    public Long contar(String nombre, String ciudad, String ruc, Integer empresa){
        return transportistaDAO.contar(nombre, ciudad, ruc, empresa);
    }
    
    public Long contar(Integer empresa){
        return transportistaDAO.contar(empresa);
    }
    
    public void eliminar(Transportista parametro) throws Exception {
        this.transportistaDAO.eliminar(parametro);
    }
    
    public void insertar(Transportista parametro) throws Exception {
        this.transportistaDAO.insertar(parametro);
    }

    public Transportista actualizar(Transportista parametro) throws Exception {
        return (Transportista) this.transportistaDAO.actualizar(parametro);
    }
}
