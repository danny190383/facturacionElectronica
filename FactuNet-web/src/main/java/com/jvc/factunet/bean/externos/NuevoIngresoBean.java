package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.GarantiaDetalle;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NuevoIngresoBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoIngresoBean.class.getName());
    
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;

    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private GarantiaDetalle garantiaDetalle;
    
    public NuevoIngresoBean() {
    }
    
    @PostConstruct
    public void init(){
        this.garantiaDetalle = (GarantiaDetalle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("detalle");
        if(this.garantiaDetalle == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.garantiaDetalle = new GarantiaDetalle();
        this.garantiaDetalle.setCantidad(1);
        this.garantiaDetalle.setEstado("0");
        this.garantiaDetalle.setTipo("1");
        this.garantiaDetalle.setFechaEstimada(new Date());
    }
    
    public void buscarProductoBarras()
    {
        Producto proTMP;
        proTMP = this.productoBodegaServicio.buscarCodigoBarras(this.garantiaDetalle.getCodigoBarras(), this.empresa.getCodigo(), null);
        if(proTMP == null)
        {
            Producto proTMPS = this.productoBodegaServicio.buscarSerieFactura(this.garantiaDetalle.getCodigoBarras(), this.empresa.getCodigo(), "2", "3");
            if(proTMPS != null){
                this.garantiaDetalle.setFacturaCompra(proTMPS.getSerie().getFacturaDetalle().getFactura());
            }
            proTMPS = this.productoBodegaServicio.buscarSerieFactura(this.garantiaDetalle.getCodigoBarras(), this.empresa.getCodigo(), "1", "2");
            if(proTMPS != null){
                this.garantiaDetalle.setFacturaVenta(proTMPS.getSerie().getFacturaDetalle().getFactura());
                this.garantiaDetalle.setProductoServicio(proTMPS);
                this.garantiaDetalle.setProductoRecepcion(proTMPS.getNombre());
            }
        }
        else
        {
            this.garantiaDetalle.setProductoServicio(proTMP);
            this.garantiaDetalle.setProductoRecepcion(proTMP.getNombre());
        }
    }
    
    public void verBusquedaFacturasVenta() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 1);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoDocumento", 0);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasVentaDialog", options, null);
    }
    
    public void onFacturaVentaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
             this.garantiaDetalle.setFacturaVenta((Factura) event.getObject());
        }
    }
    
    public void verBusquedaFacturasCompra() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasCompraDialog", options, null);
    }
    
    public void onFacturaCompraSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
             this.garantiaDetalle.setFacturaCompra((Factura) event.getObject());
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Factura de Venta");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Factura de Compra");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.garantiaDetalle);
    }

    public GarantiaDetalle getGarantiaDetalle() {
        return garantiaDetalle;
    }

    public void setGarantiaDetalle(GarantiaDetalle garantiaDetalle) {
        this.garantiaDetalle = garantiaDetalle;
    }
}
