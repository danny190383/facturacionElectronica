package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.NotaCredito;
import com.jvc.factunet.entidades.NotaDebito;
import com.jvc.factunet.entidades.Producto;
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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@Named(value = "notaDebitoBean")
@ViewScoped
public class NotaDebitoBean extends FacturaVentaBean{
    
    private static final Logger LOG = Logger.getLogger(NotaDebitoBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    
    private NotaDebito notaDebito;
    
    public NotaDebitoBean() {
        this.notaDebito = new NotaDebito();
    }
    
    @Override
    public void inicializar()
    {
        this.notaDebito = new NotaDebito();
        this.notaDebito.setFecha(new Date());
        this.notaDebito.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.notaDebito.setFacturaDetalleList(new ArrayList<>());
        this.notaDebito.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.notaDebito.setGuiaRemisionList(new ArrayList<>());
        this.notaDebito.setTotal(BigDecimal.ZERO);
        this.notaDebito.setFacturaPagoList(new ArrayList<>());
        this.notaDebito.setEstado("1");
        this.notaDebito.setNumero(0);
        this.notaDebito.setTipoNota(5); 
        super.setDescuento(BigDecimal.ZERO);
        this.iniciarCliente();
        this.calcularTotales();
    }
    
    @Override
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.notaDebito);
        this.calcularTotales();
    }
    
    @Override
    public void calcularTotales()
    {
        this.notaDebito = (NotaDebito) this.calcularTotalPago(this.notaDebito);
    }
    
    @Override
    public void eliminar(FacturaDetalle parametro) {
        try {
            this.notaDebito.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void verPago() {
        if(this.notaDebito.getDocumentoRelacionado() == null){
            FacesUtils.addErrorMessage("Debe seleccionar un documento para relacionar a la nota de débito.");
            return;
        }
        if(this.notaDebito.getTotal() != null)
        {
            if(this.notaDebito.getTotal().doubleValue() > 0)
            {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.notaDebito.getFacturaPagoList());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.notaDebito.getTotal());
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
    
    @Override
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            this.notaDebito.getFacturaPagoList().clear();
            for(FacturaPago pago : listaPagosFin){
                pago.setFactura(this.notaDebito);
                this.notaDebito.getFacturaPagoList().add(pago);    
            }
            this.guardar();
        }
    }
    
    @Override
    public void guardar()
    {
        try {
            if(this.notaDebito.getFacturaDetalleList().size()>0)
            {
                if(this.notaDebito.getCodigo() == null)
                {
                    this.notaDebito.setBodega(new Bodega(super.getBodegaSelect()));
                    this.notaDebito.setEstado("2");
                    this.notaDebito.setCliente(super.getCliente()); 
                    this.documentosServicios.insertarNotaDebito(this.notaDebito);
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
    
    @Override
    public void generarReporte(String tipoReporte) {
        if(this.notaDebito.getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", this.notaDebito.getCodigo());
                super.getParametros().put("nombreReporte", "Nota de Débito");
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
        for(FacturaDetalle detalle : this.notaDebito.getFacturaDetalleList()){
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
                FacesUtils.addErrorMessage("No se puede generar una nota de débito sobre un documento anulado.");
                return;
            }
            super.setCliente(super.getFacturaVenta().getCliente());
            this.notaDebito.setDocumentoRelacionado(super.getFacturaVenta()); 
            this.notaDebito.setPuntoVenta(super.getFacturaVenta().getPuntoVenta());
            this.calcularTotales();
            for(SecuenciaDocumento secuencia : ((FacturaVenta)this.notaDebito.getDocumentoRelacionado()).getPuntoVenta().getSecuenciaDocumentoList()){
                if(Objects.equals(secuencia.getTipoDocumento().getNombre(), "NOTA DE DÉBITO") && (secuencia.getEstado().equals("1"))){
                    SecuenciaDocumento secuenciaBase = this.puntoVentaServicio.numeroActual(secuencia.getCodigo());
                    this.notaDebito.setSecuenciaDocumento(secuenciaBase);
                    this.notaDebito.setNumero(secuenciaBase.getNumeroActual() + 1);
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
            this.notaDebito.setDocumentoRelacionado((NotaCredito) event.getObject());
            this.notaDebito.setPuntoVenta(((FacturaVenta)this.notaDebito.getDocumentoRelacionado()).getPuntoVenta()); 
            for(SecuenciaDocumento secuencia : ((FacturaVenta)((NotaCredito)this.notaDebito.getDocumentoRelacionado()).getDocumentoRelacionado()).getPuntoVenta().getSecuenciaDocumentoList()){
                if(Objects.equals(secuencia.getTipoDocumento().getNombre(), "NOTA DE DÉBITO") && (secuencia.getEstado().equals("1"))){
                    SecuenciaDocumento secuenciaBase = this.puntoVentaServicio.numeroActual(secuencia.getCodigo());
                    this.notaDebito.setSecuenciaDocumento(secuenciaBase);
                    this.notaDebito.setNumero(secuenciaBase.getNumeroActual() + 1);
                    break;
                }
            }
            super.setCliente(((FacturaVenta)((NotaCredito)this.notaDebito.getDocumentoRelacionado()).getDocumentoRelacionado()).getCliente());
        }
    }
    
    public void verBusquedaNotasDebito() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 1);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 500);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarNotasDebitoDialog", options, null);
    }
    
    public void onNotaDebitoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.notaDebito = (NotaDebito) event.getObject();
            if(this.notaDebito.getDocumentoRelacionado() instanceof FacturaVenta){
                super.setCliente(((FacturaVenta)this.notaDebito.getDocumentoRelacionado()).getCliente()); 
            }
            if(this.notaDebito.getDocumentoRelacionado() instanceof NotaCredito){
                super.setCliente(((FacturaVenta)((NotaCredito)this.notaDebito.getDocumentoRelacionado()).getDocumentoRelacionado()).getCliente());
            }
        }
    }

    public NotaDebito getNotaDebito() {
        return notaDebito;
    }

    public void setNotaDebito(NotaDebito notaDebito) {
        this.notaDebito = notaDebito;
    }
}
