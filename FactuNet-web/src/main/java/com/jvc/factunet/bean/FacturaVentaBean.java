package com.jvc.factunet.bean;

import com.jvc.factunet.bean.externos.CuentaCobroBean;
import com.jvc.factunet.entidades.CabeceraFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.ComisionTarjeta;
import com.jvc.factunet.entidades.DetalleFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.FormaPago;
import com.jvc.factunet.entidades.GarantiaDetalle;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.PaqueteVenta;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoImpuestoTarifa;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.entidades.Proforma;
import com.jvc.factunet.entidades.PuntoRestriccion;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import com.jvc.factunet.entidades.ServicioDetalle;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.print.Ticket;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.servicios.ProductoStockServicio;
import com.jvc.factunet.servicios.PuntoVentaServicio;
import com.jvc.factunet.servicios.TarjetaEmpresaServicio;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext; 
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class FacturaVentaBean extends PedidoCompraBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(FacturaVentaBean.class.getName());
    
    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private TarjetaEmpresaServicio tarjetaEmpresaServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private ProductoStockServicio productoStockServicio;
    
    private FacturaVenta facturaVenta;
    private Cliente cliente;
    private FacturaDetalle facturaDetalleSelect;
    private ProductoPaquete paqueteGlobal;
    private Boolean efectivo;
    private Boolean tarjeta;
    private Boolean credito;
    private Boolean otraFP;
    private Boolean debito;
    private Integer numeroCuentas;
    private Integer diasCredito;
    private List<TarjetaEmpresa> listaTarjetaEmpresa;
    private List<ComisionTarjeta> listaComisionTarjeta;
    private Integer tarjetaEmpresaSlc;
    private Integer comisionTarjetaSlc;
    private List<PuntoVenta> listaPuntoVenta;
    private final List<FacturaVenta> listaFacturaVentas = new ArrayList<>();
    private List<FacturaDetalle> lotes;

    public FacturaVentaBean() {
        this.facturaVenta = new FacturaVenta();
        this.listaTarjetaEmpresa = new ArrayList<>();
        this.listaComisionTarjeta = new ArrayList<>();
        this.listaPuntoVenta = new ArrayList<>();
        this.lotes = new ArrayList<>();
        this.efectivo = Boolean.TRUE;
        this.tarjeta = Boolean.FALSE;
        this.credito = Boolean.FALSE;
        this.debito = Boolean.FALSE;
        this.otraFP = Boolean.FALSE;
    }
    
    public void cargarPuntosVenta(){
        this.listaPuntoVenta.clear();
        List<PuntoVenta> listaTmp = this.puntoVentaServicio.listarPuntoVenta(super.getEmpresa().getCodigo());
        Boolean ban;
        for(PuntoVenta punto : listaTmp){
            ban = Boolean.FALSE;
            for(SecuenciaDocumento secuencia : punto.getSecuenciaDocumentoList())
            {
                if((secuencia.getTipoDocumento().getCodigo() == 21) && (secuencia.getEstado().equals("1")))
                {
                    ban=Boolean.TRUE;
                    break;
                }
            }
            if(ban){
                if(!Objects.equals(this.facturaVenta.getSecuenciaDocumento().getPuntoVenta().getCodigo(), punto.getCodigo())){
                    this.listaPuntoVenta.add(punto);
                }
            }
        }
    }
    
    public FacturaVenta inicializar(FacturaVenta factura, PuntoVenta punto){
        factura.setFecha(new Date());
        factura.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        factura.setFacturaDetalleList(new ArrayList<>());
        factura.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        factura.setGuiaRemisionList(new ArrayList<>());
        factura.setTotal(BigDecimal.ZERO);
        factura.setFacturaPagoList(new ArrayList<>());
        factura.setCliente(new Cliente());
        factura.setNumero(0);
        factura.setDescripcion(StringUtils.EMPTY);
        this.asignarSecuencia(factura,punto);
        factura.setBodega(factura.getSecuenciaDocumento().getPuntoVenta().getBodega());
        factura.setEstado("2");
        return factura;
    }
    
    @Override
    public void inicializar()
    {
        this.facturaVenta = new FacturaVenta();
        this.facturaVenta.setFecha(new Date());
        this.facturaVenta.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.facturaVenta.setFacturaDetalleList(new ArrayList<>());
        this.facturaVenta.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.facturaVenta.setGuiaRemisionList(new ArrayList<>());
        this.facturaVenta.setTotal(BigDecimal.ZERO);
        this.facturaVenta.setFacturaPagoList(new ArrayList<>());
        this.facturaVenta.setDocumentoRetencion(new ArrayList<>()); 
        this.facturaVenta.setEstado("1");
        this.facturaVenta.setCliente(new Cliente());
        this.facturaVenta.setNumero(0);
        this.facturaVenta.setDescripcion(StringUtils.EMPTY);
        this.facturaVenta.setTarifaIva(this.getIvaEmpresa()); 
        this.iniciarCliente();
        super.setDescuento(BigDecimal.ZERO);
        super.setComision(BigDecimal.ZERO);
        super.setTotalTransporte(BigDecimal.ZERO);
        if(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getSecuenciaDocumentoList() != null)
        {
            this.asignarSecuencia(this.facturaVenta,((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta());
        }
        this.facturaVenta.setBodega(this.facturaVenta.getSecuenciaDocumento().getPuntoVenta().getBodega());
        if(this.facturaVenta.getNumero() == 0)
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("secuanciaNoDefinida"));
        }
        this.facturaVenta.setObservacion(this.facturaVenta.getEmpresa().getCodigoSri() + "-" + this.facturaVenta.getPuntoVenta().getCodigoSri()); 
        this.verificarPedidoVenta();
        this.calcularTotales();
        this.listaTarjetaEmpresa.addAll(this.tarjetaEmpresaServicio.listar(super.getEmpresa().getCodigo()));
        this.listaComisionTarjeta.addAll(this.listaTarjetaEmpresa.get(0).getComisionTarjetaList());
        this.tarjetaEmpresaSlc = this.listaTarjetaEmpresa.get(0).getCodigo();
        this.comisionTarjetaSlc = this.listaTarjetaEmpresa.get(0).getComisionTarjetaList().get(0).getCodigo();  
        this.efectivo = Boolean.TRUE;
        this.tarjeta = Boolean.FALSE;
        this.credito = Boolean.FALSE;
        this.debito = Boolean.FALSE;
        this.otraFP = Boolean.FALSE;
        this.numeroCuentas = 1;
        this.diasCredito = 1;
        this.cargarPuntosVenta();
        this.listaFacturaVentas.clear();
    }
    
    public void verificarPedidoVenta(){
         if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if(request.getParameter("pedido") != null)
            {
                List<Integer> listaPedFac = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaPedidosF");
                for(Integer pedidoCod : listaPedFac){
                    PedidoVenta pedido = this.documentosServicios.buscarPedidoVenta(pedidoCod);
                    this.cargarPedidoVenta(pedido);
                }
            }
            if(request.getParameter("ingreso") != null)
            {
                this.facturaVenta.setIngreso(Integer.parseInt(request.getParameter("ingreso")));
                GarantiaDetalle ingreso = this.documentosServicios.buscarIngreso(this.facturaVenta.getIngreso());
                this.cliente = ingreso.getGarantiaCabecera().getCliente();
                this.facturaVenta.setCliente(this.cliente);
                for(ServicioDetalle servicio : ingreso.getServicioDetalleList())
                {
                    FacturaDetalle detalle = new FacturaDetalle();
                    detalle.setFactura(this.facturaVenta);
                    detalle.setFecha(new Date());
                    detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                    detalle.setProductoServicio(servicio.getProductoServicio());
                    if(servicio.getProductoServicio() instanceof ProductoBodega)
                    {
                        ProductoBodega productoBodega = (ProductoBodega) servicio.getProductoServicio();
                        detalle.setBodega(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getBodega());
                        detalle.setStock(this.setStockBodega(productoBodega, detalle.getBodega().getCodigo()));
                        detalle.setUtilidad(productoBodega.getUtilidad());
                    }
                    if(servicio.getProductoServicio() instanceof ProductoPaquete)
                    {
                        detalle.setIsPaquete(Boolean.TRUE);
                        for(PaqueteVenta proPa : ((ProductoPaquete)servicio.getProductoServicio()).getPaqueteVentaList())
                        {
                            if(proPa.getProducto() instanceof ProductoBodega)
                            {
                                proPa.setIsBodega(Boolean.TRUE);
                                proPa.setBodegaSlc(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                                proPa.setBodega(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega());
                                proPa.setStock(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getStock());
                            }
                        }
                    }
                    detalle.setCantidad(servicio.getCantidad());
                    detalle.setPvp(servicio.getProductoServicio().getPvp());
                    detalle.setPrecioVentaUnitario(servicio.getProductoServicio().getPvp());
                    detalle.setPrecioVentaUnitarioDescuento(servicio.getProductoServicio().getPvp());
                    detalle.setPvpIva(servicio.getProductoServicio().getPvp().add(servicio.getProductoServicio().getPvp().multiply(this.getIvaEmpresa().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                    detalle.setSubtotalSinDescuento(detalle.getPvp().multiply(servicio.getCantidad()));
                    detalle.setSubtotalConDescuento(detalle.getPvp().multiply(servicio.getCantidad()));
                    detalle.setValorComision(BigDecimal.ZERO);
                    detalle.setValorDescuento(BigDecimal.ZERO);
                    detalle.setComision(BigDecimal.ZERO);
                    detalle.setDescuento(BigDecimal.ZERO);
                    this.facturaVenta.getFacturaDetalleList().add(detalle);
                    
                }
            }
         }
    }
    
    public Boolean verificarExiste(FacturaDetalle detalleExiste){
        for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList()){
            if(Objects.equals(detalleExiste.getProductoServicio().getCodigo(), detalle.getProductoServicio().getCodigo())){
                detalle.setCantidad(detalle.getCantidad().add(detalleExiste.getCantidad()));
                detalle.setSubtotalSinDescuento((detalle.getPvp().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                detalle.setSubtotalConDescuento(detalle.getSubtotalSinDescuento());
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    
    public void cargarPedidoVenta(PedidoVenta pedido){
        this.facturaVenta.getPedidosVenta().put(pedido.getCodigo(),pedido.getMesa().getNombre());
        for(FacturaDetalle detalle : pedido.getFacturaDetalleList())
        {
            if(detalle.getCantidad().floatValue() > 0)
            {
                if(!this.verificarExiste(detalle)){
                    detalle.setCodigo(null);
                    detalle.setFactura(this.facturaVenta);
                    detalle.setFecha(new Date());
                    detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                    if(detalle.getProductoServicio() instanceof ProductoBodega)
                    {
                        ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                        detalle.setBodega(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getBodega());
                        detalle.setStock(this.setStockBodega(productoBodega, detalle.getBodega().getCodigo()));
//                        detalle.setCostoFecha(productoBodega.getPrecioUltimaCompra());
                        detalle.setUtilidad(productoBodega.getUtilidad());
                    }
                    if(detalle.getProductoServicio() instanceof ProductoPaquete)
                    {
                        detalle.setIsPaquete(Boolean.TRUE);
                        for(PaqueteVenta proPa : ((ProductoPaquete)detalle.getProductoServicio()).getPaqueteVentaList())
                        {
                            if(proPa.getProducto() instanceof ProductoBodega)
                            {
                                proPa.setIsBodega(Boolean.TRUE);
                                proPa.setBodegaSlc(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                                proPa.setBodega(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega());
                                proPa.setStock(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getStock());
                            }
                        }
                    }
                    detalle.setCostoFecha(detalle.getProductoServicio().getPrecioUltimaCompra());
                    detalle.setPvp(detalle.getProductoServicio().getPvp());
                    detalle.setPrecioVentaUnitario(detalle.getPvp());
                    detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario());
                    detalle.setPrecioUnitarioTmp(detalle.getPrecioVentaUnitario());
                    detalle.setPvpIva(detalle.getPvp().add(detalle.getPvp().multiply(this.getIvaEmpresa().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                    detalle.setSubtotalSinDescuento((detalle.getPvp().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                    detalle.setSubtotalConDescuento(detalle.getSubtotalSinDescuento());
                    detalle.setValorComision(BigDecimal.ZERO);
                    detalle.setValorDescuento(BigDecimal.ZERO);
                    detalle.setComision(BigDecimal.ZERO);
                    detalle.setDescuento(BigDecimal.ZERO);
                    this.facturaVenta.getFacturaDetalleList().add(detalle);
                }
            }
        }
        if(pedido.getCliente().getPersona().getCedula() != null){
            this.facturaVenta.setCliente(pedido.getCliente());
            this.cliente = pedido.getCliente();
        }
    }
    
    public FacturaVenta asignarSecuencia(FacturaVenta factura, PuntoVenta punto){
        for(SecuenciaDocumento secuencia : punto.getSecuenciaDocumentoList())
        {
            if((secuencia.getTipoDocumento().getCodigo() == 21) && (secuencia.getEstado().equals("1")))
            {
                SecuenciaDocumento secuenciaBase = this.puntoVentaServicio.numeroActual(secuencia.getCodigo());
                factura.setSecuenciaDocumento(secuenciaBase);
                factura.setNumero(secuenciaBase.getNumeroActual() + 1);
                factura.setPuntoVenta(secuenciaBase.getPuntoVenta());
                break;
            }
        }
        return factura;
    }
    
    public void anular()
    {
        try {
            this.facturaVenta.setEstado("3");
            this.documentosServicios.anularFacturaVenta(this.facturaVenta);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("facturaAnulada"));
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorAnulacion"));
            Logger.getLogger(FacturaVentaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscarCliente()
    {
        try {
            Cliente clienteTmp = this.clienteServicio.buscarCedula(this.cliente.getPersona().getCedula(), super.getEmpresa().getCodigo());
            if(clienteTmp == null)
            {
                this.iniciarCliente();
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
            }
            else
            {
                this.cliente = clienteTmp;
                this.facturaVenta.setCliente(this.cliente);
                super.setDescuento(this.cliente.getTipoCliente().getDescuento());
                this.generalDescuento();
                this.consumidor = Boolean.FALSE;
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("clienteEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    public void iniciarCliente()
    {
        this.cliente = new Cliente();
        this.cliente.setPersona(new Persona());
        this.cliente.getPersona().setCedula(StringUtils.EMPTY);
        this.consumidor = Boolean.FALSE;
        super.setDescuento(BigDecimal.ZERO);
        super.setComision(BigDecimal.ZERO);
        this.generalDescuento();
    }
    
    @Override
    public void eliminar(int parametro) {
        try {
            this.facturaVenta.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarProductoPaquete(ProductoPaquete paquete, PaqueteVenta parametro) {
        try {
            paquete.getPaqueteVentaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.facturaVenta);
        this.calcularTotales();
    }
    
    @Override
    public void calcularTotales()
    {
        this.facturaVenta = (FacturaVenta) this.calcularTotalPago(this.facturaVenta);
    }
     
    @Override
    public FacturaDetalle crearDetalle(FacturaDetalle detalle)
    {
        detalle.setFecha(new Date());
        detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        detalle.setDescuento(detalle.getProductoServicio().getDescuentoVenta());
        detalle.setComision(super.getComision());
        detalle.setPrecioVentaUnitario(BigDecimal.ZERO);
        detalle.setStock(BigDecimal.ZERO);
        if(detalle.getProductoServicio() instanceof ProductoBodega)
        {
            ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
            detalle.setCostoFecha(productoBodega.getPrecioUltimaCompra());
            detalle.setPvp(productoBodega.getPvp());
            detalle.setPrecioVentaUnitario(productoBodega.getPvp().add((productoBodega.getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP)));  
            detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario().subtract((detalle.getPrecioVentaUnitario().multiply(detalle.getDescuento())).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP)));  
            if(productoBodega.getBodega() != null)
            {
                detalle.setBodega(productoBodega.getBodega());
            }
            else
            {
                detalle.setBodega(super.getListaBodegas().get(0));
            }
            if(productoBodega.getStock() == null)
            {
                detalle.setStock(this.setStockBodega(productoBodega,detalle.getBodega().getCodigo()));
            }
            else
            {    
                detalle.setStock(productoBodega.getStock());
            }   
            detalle.setLoteVenta(productoBodega.getLote()); 
        }
        else
        {
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                detalle.setIsPaquete(Boolean.TRUE);
                for(PaqueteVenta proPa : ((ProductoPaquete)detalle.getProductoServicio()).getPaqueteVentaList())
                {
                    if(proPa.getProducto() instanceof ProductoBodega)
                    {
                        proPa.setIsBodega(Boolean.TRUE);
                        proPa.setBodegaSlc(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                        proPa.setBodega(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega());
                        proPa.setStock(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getStock());
                    }
                }
            }
            detalle.setCostoFecha(detalle.getProductoServicio().getPrecioUltimaCompra());
            detalle.setPvp(detalle.getProductoServicio().getPvp());
            detalle.setPrecioVentaUnitario(detalle.getProductoServicio().getPvp().add((detalle.getProductoServicio().getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));  
            detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario().subtract((detalle.getPrecioVentaUnitario().multiply(detalle.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        }
        detalle.setPrecioUnitarioTmp(detalle.getPrecioVentaUnitario());
        detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        detalle.setValorDescuento((((detalle.getPrecioVentaUnitarioDescuento().multiply(detalle.getDescuento())).divide(new BigDecimal(100))).multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));  
        detalle.setSubtotalConDescuento(detalle.getPrecioVentaUnitarioDescuento().multiply(detalle.getCantidad()));
        detalle.setValorComision((((detalle.getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100))).multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));  
        for(ProductoImpuestoTarifa tarifaImpuesto : detalle.getProductoServicio().getProductoImpuestoTarifaList()){
            if(tarifaImpuesto.getImpuestoTarifa().getImpuesto().getId() == 1){
                detalle.setImpuestoTarifa(tarifaImpuesto.getImpuestoTarifa());
                break;
            }
        }
        return detalle;
    }
    
    @Override
    public void setStockPrecio()
    {
        for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList())
        {
            detalle = this.crearDetalle(detalle);
        }
        this.calcularTotales();
    }
    
    @Override
    public void verGuia(GuiaRemision guia) {
        if(!Fecha.fechaInicio(new Date()).equals(Fecha.fechaInicio(this.facturaVenta.getFecha()))){
            FacesUtils.addErrorMessage("No se puede generar guias de remisión en documentos de fechas anteriores.");
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guia", guia);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("factura", this.facturaVenta);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaGuiaDialog", options, null);
    }
    
    @Override
    public void onGuiaSelect(SelectEvent event) {
        try {
            if(event.getObject() != null)
            {
                GuiaRemision guia = (GuiaRemision) event.getObject();
                if(this.facturaVenta.getGuiaRemisionList() == null)
                {
                    this.facturaVenta.setGuiaRemisionList(new ArrayList<>());
                }
                if(this.facturaVenta.getCodigo()!= null)
                {
                    this.documentosServicios.insertarGuia(guia);
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                }
                if((!this.facturaVenta.getGuiaRemisionList().contains(guia)) && (guia != null))
                {
                    this.facturaVenta.getGuiaRemisionList().add(guia);
                }
                this.calcularTransporte();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    @Override
    public void eliminarGuia(GuiaRemision parametro) {
        try {
            this.facturaVenta.getGuiaRemisionList().remove(parametro);
            this.calcularTransporte();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void actualizarGuia(GuiaRemision parametro) {
        parametro.setEstado("2");
        this.calcularTransporte();
    }
    
    @Override
    public void calcularTransporte()
    {
        super.setTotalTransporte(BigDecimal.ZERO);
        for(GuiaRemision obj : this.facturaVenta.getGuiaRemisionList())
        {
            if(this.facturaVenta.getCodigo()!= null)
            {
                try {
                    this.documentosServicios.actualizar(obj);
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                }
            }
            super.setTotalTransporte(super.getTotalTransporte().add(obj.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
    }
    
    public void guardar(FacturaVenta factura)
    {
        try {
            this.documentosServicios.insertar(factura);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado") + " " + FacesUtils.getResourceBundle().getString("factura")  + " " + factura.getNumero());
            if(factura.getTipoDocumento() == 21){
                this.verificarAutoPrint(factura);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            this.facturaVenta.setEstado("1");
            this.listaFacturaVentas.clear();
//            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("actualiceSecuencia"));
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verPago(String tipo) {
        if(!this.efectivo && !this.tarjeta && !this.credito && !this.debito && !this.otraFP){
            FacesUtils.addErrorMessage("Debe seleccionar una forma de pago.");
            return;
        }
        this.listaFacturaVentas.clear();
        if(this.facturaVenta.getFacturaDetalleList().size()>0)
        {
            if(this.facturaVenta.getCliente().getCodigo() != null)
            {
                if(this.facturaVenta.getTotal() != null)
                {
                    if(this.facturaVenta.getTotal().doubleValue() > 0)
                    {
                        for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList()){
                            if(detalle.getPrecioVentaUnitario().doubleValue() <= 0){
                                FacesUtils.addErrorMessage("El monto del producto " + detalle.getProductoServicio().getNombre() + " no puede ser cero");
                                return;
                            }
                        }
                        this.facturaVenta.setTipoDocuemento(tipo);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.facturaVenta.getFacturaPagoList());
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.facturaVenta.getTotal());
                        if(this.otraFP){
                            if(this.verificarRestriccion(this.facturaVenta))
                            {
                                PrimeFaces.current().executeScript("PF('dlgRestriccion').show();");
                            }
                            else
                            {
                                this.verPagoVentana();
                            }
                        }else if(this.efectivo){
                            this.listaFacturaVentas.addAll(this.generarFacturasGuardar());
                            for(FacturaVenta factura : this.listaFacturaVentas){
                                factura.setTipoDocuemento(tipo);
                                factura = this.pagoEfectivo(factura);
                                this.guardar(factura);
                            }
                            this.facturaVenta = this.listaFacturaVentas.get(0);
                        }else if(this.tarjeta){
                            this.listaFacturaVentas.addAll(this.generarFacturasGuardar());
                            for(FacturaVenta factura : this.listaFacturaVentas){
                                factura.setTipoDocuemento(tipo);
                                factura = this.pagoTarjeta(factura);
                                this.guardar(factura);
                            }
                            this.facturaVenta = this.listaFacturaVentas.get(0);
                        }else if(this.credito){
                            this.listaFacturaVentas.addAll(this.generarFacturasGuardar());
                            for(FacturaVenta factura : this.listaFacturaVentas){
                                factura.setTipoDocuemento(tipo);
                                factura = this.pagoCredito(factura);
                                this.guardar(factura);
                            }
                            this.facturaVenta = this.listaFacturaVentas.get(0);
                        }else if(this.debito){
                            this.listaFacturaVentas.addAll(this.generarFacturasGuardar());
                            for(FacturaVenta factura : this.listaFacturaVentas){
                                factura.setTipoDocuemento(tipo);
                                factura = this.pagoDebito(factura);
                                this.guardar(factura);
                            }
                            this.facturaVenta = this.listaFacturaVentas.get(0);
                        }   
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
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneCliente"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("debeingresarproductos"));
        }
    }
    
    public void verPagoVentana(){
        List<FacturaDetalle> listaVerificacion = this.verificarRestriccion(this.facturaVenta.getFacturaDetalleList(), this.facturaVenta.getSecuenciaDocumento().getPuntoVenta());
        if(listaVerificacion.size() > 0 ){
            this.facturaVenta.getFacturaDetalleList().clear();
            this.facturaVenta.getFacturaDetalleList().addAll(listaVerificacion);
            this.calcularTotales();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.facturaVenta.getFacturaPagoList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.facturaVenta.getTotal());
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
             FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("restriccionTotal"));
        }
    }
    
    public List<FacturaVenta> generarFacturasGuardar(){
        List<FacturaVenta> listaFacturaVentasReturn = new ArrayList<>();
        List<FacturaDetalle> listaTotal = new ArrayList<>();
        listaTotal.addAll(this.facturaVenta.getFacturaDetalleList());
        List<FacturaDetalle> listaVerificacion = this.verificarRestriccion(this.facturaVenta.getFacturaDetalleList(), this.facturaVenta.getSecuenciaDocumento().getPuntoVenta());
        if(listaVerificacion.size() != this.facturaVenta.getFacturaDetalleList().size()){
            if(listaVerificacion.size()>0){
                this.facturaVenta.setEstado("2");
                this.facturaVenta.getFacturaDetalleList().clear();
                this.facturaVenta.getFacturaDetalleList().addAll(listaVerificacion);
                this.calcularTotales();
                listaFacturaVentasReturn.add(this.facturaVenta);
            }
            for(PuntoVenta punto : this.listaPuntoVenta){
                FacturaVenta factura = new FacturaVenta();
                factura = this.inicializar(factura, punto);
                factura.setFacturaDetalleList(this.verificarRestriccion(listaTotal, punto));
                factura.setCliente(this.facturaVenta.getCliente());
                factura.setPedidosVenta(this.facturaVenta.getPedidosVenta());
                if(factura.getFacturaDetalleList().size()>0){
                    for(FacturaDetalle detalle : factura.getFacturaDetalleList()){
                        detalle.setFactura(factura);
                    }
                    factura = (FacturaVenta) this.calcularTotalPago(factura);
                    listaFacturaVentasReturn.add(factura);
                }
            }
        }
        else
        {
            this.facturaVenta.setEstado("2");
            listaFacturaVentasReturn.add(this.facturaVenta);
        }
        return listaFacturaVentasReturn;
    }
    
    public List<FacturaDetalle> verificarRestriccion(List<FacturaDetalle> listaDetalles, PuntoVenta punto){
        List<FacturaDetalle> listaDetallesReturn = new ArrayList<>();
        if(punto.getPuntoRestriccionList() != null){
            Boolean ban;
            for(FacturaDetalle detalle : listaDetalles)
            {
                ban = Boolean.TRUE;
                for(PuntoRestriccion restriccion : punto.getPuntoRestriccionList()){
                    if(Objects.equals(detalle.getProductoServicio().getGrupo().getCodigo(), restriccion.getGrupo().getCodigo())){
                        ban = Boolean.FALSE;
                        break;
                    }
                }
                if(ban){
                    listaDetallesReturn.add(detalle);
                }
            }
        }
        return listaDetallesReturn;
    }
    
    public Boolean verificarRestriccion(FacturaVenta factura){
        if(factura.getPuntoVenta().getPuntoRestriccionList() != null){
            for(FacturaDetalle detalle : factura.getFacturaDetalleList())
            {
                for(PuntoRestriccion restriccion : factura.getPuntoVenta().getPuntoRestriccionList()){
                    if(Objects.equals(detalle.getProductoServicio().getGrupo().getCodigo(), restriccion.getGrupo().getCodigo())){
                        return Boolean.TRUE;
                    }
                }
            }
        }
        return Boolean.FALSE;
    }
    
    public FacturaVenta pagoEfectivo(FacturaVenta factura){
        CuentaCobroBean cuentaCobroBean = (CuentaCobroBean) FacesUtils.getManagedBean("cuentaCobroBean");
        cuentaCobroBean.setListaFacturaPago(factura.getFacturaPagoList());
        cuentaCobroBean.setTotalFactura(factura.getTotal());
        cuentaCobroBean.setTotalPagos(BigDecimal.ZERO);
        cuentaCobroBean.setMontoPago(factura.getTotal());
        cuentaCobroBean.setFormaSlc(140);
        if(cuentaCobroBean.agregarPago()){
            for(FacturaPago pago : cuentaCobroBean.getListaFacturaPago()){
                pago.setFactura(factura);  
            }
        }
        return factura;
    }
    
    public FacturaVenta pagoDebito(FacturaVenta factura){
        CuentaCobroBean cuentaCobroBean = (CuentaCobroBean) FacesUtils.getManagedBean("cuentaCobroBean");
        cuentaCobroBean.setListaFacturaPago(factura.getFacturaPagoList());
        cuentaCobroBean.setTotalFactura(factura.getTotal());
        cuentaCobroBean.setTotalPagos(BigDecimal.ZERO);
        cuentaCobroBean.setMontoPago(factura.getTotal());
        cuentaCobroBean.setFormaSlc(141);
        if(cuentaCobroBean.agregarPago()){
            for(FacturaPago pago : cuentaCobroBean.getListaFacturaPago()){
                pago.setFactura(factura);  
            }
        }
        return factura;
    }
    
    public FacturaVenta pagoTarjeta(FacturaVenta factura){
        CuentaCobroBean cuentaCobroBean = (CuentaCobroBean) FacesUtils.getManagedBean("cuentaCobroBean");
        cuentaCobroBean.setListaFacturaPago(factura.getFacturaPagoList());
        cuentaCobroBean.setTotalFactura(factura.getTotal());
        cuentaCobroBean.setTotalPagos(BigDecimal.ZERO);
        cuentaCobroBean.setMontoPago(factura.getTotal());
        cuentaCobroBean.setFormaSlc(149);
        cuentaCobroBean.setFormaCuentaSlc(153);
        cuentaCobroBean.setTarjetaEmpresaSlc(this.tarjetaEmpresaSlc);
        cuentaCobroBean.setComisionTarjetaSlc(this.comisionTarjetaSlc);
        if(cuentaCobroBean.agregarPago()){
            for(FacturaPago pago : cuentaCobroBean.getListaFacturaPago()){
                pago.setFactura(factura);  
            }
        }
        return factura;
    }
    
    public FacturaVenta pagoCredito(FacturaVenta factura){
        CuentaCobroBean cuentaCobroBean = (CuentaCobroBean) FacesUtils.getManagedBean("cuentaCobroBean");
        cuentaCobroBean.setListaFacturaPago(factura.getFacturaPagoList());
        cuentaCobroBean.setTotalFactura(factura.getTotal());
        cuentaCobroBean.setTotalPagos(BigDecimal.ZERO);
        cuentaCobroBean.setMontoPago(factura.getTotal());
        cuentaCobroBean.setFormaSlc(149);
        cuentaCobroBean.setFormaCuentaSlc(154);
        cuentaCobroBean.setNumeroCuentas(this.numeroCuentas);
        cuentaCobroBean.setDiasCredito(this.diasCredito);
        cuentaCobroBean.calcularFechaVencimiento();
        if(cuentaCobroBean.agregarPago()){
            for(FacturaPago pago : cuentaCobroBean.getListaFacturaPago()){
                pago.setFactura(factura);  
            }
        }
        return factura;
    }
    
    @Override
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            for(FacturaPago pago : listaPagosFin){
                pago.setFactura(this.facturaVenta);
                this.facturaVenta.getFacturaPagoList().add(pago);    
            }
            this.facturaVenta.setEstado("2");
            this.guardar(this.facturaVenta);
            this.listaFacturaVentas.add(this.facturaVenta);
        }
    }
    
    public void onPagoRetencionSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            this.facturaVenta.getFacturaPagoList().clear();
            this.facturaVenta.getFacturaPagoList().addAll(listaPagosFin);
            super.actualizar(this.facturaVenta); 
        }
    }
    
    public void verBusquedaFacturas() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 1);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoDocumento", 0);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasVentaDialog", options, null);
    }
    
    public void verBusquedaPedidosVenta() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("visible", true);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedidosVentaDialog", options, null);
    }
    
    public Boolean verificarPedidoLista(Integer nuevo){
        if(this.facturaVenta.getPedidosVenta()!= null){
            for(Map.Entry pedido : this.facturaVenta.getPedidosVenta().entrySet()){
                if(Objects.equals(pedido.getKey(), nuevo)){
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }
    
    public void onPedidoVentaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            if(!((PedidoVenta) event.getObject()).getEstado().equals("2")){
                if(!(((PedidoVenta) event.getObject()).getFacturaDetalleList().isEmpty())){
                    if(this.verificarPedidoLista(((PedidoVenta) event.getObject()).getCodigo())){
                        this.cargarPedidoVenta((PedidoVenta) event.getObject());
                        this.calcularTotales();
                    }
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("pedidoSinItems"));
                }
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("pedidoFacturado"));
            }
        }
    }
    
    public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 750);
        options.put("height", 500);
        options.put("contentWidth", 750);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
    }
    
    public void verNuevoCliente(Boolean editar) {
        if(editar)
        {
            if(this.cliente.getCodigo() != null){
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", this.cliente.getPersona().getCodigo());
            }
            else
            {
                FacesUtils.addErrorMessage("Debe seleccionar un cliente.");
                return;
            }
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", null);
        }
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        if(super.getEmpresa().getClienteCompleto().equals("1")){
            options.put("width", 1000);
            options.put("contentWidth", 1000);
        }
        else
        {
            options.put("width", 1000);
            options.put("contentWidth", 1000);
        }
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoClienteDialog", options, null);
    }
    
    public void verBusquedaProductosStock(ProductoPaquete paquete) {
        this.paqueteGlobal = paquete;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 600);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 600);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProductosStockDialog", options, null);
    }
    
    public void onProductoPaqueteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            for(Producto producto : listaProductos){
                if(!(producto instanceof ProductoPaquete))
                {
                    PaqueteVenta venta = new PaqueteVenta();
                    venta.setCantidad(BigDecimal.ONE);
                    venta.setProducto(producto);
                    venta.setProductoPadre(this.paqueteGlobal);
                    venta.setPvp(producto.getPvp());
                    venta.setTotal(producto.getPvp());
                    venta.setDescuento(BigDecimal.ZERO);
                    venta.setComision(BigDecimal.ZERO);
                    if(producto instanceof ProductoBodega){
                        venta.setIsBodega(Boolean.TRUE);
                        venta.setBodegaSlc(((ProductoBodega)venta.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                        venta.setBodega(((ProductoBodega)venta.getProducto()).getProductoStockList().get(0).getBodega());
                        venta.setStock(((ProductoBodega)venta.getProducto()).getProductoStockList().get(0).getStock());
                    
                    }
                    else
                    {
                        venta.setIsBodega(Boolean.FALSE);
                    }
                    this.paqueteGlobal.getPaqueteVentaList().add(venta);
                }
            }
        }
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            if((((Cliente)event.getObject()).getPersona().getCedula() != null) && !(((Cliente)event.getObject()).getPersona().getCedula().isEmpty()))
            {
                this.cliente = (Cliente) event.getObject();
                if(this.cliente != null)
                {
                    this.facturaVenta.setCliente(this.cliente);
                    super.setDescuento(this.cliente.getTipoCliente().getDescuento());
                    this.generalDescuento();
                    this.consumidor = Boolean.FALSE;
                }
                else
                {
                    this.iniciarCliente();
                }
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaObligatoria"));
            }
        }
    }
    
    public void onFacturaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.facturaVenta = (FacturaVenta) event.getObject();
            this.cliente = this.facturaVenta.getCliente();
            for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList())
            {
                if(detalle.getProductoServicio() instanceof ProductoBodega)
                {
                    ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                    detalle.setStock(this.setStockBodega(productoBodega, super.getBodegaSelect()));
                }
                if(detalle.getProductoServicio() instanceof ProductoPaquete)
                {
                    detalle.setIsPaquete(Boolean.TRUE);
                }
            }
            this.listaFacturaVentas.clear();
            this.listaFacturaVentas.add(this.facturaVenta);
        }
    }
    
    public void verRegistroSeries(FacturaDetalle detalle) {
        this.facturaDetalleSelect = detalle;
        if(detalle.getFacturaDetalleSeriesList() == null)
        {
            detalle.setFacturaDetalleSeriesList(new ArrayList<FacturaDetalleSeries>());
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("series", detalle.getFacturaDetalleSeriesList());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cantidad", detalle.getCantidad());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 450);
        options.put("contentHeight", 450);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/codigosProductosDialog", options, null);
    }
    
    public void onRegistroSeriesSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaDetalleSeries> lista = (List) event.getObject();
            this.facturaDetalleSelect.setFacturaDetalleSeriesList(lista);
        }
    }
    
    public void verBusquedaProformas() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProformasDialog", options, null);
    }
    
    public void onProformaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Proforma proforma = (Proforma) event.getObject();
            this.facturaVenta.setCodigo(null);
            this.facturaVenta.setProforma(proforma.getCodigo());
            this.facturaVenta.setCliente(proforma.getCliente());
            this.cliente = facturaVenta.getCliente();
            this.facturaVenta.setSubtotal(proforma.getSubtotal());
//            this.facturaVenta.setSubtotalConIva(proforma.getSubtotalConIva());
//            this.facturaVenta.setSubtotalSinIva(proforma.getSubtotalSinIva());
            this.facturaVenta.setTotal(proforma.getTotal());
            this.facturaVenta.setTotalPagar(proforma.getTotal());
            this.facturaVenta.setIva(proforma.getIva());
            this.facturaVenta.setEstado("1");
            for(FacturaDetalle detalle : proforma.getFacturaDetalleList())
            {
                detalle.setCodigo(null);
                detalle.setFactura(this.facturaVenta);
                detalle.setFecha(new Date());
                detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                if(detalle.getProductoServicio() instanceof ProductoBodega)
                {
                    ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                    detalle.setStock(this.setStockBodega(productoBodega, super.getBodegaSelect()));
                    detalle.setUtilidad(productoBodega.getUtilidad());
                }
                if(detalle.getProductoServicio() instanceof ProductoPaquete)
                {
                    detalle.setIsPaquete(Boolean.TRUE);
                    for(PaqueteVenta proPa : ((ProductoPaquete)detalle.getProductoServicio()).getPaqueteVentaList())
                    {
                        if(proPa.getProducto() instanceof ProductoBodega)
                        {
                            proPa.setIsBodega(Boolean.TRUE);
                            proPa.setBodegaSlc(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                            proPa.setBodega(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega());
                            proPa.setStock(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getStock());
                        }
                    }
                }
                detalle.setPvp(detalle.getProductoServicio().getPvp());
                detalle.setPvpIva(detalle.getPvp().add(detalle.getPvp().multiply(this.getIvaEmpresa().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
                this.facturaVenta.getFacturaDetalleList().add(detalle);
            }
        }
    }
    
    public void cambiarBodega(PaqueteVenta paquete)
    {
        ProductoBodega producto = (ProductoBodega) paquete.getProducto();
        for(ProductoStock stock : producto.getProductoStockList())
        {
            if(Objects.equals(paquete.getBodegaSlc(), stock.getBodega().getCodigo()))
            {
                paquete.setStock(stock.getStock());
                paquete.setBodega(stock.getBodega());
                return;
            }
        }
    }
    
    public void generarReportePrint(FacturaVenta factura) throws SQLException {
        if(factura != null)
        {
            try {
                super.getParametros().put("factura", factura.getCodigo());
                super.getParametros().put("nombreReporte", "Factura de Venta");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReportPrint(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION_DARIO, JasperReportUtil.TIPO_PDF ,factura.getPuntoVenta().getImpresora(),this.getParametros());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FacturaVentaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void imprimirConsulta(){
        try {
            if(this.facturaVenta.getPuntoVenta().getTipoImpresora().equals("1")){
                this.generarReportePrint(this.facturaVenta);
            }
            else
            {
                for(SecuenciaDocumento secuencia : this.facturaVenta.getPuntoVenta().getSecuenciaDocumentoList()){
                    if(Objects.equals(secuencia.getTipoDocumento().getCodigo(), this.facturaVenta.getTipoDocumento())){
                        this.facturaVenta.setSecuenciaDocumento(secuencia);
                        break;
                    }
                }
                this.imprimirTiket(this.facturaVenta);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVentaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void verificarAutoPrint(FacturaVenta factura){
        if(factura.getSecuenciaDocumento().getAutoPrintB()){
            try {
                if(factura.getPuntoVenta().getTipoImpresora().equals("1")){
                    this.generarReportePrint(factura);
                }
                else
                {
                    this.imprimirTiket(factura);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FacturaVentaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        for(FacturaVenta factura : this.listaFacturaVentas){
            if(factura != null)
            {
                try {
                    super.getParametros().put("factura", factura.getCodigo());
                    super.getParametros().put("nombreReporte", "Factura de Venta");
                    super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                    JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION_DARIO,JasperReportUtil.TIPO_PDF, null, this.getParametros());
                } catch (ClassNotFoundException ex) {
                    LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
                }
            }
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer guia) {
        if(this.facturaVenta.getCodigo() != null)
        {
            try {
                super.getParametros().put("guia", guia);
                super.getParametros().put("nombreReporte", "GUIA DE REMISION");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_GUIA_REMISION,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FacturaVentaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void generalRecargo()
    {
        for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList()){
            detalle.setComision(super.getComision());
            super.onCellEditComision(detalle, false);
        }
        this.calcularTodo();
    }
    
    @Override
    public void generalDescuento()
    {
        for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList()){
            detalle.setDescuento(super.getDescuento());
            super.onCellEditDescuento(detalle, false);
        }
        this.calcularTodo();
    }
    
    public void calcularTodo(){
        this.calcularTotales();
    }
    
    private Boolean consumidor = Boolean.FALSE;
    public void consumidorFinal(){
        if(this.consumidor)
        {
            try {
                Cliente clienteTmp = this.clienteServicio.buscarConsumidorFinal(super.getEmpresa().getCodigo());
                if(clienteTmp == null)
                {
                    this.iniciarCliente();
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("clienteNE"));
                }
                else
                {
                    this.cliente = clienteTmp;
                    this.facturaVenta.setCliente(this.cliente);
                    super.setDescuento(BigDecimal.ZERO);
                    super.setComision(BigDecimal.ZERO);
                    this.generalDescuento();
                }
            } catch (Exception e) {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("clienteNE"));
            }
        }
        else{
            this.iniciarCliente();
        }
    }
    
    public void cambiarFormaPago(){
        if(this.efectivo){
            super.setComision(BigDecimal.ZERO);
        }
        if(this.tarjeta){
            super.setComision(this.verComision());
        }
        if(this.credito){
            super.setComision(BigDecimal.ZERO);
            this.numeroCuentas = 1;
            this.diasCredito = 1;
        }
        if(this.otraFP){
            super.setComision(BigDecimal.ZERO);
        }
        this.generalRecargo();
    }
    
    public void cargarComisiones()
    {
        for(TarjetaEmpresa obj : this.listaTarjetaEmpresa)
        {
            if(Objects.equals(this.tarjetaEmpresaSlc, obj.getCodigo()))
            {
                this.listaComisionTarjeta.clear();
                this.listaComisionTarjeta.addAll(obj.getComisionTarjetaList());
                this.comisionTarjetaSlc = this.listaComisionTarjeta.get(0).getCodigo();
                super.setComision(this.verComision());
                this.generalRecargo();
                break;
            }
        }
    }
    
    public void cambiarComision(){
        super.setComision(this.verComision());
        this.generalRecargo();
    }
    
    public BigDecimal verComision(){
        for(ComisionTarjeta comision : this.listaComisionTarjeta){
            if(Objects.equals(this.comisionTarjetaSlc, comision.getCodigo()))
            {
                return comision.getValor();
            } 
        }
        return BigDecimal.ZERO;
    }

    public Boolean getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(Boolean consumidor) {
        this.consumidor = consumidor;
    }

    public FacturaVenta getFacturaVenta() {
        return facturaVenta;
    }

    public void setFacturaVenta(FacturaVenta facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FacturaDetalle getFacturaDetalleSelect() {
        return facturaDetalleSelect;
    }

    public void setFacturaDetalleSelect(FacturaDetalle facturaDetalleSelect) {
        this.facturaDetalleSelect = facturaDetalleSelect;
    }

    public Boolean getOtraFP() {
        return otraFP;
    }

    public void setOtraFP(Boolean otraFP) {
        this.otraFP = otraFP;
        if(this.otraFP){
            this.tarjeta = Boolean.FALSE;
            this.credito = Boolean.FALSE;
            this.debito = Boolean.FALSE;
            this.efectivo = Boolean.FALSE;
        }
    }

    public Boolean getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Boolean efectivo) {
        this.efectivo = efectivo;
        if(this.efectivo){
            this.tarjeta = Boolean.FALSE;
            this.credito = Boolean.FALSE;
            this.debito = Boolean.FALSE;
            this.otraFP = Boolean.FALSE;
        }
    }

    public Boolean getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Boolean tarjeta) {
        this.tarjeta = tarjeta;
        if(this.tarjeta){
            this.efectivo = Boolean.FALSE;
            this.credito = Boolean.FALSE;
            this.debito = Boolean.FALSE;
            this.otraFP = Boolean.FALSE;
        }
    }
    
    public Boolean getCredito() {
        return credito;
    }

    public void setCredito(Boolean credito) {
        this.credito = credito;
        if(this.credito){
            this.efectivo = Boolean.FALSE;
            this.tarjeta = Boolean.FALSE;
            this.debito = Boolean.FALSE;
            this.otraFP = Boolean.FALSE;
        }
    }
    
    public Boolean getDebito() {
        return debito;
    }

    public void setDebito(Boolean debito) {
        this.debito = debito;
        if(this.debito){
            this.efectivo = Boolean.FALSE;
            this.tarjeta = Boolean.FALSE;
            this.credito = Boolean.FALSE;
            this.otraFP = Boolean.FALSE;
        }
    }
    
    public void facturarReserva(){
        if(this.facturaVenta.getTipoDocumento() == 22){
            if(this.facturaVenta.getDocumentoRelacionado() != null){
                FacesUtils.addErrorMessage("La reserva ya fue facturada.");
                return;
            }
            try {
                FacturaVenta facturaReserva = new FacturaVenta();
                facturaReserva.setTipoDocumento(23);
                facturaReserva.setCliente(this.facturaVenta.getCliente());
                facturaReserva.setFecha(new Date());
                facturaReserva.setIva(this.facturaVenta.getIva());
                facturaReserva.setTotal(this.facturaVenta.getTotal());
                facturaReserva.setSubtotal(this.facturaVenta.getSubtotal());
                facturaReserva.setEstado("2");
                facturaReserva.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                facturaReserva.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
                if(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getSecuenciaDocumentoList() != null)
                {
                    this.asignarSecuencia(facturaReserva,((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta());
                }
                facturaReserva.setBodega(facturaReserva.getSecuenciaDocumento().getPuntoVenta().getBodega());
                facturaReserva.setTotalPagar(this.facturaVenta.getTotalPagar());
                facturaReserva.setDescuento(this.facturaVenta.getDescuento());
                facturaReserva.setComision(this.facturaVenta.getComision());
                facturaReserva.setPuntoVenta(this.facturaVenta.getPuntoVenta());
                facturaReserva.setTotalRetencion(this.facturaVenta.getTotalRetencion());
                facturaReserva.setTarifaIva(this.facturaVenta.getTarifaIva());
                facturaReserva.setIce(this.facturaVenta.getIce());
                facturaReserva.setIrbpnr(this.facturaVenta.getIrbpnr());
                facturaReserva.setFacturaDetalleList(new ArrayList<>());
                facturaReserva.setCabeceraFacturaImpuestoTarifaList(new ArrayList<>());
                
                for(CabeceraFacturaImpuestoTarifa impuestoCabecera : this.facturaVenta.getCabeceraFacturaImpuestoTarifaList()){
                    CabeceraFacturaImpuestoTarifa impuestoReserva = new CabeceraFacturaImpuestoTarifa();
                    impuestoReserva.setCabeceraFactura(facturaReserva);
                    impuestoReserva.setBaseImponible(impuestoCabecera.getBaseImponible());
                    impuestoReserva.setEtiqueta(impuestoCabecera.getEtiqueta());
                    impuestoReserva.setImpuestoTarifa(impuestoCabecera.getImpuestoTarifa());
                    impuestoReserva.setPorcentaje(impuestoCabecera.getPorcentaje());
                    impuestoReserva.setValor(impuestoCabecera.getValor());
                    facturaReserva.getCabeceraFacturaImpuestoTarifaList().add(impuestoReserva);
                }
                
                for(FacturaDetalle detalle : this.facturaVenta.getFacturaDetalleList()){
                    FacturaDetalle detalleReserva = new FacturaDetalle();
                    detalleReserva.setFactura(facturaReserva);
                    detalleReserva.setBodega(detalle.getBodega());
                    detalleReserva.setCantidad(detalle.getCantidad());
                    detalleReserva.setPrecioVentaUnitario(detalle.getPrecioVentaUnitario());
                    detalleReserva.setSubtotalSinDescuento(detalle.getSubtotalSinDescuento());
                    detalleReserva.setSubtotalConDescuento(detalle.getSubtotalConDescuento());
                    detalleReserva.setFecha(new Date());
                    detalleReserva.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                    detalleReserva.setProductoServicio(detalle.getProductoServicio());
                    detalleReserva.setDescuento(detalle.getDescuento());
                    detalleReserva.setValorDescuento(detalle.getValorDescuento());
                    detalleReserva.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitarioDescuento());
                    detalleReserva.setComision(detalle.getComision());
                    detalleReserva.setValorComision(detalle.getValorComision());
                    detalleReserva.setImpuestoTarifa(detalle.getImpuestoTarifa());
                    detalleReserva.setDetalleFacturaImpuestoTarifaList(new ArrayList<>());
                    for(DetalleFacturaImpuestoTarifa impuestoDetalle : detalle.getDetalleFacturaImpuestoTarifaList()){
                        DetalleFacturaImpuestoTarifa impuestoReservaD = new DetalleFacturaImpuestoTarifa();
                        impuestoReservaD.setDetalleFactura(detalleReserva);
                        impuestoReservaD.setBaseImponible(impuestoDetalle.getBaseImponible());
                        impuestoReservaD.setEtiqueta(impuestoDetalle.getEtiqueta());
                        impuestoReservaD.setImpuestoTarifa(impuestoDetalle.getImpuestoTarifa());
                        impuestoReservaD.setPorcentaje(impuestoDetalle.getPorcentaje());
                        impuestoReservaD.setValor(impuestoDetalle.getValor());
                        detalleReserva.getDetalleFacturaImpuestoTarifaList().add(impuestoReservaD);
                    }
                    facturaReserva.getFacturaDetalleList().add(detalleReserva);
                }
                
                facturaReserva.setFacturaPagoList(new ArrayList<>());
                FacturaPago facturaPagoReserva = new FacturaPago();
                facturaPagoReserva.setFactura(facturaReserva);
                facturaPagoReserva.setFormaPago(new FormaPago(140));
                facturaPagoReserva.setValor(facturaReserva.getTotal()); 
                facturaReserva.getFacturaPagoList().add(facturaPagoReserva);
                facturaReserva.setDocumentoRelacionado(this.facturaVenta); 
                facturaReserva.setObservacion(this.facturaVenta.getObservacion()); 
                this.documentosServicios.facturarReserva(facturaReserva);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public void verificarLores(){
        if(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta() != null)
        {
            super.setProductoVer(this.productoBodegaServicio.buscarCodigoBarras(super.getCodigoBarras(), super.getEmpresa().getCodigo(), ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getBodega().getCodigo()));
        }
        if(super.getProductoVer() != null)
        {
            if(super.getProductoVer() instanceof ProductoBodega){
                this.lotes.clear();
                this.lotes.addAll(documentosServicios.buscarLotesCompraMayorCero(super.getProductoVer().getCodigo()));
                this.lotes.addAll(documentosServicios.buscarLotesCompraMayorCeroDestino(super.getProductoVer().getCodigo()));
                if(lotes.size()>1){
                    PrimeFaces.current().executeScript("PF('mdlLoteProducto').show();");
                    PrimeFaces.current().executeScript("PF('mdlLoteProducto').update();");
                    super.setCodigoBarras(StringUtils.EMPTY);
                }
                else
                {
                    if(lotes.size() == 1){
                        super.buscarProductoBarras(this.lotes.get(0)); 
                    }
                    else
                    {
                        super.buscarProductoBarras(null);
                    }
                }
            }
            else
            {
                super.buscarProductoBarras(null);
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoNoEncontrado"));
            super.setCodigoBarras(StringUtils.EMPTY);
        }
    }
    
    public void seleccionarLote(FacturaDetalle lote){
        ProductoBodega producto;
        if(lote.getFactura().getNumero() == -100){
            producto = (ProductoBodega)lote.getProductoServicioDestino();
        }
        else
        {
            producto = (ProductoBodega)lote.getProductoServicio();   
        }
        producto.setLote(lote); 
        List<Producto> lista = new ArrayList<>();
        lista.add(producto);
        super.setProductoVer(producto); 
        this.agregarCalcular(lista);
        PrimeFaces.current().executeScript("PF('mdlLoteProducto').hide();");
    } 

    public List<TarjetaEmpresa> getListaTarjetaEmpresa() {
        return listaTarjetaEmpresa;
    }

    public void setListaTarjetaEmpresa(List<TarjetaEmpresa> listaTarjetaEmpresa) {
        this.listaTarjetaEmpresa = listaTarjetaEmpresa;
    }

    public List<ComisionTarjeta> getListaComisionTarjeta() {
        return listaComisionTarjeta;
    }

    public void setListaComisionTarjeta(List<ComisionTarjeta> listaComisionTarjeta) {
        this.listaComisionTarjeta = listaComisionTarjeta;
    }

    public Integer getTarjetaEmpresaSlc() {
        return tarjetaEmpresaSlc;
    }

    public void setTarjetaEmpresaSlc(Integer tarjetaEmpresaSlc) {
        this.tarjetaEmpresaSlc = tarjetaEmpresaSlc;
    }

    public Integer getComisionTarjetaSlc() {
        return comisionTarjetaSlc;
    }

    public void setComisionTarjetaSlc(Integer comisionTarjetaSlc) {
        this.comisionTarjetaSlc = comisionTarjetaSlc;
    }

    public List<PuntoVenta> getListaPuntoVenta() {
        return listaPuntoVenta;
    }

    public void setListaPuntoVenta(List<PuntoVenta> listaPuntoVenta) {
        this.listaPuntoVenta = listaPuntoVenta;
    }

    public Integer getNumeroCuentas() {
        return numeroCuentas;
    }

    public void setNumeroCuentas(Integer numeroCuentas) {
        this.numeroCuentas = numeroCuentas;
    }

    public Integer getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(Integer diasCredito) {
        this.diasCredito = diasCredito;
    }
    
    char[] temp=new char[]{ ' ' };
    public String generaItem(String cantidad, String nombre, String precio,String total){
        for(int i = cantidad.length() ; i<=4 ; i++){
            cantidad = cantidad +temp[0];
        }
        if(nombre.length()<21){
            for(int i = nombre.length() ; i<=20 ; i++){
                nombre = nombre +temp[0];
            }
        }
        else
        {
            nombre = nombre.substring(0, 20)+temp[0]+temp[0];
        }
        return cantidad+temp[0]+nombre+temp[0]+precio+temp[0]+total;
    }
    
    public String generaItemComprobante(String cantidad, String nombre, String total){
        for(int i = cantidad.length() ; i<=4 ; i++){
            cantidad = cantidad +temp[0];
        }
        if(nombre.length()<21){
            for(int i = nombre.length() ; i<=20 ; i++){
                nombre = nombre +temp[0];
            }
        }
        else
        {
            nombre = nombre.substring(0, 20)+temp[0];
        }
        return cantidad+nombre+total;
    }
    
    public void imprimirTiket(FacturaVenta factura){
        String items = StringUtils.EMPTY;
        if(factura.getPuntoVenta().getRise().equals("3")){
            for(FacturaDetalle detalleTiket : factura.getFacturaDetalleList()){
                items = items + this.generaItemComprobante(detalleTiket.getCantidad().toString(), detalleTiket.getProductoServicio().getNombre(), detalleTiket.getSubtotalSinDescuento().setScale(2,RoundingMode.FLOOR).toString()) + "\n";
            }
        }
        else
        {
            for(FacturaDetalle detalleTiket : factura.getFacturaDetalleList()){
                items = items + this.generaItem(detalleTiket.getCantidad().toString(), detalleTiket.getProductoServicio().getNombre(), detalleTiket.getPrecioVentaUnitario().toString(), detalleTiket.getSubtotalSinDescuento().setScale(2,RoundingMode.FLOOR).toString()) + "\n";
            }
            for(int i = factura.getFacturaDetalleList().size() ; i<=factura.getSecuenciaDocumento().getMaxItems() ; i++){
                items = items + "\n";
            }
        }
        String nombreCompleto = factura.getCliente().getPersona().getNombres() + " " + (factura.getCliente().getPersona().getApellidos() == null ? " " : factura.getCliente().getPersona().getApellidos());
        if(nombreCompleto.length()>30){
            nombreCompleto = nombreCompleto.substring(0, 30);
        }
        String direccionCompleta = factura.getCliente().getPersona().getDireccion() == null ? " " : factura.getCliente().getPersona().getDireccion();
        if(direccionCompleta.length()>30){
            direccionCompleta = direccionCompleta.substring(0, 30);
        }
        String mesa = StringUtils.EMPTY;
        if(factura.getPedidosVenta() != null){
            for(Map.Entry pedido : factura.getPedidosVenta().entrySet()){
                mesa = mesa + pedido.getValue() + " ";
            }
        }
        switch (factura.getPuntoVenta().getRise()) {
            case "2":
                {
                    Ticket ticket = new Ticket(factura.getNumero().toString(),
                            super.getEmpresa().getCiudad().getNombre() + "," + Fecha.formatoDateStringF0(factura.getFecha()),
                            nombreCompleto,
                            direccionCompleta,
                            factura.getCliente().getPersona().getTelefono() == null ? " " : factura.getCliente().getPersona().getTelefono(),
                            factura.getCliente().getPersona().getCedula() == null ? " " : factura.getCliente().getPersona().getCedula(),
                            items,
                            factura.getSubtotal().toString(),
                            BigDecimal.ZERO.toString(),
                            BigDecimal.ZERO.toString(),
//                                       factura.getSubtotalSinIva().setScale(2,RoundingMode.FLOOR).toString(),
//                                       factura.getSubtotalConIva().setScale(2,RoundingMode.FLOOR).toString(),
                            factura.getIva().toString(),
                            factura.getTotal().toString(),
                            mesa == null ? " " : mesa);
                    ticket.print(factura.getPuntoVenta().getImpresora(),factura.getPuntoVenta().getRise());
                    break;
                }
            case "1":
                {
                    Ticket ticket = new Ticket(factura.getNumero().toString(),
                            super.getEmpresa().getCiudad().getNombre() + "," + Fecha.formatoDateStringF0(factura.getFecha()),
                            nombreCompleto,
                            direccionCompleta,
                            factura.getCliente().getPersona().getTelefono() == null ? " " : factura.getCliente().getPersona().getTelefono(),
                            factura.getCliente().getPersona().getCedula() == null ? " " : factura.getCliente().getPersona().getCedula(),
                            items,
                            factura.getTotal().toString(),
                            mesa == null ? " " : mesa);
                    ticket.print(factura.getPuntoVenta().getImpresora(),factura.getPuntoVenta().getRise());
                    break;
                }
            default:
                {
                    Ticket ticket = new Ticket(factura.getEmpresa().getNombreAbreviado(),
                            factura.getNumero().toString(),
                            super.getEmpresa().getCiudad().getNombre() + "," + Fecha.formatoDateStringF0(factura.getFecha()),
                            nombreCompleto,
                            direccionCompleta,
                            factura.getCliente().getPersona().getTelefono() == null ? " " : factura.getCliente().getPersona().getTelefono(),
                            factura.getCliente().getPersona().getCedula() == null ? " " : factura.getCliente().getPersona().getCedula(),
                            items,
                            factura.getSubtotal().toString(),
                            factura.getIva().toString(),
                            factura.getTotal().toString(),
                            factura.getDescuento().toString());
                    ticket.print(factura.getPuntoVenta().getImpresora(),factura.getPuntoVenta().getRise());
                    break;
                }
        }
    }

    public List<FacturaDetalle> getLotes() {
        return lotes;
    }

    public void setLotes(List<FacturaDetalle> lotes) {
        this.lotes = lotes;
    }
}
