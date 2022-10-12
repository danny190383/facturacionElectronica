package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.DocumentosDAO;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.DetalleFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.DocumentoRetencion;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaDetalleSeriesPK;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GarantiaDetalle;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.NotaCredito;
import com.jvc.factunet.entidades.NotaDebito;
import com.jvc.factunet.entidades.PaqueteVenta;
import com.jvc.factunet.entidades.PedidoCompra;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.PendientesCompra;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.entidades.Proforma;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import com.jvc.factunet.entidades.TransferenciaProductos;
import com.jvc.factunet.utilitarios.Fecha;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class DocumentosServicios {
    private static final Logger LOG = Logger.getLogger(DocumentosServicios.class.getName());
    
    @EJB
    private DocumentosDAO documentosDAO;
    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    
    public List<DocumentoRetencion> listarFacturasRetencionCliente(Integer cliente){
        return this.documentosDAO.listarFacturasRetencionCliente(cliente);
    }
    
    public List<DocumentoRetencion> listarFacturasRetencionProveedor(Integer cliente){
        return this.documentosDAO.listarFacturasRetencionProveedor(cliente);
    }
    
    public PendientesCompra buscarPendientesCompra(Integer bodega){
        return this.documentosDAO.buscar(bodega);
    }
    
    public PedidoVenta buscarPedidoVenta(Integer codigo){
        return this.documentosDAO.pedidoVenta(codigo);
    }
    
    public GarantiaDetalle buscarIngreso(Integer codigo){
        return this.documentosDAO.ingresoProducto(codigo);
    }
    
    public FacturaDetalle buscarDetalle(Integer codigo){
        return this.documentosDAO.buscarDetalle(codigo);
    }
    
    public Factura buscarDocumento(Integer codigo){
        return this.documentosDAO.buscarDocumento(codigo);
    }
    
    public List<FacturaDetalle> buscarLotesCompraOrigen(Integer producto, Integer bodega){
        return this.documentosDAO.buscarLotesCompraOrigen(producto, bodega);
    }
    
    public List<FacturaDetalle> buscarLotesCompraDestino(Integer producto, Integer bodega){
        return this.documentosDAO.buscarLotesCompraDestino(producto, bodega);
    }
    
    public List<PedidoCompra> listarPedidosCompra(Integer empresa, String estado){
        return this.documentosDAO.listarPedidosCompra(empresa, estado);
    }
    
    public List<PedidoVenta> listarPedidosVenta(Integer empresa, String estado){
        return this.documentosDAO.listarPedidosVenta(empresa, estado);
    }
    
    public List<PedidoVenta> listarPedidosVentaCliente(Integer cliente, String estado){
        return this.documentosDAO.listarPedidosVentaCliente(cliente, estado);
    }
    
    public List<Empresa> listarPedidosVentaEmpresasCliente(Integer cliente, String estado) {
        return this.documentosDAO.listarPedidosVentaEmpresasCliente(cliente, estado);
    }
    
    public List<PedidoCompra> listarPedidoComprasFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarPedidosCompraFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<FacturaCompra> listarFacturaCompraFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarFacturaCompraFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<Proforma> listarProformasFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarProformasFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<TransferenciaProductos> listarTransferenciaProductosFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarTransferenciaProductosFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<FacturaVenta> listarFacturasVentaFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarFacturasVentaFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<GuiaRemision> listarGuiaRemisionFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula){
        return this.documentosDAO.listarGuiaRemisionFiltro(empresa, numero, fecha, proveedor, estado, cedula);
    }
    
    public List<DocumentoRetencion> listarDocumentoRetencionFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula){
        return this.documentosDAO.listarDocumentoRetencionFiltro(empresa, numero, fecha, proveedor, cedula);
    }
    
    public List<NotaCredito> listarNotasCreditoFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula){
        return this.documentosDAO.listarNotasCreditoFiltro(empresa, numero, fecha, proveedor, cedula);
    }
    
    public List<NotaDebito> listarNotasDebitoFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula){
        return this.documentosDAO.listarNotasDebitoFiltro(empresa, numero, fecha, proveedor, cedula);
    }
    
    public List<PedidoVenta> listarPedidoVentaFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula, String mascota, String mesa){
        return this.documentosDAO.listarPedidoVentaFiltro(empresa, numero, fecha, proveedor, estado, cedula, mascota, mesa);
    }
    
    public List<PedidoVenta> listarPedidoVentaFiltroCliente(Integer cliente,Integer numero, Date fecha, String proveedor, String estado, String cedula, String mascota, String mesa){
        return this.documentosDAO.listarPedidoVentaFiltroCliente(cliente, numero, fecha, proveedor, estado, cedula, mascota, mesa);
    }
    
    public List<Object> kardexProducto(Integer producto, Integer bodega){
        return this.documentosDAO.kardexProducto(producto, bodega);
    }
    
    public List<Object> kardexServicios(Integer producto){
        return this.documentosDAO.kardexServicios(producto);
    }
    
    public List<Object> kardexProductoFechas(Integer producto, Integer bodega, Date fechaInicion, Date fechaFin){
        return this.documentosDAO.kardexProductoFechas(producto, bodega, fechaInicion, fechaFin);
    }
    
    public List<FacturaPago> listarCreditosCliente(Integer empresa, Integer cliente){
        return this.documentosDAO.listarCreditosCliente(empresa, cliente);
    }
    
    public List<CuentaFactura> listarCuentasCliente(Integer empresa){
        return this.documentosDAO.listarCuentasCliente(empresa);
    }
    
    public List<CuentaFactura> listarCuentasProveedor(Integer empresa){
        return this.documentosDAO.listarCuentasProveedor(empresa);
    }
    
    public List<FacturaPago> listarCreditosClienteFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula){
        return this.documentosDAO.listarCreditosClienteFiltro(empresa, numero, fecha, cliente, cedula);
    }
    
    public List<CuentaFactura> listarCuentasClienteFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula, Date fechaV){
        return this.documentosDAO.listarCuentasClienteFiltro(empresa, numero, fecha, cliente, cedula, fechaV);
    }
    
    public List<CuentaFactura> listarCuentasProveedorFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula, Date fechaV){
        return this.documentosDAO.listarCuentasProveedorFiltro(empresa, numero, fecha, cliente, cedula, fechaV);
    }
    
    public List<FacturaPago> listarCreditosProveedor(Integer empresa, Integer proveedor){
        return this.documentosDAO.listarCreditosProveedor(empresa, proveedor);
    }
    
    public List<FacturaPago> listarCreditosProveedorFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula){
        return this.documentosDAO.listarCreditosProveedorFiltro(empresa, numero, fecha, proveedor, cedula);
    }
    
    public List<FacturaCompra> listarFacturasCompra(Integer empresa, String estado){
        return this.documentosDAO.listarFacturasCompra(empresa, estado);
    }
    
    public List<FacturaVenta> listarFacturasVenta(Integer empresa, String estado){
        return this.documentosDAO.listarFacturasVenta(empresa, estado);
    }
    
    public List<GuiaRemision> listarGuiasRemision(Integer empresa, String estado){
        return this.documentosDAO.listarGuiasRemision(empresa, estado);
    }
    
    public List<DocumentoRetencion> listarDocumentosRetencion(Integer empresa){
        return this.documentosDAO.listarDocumentosRetencion(empresa);
    }
    
    public List<NotaCredito> listarNotasCredito(Integer empresa){
        return this.documentosDAO.listarNotasCredito(empresa);
    }
    
    public List<NotaDebito> listarNotasDebito(Integer empresa){
        return this.documentosDAO.listarNotasDebito(empresa);
    }
    
    public List<PedidoVenta> listarPedidoVenta(Integer empresa, String estado){
        return this.documentosDAO.listarPedidoVenta(empresa, estado);
    }
    
    public List<PedidoVenta> listarPedidoVentaCliente(Integer cliente, String estado){
        return this.documentosDAO.listarPedidoVentaCliente(cliente, estado);
    }
    
    public List<Proforma> listarProformas(Integer empresa, String estado){
        return this.documentosDAO.listarProformas(empresa, estado);
    }
    
    public List<TransferenciaProductos> listarTransferenciaProductos(Integer empresa, String estado){
        return this.documentosDAO.listarTransferenciaProductos(empresa, estado);
    }
    
    public void eliminar(Factura parametro) throws Exception {
        this.documentosDAO.eliminar(parametro);
    }
    
    public void eliminar(FacturaDetalle parametro) throws Exception {
        this.documentosDAO.eliminar(parametro);
    }
    
    public void insertarDetalle(FacturaDetalle parametro){
        this.documentosDAO.insertar(parametro);
    }
    
    public void insertarDocumentoRetencion(DocumentoRetencion parametro){
        this.documentosDAO.insertar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarGuia(GuiaRemision parametro) throws Exception{
        FacturaVenta venta = (FacturaVenta) parametro.getFactura();
        SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(23,venta.getEmpresa().getCodigo(), venta.getPuntoVenta().getCodigo());
        if(secuencia!= null)
        {
            parametro.setSecuencia(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getSecuencia());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else
        {
            parametro.setSecuencia(001);
        }
        if(parametro.getFactura().getPuntoVenta().getFacturacionElectronica()){
            parametro.setCodigoBarras(this.generarClaveGuiaRemisionSRI("06", parametro.getFactura().getPuntoVenta().getAmbienteElectronica(), parametro));
        }
        this.documentosDAO.insertar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarDocumento(Factura parametro) throws Exception{
        this.documentosDAO.insertar(parametro);
        for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
        {
            if(detalle.getFacturaDetalleSeriesList()!= null)
            {
                for(FacturaDetalleSeries serie : detalle.getFacturaDetalleSeriesList())
                {
                    serie.setFacturaDetalle(detalle);
                    serie.getFacturaDetalleSeriesPK().setFacturaDetalle(detalle.getCodigo());
                    this.documentosDAO.insertar(serie);
                }
            }
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void facturarReserva(Factura parametro) throws Exception{
        SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(parametro.getSecuenciaDocumento().getCodigo());
        if(secuencia.getHasta() == null)
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else if(secuencia.getNumeroActual() < secuencia.getHasta())
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else
        {
            secuencia.setEstado("2");
            this.puntoVentaServicio.actualizar(secuencia);
        }
        if(parametro.getPuntoVenta().getFacturacionElectronica()){
            parametro.setCodigoBarras(this.generarClaveSRI("01", parametro.getPuntoVenta().getAmbienteElectronica(), parametro));
        }
        this.documentosDAO.insertar(parametro);
        parametro.getDocumentoRelacionado().setDocumentoRelacionado(parametro); 
        this.actualizarDocumento(parametro.getDocumentoRelacionado());
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(Factura parametro) throws Exception{
        if(parametro instanceof FacturaVenta)
        {
            FacturaVenta venta = (FacturaVenta) parametro;
            
            venta.getGuiaRemisionList().forEach((guia) -> {
                SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(23,venta.getEmpresa().getCodigo(), venta.getPuntoVenta().getCodigo());
                if(secuencia!= null)
                {
                    guia.setSecuencia(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(guia.getSecuencia());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else
                {
                    guia.setSecuencia(001);
                }
                if(venta.getPuntoVenta().getFacturacionElectronica()){
                    guia.setCodigoBarras(this.generarClaveGuiaRemisionSRI("06", guia.getFactura().getPuntoVenta().getAmbienteElectronica(), guia));
                }
            });
            
            venta.setFecha(new Date()); 
            if(venta.getTipoDocuemento().equals("1"))
            {
                SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(venta.getSecuenciaDocumento().getCodigo());
                venta.setTipoDocumento(secuencia.getTipoDocumento().getCodigo());
                if(secuencia.getHasta() == null)
                {
                    parametro.setNumero(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(parametro.getNumero());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else if(secuencia.getNumeroActual() < secuencia.getHasta())
                {
                    parametro.setNumero(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(parametro.getNumero());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else
                {
                    secuencia.setEstado("2");
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                for(GuiaRemision guia : parametro.getGuiaRemisionList()){
                    SecuenciaDocumento secuenciaGuia = this.puntoVentaServicio.numeroActual(23,venta.getEmpresa().getCodigo(), venta.getPuntoVenta().getCodigo());
                    if(secuenciaGuia!= null)
                    {
                        guia.setSecuencia(secuenciaGuia.getNumeroActual()+1);
                        secuenciaGuia.setNumeroActual(guia.getSecuencia());
                        this.puntoVentaServicio.actualizar(secuenciaGuia);
                    }
                    else
                    {
                        guia.setSecuencia(001);
                    }
                }
                if(venta.getPuntoVenta().getFacturacionElectronica()){
                    parametro.setCodigoBarras(this.generarClaveSRI("01", parametro.getPuntoVenta().getAmbienteElectronica(), parametro));
                }
            }
            else
            {
                SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(22,venta.getEmpresa().getCodigo(), venta.getPuntoVenta().getCodigo());
                if(secuencia!= null)
                {
                    venta.setTipoDocumento(secuencia.getTipoDocumento().getCodigo());
                    parametro.setNumero(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(parametro.getNumero());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else
                {
                    parametro.setNumero(001);
                }
            }
            venta.setObservacion(venta.getEmpresa().getCodigoSri() + "-" + venta.getPuntoVenta().getCodigoSri()); 
            for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
            {
                detalle.setFecha(new Date()); 
                if(detalle.getProductoServicio() instanceof ProductoBodega)
                {
                    ProductoStock stock = this.productoStockServicio.buscarProducto(detalle.getProductoServicio().getCodigo(), parametro.getBodega().getCodigo());
                    if(stock != null) 
                    {
                        stock.setStock(stock.getStock().subtract(detalle.getCantidad()));
                        this.productoStockServicio.actualizar(stock);
                        detalle.setStockFecha(stock.getStock());
                    }
                    if(detalle.getLoteVenta() != null){
                        FacturaDetalle lote = detalle.getLoteVenta();
                        lote.setStockActual(lote.getStockActual().subtract(detalle.getCantidad())); 
                        this.actualizar(lote);
                    }
                }
                if(detalle.getProductoServicio() instanceof ProductoPaquete)
                {
                    BigDecimal costoPaquete = BigDecimal.ZERO;
                    detalle.setDetallePaqueteList(new ArrayList<>());
                    for(PaqueteVenta proPaquete : ((ProductoPaquete)detalle.getProductoServicio()).getPaqueteVentaList())
                    {
                        costoPaquete = costoPaquete.add(proPaquete.getProducto().getPrecioUltimaCompra().multiply(proPaquete.getCantidad())); 
                        if(proPaquete.getProducto() instanceof ProductoBodega)
                        {
                            ProductoStock stock = this.productoStockServicio.buscarProducto(proPaquete.getProducto().getCodigo(), proPaquete.getBodega().getCodigo());
                            if(stock != null)
                            {
                                stock.setStock(stock.getStock().subtract(proPaquete.getCantidad().multiply(detalle.getCantidad())));
                                this.productoStockServicio.actualizar(stock);
                            }
                            if(proPaquete.getLote() != null){
                                FacturaDetalle lote = proPaquete.getLote();
                                lote.setStockActual(lote.getStockActual().subtract(proPaquete.getCantidad().multiply(detalle.getCantidad()))); 
                                this.actualizar(lote);
                            }
                            FacturaDetalle detallePaquete = new FacturaDetalle();
                            detallePaquete.setCostoFecha(proPaquete.getProducto().getPrecioUltimaCompra());
                            detallePaquete.setBodega(proPaquete.getBodega());
                            detallePaquete.setCantidad(proPaquete.getCantidad().multiply(detalle.getCantidad()));
                            detallePaquete.setPrecioVentaUnitarioDescuento((proPaquete.getTotal().divide((detallePaquete.getCantidad()),RoundingMode.HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            detallePaquete.setSubtotalConDescuento((detallePaquete.getPrecioVentaUnitarioDescuento().multiply(detallePaquete.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                            detallePaquete.setFecha(new Date());
                            detallePaquete.setStockFecha(stock.getStock());
                            detallePaquete.setProductoServicio(proPaquete.getProducto());
                            detallePaquete.setPaquete(detalle);
                            detalle.getDetallePaqueteList().add(detallePaquete);
                        }
                        else
                        {
                            FacturaDetalle detallePaquete = new FacturaDetalle();
                            detallePaquete.setCostoFecha(proPaquete.getProducto().getPrecioUltimaCompra());
                            detallePaquete.setCantidad(proPaquete.getCantidad().multiply(detalle.getCantidad()));
                            detallePaquete.setPrecioVentaUnitarioDescuento((proPaquete.getTotal().divide((detallePaquete.getCantidad()),RoundingMode.HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                            detallePaquete.setSubtotalConDescuento((detallePaquete.getPrecioVentaUnitarioDescuento().multiply(detallePaquete.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                            detallePaquete.setFecha(new Date());
                            detallePaquete.setProductoServicio(proPaquete.getProducto());
                            detallePaquete.setPaquete(detalle);
                            detalle.getDetallePaqueteList().add(detallePaquete);
                        }
                    }
                    detalle.setCostoFecha(costoPaquete.setScale(2, BigDecimal.ROUND_HALF_UP)); 
                }
                detalle.setDetalleFacturaImpuestoTarifaList(new ArrayList<>()); 
                
                DetalleFacturaImpuestoTarifa detalleImpuestoDefecto = new DetalleFacturaImpuestoTarifa();
                detalleImpuestoDefecto.setBaseImponible(detalle.getSubtotalConDescuento());
                detalleImpuestoDefecto.setDetalleFactura(detalle); 
                detalleImpuestoDefecto.setImpuestoTarifa(detalle.getImpuestoTarifa());
                detalleImpuestoDefecto.setPorcentaje(new BigDecimal(detalle.getImpuestoTarifa().getPorcentaje())); 
                detalleImpuestoDefecto.setValor((detalleImpuestoDefecto.getBaseImponible().multiply(detalleImpuestoDefecto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                detalle.getDetalleFacturaImpuestoTarifaList().add(detalleImpuestoDefecto);
                
                detalle.getProductoServicio().getProductoImpuestoTarifaList().stream().map((impuestoTarifa) -> {
                    DetalleFacturaImpuestoTarifa detalleImpuesto = new DetalleFacturaImpuestoTarifa();
                    detalleImpuesto.setBaseImponible(detalle.getSubtotalConDescuento());
                    detalleImpuesto.setDetalleFactura(detalle); 
                    detalleImpuesto.setImpuestoTarifa(impuestoTarifa.getImpuestoTarifa());
                    detalleImpuesto.setPorcentaje(new BigDecimal(impuestoTarifa.getImpuestoTarifa().getPorcentaje())); 
                    return detalleImpuesto;
                }).map((detalleImpuesto) -> {
                    detalleImpuesto.setValor((detalleImpuesto.getBaseImponible().multiply(detalleImpuesto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                    return detalleImpuesto;
                }).forEachOrdered((detalleImpuesto) -> {
                    if(!Objects.equals(detalleImpuesto.getImpuestoTarifa().getImpuesto().getId(), detalle.getImpuestoTarifa().getImpuesto().getId())){
                        detalle.getDetalleFacturaImpuestoTarifaList().add(detalleImpuesto);
                    }
                });
            }
            if(((FacturaVenta) parametro).getProforma() != null)
            {
                Proforma proforma = this.documentosDAO.buscarProforma(((FacturaVenta) parametro).getProforma());
                proforma.setEstado("2");
                this.documentosDAO.actualizar(proforma);
            }

            if(((FacturaVenta) parametro).getPedidosVenta() != null)
            {
                for(Map.Entry pedidoCod : ((FacturaVenta) parametro).getPedidosVenta().entrySet()){
                    PedidoVenta pedidoVenta = this.documentosDAO.pedidoVenta(Integer.parseInt(pedidoCod.getKey().toString()));
                    for(FacturaDetalle detalleV: venta.getFacturaDetalleList()){
                        //cantidad total vendica de ese producto
                        BigDecimal cantidadVendida = detalleV.getCantidad();
                        for(FacturaDetalle detalleP : pedidoVenta.getFacturaDetalleList()){
                            //si el producto del pedido es igual al producto vendido
                            if(Objects.equals(detalleP.getProductoServicio().getCodigo(), detalleV.getProductoServicio().getCodigo()))
                            {
                                //si hay algo por facturar en este producto
                                if(detalleP.getCantidadPorFacturar().floatValue() > 0){
                                    cantidadVendida = this.calcularFacturado(detalleP, cantidadVendida);
                                }
                                if(cantidadVendida.floatValue()==0){
                                    break;
                                }
                            }
                        }
                    }
                    this.documentosDAO.actualizar(pedidoVenta);
                    Boolean ban = Boolean.TRUE;
                    for(FacturaDetalle detalleP : pedidoVenta.getFacturaDetalleList()){
                        if(detalleP.getCantidadPorFacturar().floatValue()>0){
                            ban = Boolean.FALSE;
                        }
                    }
                    if(ban){
                        pedidoVenta.setEstado("2");
                    }
                    this.documentosDAO.actualizar(pedidoVenta);
                }
            }
        }
        if(parametro instanceof FacturaCompra)
        {
            for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
            {
                detalle.setFecha(new Date()); 
                ProductoStock stock = this.productoStockServicio.buscarProducto(detalle.getProductoServicio().getCodigo(), parametro.getBodega().getCodigo());
                if(stock != null)
                {
                    stock.setStock(stock.getStock().add(detalle.getCantidad()));
                    this.productoStockServicio.actualizar(stock);
                }
                else
                {
                    stock = new ProductoStock();
                    stock.setEmpresa(parametro.getEmpresa());
                    stock.setStock(detalle.getCantidad());
                    stock.setBodega(parametro.getBodega());
                    stock.setProductoBodega((ProductoBodega)detalle.getProductoServicio()); 
                    this.productoStockServicio.insertar(stock);
                }
                detalle.setStockActual(detalle.getCantidad()); 
                detalle.setStockFecha(stock.getStock());
                detalle.setCostoFecha(detalle.getPrecioVentaUnitarioTransporte()); 
                ProductoBodega producto = stock.getProductoBodega();
                producto.setUtilidad(detalle.getUtilidad());
                producto.setPvp(detalle.getPvp());
                producto.setPrecioUltimaCompra(detalle.getPrecioVentaUnitarioTransporte());
                producto.setFechaUltimaCompra(new Date());
                producto.setDescuentoVenta(detalle.getDescuentoVentas()); 
                this.productoBodegaServicio.actualizar(producto);
            }
            if(((FacturaCompra) parametro).getPedidoCompra() != null)
            {
                PedidoCompra pedido = this.documentosDAO.pedidoCompra(((FacturaCompra) parametro).getPedidoCompra());
                pedido.setEstado("3");
                this.documentosDAO.actualizar(pedido);
            }
            this.actualizarPendientes(parametro);
        }
        if(parametro instanceof PedidoCompra)
        {
            if(parametro.getSecuenciaDocumento() != null)
            {
                SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(parametro.getSecuenciaDocumento().getCodigo());
                if(secuencia!= null)
                {
                    parametro.setNumero(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(parametro.getNumero());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else
                {
                    parametro.setNumero(001);
                }
            }
        }
        this.documentosDAO.insertar(parametro);
        if((parametro instanceof FacturaCompra) || (parametro instanceof FacturaVenta))
        {
            for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
            {
                if(detalle.getFacturaDetalleSeriesList()!= null)
                {
                    for(FacturaDetalleSeries serie : detalle.getFacturaDetalleSeriesList())
                    {
                        if(parametro instanceof FacturaVenta)
                        {
                            serie.setEstado("2");
                        }
                        else
                        {
                            serie.setEstado("1");
                        }
                        serie.setBodegaActual(serie.getBodegaActual());
                        serie.setFacturaDetalle(detalle);
                        serie.setFacturaDetalleSeriesPK(new FacturaDetalleSeriesPK(detalle.getCodigo(), serie.getFacturaDetalleSeriesPK().getCodigoBarras()));
                        this.documentosDAO.insertar(serie);
                    }
                }
            }
        }
        this.revisarFacturaKardex(parametro);
    }
    
    public BigDecimal calcularFacturado(FacturaDetalle detalleP, BigDecimal cantidad){
        BigDecimal cantidadPorFacturar = detalleP.getCantidadPorFacturar().subtract(cantidad);
        BigDecimal pendiente = cantidad.subtract(detalleP.getCantidadPorFacturar());
        if(cantidadPorFacturar.floatValue()<0){
            detalleP.setCantidadPorFacturar(BigDecimal.ZERO);
            return pendiente;
        }
        detalleP.setCantidadPorFacturar(cantidadPorFacturar);
        return BigDecimal.ZERO;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void actualizarPendientes(Factura parametro) throws Exception {
        PendientesCompra pendientes = this.buscarPendientesCompra(parametro.getBodega().getCodigo());
        if(pendientes != null)
        {
            for(FacturaDetalle pendiente : pendientes.getFacturaDetalleList())
            {
                for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
                {
                    if(Objects.equals(pendiente.getProductoServicio().getCodigo(), detalle.getProductoServicio().getCodigo()))
                    {
                        pendiente.setCantidad(pendiente.getCantidad().subtract(detalle.getCantidad()));
                        break;
                    }
                }
                if(pendiente.getCantidad().floatValue() > 0)
                {
                    this.actualizar(pendiente);
                }
                else
                {
                    this.eliminar(pendiente);
                }
            }
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void anularFacturaVenta(Factura parametro) throws Exception {
        this.documentosDAO.actualizar(parametro);
        for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
        {
            if(detalle.getProductoServicio() instanceof ProductoBodega)
            {
                ProductoStock stock = this.productoStockServicio.buscarProducto(detalle.getProductoServicio().getCodigo(), parametro.getBodega().getCodigo());
                if(stock != null)
                {
                    stock.setStock(stock.getStock().add(detalle.getCantidad()));
                    this.productoStockServicio.actualizar(stock);
                    this.actualizarKardexAnular(detalle);
                }
                if(detalle.getLoteVenta() != null){
                    FacturaDetalle lote = detalle.getLoteVenta();
                    lote.setStockActual(lote.getStockActual().add(detalle.getCantidad())); 
                    this.actualizar(lote);
                }
            }
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                for(FacturaDetalle proPaquete : detalle.getDetallePaqueteList())
                {
                    if(proPaquete.getProductoServicio() instanceof ProductoBodega)
                    {
                        ProductoStock stock = this.productoStockServicio.buscarProducto(proPaquete.getProductoServicio().getCodigo(), proPaquete.getBodega().getCodigo());
                        if(stock != null)
                        {
                            stock.setStock(stock.getStock().add(proPaquete.getCantidad()));
                            this.productoStockServicio.actualizar(stock);
                            this.actualizarKardexAnular(proPaquete);
                        }
                        if(proPaquete.getLoteVenta() != null){
                            FacturaDetalle lote = proPaquete.getLoteVenta();
                            lote.setStockActual(lote.getStockActual().add(proPaquete.getCantidad())); 
                            this.actualizar(lote);
                        }
                    }
                }
            }
            if(detalle.getFacturaDetalleSeriesList() != null){
                for(FacturaDetalleSeries serie : detalle.getFacturaDetalleSeriesList()){
                    FacturaDetalleSeries serieUpdate = this.documentosDAO.serie(serie.getFacturaDetalleSeriesPK().getCodigoBarras(),detalle.getBodega().getCodigo(), "2");
                    if(serieUpdate != null){
                        serieUpdate.setEstado("1");
                        this.documentosDAO.actualizar(serieUpdate);
                    }
                }
            }
        }
    }
    
    public void revisarFacturaKardex(Factura parametro) throws Exception {
        if(parametro instanceof FacturaVenta || parametro instanceof FacturaCompra)
        {
            for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
            {
                if(detalle.getProductoServicio() instanceof ProductoBodega)
                {
                    if(parametro instanceof FacturaVenta)
                        this.actualizarKardex(detalle, Boolean.FALSE,this.documentosDAO.maxFactura(detalle.getFactura().getEmpresa().getCodigo()));
                    if(parametro instanceof FacturaCompra)
                        this.actualizarKardex(detalle, Boolean.TRUE, this.documentosDAO.maxFactura(detalle.getFactura().getEmpresa().getCodigo()));
                }
                if(detalle.getProductoServicio() instanceof ProductoPaquete)
                {
                    for(FacturaDetalle proPaquete : detalle.getDetallePaqueteList())
                    {
                        if(proPaquete.getProductoServicio() instanceof ProductoBodega)
                        {
                            if(parametro instanceof FacturaVenta)
                                this.actualizarKardex(proPaquete, Boolean.FALSE, this.documentosDAO.maxFactura(detalle.getFactura().getEmpresa().getCodigo()));
                            if(parametro instanceof FacturaCompra)
                                this.actualizarKardex(proPaquete, Boolean.TRUE, this.documentosDAO.maxFactura(detalle.getFactura().getEmpresa().getCodigo()));
                        }
                    }
                }
            }
        }
    }
    
    public void actualizarKardexAnular(FacturaDetalle detalle) throws Exception {
        Integer empresa;
        if(detalle.getFactura() == null){
            empresa = detalle.getPaquete().getFactura().getEmpresa().getCodigo();
        }
        else
        {
            empresa = detalle.getFactura().getEmpresa().getCodigo();
        }
        List<Object> listaKardex = this.kardexProductoFechas(detalle.getProductoServicio().getCodigo(), detalle.getBodega().getCodigo(), detalle.getFecha(), this.documentosDAO.maxFactura(empresa));
        for(Object obj : listaKardex)
        {
            Object[] nuevo = (Object[]) obj;
            FacturaDetalle detalleActualizar = this.buscarDetalle(Integer.parseInt(nuevo[0].toString()));
            detalleActualizar.setStockFecha(detalleActualizar.getStockFecha().add(detalle.getCantidad()));
            this.actualizar(detalleActualizar);
        }
    }
    
    public void actualizarKardex(FacturaDetalle detalle, Boolean tipo, Date fecha) throws Exception {
        List<Object> listaKardex = this.kardexProductoFechas(detalle.getProductoServicio().getCodigo(), detalle.getBodega().getCodigo(), detalle.getFecha(), fecha); 
        if(listaKardex.size() != 1)
        {
            Integer acumulador = 1;
            FacturaDetalle detalleAnterior = new FacturaDetalle();
            for(Object obj : listaKardex)
            {
                Object[] nuevo = (Object[]) obj;
                FacturaDetalle detalleActualizar = this.buscarDetalle(Integer.parseInt(nuevo[0].toString()));
                if(acumulador < listaKardex.size()){
                    detalleAnterior = detalleActualizar;
                    if(tipo)
                        detalleActualizar.setStockFecha(detalleActualizar.getStockFecha().add(detalle.getCantidad()));
                    else
                        detalleActualizar.setStockFecha(detalleActualizar.getStockFecha().subtract(detalle.getCantidad()));
                }
                else
                {
                    detalleActualizar.setStockFecha(detalleAnterior.getStockFecha().subtract(detalleAnterior.getCantidad()));
                }
                this.actualizar(detalleActualizar);
                acumulador ++;
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Factura actualizar(Factura parametro) throws Exception {
        if(parametro instanceof FacturaVenta)
        {
            for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
            {
                if(detalle.getListaSeriesDelete() != null)
                {
                    for(FacturaDetalleSeries serieDell : detalle.getListaSeriesDelete())
                    {
                        FacturaDetalleSeries serieDell1 = this.documentosDAO.serie(serieDell.getFacturaDetalleSeriesPK().getCodigoBarras(), detalle.getBodega().getCodigo(), "2");
                        serieDell1.setEstado("1");
                        this.documentosDAO.actualizar(serieDell1);
                    }
                }
                if(detalle.getFacturaDetalleSeriesList()!= null)
                {
                    for(FacturaDetalleSeries serie : detalle.getFacturaDetalleSeriesList())
                    {
                        serie.setFacturaDetalle(detalle);
                        serie.getFacturaDetalleSeriesPK().setFacturaDetalle(detalle.getCodigo());
                        FacturaDetalleSeries serieVenta = this.documentosDAO.serie(serie.getFacturaDetalleSeriesPK().getCodigoBarras(), detalle.getBodega().getCodigo(), "1");
                        if(serieVenta != null)
                        {
                            serieVenta.setEstado("2");
                            this.documentosDAO.actualizar(serieVenta);
                        }
                    }
                }
            }
        }
        return (Factura) this.documentosDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarNotaDebito(Factura parametro) throws Exception{
        SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(parametro.getSecuenciaDocumento().getCodigo());
        if(secuencia.getHasta() == null)
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else if(secuencia.getNumeroActual() < secuencia.getHasta())
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else
        {
            secuencia.setEstado("2");
            this.puntoVentaServicio.actualizar(secuencia);
        }
        parametro.setFecha(new Date()); 
        if(parametro.getPuntoVenta().getFacturacionElectronica()){
            parametro.setCodigoBarras(this.generarClaveSRI("05", parametro.getPuntoVenta().getAmbienteElectronica(), parametro));
        }
        this.documentosDAO.insertar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarNotaCredito(NotaCredito parametro) throws Exception {
        SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(parametro.getSecuenciaDocumento().getCodigo());
        if(secuencia.getHasta() == null)
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else if(secuencia.getNumeroActual() < secuencia.getHasta())
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else
        {
            secuencia.setEstado("2");
            this.puntoVentaServicio.actualizar(secuencia);
        }
        
        for(FacturaDetalle detalle : parametro.getFacturaDetalleList())
        {
            detalle.setDetalleFacturaImpuestoTarifaList(new ArrayList<>()); 
            DetalleFacturaImpuestoTarifa detalleImpuestoDefecto = new DetalleFacturaImpuestoTarifa();
            detalleImpuestoDefecto.setBaseImponible(detalle.getSubtotalConDescuento());
            detalleImpuestoDefecto.setDetalleFactura(detalle); 
            detalleImpuestoDefecto.setImpuestoTarifa(detalle.getImpuestoTarifa());
            detalleImpuestoDefecto.setPorcentaje(new BigDecimal(detalle.getImpuestoTarifa().getPorcentaje())); 
            detalleImpuestoDefecto.setValor((detalleImpuestoDefecto.getBaseImponible().multiply(detalleImpuestoDefecto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
            detalle.getDetalleFacturaImpuestoTarifaList().add(detalleImpuestoDefecto);
            
            detalle.getProductoServicio().getProductoImpuestoTarifaList().stream().map((impuestoTarifa) -> {
                DetalleFacturaImpuestoTarifa detalleImpuesto = new DetalleFacturaImpuestoTarifa();
                detalleImpuesto.setBaseImponible(detalle.getSubtotalConDescuento());
                detalleImpuesto.setDetalleFactura(detalle); 
                detalleImpuesto.setImpuestoTarifa(impuestoTarifa.getImpuestoTarifa());
                detalleImpuesto.setPorcentaje(new BigDecimal(impuestoTarifa.getImpuestoTarifa().getPorcentaje())); 
                return detalleImpuesto;
            }).map((detalleImpuesto) -> {
                detalleImpuesto.setValor((detalleImpuesto.getBaseImponible().multiply(detalleImpuesto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                return detalleImpuesto;
            }).forEachOrdered((detalleImpuesto) -> {
                if(!Objects.equals(detalleImpuesto.getImpuestoTarifa().getImpuesto().getId(), detalle.getImpuestoTarifa().getImpuesto().getId())){
                    detalle.getDetalleFacturaImpuestoTarifaList().add(detalleImpuesto);
                }
            });
            
            if(detalle.getProductoServicio() instanceof ProductoBodega)
            {
                ProductoStock stock = this.productoStockServicio.buscarProducto(detalle.getProductoServicio().getCodigo(), parametro.getBodega().getCodigo());
                if(stock != null)
                {
                    stock.setStock(stock.getStock().add(detalle.getCantidad()));
                    detalle.setStockFecha(stock.getStock());
                    this.productoStockServicio.actualizar(stock);
                    this.actualizarKardexAnular(detalle);
                }
                if(detalle.getLoteVenta() != null){
                    FacturaDetalle lote = detalle.getLoteVenta();
                    lote.setStockActual(lote.getStockActual().add(detalle.getCantidad())); 
                    this.actualizar(lote);
                }
            }
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                for(FacturaDetalle proPaquete : detalle.getDetallePaqueteList())
                {
                    if(proPaquete.getProductoServicio() instanceof ProductoBodega)
                    {
                        ProductoStock stock = this.productoStockServicio.buscarProducto(proPaquete.getProductoServicio().getCodigo(), proPaquete.getBodega().getCodigo());
                        if(stock != null)
                        {
                            stock.setStock(stock.getStock().add(proPaquete.getCantidad()));
                            this.productoStockServicio.actualizar(stock);
                            this.actualizarKardexAnular(proPaquete);
                        }
                    }
                }
            }
            if(detalle.getFacturaDetalleSeriesList() != null){
                for(FacturaDetalleSeries serie : detalle.getFacturaDetalleSeriesList()){
                    FacturaDetalleSeries serieUpdate = this.documentosDAO.serie(serie.getFacturaDetalleSeriesPK().getCodigoBarras(),detalle.getBodega().getCodigo(), "2");
                    if(serieUpdate != null){
                        serieUpdate.setEstado("1");
                        this.documentosDAO.actualizar(serieUpdate);
                    }
                }
            }
        }
        parametro.setFecha(new Date()); 
        if(parametro.getPuntoVenta().getFacturacionElectronica()){
            parametro.setCodigoBarras(this.generarClaveSRI("04", parametro.getPuntoVenta().getAmbienteElectronica(), parametro));
        }
        this.documentosDAO.insertar(parametro);
    }
    
    public String generarClaveSRI(String tipo, String ambiente, Factura factura){
        String clave;
        String dia = this.diaSRI(String.valueOf(Fecha.getDia(factura.getFecha())));
        String mes = this.mesSRI(String.valueOf(Fecha.getMes(factura.getFecha())));
        String fecha = dia+mes+String.valueOf(Fecha.getAnio(factura.getFecha()));
        clave = fecha+
                tipo+
                factura.getEmpresa().getRuc()+
                ambiente+
                factura.getEmpresa().getCodigoSri()+
                factura.getSecuenciaDocumento().getPuntoVenta().getCodigoSri()+
                this.numerocomprobanteSRI(factura.getNumero())+
                "12345678"+
                "1";
        clave = clave + this.digitoVerificador(clave);
        return clave;
    }
    
    public String generarClaveGuiaRemisionSRI(String tipo, String ambiente, GuiaRemision factura){
        String clave;
        String dia = this.diaSRI(String.valueOf(Fecha.getDia(factura.getFactura().getFecha())));
        String mes = this.mesSRI(String.valueOf(Fecha.getMes(factura.getFactura().getFecha())));
        String fecha = dia+mes+String.valueOf(Fecha.getAnio(factura.getFactura().getFecha()));
        clave = fecha+
                tipo+
                factura.getFactura().getEmpresa().getRuc()+
                ambiente+
                factura.getFactura().getEmpresa().getCodigoSri()+
                factura.getFactura().getPuntoVenta().getCodigoSri()+
                this.numerocomprobanteSRI(factura.getSecuencia())+
                "12345678"+
                "1";
        clave = clave + this.digitoVerificador(clave);
        return clave;
    }
    
    public String generarClaveRetencionSRI(String tipo, String ambiente, DocumentoRetencion factura){
        String clave;
        String dia = this.diaSRI(String.valueOf(Fecha.getDia(factura.getFactura().getFecha())));
        String mes = this.mesSRI(String.valueOf(Fecha.getMes(factura.getFactura().getFecha())));
        String fecha = dia+mes+String.valueOf(Fecha.getAnio(factura.getFactura().getFecha()));
        clave = fecha+
                tipo+
                factura.getFactura().getEmpresa().getRuc()+
                ambiente+
                factura.getFactura().getEmpresa().getCodigoSri()+
                factura.getFactura().getEmpleado().getPuntoVenta().getCodigoSri()+ 
                this.numerocomprobanteSRI(factura.getNumero())+
                "12345678"+
                "1";
        clave = clave + this.digitoVerificador(clave);
        return clave;
    }
    
    public String diaSRI(String dia){
        char[] temp=new char[]{ '0' };
        if(dia.length()<2){
            dia = temp[0] + dia;
        }
        return dia;
    }
    
    public String mesSRI(String mes){
        char[] temp=new char[]{ '0' };
        if(mes.length()<2){
             mes = temp[0] + mes;
        }
        return mes;
    }
    
    public String numerocomprobanteSRI(Integer numero){
        String numeroS = numero.toString();
        char[] temp=new char[]{ '0' };
        if(numeroS.length()<9){
            for(int i = numeroS.length() ; i<9 ; i++){
                numeroS = temp[0] + numeroS;
            }
        }
        return numeroS;
    }
    
    public String digitoVerificador(String numero){
        Integer digito = this.obtenerSumaPorDigitos(this.invertirCadena(numero)); 
        return digito.toString();
    }
    
    public String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x--) {
            cadenaInvertida = cadenaInvertida + cadena.charAt(x);
        }
        return cadenaInvertida;
    }
 
    public int obtenerSumaPorDigitos(String cadena) {
        int pivote = 2;
        int longitudCadena = cadena.length();
        int cantidadTotal = 0;
        int b = 1;
        for (int i = 0; i < longitudCadena; i++) {
            if (pivote == 8) {
                pivote = 2;
            }
            int temporal = Integer.parseInt("" + cadena.substring(i, b));
            b++;
            temporal *= pivote;
            pivote++;
            cantidadTotal += temporal;
        }
        cantidadTotal = 11 - cantidadTotal % 11;
        switch(cantidadTotal){
            case 10:
                return 1;
            case 11:
                return 0;
        }
        return cantidadTotal;
    }
    
    public String generarXML(Integer codigoDocumento) {
        return this.documentosDAO.generarXML(codigoDocumento);
    }
    
    public PedidoVenta actualizar(PedidoVenta parametro){
        return (PedidoVenta) this.documentosDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Factura actualizarFactura(Factura parametro) throws Exception{
        if(parametro instanceof FacturaCompra)
        {
            FacturaCompra compra = (FacturaCompra) parametro;
            compra.getDocumentoRetencion().forEach((retencion) -> {
                SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(472,compra.getEmpresa().getCodigo(), compra.getEmpleado().getPuntoVenta().getCodigo());
                if(secuencia!= null)
                {
                    retencion.setNumero(secuencia.getNumeroActual()+1);
                    secuencia.setNumeroActual(retencion.getNumero());
                    this.puntoVentaServicio.actualizar(secuencia);
                }
                else
                {
                    retencion.setNumero(001);
                }
                if(parametro.getPuntoVenta().getFacturacionElectronica()){
                    retencion.setCodigoBarras(this.generarClaveRetencionSRI("07", "1", retencion));
                }
            });
        }
        this.documentosDAO.actualizar(parametro);
        return (Factura) parametro; 
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GuiaRemision actualizarGriaRemision(GuiaRemision parametro) throws Exception{
        this.documentosDAO.actualizar(parametro);
        return (GuiaRemision) parametro; 
    }
    
    public GuiaRemision actualizar(GuiaRemision parametro) throws Exception {
        return (GuiaRemision) this.documentosDAO.actualizar(parametro);
    }
    
    public FacturaDetalle actualizar(FacturaDetalle parametro) throws Exception {
        return (FacturaDetalle) this.documentosDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public DocumentoRetencion actualizarDocumentoRetencion(DocumentoRetencion parametro) throws Exception {
        return (DocumentoRetencion) this.documentosDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Factura actualizarDocumento(Factura parametro) throws Exception {
        return (Factura) this.documentosDAO.actualizar(parametro);
    }
    
    public List<FacturaDetalle> buscarLotesCompraMayorCero(Integer producto, Integer bodega){
        return this.documentosDAO.buscarLotesCompraMayorCero(producto, bodega);
    }
    
    public List<FacturaDetalle> buscarLotesCompraMayorCeroDestino(Integer producto, Integer bodega){
        return this.documentosDAO.buscarLotesCompraMayorCeroDestino(producto, bodega);
    }
    
    public List<FacturaDetalle> buscarLotesCompraMayorCero(Integer producto){
        return this.documentosDAO.buscarLotesCompraMayorCero(producto);
    }
    
    public List<FacturaDetalle> buscarLotesCompraMayorCeroDestino(Integer producto){
        return this.documentosDAO.buscarLotesCompraMayorCeroDestino(producto);
    }
}
