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
    
    public ProductoServicio buscarCodigoBarras(String barras) {
        return productoDAO.buscarBarras(barras);
    }
    
    public List<ProductoServicio> listar(Integer empresa, Integer grupo, int maxResults, int firstResult) {
        return productoDAO.listar(empresa, grupo, maxResults, firstResult);
    }
    
    public Long contar(Integer empresa, Integer grupo){
        return productoDAO.contar(empresa, grupo);
    }
    
    public List<ProductoServicio> listarBuscar(String nombre,  Integer empresa, Integer grupo, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, maxResults, firstResult);
    }
    
    public List<ProductoServicio> listarBuscar(String nombre,  Integer empresa, String grupo, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, empresa, grupo, maxResults, firstResult);
    }
    
    public List<ProductoServicio> listarBuscarBarras(String codigoBarras, Integer empresa, int maxResults, int firstResult) {
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
    
    public List<ProductoServicio> listarPadre(Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoDAO.listarPadre(empresa, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar){
        return productoDAO.contarPadre(empresa, gruposBuscar);
    }
    
    public List<ProductoServicio> listarBuscarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoDAO.listarBuscarPadre(nombre, empresa, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar){
        return productoDAO.contarPadre(nombre, empresa, gruposBuscar);
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
