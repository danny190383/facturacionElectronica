package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProductoServiciosDAO;
import com.jvc.factunet.entidades.ProductoServicio;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class ProductoServiciosServicio {
    
    private static final Logger LOG = Logger.getLogger(ProductoServiciosServicio.class.getName());
    
    @EJB
    private ProductoServiciosDAO productoDAO;
    
    public ProductoServicio buscarCodigoBarras(String barras, Boolean estado) {
        return productoDAO.buscarBarras(barras, estado);
    }
    
    public List<ProductoServicio> listar(Integer empresa, Integer grupo, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listar(empresa, grupo, estado, maxResults, firstResult);
    }
    
    public Long contar(Integer empresa, Integer grupo, Boolean estado){
        return productoDAO.contar(empresa, grupo, estado);
    }
    
    public List<ProductoServicio> listarBuscar(String nombre,  Integer empresa, Integer grupo, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, estado, maxResults, firstResult);
    }
    
    public List<ProductoServicio> listarBuscar(String nombre,  Integer empresa, String grupo, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, estado, maxResults, firstResult);
    }
    
    public List<ProductoServicio> listarBuscarBarras(String codigoBarras, Integer empresa, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listarBuscarBarras(codigoBarras, empresa, estado, maxResults, firstResult);
    }
    
    public Long contar(String nombre, Integer empresa, Integer grupo, Boolean estado){
        return productoDAO.contar(nombre, empresa, grupo, estado);
    }
    
    public Long contar(String nombre, Integer empresa, String grupo, Boolean estado){
        return productoDAO.contar(nombre, empresa, grupo, estado);
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa, Boolean estado){
        return productoDAO.contarBarras(codigoBarras, empresa, estado);
    }
    
    public List<ProductoServicio> listarPadre(Integer empresa, List<Integer> gruposBuscar, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listarPadre(empresa, gruposBuscar, estado, maxResults, firstResult);
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar, Boolean estado){
        return productoDAO.contarPadre(empresa, gruposBuscar, estado);
    }
    
    public List<ProductoServicio> listarBuscarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, Boolean estado, int maxResults, int firstResult) {
        return productoDAO.listarBuscarPadre(nombre, empresa, gruposBuscar, estado, maxResults, firstResult);
    }
    
    public Long contarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, Boolean estado){
        return productoDAO.contarPadre(nombre, empresa, gruposBuscar, estado);
    }
    
    public void eliminar(ProductoServicio parametro) throws Exception {
        this.productoDAO.eliminar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(ProductoServicio parametro) throws Exception {
        this.productoDAO.insertar(parametro);
    }

    public ProductoServicio actualizar(ProductoServicio parametro) throws Exception {
        return (ProductoServicio) this.productoDAO.actualizar(parametro);
    }
}
