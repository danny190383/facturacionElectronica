package com.jvc.factunet.bean.externos;

import com.jvc.factunet.bean.CuentasAdminBean;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class NuevaCuentaBean implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(NuevaCuentaBean.class.getName());
    
    @EJB
    public DocumentosServicios documentosServicios;
    
    private Factura factura;
    private BigDecimal totalCuenta;
    private Cliente cliente;
    private Proveedor proveedor;
    private String tipo;
    
    public NuevaCuentaBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.tipo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipo");
        this.cliente = (Cliente) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente");
        this.proveedor = (Proveedor) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proveedor");
        this.totalCuenta = BigDecimal.ZERO;
    }
 
    public void inicializar()
    {
        if("1".equals(this.tipo))
        {
            this.factura = new FacturaCompra();
            this.factura.setProveedor(this.proveedor);
        }
        else
        {
            this.factura = new FacturaVenta();
            this.factura.setCliente(this.cliente);
        }
        this.factura.setFecha(new Date());
        this.factura.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.factura.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        this.factura.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.factura.setGuiaRemisionList(new ArrayList<GuiaRemision>());
        this.factura.setTotal(this.totalCuenta);
        this.factura.setFacturaPagoList(new ArrayList<FacturaPago>());
        this.factura.setEstado("2");
        this.factura.setNumero(-1);
        this.factura.setTotalPagar(this.totalCuenta);
    }
    
    public void onPagoNuevoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            try {
                List<FacturaPago> listaPagosFin = new ArrayList<>();
                listaPagosFin.addAll((List) event.getObject());
                for(FacturaPago pago : listaPagosFin){
                    pago.setFactura(this.factura);
                    this.factura.getFacturaPagoList().add(pago);    
                }
            } catch (Exception ex) {
                Logger.getLogger(CuentasAdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    
    public void onFacturaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            if("1".equals(this.tipo))
            {
                if(((Factura) event.getObject()).getEstado().equals("3"))
                {
                    this.factura = (Factura) event.getObject();
                }
            }
            else
            {
                if(((Factura) event.getObject()).getEstado().equals("2"))
                {
                    this.factura = (Factura) event.getObject();
                }
            }
            if(this.factura == null)
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("facturaEstaAnulada"));
            }
            else
            {
                this.totalCuenta = BigDecimal.ZERO;
            }
        }
    }
    
    public void verPago() {
        if(this.factura != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.factura.getTotal());
        }
        else
        {
            if(this.totalCuenta.floatValue() > 0)
            {
                this.inicializar();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.totalCuenta);
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("valorMayorCero"));
                return;
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.factura.getFacturaPagoList());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
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
    
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            this.factura.getFacturaPagoList().clear();   
            for(FacturaPago pago : listaPagosFin){
                pago.setFactura(this.factura);
                this.factura.getFacturaPagoList().add(pago);    
            }
            this.guardar();
        }
    }
    
     public void guardar()
    {
        try {
            if(this.factura.getCodigo() == null)
            {
                this.documentosServicios.insertarDocumento(this.factura);
            }
            else
            {
                this.documentosServicios.actualizarFactura(this.factura);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.factura);
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public BigDecimal getTotalCuenta() {
        return totalCuenta;
    }

    public void setTotalCuenta(BigDecimal totalCuenta) {
        this.totalCuenta = totalCuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
