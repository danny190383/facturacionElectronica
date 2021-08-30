package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.BodegaDAO;
import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaDetalleSeriesPK;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.entidades.TransferenciaProductos;
import java.math.BigDecimal;
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
public class BodegaServicio {
    
    private static final Logger LOG = Logger.getLogger(BodegaServicio.class.getName());
    
    @EJB
    private BodegaDAO bodegaDAO;
    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private SerieProductoServicios serieProductoServicios;
    
    public List<Bodega> listar(Integer empresa) {
        return bodegaDAO.listar(empresa);
    }
    
    public List<Bodega> listarPorNivel(Integer nivel) {
        return this.bodegaDAO.listarPorNivel(nivel);
    }
    
    public Bodega bodegaPrincipal(Integer empresa) {
        return this.bodegaDAO.bodegaPrincipal(empresa);
    }
    
    public void eliminar(Bodega parametro) throws Exception {
        this.bodegaDAO.eliminar(parametro);
    }
    
    public void insertar(Bodega parametro) throws Exception {
        this.bodegaDAO.insertar(parametro);
    }

    public Bodega actualizar(Bodega parametro) throws Exception {
        return (Bodega) this.bodegaDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void trasnferirProductos(List<ProductoStock> parametro, Bodega bodegaOrigen, Bodega bodegaDestino, Empleado responsable) throws Exception {
        TransferenciaProductos transferencia = new TransferenciaProductos();
        transferencia.setBodega(bodegaOrigen);
        transferencia.setEmpleado(responsable);
        transferencia.setEmpresa(responsable.getEmpresa());
        transferencia.setEstado("1");
        transferencia.setFecha(new Date());
        transferencia.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        for(ProductoStock producto : parametro)
        {
            ProductoStock productoDestino = new ProductoStock();
            if(producto.getStock().floatValue() >= producto.getCantidad().floatValue() )
            {
                producto.setStock(producto.getStock().subtract(producto.getCantidad()));
                this.productoStockServicio.actualizar(producto);
//                productoDestino = this.productoStockServicio.buscarProducto(producto.getProductoStockPK().getProducto(), bodegaDestino.getCodigo());
                if(productoDestino != null)
                {
                    productoDestino.setStock(productoDestino.getStock().add(producto.getCantidad()));
                    this.productoStockServicio.actualizar(productoDestino);
                }
                else
                {
                    productoDestino = new ProductoStock();
                    productoDestino.setStock(producto.getCantidad());
                    productoDestino.setStockMax(BigDecimal.ZERO);
                    productoDestino.setStockMin(BigDecimal.ZERO);
                    productoDestino.setEmpresa(producto.getEmpresa());
//                    productoDestino.setProductoStockPK(new ProductoStockPK(producto.getProductoStockPK().getProducto(),bodegaDestino.getCodigo()));
                    this.productoStockServicio.insertar(productoDestino);
                }
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setProductoServicio(producto.getProductoBodega());
                detalle.setFactura(transferencia);
                detalle.setBodega(bodegaDestino);
                detalle.setCantidad(producto.getCantidad());
                detalle.setFecha(new Date());
                detalle.setEmpleado(responsable);
                detalle.setStockFecha(productoDestino.getStock());
                detalle.setStockOrigenFecha(producto.getStock());
                if(producto.getListaSeries() != null)
                {
                    detalle.setFacturaDetalleSeriesList(new ArrayList<FacturaDetalleSeries>());
                    for(FacturaDetalleSeries serie : producto.getListaSeries())
                    {
                        FacturaDetalleSeries serieOriginal = this.serieProductoServicios.buscarProductoSerie(serie.getFacturaDetalleSeriesPK().getCodigoBarras(),responsable.getEmpresa().getCodigo(), producto.getProductoBodega().getCodigo());
                        if(serieOriginal != null)
                        {
                            serieOriginal.setBodegaActual(bodegaDestino);
                            this.serieProductoServicios.actualizar(serieOriginal);
                            FacturaDetalleSeries serieT = new FacturaDetalleSeries();
                            serieT.setFacturaDetalleSeriesPK(new FacturaDetalleSeriesPK(0, serie.getFacturaDetalleSeriesPK().getCodigoBarras()));
                            serieT.setFacturaDetalle(detalle);
                            detalle.getFacturaDetalleSeriesList().add(serieT);
                        }
                    }
                }
                transferencia.getFacturaDetalleList().add(detalle);
            }
        }
        this.documentosServicios.insertarDocumento(transferencia);
    }
}
