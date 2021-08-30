package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.PendientesCompra;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class PendientesCompraBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ProductoAdminBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private BodegaServicio bodegaServicio;
    
    private String codigoBarras;
    private PendientesCompra pendientesCompra;
    private List<Bodega> listaBodegas;
    private Integer bodegaSelect;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public PendientesCompraBean() {
        this.listaBodegas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
        this.bodegaSelect = this.listaBodegas.get(0).getCodigo();
        this.buscarPendientesBodega();
    }
    
    public void inicializarPendientes()
    {
        this.pendientesCompra = new PendientesCompra();
        this.pendientesCompra.setFecha(new Date());
        this.pendientesCompra.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.pendientesCompra.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        this.pendientesCompra.setEmpresa(this.empresa);
        this.pendientesCompra.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.pendientesCompra.setTotal(BigDecimal.ZERO);
    }
    
    public void buscarPendientesBodega()
    {
        this.pendientesCompra = this.documentosServicios.buscarPendientesCompra(this.bodegaSelect);
        if(this.pendientesCompra == null)
        {
            this.inicializarPendientes();
        }
        else
        {
            this.setStock(this.pendientesCompra.getFacturaDetalleList());
            this.calcularTotal();
        }
    }
    
    public void setStock(List<FacturaDetalle> lista)
    {
        for(FacturaDetalle detalle : lista)
        {
            ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
            for(ProductoStock obj : productoBodega.getProductoStockList())
            {
                if(Objects.equals(this.bodegaSelect, obj.getBodega().getCodigo()))
                {
                    detalle.setStock(obj.getStock());
                    break;
                }
            }
        }
    }
    
    public void guardar()
    {
        try {
            if(this.pendientesCompra.getCodigo() == null)
            {
                this.pendientesCompra.setBodega(new Bodega(this.bodegaSelect));
                this.documentosServicios.insertar(this.pendientesCompra);
            }
            else
            {
                this.documentosServicios.actualizar(this.pendientesCompra);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verBusquedaProductos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", "1");
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
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<ProductoBodega> listaProductos = (List) event.getObject();
            Boolean ban;
            for(ProductoBodega productoBodega : listaProductos)
            {
                ban = Boolean.TRUE;
                for(FacturaDetalle obj: this.pendientesCompra.getFacturaDetalleList())
                {
                    if(Objects.equals(obj.getProductoServicio().getCodigo(), productoBodega.getCodigo()))
                    {
                        ban = Boolean.FALSE;
                        break;
                    }
                }
                if(ban)
                {
                    FacturaDetalle detalle = new FacturaDetalle();
                    detalle.setProductoServicio(productoBodega);
                    detalle.setFactura(this.pendientesCompra);
                    detalle.setFecha(new Date());
                    detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                    detalle.setCantidad(BigDecimal.ONE);
                    detalle.setStock(BigDecimal.ZERO);
                    detalle.setPrecioVentaUnitario(productoBodega.getPrecioUltimaCompra());
                    detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                    this.pendientesCompra.getFacturaDetalleList().add(detalle);
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoExistente"));
                }
            }
            this.setStock(this.pendientesCompra.getFacturaDetalleList());
            this.calcularTotal();
        }
    }
    
    public void buscarProductoBarras()
    {
        ProductoBodega proTMP = this.productoBodegaServicio.buscarCodigoBarrasBodega(this.codigoBarras, this.empresa.getCodigo());
        if(proTMP != null)
        {
            Boolean ban = Boolean.TRUE;
            for(FacturaDetalle obj: this.pendientesCompra.getFacturaDetalleList())
            {
                if(Objects.equals(obj.getProductoServicio().getCodigo(), proTMP.getCodigo()))
                {
                    ban = Boolean.FALSE;
                    break;
                }
            }
            if(ban)
            {
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setProductoServicio(proTMP);
                detalle.setFactura(this.pendientesCompra);
                detalle.setFecha(new Date());
                detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                detalle.setCantidad(BigDecimal.ONE);
                detalle.setPrecioVentaUnitario(proTMP.getPrecioUltimaCompra());
                detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                this.pendientesCompra.getFacturaDetalleList().add(detalle);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("productoAgregado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoExistente"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoNoEncontrado"));
        }
        this.calcularTotal();
        this.codigoBarras = StringUtils.EMPTY;
    }
    
    public void eliminar(FacturaDetalle parametro) {
        try {
            this.pendientesCompra.getFacturaDetalleList().remove(parametro);
            this.calcularTotal();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void calcularTotal()
    {
        this.pendientesCompra.setTotal(BigDecimal.ZERO);
        for(FacturaDetalle obj : this.pendientesCompra.getFacturaDetalleList())
        {
            this.pendientesCompra.setTotal(this.pendientesCompra.getTotal().add(obj.getSubtotalSinDescuento()));
        }
        this.pendientesCompra.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public void onCellEdit(FacturaDetalle event) {
        event.setSubtotalSinDescuento(event.getPrecioVentaUnitario().multiply(event.getCantidad()));
        this.calcularTotal();
    }
   
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("factura", this.pendientesCompra.getCodigo());
            super.getParametros().put("nombreReporte", "Pendientes de Compra");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public PendientesCompra getPendientesCompra() {
        return pendientesCompra;
    }

    public void setPendientesCompra(PendientesCompra pendientesCompra) {
        this.pendientesCompra = pendientesCompra;
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
}
