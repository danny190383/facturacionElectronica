package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProductoBodegaDAO;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class ProductoBodegaServicio {
    
    private static final Logger LOG = Logger.getLogger(ProductoBodegaServicio.class.getName());
    
    @EJB
    private ProductoBodegaDAO productoDAO;
    
    public Producto buscarCodigoBarras(String barras, Integer empresa, Integer bodega) {
        return productoDAO.buscarBarras(barras, empresa, bodega);
    }
    
    public Producto buscarSeries(String barras, Integer empresa) {
        return productoDAO.buscarSerie(barras, empresa);
    }
    
    public Producto buscarSerieFactura(String serie, Integer empresa, String estadoSerie, String estadoFactura) {
        return productoDAO.buscarSerieFactura(serie, empresa, estadoSerie, estadoFactura);
    }
    
    public ProductoBodega buscarCodigoBarrasBodega(String barras, Integer empresa) {
        return productoDAO.buscarBarrasBodega(barras, empresa);
    }
    
    public ProductoBodega buscarNombreBodega(String nombre, Integer empresa) {
        return productoDAO.buscarNombreBodega(nombre, empresa);
    }
    
    public List<ProductoBodega> listar(Integer empresa, Integer grupo, String lugar, int maxResults, int firstResult) {
        return productoDAO.listar(empresa, grupo, lugar, maxResults, firstResult);
    }
    
    public List<ProductoBodega> listarTodo(Integer empresa) {
        return productoDAO.listarTodo(empresa);
    }
    
    public Long contar(Integer empresa, Integer grupo, String lugar){
        return productoDAO.contar(empresa, grupo, lugar);
    }
    
    public List<ProductoBodega> listarBuscar(String nombre, String modelo, String marca, Integer empresa, Integer grupo, String lugar, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, modelo, marca, empresa, grupo, lugar, maxResults, firstResult);
    }
    
    public List<ProductoBodega> listarBuscar(String nombre, String modelo, String marca, Integer empresa, String grupo, String lugar, int maxResults, int firstResult) {
        return productoDAO.listarBuscar(nombre, modelo, marca, empresa, grupo, lugar, maxResults, firstResult);
    }
    
    public List<ProductoBodega> listarBuscarBarras(String codigoBarras, Integer empresa, String lugar, int maxResults, int firstResult) {
        return productoDAO.listarBuscarBarras(codigoBarras, empresa, lugar, maxResults, firstResult);
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer empresa, Integer grupo, String lugar){
        return productoDAO.contar(nombre, modelo, marca, empresa, grupo, lugar);
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer empresa, String grupo, String lugar){
        return productoDAO.contar(nombre, modelo, marca, empresa, grupo, lugar);
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa, String lugar){
        return productoDAO.contarBarras(codigoBarras, empresa, lugar);
    }
    
    public List<ProductoBodega> listarPadre(Integer empresa, List<Integer> gruposBuscar, String lugar, int maxResults, int firstResult) {
        return productoDAO.listarPadre(empresa, gruposBuscar, lugar, maxResults, firstResult);
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar, String lugar){
        return productoDAO.contarPadre(empresa, gruposBuscar, lugar);
    }
    
    public List<ProductoBodega> listarBuscarPadre(String nombre, String modelo, String marca, Integer empresa, List<Integer> gruposBuscar, String lugar, int maxResults, int firstResult) {
        return productoDAO.listarBuscarPadre(nombre, modelo, marca, empresa, gruposBuscar, lugar, maxResults, firstResult);
    }
    
    public Long contarPadre(String nombre, String modelo, String marca, Integer empresa, List<Integer> gruposBuscar, String lugar){
        return productoDAO.contarPadre(nombre, modelo, marca, empresa, gruposBuscar, lugar);
    }
    
    public void eliminar(ProductoBodega parametro) throws Exception {
        this.productoDAO.eliminar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(ProductoBodega parametro) throws Exception {
//        try {
            this.productoDAO.insertar(parametro);
//            for(ProductoStock obj : parametro.getProductoStockList())
//            {
//                obj.setProductoStockPK(new ProductoStockPK(obj.getProductoBodega().getCodigo(), obj.getBodega().getCodigo()));
//                this.productoDAO.insertar(obj);
//            }
//
//            if(!parametro.getPacaProductoList().isEmpty()){
//                ProductoStock pro = parametro.getPacaProductoList().get(0).getProductoStockList().get(0);
//                pro.setProductoStockPK(new ProductoStockPK(pro.getProductoBodega().getCodigo(), pro.getBodega().getCodigo()));
//                this.productoDAO.insertar(pro);
//            }
//        } catch (Exception e) {
//            return;
//        }
        
    }

    public ProductoBodega actualizar(ProductoBodega parametro) throws Exception {
        return (ProductoBodega) this.productoDAO.actualizar(parametro);
    }
    
    public Producto actualizar(Producto parametro) throws Exception {
        return (Producto) this.productoDAO.actualizar(parametro);
    }
}
