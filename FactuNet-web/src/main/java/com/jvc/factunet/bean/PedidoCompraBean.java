package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.CabeceraFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.DocumentoRetencion;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import com.jvc.factunet.entidades.PaqueteVenta;
import com.jvc.factunet.entidades.PedidoCompra;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoImpuestoTarifa;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.Retencion;
import com.jvc.factunet.entidades.RetencionPK;
import com.jvc.factunet.entidades.RetencionPersona;
import com.jvc.factunet.entidades.RetencionServicio;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import com.jvc.factunet.entidades.TipoRetencion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.FormaPagoServicio;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.servicios.ProveedorServicio;
import com.jvc.factunet.servicios.PuntoVentaServicio;
import com.jvc.factunet.servicios.TipoRetencionServicio;
import com.jvc.factunet.servicios.TipoTarifaImpuestoServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class PedidoCompraBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(PedidoCompraBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private BodegaServicio bodegaServicio;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private ProveedorServicio proveedorServicio;
    @EJB
    private TipoRetencionServicio tipoRetencionServicio;
    @EJB
    private FormaPagoServicio formaPagoServicio;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    @EJB
    private TipoTarifaImpuestoServicio tipoTarifaImpuestoServicio;

    private List<Bodega> listaBodegas;
    private Integer bodegaSelect;
    private String codigoBarras;
    private PedidoCompra pedidoCompra;
    private List<Proveedor> listaProveedores;
    private List<TipoRetencion> listaTipoRetencion;
    private Integer retencionSlc;
    private BigDecimal totalTransporte;
    private BigDecimal totalProrrateo;
    private BigDecimal ivaEmpresa;
    private BigDecimal descuento;
    private BigDecimal comision;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private Producto productoVer;
    private List<ImpuestoTarifa> listaTarifas;
    
    public PedidoCompraBean() {
        if(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta() == null){
            FacesUtils.addErrorMessage("El empleado no tiene asignado un punto de venta.");
            FacesUtils.redireccionar("/index.xhtml");
        }
        this.listaBodegas = new ArrayList<>();
        this.listaProveedores = new ArrayList<>();
        this.listaTipoRetencion = new ArrayList<>();
        this.listaTarifas = new ArrayList<>();
        this.comision = BigDecimal.ZERO;
        this.productoVer = new Producto();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaTarifas.addAll(this.tipoTarifaImpuestoServicio.listarImpuesto(1)); 
        this.listaProveedores.addAll(proveedorServicio.listar(this.empresa.getCodigo()));
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
        this.bodegaSelect = this.listaBodegas.get(0).getCodigo();
        this.listaTipoRetencion.addAll(this.tipoRetencionServicio.listar());
        this.retencionSlc = this.listaTipoRetencion.get(0).getCodigo();
        this.totalTransporte = BigDecimal.ZERO;
        this.totalProrrateo = BigDecimal.ZERO;
        this.ivaEmpresa = ((Login)FacesUtils.getManagedBean("login")).getIvaEmpresa();
        this.inicializar();
    }
    
    public void inicializar()
    {
        this.pedidoCompra = new PedidoCompra();
        this.pedidoCompra.setFecha(new Date());
        this.pedidoCompra.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.pedidoCompra.setFacturaDetalleList(new ArrayList<>());
        this.pedidoCompra.setEmpresa(this.empresa);
        this.pedidoCompra.setGuiaRemisionList(new ArrayList<>());
        this.pedidoCompra.setTotal(BigDecimal.ZERO);
        this.pedidoCompra.setFacturaPagoList(new ArrayList<>());
        if(!this.listaProveedores.isEmpty()){
            this.pedidoCompra.setProveedor(this.listaProveedores.get(0));
            this.onProveedorSelect();
        }
        this.pedidoCompra.setEstado("1");
        this.asignarNumeroPedido();
    }
    
    public void asignarNumeroPedido(){
        List<PuntoVenta> listaTmp = this.puntoVentaServicio.listarPuntoVenta(this.empresa.getCodigo());
        for(PuntoVenta punto : listaTmp){
            for(SecuenciaDocumento secuencia : punto.getSecuenciaDocumentoList())
            {
                if((secuencia.getTipoDocumento().getCodigo() == 775) && (secuencia.getEstado().equals("1")))
                {
                    this.pedidoCompra.setSecuenciaDocumento(secuencia); 
                    this.pedidoCompra.setNumero(secuencia.getNumeroActual()+1); 
                    break;
                }
            }
        }
        if(this.pedidoCompra.getSecuenciaDocumento() == null)
        {
            this.pedidoCompra.setNumero(1); 
        }
    }
    
    public void guardar()
    {
        try {
            if(this.pedidoCompra.getFacturaDetalleList().size()>0)
            {
                if(this.pedidoCompra.getCodigo() == null)
                {
                    this.pedidoCompra.setBodega(new Bodega(this.bodegaSelect));
                    this.documentosServicios.insertar(this.pedidoCompra);
                }
                else
                {
                    this.documentosServicios.actualizar(this.pedidoCompra);
                }
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("debeingresarproductos"));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verPagoRetencionVentana(Factura factura){
        if(factura.getTotalRetencion().floatValue() > 0)
        {
            this.generarPagoRetencion(factura);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", factura.getFacturaPagoList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", factura.getTotal());
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", true);
            options.put("modal", true);
            options.put("width", 800);
            options.put("height", 500);
            options.put("contentWidth", 800);
            options.put("contentHeight", 500);
            options.put("includeViewParams", true);
            PrimeFaces.current().dialog().openDynamic("/transacciones/extras/cuentaCobroDialog", options, null);
        }
        else
        {
            this.actualizar(factura); 
        }
    }
    
    public void actualizar(Factura factura)
    {
        try {
            this.documentosServicios.actualizarFactura(factura);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void generarPagoRetencion(Factura factura)
    {
        FacturaPago pago = new FacturaPago();
        if(factura.getTotalRetencion().floatValue() > 0)
        {
            pago.setFormaPago(this.formaPagoServicio.buscar(170));
            pago.setFactura(factura);
            pago.setValor(factura.getTotalRetencion());
            for(FacturaPago pagoDelete : factura.getFacturaPagoList()){
                if(pagoDelete.getFormaPago().getCodigo() == 170){
                    factura.getFacturaPagoList().remove(pagoDelete);
                    break;
                }
            }
            factura.getFacturaPagoList().add(pago);
        }
    }
    
    public void verBusquedaProductos() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 600);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 600);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProductosDialog", options, null);
    }
    
    public BigDecimal setStockBodega(ProductoBodega productoBodega, Integer bodega)
    {
        for(ProductoStock obj : productoBodega.getProductoStockList())
        {
            if(Objects.equals(bodega, obj.getBodega().getCodigo()))
            {
                return obj.getStock();
            }
        }
        return BigDecimal.ZERO;
    }
    
    public FacturaDetalle crearDetalle(FacturaDetalle detalle)
    {
        detalle.setFecha(new Date());
        detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        detalle.setCantidad(detalle.getCantidad());
        detalle.setDescuento(this.descuento);
        detalle.setComision(this.comision);
        ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
        if(detalle.getPrecioVentaUnitario() == null)
        {
            detalle.setPrecioVentaUnitario(productoBodega.getPrecioUltimaCompra());
        }
        detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario().subtract(((detalle.getPrecioVentaUnitario().multiply(detalle.getDescuento())).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP))));  
        detalle.setUtilidad(productoBodega.getUtilidad());
        BigDecimal valorUtilidad = detalle.getPrecioVentaUnitario().multiply(productoBodega.getUtilidad().divide(new BigDecimal("100"),BigDecimal.ROUND_HALF_UP));
        detalle.setPvp(detalle.getPrecioVentaUnitario().add(valorUtilidad));
        detalle.setStock(this.setStockBodega(productoBodega, this.bodegaSelect));
        detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        detalle.setValorDescuento((detalle.getSubtotalSinDescuento().multiply(detalle.getDescuento().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP)); 
        detalle.setSubtotalConDescuento((detalle.getPrecioVentaUnitarioDescuento().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        detalle.setPrecioVentaUnitarioTransporte(detalle.getPrecioVentaUnitarioDescuento());
        detalle.setValorComision((detalle.getSubtotalConDescuento().multiply(detalle.getComision().divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));  
        for(ProductoImpuestoTarifa tarifaImpuesto : detalle.getProductoServicio().getProductoImpuestoTarifaList()){
            if(tarifaImpuesto.getImpuestoTarifa().getImpuesto().getId() == 1){
                detalle.setImpuestoTarifa(tarifaImpuesto.getImpuestoTarifa());
                break;
            }
        }
        detalle.setValorProrrateo(BigDecimal.ZERO);
        detalle.setPvpIva(detalle.getPvp().multiply((this.getIvaEmpresa().divide(new BigDecimal("100"),RoundingMode.HALF_UP)).add(BigDecimal.ONE)));
        detalle.setBodega(new Bodega(this.bodegaSelect));
        return detalle;
    }
    
    public void cambiarBodega(Factura factura)
    {
        for(FacturaDetalle detalle : factura.getFacturaDetalleList())
        {
            detalle.setBodega(new Bodega(this.bodegaSelect));
        }
    }
    
    public void onPendientesSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaDetalle> listaProductos = (List) event.getObject();
            Boolean ban;
            for(FacturaDetalle detalle : listaProductos)
            {
                ban = Boolean.TRUE;
                for(FacturaDetalle obj: this.pedidoCompra.getFacturaDetalleList())
                {
                    if(Objects.equals(obj.getProductoServicio().getCodigo(), detalle.getProductoServicio().getCodigo()))
                    {
                        ban = Boolean.FALSE;
                        break;
                    }
                }
                if(ban)
                {
                    detalle.setCodigo(null);
                    detalle.setFactura(this.pedidoCompra);
                    this.pedidoCompra.getFacturaDetalleList().add(this.crearDetalle(detalle));
                }
            }
            this.calcularTotales();
        }
    }
    
    public void setStockPrecio()
    {
        for(FacturaDetalle detalle : this.pedidoCompra.getFacturaDetalleList())
        {
            detalle = this.crearDetalle(detalle);
        }
        this.calcularTotales();
    }
    
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.pedidoCompra);
        this.calcularTotales();
    }
    
    public void agregarProductos(List<Producto> listaProductos, Factura factura)
    {
        if(listaProductos != null)
        {
            Boolean banNoExiste;
            for(Producto producto : listaProductos)
            {
                if(producto.getProductoImpuestoTarifaList() == null || producto.getProductoImpuestoTarifaList().isEmpty()){
                    FacesUtils.addErrorMessage("El producto " + producto.getNombre() + " No tiene definido un tipo de impuesto");
                    return;
                }
                banNoExiste = Boolean.TRUE;
                for(FacturaDetalle detalle: factura.getFacturaDetalleList())
                {
                    if(Objects.equals(detalle.getProductoServicio().getCodigo(), producto.getCodigo()))
                    {
                        if(this.verificarDetalle(detalle, producto)) 
                        {
                            banNoExiste = Boolean.FALSE;
                            break;
                        }
                    }
                }
                if(banNoExiste)
                {
                    this.nuevoDetalle(factura, producto, BigDecimal.ONE);
                }
            }
        }
    }
    
    public Boolean verificarDetalle(FacturaDetalle detalle, Producto producto)
    {
        Boolean banSerie;
        if(producto.getSerie() != null)
        {
            banSerie = Boolean.TRUE;
            for(FacturaDetalleSeries var : detalle.getFacturaDetalleSeriesList())
            {
                if(producto.getSerie().getFacturaDetalleSeriesPK().getCodigoBarras().equals(var.getFacturaDetalleSeriesPK().getCodigoBarras()))
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("serieAgregada"));
                    return true;
                }
            }
            if(banSerie)
            {
                detalle.setCantidad(detalle.getCantidad().add(BigDecimal.ONE));
                FacturaDetalleSeries serie = producto.getSerie();
                detalle.getFacturaDetalleSeriesList().add(serie);
                this.onCellEditCantidad(detalle);
                return true;
            }
        }
        else
        {
            if(producto instanceof ProductoBodega)
            {
                if(detalle.getFechaCaducidad() == null)
                {
                    detalle.setCantidad(detalle.getCantidad().add(BigDecimal.ONE));
                    this.onCellEditCantidad(detalle);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            detalle.setCantidad(detalle.getCantidad().add(BigDecimal.ONE));
            this.onCellEditCantidad(detalle);
            return true;
        }
        return false;
    }
    
    public void nuevoDetalle(Factura factura, Producto producto, BigDecimal cantidad)
    {
        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setFactura(factura);
        detalle.setProductoServicio(producto);
        detalle.setCantidad(cantidad);
        if(producto.getSerie() != null)
        {
            detalle.setFacturaDetalleSeriesList(new ArrayList<>());
            detalle.getFacturaDetalleSeriesList().add(producto.getSerie());
        }
        factura.getFacturaDetalleList().add(this.crearDetalle(detalle));
    }
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            this.productoVer = new Producto();
            this.agregarCalcular(listaProductos);
        }
    }
    
    public void verNuevoProducto() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipo", 2);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", null);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/nuevoProductoDialog", options, null);
    }
    
    public void onProductoNuevoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            ProductoBodega producto = (ProductoBodega) event.getObject();
            if(producto != null)
            {
                List<Producto> lista = new ArrayList<>();
                lista.add(producto);
                this.agregarCalcular(lista);
            }
        }
    }
    
    public Factura calcularTotalPago(Factura factura) {
        factura.setSubtotal(BigDecimal.ZERO);
        factura.setIva(BigDecimal.ZERO);
        factura.setTotal(BigDecimal.ZERO);
        factura.setTotalPagar(BigDecimal.ZERO);
        factura.setDescuento(BigDecimal.ZERO);
        factura.setComision(BigDecimal.ZERO);
        factura.setTotalRetencion(BigDecimal.ZERO);
        factura = this.calcularSubTotalales(factura);
        for (FacturaDetalle pago : factura.getFacturaDetalleList()) {
            factura.setSubtotal(factura.getSubtotal().add(pago.getSubtotalSinDescuento()));
            factura.setDescuento(factura.getDescuento().add(pago.getValorDescuento()));
            factura.setComision(factura.getComision().add(pago.getValorComision()));
        }
        BigDecimal subtotalConIva = BigDecimal.ZERO;
        BigDecimal subtotalSinIva = BigDecimal.ZERO;
        BigDecimal subtotalICE = BigDecimal.ZERO;
        BigDecimal subtotalIRBPNR = BigDecimal.ZERO;
        
        for(CabeceraFacturaImpuestoTarifa impuestoTarifa : factura.getCabeceraFacturaImpuestoTarifaList()){
            
            if(impuestoTarifa.getImpuestoTarifa().getImpuesto().getId() == 1){ // IVA
                if(impuestoTarifa.getValor().floatValue() > 0){
                    subtotalConIva = subtotalConIva.add(impuestoTarifa.getBaseImponible()); 
                }
                else
                {
                    subtotalSinIva = subtotalSinIva.add(impuestoTarifa.getBaseImponible());
                }
            }
            
            if(impuestoTarifa.getImpuestoTarifa().getImpuesto().getId() == 2){ // ICE
                subtotalICE = subtotalICE.add(impuestoTarifa.getValor());
            }
            
            if(impuestoTarifa.getImpuestoTarifa().getImpuesto().getId() == 3){ // IRBPNR
                subtotalIRBPNR = subtotalIRBPNR.add(impuestoTarifa.getValor());
            }
        }
        
        factura.setIce(subtotalICE);
        factura.setIrbpnr(subtotalIRBPNR);

        if (subtotalConIva.doubleValue() > 0) {
            factura.setIva((subtotalConIva.multiply(this.ivaEmpresa.divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            factura.setIva(new BigDecimal(0));
        }
        factura.setTotal((subtotalConIva.add(factura.getIva()).add(subtotalSinIva)).setScale(2, BigDecimal.ROUND_HALF_UP));
        factura.setTotalPagar((factura.getTotal().subtract(factura.getTotalRetencion())).setScale(2, BigDecimal.ROUND_HALF_UP));
        return factura; 
    }
    
    public Factura calcularSubTotalales(Factura factura) {
        factura.setCabeceraFacturaImpuestoTarifaList(new ArrayList<>()); 
        for (ImpuestoTarifa tarifa : this.listaTarifas){
            CabeceraFacturaImpuestoTarifa impuesto = new CabeceraFacturaImpuestoTarifa();
            BigDecimal descuentoT = BigDecimal.ZERO;
            BigDecimal subtotal = BigDecimal.ZERO;
            for (FacturaDetalle detalle : factura.getFacturaDetalleList()) {
                if(Objects.equals(tarifa.getId(), detalle.getImpuestoTarifa().getId())){
                    subtotal = subtotal.add(detalle.getSubtotalSinDescuento());
                    descuentoT = descuentoT.add(detalle.getValorDescuento());
                }
            }
            impuesto.setEtiqueta("Subtotal " + tarifa.getDescripcion()); 
            impuesto.setBaseImponible(subtotal.subtract(descuentoT)); 
            impuesto.setCabeceraFactura(factura);
            impuesto.setImpuestoTarifa(tarifa); 
            impuesto.setPorcentaje(new BigDecimal(tarifa.getPorcentaje())); 
            impuesto.setValor((impuesto.getBaseImponible().multiply(impuesto.getPorcentaje().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
            factura.getCabeceraFacturaImpuestoTarifaList().add(impuesto);
        }
        return factura; 
    }
    
    public void calcularRetenciones(DocumentoRetencion documento)
    {
        documento.setTotalRetencion(BigDecimal.ZERO);
        for(Retencion obj : documento.getRetencionList())
        {
            documento.setTotalRetencion(documento.getTotalRetencion().add(obj.getValor()));
        }
        documento.getTotalRetencion().setScale(2, BigDecimal.ROUND_HALF_UP);
        this.calcularTotalRetenciones(documento.getFactura()); 
    }
    
    public void calcularTotalRetenciones(Factura factura)
    {
        factura.setTotalRetencion(BigDecimal.ZERO);
        for(DocumentoRetencion obj : factura.getDocumentoRetencion())
        {
            if(obj.getTotalRetencion().floatValue()>0)
            {
                factura.setTotalRetencion(factura.getTotalRetencion().add(obj.getTotalRetencion()));
            }
        }
        factura.getTotalRetencion().setScale(2, BigDecimal.ROUND_HALF_UP);
        factura.setTotalPagar((factura.getTotal().subtract(factura.getTotalRetencion())).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    
    public void buscarProductoBarras()
    {
        Producto proTMP;
        if(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta() != null)
        {
            proTMP = this.productoBodegaServicio.buscarCodigoBarras(this.codigoBarras, this.empresa.getCodigo(), ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getBodega().getCodigo());
        }
        else
        {
            proTMP = this.productoBodegaServicio.buscarCodigoBarras(this.codigoBarras, this.empresa.getCodigo(), null);
        }
        if(proTMP == null)
        {
            proTMP = this.productoBodegaServicio.buscarSeries(this.codigoBarras, this.empresa.getCodigo());
        }
        if(proTMP != null)
        {
            List<Producto> lista = new ArrayList<>();
            this.productoVer = proTMP;
            lista.add(proTMP);
            this.agregarCalcular(lista);
        }
        else
        {
            this.productoVer = new Producto();
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoNoEncontrado"));
        }
        this.codigoBarras = StringUtils.EMPTY;
    }
    
    public void eliminar(FacturaDetalle parametro) {
        try {
            this.pedidoCompra.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarRetencion(DocumentoRetencion documento,Retencion parametro) {
        try {
            documento.getRetencionList().remove(parametro);
            this.calcularRetenciones(documento); 
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarDocumento(Factura factura, DocumentoRetencion documento) {
        try {
            factura.getDocumentoRetencion().remove(documento);
            this.calcularTotalRetenciones(factura); 
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void calcularTotales()
    {
        this.pedidoCompra = (PedidoCompra) this.calcularTotalPago(this.pedidoCompra);
    }
    
    public void onCellEditPVP(FacturaDetalle event) {
        event.setPvpIva(BigDecimal.ZERO);
        if(event.getPrecioVentaUnitarioTransporte().floatValue()>0){
            event.setUtilidad(((event.getPvp().subtract(event.getPrecioVentaUnitarioTransporte())).divide(event.getPrecioVentaUnitarioTransporte(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
        }
    }
    
    public void onCellEditPVPIVA(FacturaDetalle event) {
        event.setPvp(event.getPvpIva().divide(this.getIvaEmpresa().divide(new BigDecimal("100")).add(BigDecimal.ONE),4,1));
//        event.setPvp(event.getPvp().setScale(2,RoundingMode.HALF_UP)); //floor
        if(event.getPrecioVentaUnitarioTransporte().floatValue()>0){
            event.setUtilidad(((event.getPvp().subtract(event.getPrecioVentaUnitarioTransporte())).divide(event.getPrecioVentaUnitarioTransporte(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
        }
    }
    
    public void onCellEditUtilidad(FacturaDetalle event) {
        event.setPvp(event.getPrecioVentaUnitarioTransporte().add(event.getPrecioVentaUnitarioTransporte().multiply(event.getUtilidad().divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));
        event.setPvpIva(event.getPvp().add(event.getPvp().multiply(this.getIvaEmpresa().divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));
    }
    
    public void onCellEditCantidad(FacturaDetalle event){
        event.setSubtotalSinDescuento((event.getPrecioVentaUnitario().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        event.setSubtotalConDescuento((event.getPrecioVentaUnitarioDescuento().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        event.setValorDescuento((event.getSubtotalSinDescuento().multiply(event.getDescuento().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        event.setValorComision((event.getSubtotalConDescuento().multiply(event.getComision().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        this.calcularTotales();
        if(event.getFactura() instanceof FacturaCompra){
            this.onCellEditUtilidad(event);
            this.calcularTotalProrrateo(event.getFactura());
        }
    }
    
    public void onCellEditValorUnitario(FacturaDetalle event){
        event.setPrecioUnitarioTmp(event.getPrecioVentaUnitario());
        event.setComision(BigDecimal.ZERO);
        event.setValorComision(BigDecimal.ZERO);
        event.setSubtotalSinDescuento((event.getPrecioVentaUnitario().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        event.setValorDescuento((event.getSubtotalSinDescuento().multiply(event.getDescuento().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        event.setPrecioVentaUnitarioDescuento(event.getPrecioVentaUnitario().subtract(((event.getPrecioVentaUnitario().multiply(event.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP)));
        event.setPrecioVentaUnitarioDescuento(event.getPrecioVentaUnitarioDescuento().add(((event.getPrecioVentaUnitarioDescuento().multiply(event.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP)));
        event.setSubtotalConDescuento((event.getPrecioVentaUnitarioDescuento().multiply(event.getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP)));
        event.setValorComision((event.getSubtotalConDescuento().multiply(event.getComision().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        this.onCellEditPrecioVentaUnitarioDescuento(event,true);
        this.calcularTotales();
    }
    
    public void onCellEditPrecioVentaUnitarioDescuento(FacturaDetalle event, Boolean calcularProrrateo) {
        BigDecimal valorUtilidad = BigDecimal.ZERO;
        if(event.getUtilidad() != null)
        {
            valorUtilidad = event.getPrecioVentaUnitario().multiply(event.getUtilidad().divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            if(event.getProductoServicio() instanceof ProductoBodega)
            {
                ProductoBodega productoBodega = (ProductoBodega) event.getProductoServicio();
                valorUtilidad = event.getPrecioVentaUnitario().multiply(productoBodega.getUtilidad().divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        if(event.getPrecioVentaUnitarioTransporte()!= null)
        {
            event.setPrecioVentaUnitarioTransporte(event.getPrecioVentaUnitarioDescuento());
            if(calcularProrrateo)
            {
                this.calcularTotalProrrateo(event.getFactura());
            }
        }
        event.setPvp(event.getPrecioVentaUnitarioDescuento().add(valorUtilidad));
        event.setPvpIva(event.getPvp().multiply((this.getIvaEmpresa().divide(new BigDecimal("100"),RoundingMode.HALF_UP)).add(BigDecimal.ONE)));
    }
    
    public void calcularTotalProrrateo(Factura factura)
    {
        this.setTotalProrrateo(BigDecimal.ZERO);
        for(FacturaDetalle detalle : factura.getFacturaDetalleList())
        {
            if(detalle.getPrecioVentaUnitarioDescuento().floatValue() > 0)
            {
                detalle.setValorProrrateo(((detalle.getPrecioVentaUnitarioTransporte().subtract(detalle.getPrecioVentaUnitarioDescuento())).multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                this.setTotalProrrateo(this.getTotalProrrateo().add(detalle.getValorProrrateo()));
            }
        }
    }
    
    public void onCellEditDescuento(FacturaDetalle event, Boolean calcularTotales) {
        event.setPrecioVentaUnitarioDescuento(event.getPrecioVentaUnitario().subtract(((event.getPrecioVentaUnitario().multiply(event.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP)));
        event.setValorDescuento((event.getSubtotalSinDescuento().multiply(event.getDescuento().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        event.setSubtotalConDescuento((event.getPrecioVentaUnitarioDescuento().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        if(event.getIsPaquete())
        {
            for(PaqueteVenta proP : ((ProductoPaquete) event.getProductoServicio()).getPaqueteVentaList())
            {
                proP.setTotal(proP.getTotal().add((proP.getTotal().multiply(event.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
                proP.setTotal(proP.getTotal().subtract((proP.getTotal().multiply(event.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
                
            }
        }
        this.onCellEditPrecioVentaUnitarioDescuento(event,false);
        if(calcularTotales)
        {
            this.calcularTotales();
        }
    }
    
    public void onCellEditComision(FacturaDetalle event, Boolean calcularTotales) {
        event.setValorComision((((event.getPrecioUnitarioTmp().multiply(event.getComision())).divide(new BigDecimal(100))).multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));  
        event.setPrecioVentaUnitario(event.getPrecioUnitarioTmp().add((event.getPrecioUnitarioTmp().multiply(event.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
        event.setSubtotalSinDescuento(event.getPrecioVentaUnitario().multiply(event.getCantidad()));
        this.onCellEditDescuento(event, calcularTotales);
    }
    
    public void onCellEditValorDescuento(FacturaDetalle event) {
        event.setDescuento((event.getValorDescuento().divide(event.getSubtotalSinDescuento(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
        this.onCellEditDescuento(event, true);
    }
    
    public void onCellEditValorComision(FacturaDetalle event) {
        event.setComision((event.getValorComision().divide(event.getPrecioUnitarioTmp(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
        this.onCellEditComision(event, true);
    }
    
    public void verBusquedaPendientes() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bodega", this.bodegaSelect);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 950);
        options.put("height", 500);
        options.put("contentWidth", 950);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPendientesCompraDialog", options, null);
    }
    
    public void verBusquedaPedidos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("estado", "-1");
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedidosCompraDialog", options, null);
    }
    
    public void onPedidoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.pedidoCompra = (PedidoCompra) event.getObject();
            for(FacturaDetalle detalle : this.pedidoCompra.getFacturaDetalleList())
            {
                ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                detalle.setStock(this.setStockBodega(productoBodega, this.bodegaSelect));
            }
            this.calcularTransporte();
            this.calcularTotales();
        }
    }
    
    public void verGuia(GuiaRemision guia) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guia", guia);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("factura", this.pedidoCompra);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaGuiaDialog", options, null);
    }
    
    public void onGuiaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            GuiaRemision guia = (GuiaRemision) event.getObject();
            if(this.pedidoCompra.getGuiaRemisionList() == null)
            {
                this.pedidoCompra.setGuiaRemisionList(new ArrayList<GuiaRemision>());
            }
            if((!this.pedidoCompra.getGuiaRemisionList().contains(guia)) && (guia != null))
            {
                this.pedidoCompra.getGuiaRemisionList().add(guia);
            }
            this.calcularTransporte();
        }
    }
    
    public void verPago() {
        if(this.pedidoCompra.getTotal() != null)
        {
            if(this.pedidoCompra.getTotal().doubleValue() > 0)
            {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 1);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.pedidoCompra.getTotal());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.pedidoCompra.getFacturaPagoList());
                Map<String, Object> options = new HashMap<>();
                options.put("resizable", true);
                options.put("draggable", true);
                options.put("modal", true);
                options.put("width", 800);
                options.put("height", 500);
                options.put("contentWidth", 800);
                options.put("contentHeight", 500);
                options.put("includeViewParams", true);
                PrimeFaces.current().dialog().openDynamic("/transacciones/extras/cuentaCobroDialog", options, null);
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoMayorCero"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoMayorCero"));
        }
    }
    
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            if(this.pedidoCompra.getFacturaPagoList() == null)
            {
                this.pedidoCompra.setFacturaPagoList(new ArrayList<FacturaPago>());
            }
            for(FacturaPago pago : listaPagosFin){
                pago.setFactura(this.pedidoCompra);
                this.pedidoCompra.getFacturaPagoList().add(pago);    
            }
            this.cerrar();
        }
    }
    
    public void verKardex(Producto producto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vista", "1");
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        options.put("includeViewParams", true);
        if(producto instanceof ProductoBodega)
        {
            PrimeFaces.current().dialog().openDynamic("/transacciones/extras/kardexProductosDialog", options, null);
        }
        else 
        {
            PrimeFaces.current().dialog().openDynamic("/transacciones/extras/kardexServiciosDialog", options, null);
        }
    }
    
    public void eliminarGuia(GuiaRemision parametro) {
        try {
            this.pedidoCompra.getGuiaRemisionList().remove(parametro);
            this.calcularTransporte();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void calcularTransporte()
    {
        this.totalTransporte = BigDecimal.ZERO;
        for(GuiaRemision obj : this.pedidoCompra.getGuiaRemisionList())
        {
            this.totalTransporte = this.totalTransporte.add(obj.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    public void cerrar()
    {
        try {
            this.pedidoCompra.setEstado("2");
            this.documentosServicios.actualizar(this.pedidoCompra);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
        
    }
    
    public void onProveedorSelect()
    {
        this.descuento = this.pedidoCompra.getProveedor().getDescuento();
        this.setStockPrecio();
    }
    
    public void cargarRetenciones(DocumentoRetencion documento){
        if(documento.getFactura() instanceof FacturaCompra)
        {
            for(RetencionPersona obj : documento.getFactura().getProveedor().getPersona().getRetencionPersonaList())
            {
                this.retencionSlc = obj.getCodigo();
                this.agregarRetencion(documento);
            }
        }
        if(documento.getFactura() instanceof FacturaVenta)
        {
            for(RetencionPersona obj : documento.getFactura().getCliente().getPersona().getRetencionPersonaList())
            {
                this.retencionSlc = obj.getCodigo();
                this.agregarRetencion(documento);
            }
        }
    }
    
    public void crearDocumentoRetencion(Factura factura) {
        DocumentoRetencion documento = new DocumentoRetencion();
        documento.setFecha(new Date());
        if(factura instanceof FacturaCompra){
            documento.setNumero(null); 
        }
        else
        {
            documento.setNumero(1); 
        }
        documento.setEstadoDocumento("1"); 
        documento.setEstadoSRI("1");
        documento.setFactura(factura);
        documento.setTotalRetencion(BigDecimal.ZERO); 
        documento.setRetencionList(new ArrayList<>()); 
        this.documentosServicios.insertarDocumentoRetencion(documento);
        if(factura.getDocumentoRetencion().isEmpty()){
            this.cargarRetenciones(documento); 
        }
        factura.getDocumentoRetencion().add(documento);
    }
    
    public void agregarRetencion(DocumentoRetencion documento) {
        Boolean ban = Boolean.TRUE;
        for(DocumentoRetencion documentos :  documento.getFactura().getDocumentoRetencion()){
            for(Retencion ret : documentos.getRetencionList())
            {
                if(Objects.equals(ret.getTipoRetencion().getCodigo(), this.retencionSlc))
                {
                    ban = Boolean.FALSE;
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("retencionAgregada"));
                    break;
                }
            }
        }
        if(ban)
        {
            Retencion retencion = new Retencion();
            for(TipoRetencion obj : this.listaTipoRetencion)
            {
                if(Objects.equals(this.retencionSlc, obj.getCodigo()))
                {
                    retencion.setTipoRetencion(obj);
                    retencion.setDocumentoRetencion(documento);
                    retencion.setValor(BigDecimal.ZERO);
                    retencion.setRetencionPK(new RetencionPK(obj.getCodigo(), documento.getCodigo()));
                    if(retencion.getTipoRetencion().getTipo().equals("2")){ //BIENES
                        List<BigDecimal> lista = this.subtotalBienes(documento.getFactura());
                        BigDecimal subtotal = lista.get(0);
                        BigDecimal iva = lista.get(1);
                        if("2".equals(retencion.getTipoRetencion().getCalculadoCon()))
                        {
                            retencion.setValor((subtotal.multiply(retencion.getTipoRetencion().getValor().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                            retencion.setBaseImponible(subtotal);
                        }
                        else
                        {
                            retencion.setValor((iva.multiply(retencion.getTipoRetencion().getValor().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                            retencion.setBaseImponible(iva);
                        }
                    }
                    else //SERVICIOS
                    {
                        List<BigDecimal> lista = this.subtotalServicios(documento.getFactura(),obj);
                        BigDecimal subtotal = lista.get(0);
                        BigDecimal iva = lista.get(1);
                        if("2".equals(retencion.getTipoRetencion().getCalculadoCon()))
                        {
                            retencion.setValor((subtotal.multiply(retencion.getTipoRetencion().getValor().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                            retencion.setBaseImponible(subtotal);
                        }
                        else
                        {
                            retencion.setValor((iva.multiply(retencion.getTipoRetencion().getValor().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                            retencion.setBaseImponible(iva);
                        }
                    }
                    break;
                }
            }
            if(retencion.getValor().floatValue()>0){
                documento.getRetencionList().add(retencion);
                this.calcularRetenciones(documento); 
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("noRetencion"));
            }
        }
    }
    
    public List<BigDecimal> subtotalBienes(Factura factura){
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal subtotalIVA = BigDecimal.ZERO;
        for (FacturaDetalle item : factura.getFacturaDetalleList()) {
            if(!(item.getProductoServicio() instanceof ProductoServicio)){
                subtotal = subtotal.add(item.getSubtotalSinDescuento());
                if(item.getImpuestoTarifa().getPorcentaje() > 0){
                    subtotalIVA = subtotalIVA.add(item.getSubtotalSinDescuento());
                }
            }
        }
        if(subtotalIVA.floatValue() > 0)
        {
            iva = subtotalIVA.multiply(this.ivaEmpresa.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        List<BigDecimal> totales = new ArrayList<>();
        totales.add(subtotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        totales.add(iva); 
        return totales;
    }
    
    public List<BigDecimal> subtotalServicios(Factura factura, TipoRetencion tipo){
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal subtotalIVA = BigDecimal.ZERO;
        for (FacturaDetalle item : factura.getFacturaDetalleList()) {
            if(item.getProductoServicio() instanceof ProductoServicio){
                Boolean ban = Boolean.FALSE;
                ProductoServicio servicio = (ProductoServicio) item.getProductoServicio();
                for(RetencionServicio retencionServicio : servicio.getRetencionServicioList()){
                    if(Objects.equals(tipo.getCodigo(), retencionServicio.getTipoRetencion().getCodigo())){
                        ban = Boolean.TRUE;
                        break;
                    }
                }
                if(ban){
                    subtotal = subtotal.add(item.getSubtotalSinDescuento());
                    if(item.getImpuestoTarifa().getPorcentaje() > 0)
                    {
                        subtotalIVA = subtotalIVA.add(item.getSubtotalSinDescuento());
                    }
                }
            }
        }
        if(subtotalIVA.floatValue() > 0)
        {
            iva = subtotalIVA.multiply(this.ivaEmpresa.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        List<BigDecimal> totales = new ArrayList<>();
        totales.add(subtotal.setScale(2, BigDecimal.ROUND_HALF_UP));
        totales.add(iva); 
        return totales;
    }
    
    public BigDecimal ivaBienes(Factura factura){
        BigDecimal subtotal = BigDecimal.ZERO;
        
        return subtotal;
    }
    
    public void setBodegaDetalle()
    {
        for(FacturaDetalle detalle : this.pedidoCompra.getFacturaDetalleList())
        {
            detalle.setBodega(new Bodega(this.getBodegaSelect()));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        if(this.pedidoCompra.getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", this.pedidoCompra.getCodigo());
                super.getParametros().put("nombreReporte", "Pedido de Compra");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
            }
        }
    }
    
    public void generalDescuento()
    {
        for(FacturaDetalle detalle : this.pedidoCompra.getFacturaDetalleList()){
            detalle.setDescuento(this.getDescuento());
            this.onCellEditDescuento(detalle, false);
        }
        this.calcularTotales();
    }
    
    public void verNuevoProveedor() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("contentWidth", 1000);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoProveedorDialog", options, null);
    }
    
    public void onProveedorNuevoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.getListaProveedores().add((Proveedor) event.getObject());
        }
    }
    
    @Override
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Retencion");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_RETENCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public Integer getBodegaSelect() {
        return bodegaSelect;
    }

    public void setBodegaSelect(Integer bodegaSelect) {
        this.bodegaSelect = bodegaSelect;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public PedidoCompra getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    public List<Proveedor> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public List<TipoRetencion> getListaTipoRetencion() {
        return listaTipoRetencion;
    }

    public void setListaTipoRetencion(List<TipoRetencion> listaTipoRetencion) {
        this.listaTipoRetencion = listaTipoRetencion;
    }

    public Integer getRetencionSlc() {
        return retencionSlc;
    }

    public void setRetencionSlc(Integer retencionSlc) {
        this.retencionSlc = retencionSlc;
    }

    public BigDecimal getIvaEmpresa() {
        return ivaEmpresa;
    }

    public void setIvaEmpresa(BigDecimal ivaEmpresa) {
        this.ivaEmpresa = ivaEmpresa;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotalTransporte() {
        return totalTransporte;
    }

    public void setTotalTransporte(BigDecimal totalTransporte) {
        this.totalTransporte = totalTransporte;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public Producto getProductoVer() {
        return productoVer;
    }

    public void setProductoVer(Producto productoVer) {
        this.productoVer = productoVer;
    }

    public BigDecimal getTotalProrrateo() {
        return totalProrrateo;
    }

    public void setTotalProrrateo(BigDecimal totalProrrateo) {
        this.totalProrrateo = totalProrrateo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<ImpuestoTarifa> getListaTarifas() {
        return listaTarifas;
    }

    public void setListaTarifas(List<ImpuestoTarifa> listaTarifas) {
        this.listaTarifas = listaTarifas;
    }
}
