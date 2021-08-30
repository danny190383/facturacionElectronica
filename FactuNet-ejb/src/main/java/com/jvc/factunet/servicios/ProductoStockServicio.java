package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ProductoStockDAO;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.IndividualizacionProducto;
import com.jvc.factunet.entidades.ProductoStock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class ProductoStockServicio {
    
    private static final Logger LOG = Logger.getLogger(ProductoStockServicio.class.getName());
    
    @EJB
    private ProductoStockDAO productoStockDAO;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    
    public ProductoStock buscarBarras(String barras) {
        return productoStockDAO.buscarBarras(barras);
    }
    
    public ProductoStock buscarProducto(int producto, int bodega) {
        return productoStockDAO.buscarProducto(producto, bodega);
    }
    
    public List<ProductoStock> listar(Integer bodega, Integer grupo, int maxResults, int firstResult) {
        return productoStockDAO.listar(bodega, grupo, maxResults, firstResult);
    }
    
    public List<ProductoStock> listar(Integer bodega, String grupo, int maxResults, int firstResult) {
        return productoStockDAO.listar(bodega, grupo, maxResults, firstResult);
    }
    
    public Long contar(Integer bodega, Integer grupo){
        return productoStockDAO.contar(bodega, grupo);
    }
    
    public Long contar(Integer bodega, String grupo){
        return productoStockDAO.contar(bodega, grupo);
    }
    
    public List<ProductoStock> listarBuscar(String nombre, String modelo, String marca, Integer bodega, Integer grupo, int maxResults, int firstResult) {
        return productoStockDAO.listarBuscar(nombre, modelo, marca, bodega, grupo, maxResults, firstResult);
    }
    
    public List<ProductoStock> listarBuscar(String nombre, String modelo, String marca, Integer bodega, String grupo, int maxResults, int firstResult) {
        return productoStockDAO.listarBuscar(nombre, modelo, marca, bodega, grupo, maxResults, firstResult);
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer bodega, Integer grupo){
        return productoStockDAO.contar(nombre, modelo, marca, bodega, grupo);
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer bodega, String grupo){
        return productoStockDAO.contar(nombre, modelo, marca, bodega, grupo);
    }
    
    public List<ProductoStock> listarPadre(Integer bodega, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoStockDAO.listarPadre(bodega, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(Integer bodega, List<Integer> gruposBuscar){
        return productoStockDAO.contarPadre(bodega, gruposBuscar);
    }
    
    public List<ProductoStock> listarBuscarPadre(String nombre, String modelo, String marca, Integer bodega, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        return productoStockDAO.listarBuscarPadre(nombre, modelo, marca, bodega, gruposBuscar, maxResults, firstResult);
    }
    
    public Long contarPadre(String nombre, String modelo, String marca, Integer bodega, List<Integer> gruposBuscar){
        return productoStockDAO.contarPadre(nombre, modelo, marca, bodega, gruposBuscar);
    }
    
    public ProductoStock actualizar(ProductoStock parametro) throws Exception {
        return (ProductoStock) this.productoStockDAO.actualizar(parametro);
    }
    
    public void insertar(ProductoStock parametro) throws Exception {
        this.productoStockDAO.insertar(parametro);
    }
    
    public List<ProductoStock> listarBuscarBarras(String codigoBarras, Integer empresa, int maxResults, int firstResult) {
        return this.productoStockDAO.listarBuscarBarras(codigoBarras, empresa, maxResults, firstResult);
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa){
        return this.productoStockDAO.contarBarras(codigoBarras, empresa);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void actualizarIndividualizacion(Empleado responsable ,ProductoStock origen, ProductoStock destino) throws Exception {
        this.actualizar(origen);
        this.actualizar(destino);
        this.productoBodegaServicio.actualizar(destino.getProductoBodega());
        IndividualizacionProducto transferencia = new IndividualizacionProducto();
        transferencia.setBodega(origen.getBodega());
        transferencia.setEmpleado(responsable);
        transferencia.setEmpresa(responsable.getEmpresa());
        transferencia.setEstado("1");
        transferencia.setFecha(new Date());
        transferencia.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setProductoServicio(origen.getProductoBodega());
        detalle.setProductoServicioDestino(destino.getProductoBodega());
        detalle.setFactura(transferencia);
        detalle.setBodega(origen.getBodega());
        detalle.setCantidad(origen.getCantidad());
        detalle.setFecha(new Date());
        detalle.setEmpleado(responsable);
        detalle.setStockFecha(destino.getStock());
        detalle.setStockOrigenFecha(origen.getStock());
        detalle.setPrecioVentaUnitarioDescuento(destino.getProductoBodega().getPrecioUltimaCompra()); 
        detalle.setCostoFecha(destino.getProductoBodega().getPrecioUltimaCompra()); 
        transferencia.getFacturaDetalleList().add(detalle);
        this.documentosServicios.insertarDocumento(transferencia);
    }
}
