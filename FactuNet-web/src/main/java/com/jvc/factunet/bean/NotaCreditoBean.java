package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.NotaCredito;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.PuntoVentaServicio;
import com.jvc.factunet.session.Login;
import java.math.BigDecimal;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NotaCreditoBean extends FacturaVentaBean {

    private static final Logger LOG = Logger.getLogger(NotaCreditoBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    
    private NotaCredito notaCredito;
    private List<FacturaDetalle> listaDetalleSlc;
    
    public NotaCreditoBean() {
        this.notaCredito = new NotaCredito();
        this.listaDetalleSlc = new ArrayList<>();
    }
    
    @Override
    public void inicializar()
    {
        this.notaCredito = new NotaCredito();
        this.notaCredito.setFecha(new Date());
        this.notaCredito.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.notaCredito.setFacturaDetalleList(new ArrayList<>());
        this.notaCredito.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.notaCredito.setGuiaRemisionList(new ArrayList<>());
        this.notaCredito.setTotal(BigDecimal.ZERO);
        this.notaCredito.setFacturaPagoList(new ArrayList<>());
        this.notaCredito.setEstado("1");
        this.notaCredito.setNumero(0);
        super.setDescuento(BigDecimal.ZERO);
        this.iniciarCliente();
        this.calcularTotales();
    }
    
    @Override
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.notaCredito);
        this.calcularTotales();
    }
    
    @Override
    public void calcularTotales()
    {
        this.notaCredito = (NotaCredito) this.calcularTotalPago(this.notaCredito);
    }
    
    @Override
    public void eliminar(int parametro) {
        try {
            this.notaCredito.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void setStockPrecio()
    {
        for(FacturaDetalle detalle : this.notaCredito.getFacturaDetalleList())
        {
            detalle.setStock(BigDecimal.ZERO);
            if(detalle.getProductoServicio() instanceof ProductoBodega)
            {
                ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                detalle.setStock(this.setStockBodega(productoBodega,detalle.getBodega().getCodigo()));
            }
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                detalle.setIsPaquete(Boolean.TRUE);
            }
        }
        this.calcularTotales();
    }
    
    public boolean verificarCantidades(){
        BigDecimal totalCantidadFacturado = BigDecimal.ZERO;
        BigDecimal totalValorNotasCredito = BigDecimal.ZERO;
        BigDecimal totalCantidadNotasCredito = BigDecimal.ZERO;
        Factura facturaOriginal = this.documentosServicios.buscarDocumento(this.notaCredito.getDocumentoRelacionado().getCodigo());
        for(FacturaDetalle detalleNotaCredito : this.notaCredito.getFacturaDetalleList()){
            totalCantidadFacturado = BigDecimal.ZERO;
            for(FacturaDetalle detalleFactura : facturaOriginal.getFacturaDetalleList()){
                if(Objects.equals(detalleNotaCredito.getProductoServicio().getCodigo(), detalleFactura.getProductoServicio().getCodigo())){
                    totalCantidadFacturado = totalCantidadFacturado.add(detalleFactura.getCantidad());
                }
            }
            totalCantidadNotasCredito = BigDecimal.ZERO;
            for(NotaCredito notasFactura : facturaOriginal.getNotaCreditoList()){
                for(FacturaDetalle detalleFactura : notasFactura.getFacturaDetalleList()){
                    if(Objects.equals(detalleFactura.getProductoServicio().getCodigo(), detalleNotaCredito.getProductoServicio().getCodigo())){
                        totalCantidadNotasCredito = totalCantidadNotasCredito.add(detalleFactura.getCantidad());
                    }
                }
                totalValorNotasCredito = totalValorNotasCredito.add(notasFactura.getTotal());
            }
            if(this.notaCredito.getTipoNota() == 1 || this.notaCredito.getTipoNota() == 2){
                if(!(totalCantidadFacturado.floatValue() >= (totalCantidadNotasCredito.add(detalleNotaCredito.getCantidad())).floatValue())){ 
                    return false;
                }
            }
        }
        if(!((totalValorNotasCredito.add(this.notaCredito.getTotal())).floatValue() <= facturaOriginal.getTotal().floatValue())){
            return false;
        }
        return true;
    }
    
    @Override
    public void guardar()
    {
        if(super.getFacturaVenta().getCodigo() == null){
            FacesUtils.addErrorMessage("Debe seleccionar una factura de venta.");
            return;
        }
        if(this.verificarCantidades()){
            try {
                if(this.notaCredito.getFacturaDetalleList().size()>0)
                {
                    if(this.notaCredito.getCodigo() == null)
                    {
                        this.notaCredito.setBodega(new Bodega(super.getBodegaSelect()));
                        this.notaCredito.setEstado("2");
                        this.notaCredito.setCliente(super.getCliente()); 
                        this.documentosServicios.insertarNotaCredito(this.notaCredito);
                    }
                    else
                    {
//                        super.documentosServicios.actualizar(this.notaCredito);
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
        else
        {
            FacesUtils.addErrorMessage("La nota de credito no puede sobrepasar el monto de la factura, verificar cantidades y valores");
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        if(this.notaCredito.getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", this.notaCredito.getCodigo());
                super.getParametros().put("nombreReporte", "Nota de Crédito");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
            }
        }
    }
    
    @Override
    public void generalDescuento()
    {
        for(FacturaDetalle detalle : this.notaCredito.getFacturaDetalleList()){
            detalle.setDescuento(super.getDescuento());
            super.onCellEditDescuento(detalle, false);
        }
        this.calcularTotales();
    }
    
    @Override
    public void onFacturaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            super.setFacturaVenta((FacturaVenta) event.getObject());
            if(super.getFacturaVenta().getEstado().equals("3")){
                FacesUtils.addErrorMessage("No se puede generar una nota de crédito sobre un documento anulado.");
                return;
            }
            if(super.getFacturaVenta().getEstadoAutorizacionSri() == null || !super.getFacturaVenta().getEstadoAutorizacionSri().equals("AUTORIZADO")){
                FacesUtils.addErrorMessage("No se puede generar una nota de crédito sobre un documento no autorizado por el sri.");
                return;
            }
            if(super.getFacturaVenta().getCliente().getPersona().getCedula().equals("9999999999999")){ 
                FacesUtils.addErrorMessage("No se puede generar una nota de crédito a CONSUMIDOR FINAL");
                return;
            }
            super.setCliente(super.getFacturaVenta().getCliente());
            for(FacturaDetalle detalle : super.getFacturaVenta().getFacturaDetalleList())
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
                if(this.notaCredito.getTipoNota() == 1 ||  this.notaCredito.getTipoNota() == 2){
                    FacturaDetalle notaDetalle = new FacturaDetalle();
                    
                    notaDetalle.setBodega(detalle.getBodega());
                    notaDetalle.setCantidad(detalle.getCantidad());
                    notaDetalle.setPrecioVentaUnitario(detalle.getPrecioVentaUnitario()); 
                    notaDetalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitarioDescuento()); 
                    notaDetalle.setProductoServicio(detalle.getProductoServicio());
                    notaDetalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado()); 
                    notaDetalle.setFactura(this.notaCredito);
                    notaDetalle.setFecha(new Date()); 
                    notaDetalle.setImpuestoTarifa(detalle.getImpuestoTarifa()); 
                    notaDetalle.setSubtotalConDescuento(detalle.getSubtotalConDescuento());
                    notaDetalle.setSubtotalPedido(detalle.getSubtotalPedido());
                    notaDetalle.setSubtotalSinDescuento(detalle.getSubtotalSinDescuento());
                    notaDetalle.setValorComision(detalle.getValorComision());
                    notaDetalle.setValorDescuento(detalle.getValorDescuento());
                    notaDetalle.setValorProrrateo(detalle.getValorProrrateo()); 
                    notaDetalle.setDescuento(detalle.getDescuento()); 
                    notaDetalle.setComision(detalle.getComision()); 
                    notaDetalle.setCostoFecha(detalle.getCostoFecha()); 
                    notaDetalle.setStock(detalle.getStock()); 
                    notaDetalle.setIsPaquete(detalle.getIsPaquete()); 
                    this.notaCredito.getFacturaDetalleList().add(notaDetalle);
                }
            }
            this.notaCredito.setDocumentoRelacionado(super.getFacturaVenta()); 
            this.notaCredito.setPuntoVenta(super.getFacturaVenta().getPuntoVenta()); 
            this.calcularTotales();
            for(SecuenciaDocumento secuencia : ((FacturaVenta)this.notaCredito.getDocumentoRelacionado()).getPuntoVenta().getSecuenciaDocumentoList()){
                if(Objects.equals(secuencia.getTipoDocumento().getNombre(), "NOTA DE CRÉDITO") && (secuencia.getEstado().equals("1"))){
                    SecuenciaDocumento secuenciaBase = this.puntoVentaServicio.numeroActual(secuencia.getCodigo());
                    this.notaCredito.setSecuenciaDocumento(secuenciaBase);
                    this.notaCredito.setNumero(secuenciaBase.getNumeroActual() + 1);
                    break;
                }
            }
        }
    }
    
    @Override
    public void verBusquedaFacturas() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 1);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoDocumento", 21);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 500);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasVentaDialog", options, null);
    }
    
    @Override
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            super.setProductoVer(new Producto());
            for(Producto producto : listaProductos){
                if(!(producto instanceof ProductoServicio)){
                    FacesUtils.addErrorMessage("Debe seleccionar un servicio para el tipo de nota de crédito");
                    return;
                }
            }
            this.agregarCalcular(listaProductos);
        }
    }
    
    public void eliminarDetallles(){
        for(FacturaDetalle detalle : this.listaDetalleSlc){
            this.notaCredito.getFacturaDetalleList().remove(detalle);
        }
        this.calcularTotales();
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
    }
    
    public void verBusquedaNotasCredito() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 1);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 500);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarNotasCreditoDialog", options, null);
    }
    
    public void onNotaCreditoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.notaCredito = (NotaCredito) event.getObject();
        }
    }
    
    public NotaCredito getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(NotaCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    public List<FacturaDetalle> getListaDetalleSlc() {
        return listaDetalleSlc;
    }

    public void setListaDetalleSlc(List<FacturaDetalle> listaDetalleSlc) {
        this.listaDetalleSlc = listaDetalleSlc;
    }
}
