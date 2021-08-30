package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProductoPaqueteDAO;
import com.jvc.factunet.entidades.ProductoPaquete;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class ProductoPaqueteServicio {
    
    private static final Logger LOG = Logger.getLogger(ProductoPaqueteServicio.class.getName());
    
    @EJB
    private ProductoPaqueteDAO productoDAO;
    
    public ProductoPaquete buscarCodigoBarras(String barras) {
        return productoDAO.buscarBarras(barras);
    }
    
    public List<ProductoPaquete> listar(Integer empresa, Integer grupo, int maxResults, int firstResult) {
        return productoDAO.listar(empresa, grupo, maxResults, firstResult);
    }
    
    public Long contar(Integer empresa, Integer grupo){
        return productoDAO.contar(empresa, grupo);
    }
    
    public List<ProductoPaquete> listarBuscar(String nombre,  Integer empresa, Integer grupo, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, maxResults, firstResult);
    }
    
    public List<ProductoPaquete> listarBuscar(String nombre,  Integer empresa, String grupo, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, maxResults, firstResult);
    }
    
    public List<ProductoPaquete> listarBuscarBarras(String codigoBarras, Integer empresa, int maxResults, int firstResult) {
        return productoDAO.listarBuscarBarras(codigoBarras, empresa, maxResults, firstResult);
    }
    
    public Long contar(String nombre, Integer empresa, Integer grupo){
        return productoDAO.contar(nombre, empresa, grupo);
    }
    
    public Long contar(String nombre, Integer empresa, String grupo){
        return productoDAO.contar(nombre, empresa, grupo);
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa){
        return productoDAO.contarBarras(codigoBarras, empresa);
    }
    
    public List<ProductoPaquete> listarPadre(Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoDAO.listarPadre(empresa, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar){
        return productoDAO.contarPadre(empresa, gruposBuscar);
    }
    
    public List<ProductoPaquete> listarBuscarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoDAO.listarBuscarPadre(nombre, empresa, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar){
        return productoDAO.contarPadre(nombre, empresa, gruposBuscar);
    }
    
    public void eliminar(ProductoPaquete parametro) throws Exception {
        this.productoDAO.eliminar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(ProductoPaquete parametro) throws Exception {
        this.productoDAO.insertar(parametro);
    }

    public ProductoPaquete actualizar(ProductoPaquete parametro) throws Exception {
        return (ProductoPaquete) this.productoDAO.actualizar(parametro);
    }
}
