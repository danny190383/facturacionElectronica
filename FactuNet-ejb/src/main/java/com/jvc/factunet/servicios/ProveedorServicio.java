package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProveedorDAO;
import com.jvc.factunet.entidades.Proveedor;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ProveedorServicio {
    
    private static final Logger LOG = Logger.getLogger(ProveedorServicio.class.getName());
    
    @EJB
    private ProveedorDAO proveedorDAO;
    
    public List<Proveedor> listar(Integer empresa, int maxResults, int firstResult) {
        return proveedorDAO.listar(empresa, maxResults, firstResult);
    }
    
    public List<Proveedor> listar(Integer empresa) {
        return proveedorDAO.listar(empresa);
    }
    
    public Proveedor buscar(Integer codigo) {
        return proveedorDAO.buscar(codigo);
    }
    
    public Proveedor buscarCedula(String cedula, Integer empresa) {
        return proveedorDAO.buscarCedula(cedula, empresa);
    }
    
    public List<Proveedor> listarBuscar(String nombre, String ciudad, String ruc, Integer empresa, int maxResults, int firstResult) {
        return proveedorDAO.listarBuscar(nombre, ciudad, ruc, empresa, maxResults, firstResult);
    }
    
    public Long contar(String nombre, String ciudad, String ruc, Integer empresa){
        return proveedorDAO.contar(nombre, ciudad, ruc, empresa);
    }
    
    public Long contar(Integer empresa){
        return proveedorDAO.contar(empresa);
    }
    
    public void eliminar(Proveedor parametro) throws Exception {
        this.proveedorDAO.eliminar(parametro);
    }
    
    public void insertar(Proveedor parametro) throws Exception {
        this.proveedorDAO.insertar(parametro);
    }

    public Proveedor actualizar(Proveedor parametro) throws Exception {
        return (Proveedor) this.proveedorDAO.actualizar(parametro);
    }
}
