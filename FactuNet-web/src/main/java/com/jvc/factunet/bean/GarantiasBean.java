package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GarantiaCabecera;
import com.jvc.factunet.entidades.GarantiaDetalle;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ServicioDetalle;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.GarantiaServicio;
import com.jvc.factunet.session.Login;
import java.io.IOException;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class GarantiasBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(GarantiasBean.class.getName());
    
    @EJB
    private GarantiaServicio garantiaServicio;
    @EJB
    public ClienteServicio clienteServicio;
   
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private GarantiaCabecera garantiaCabecera;
    private GarantiaDetalle garantiaDetalleSlc;
    private Cliente cliente;
    private String codigoBarras;

    public GarantiasBean() {
        this.garantiaDetalleSlc = new GarantiaDetalle();
    }
    
    @PostConstruct
    public void init(){
        this.inicializar();
    }
    
    public void inicializar(){
        this.garantiaCabecera = new GarantiaCabecera();
        this.garantiaCabecera.setEstado("0");
        this.garantiaCabecera.setFechaIngreso(new Date());
        this.garantiaCabecera.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.garantiaCabecera.setEmpresa(this.empresa);
        this.garantiaCabecera.setGarantiaDetalleList(new ArrayList<>());
        this.iniciarCliente();
    }
    
    public void guardar()
    {
        try {
            if(this.garantiaCabecera.getGarantiaDetalleList().size()>0)
            {
                if(this.garantiaCabecera.getCodigo() == null)
                {
                    this.garantiaCabecera.setEstado("1");
                    this.garantiaServicio.insertar(this.garantiaCabecera, ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPuntoVenta().getCodigo());
                }
                else
                {
                    this.garantiaServicio.actualizar(this.garantiaCabecera);
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
    
    public void iniciarCliente()
    {
        this.cliente = new Cliente();
        this.cliente.setPersona(new Persona());
        this.cliente.getPersona().setCedula(StringUtils.EMPTY);
    }
    
    public void buscarCliente()
    {
        try {
            Cliente clienteTmp = this.clienteServicio.buscarCedula(this.cliente.getPersona().getCedula(), this.empresa.getCodigo());
            if(clienteTmp == null)
            {
                this.iniciarCliente();
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
            }
            else
            {
                this.cliente = clienteTmp;
                this.garantiaCabecera.setCliente(this.cliente);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("clienteEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 850);
        options.put("height", 400);
        options.put("contentWidth", 840);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
    }
    
    public void verNuevoCliente() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentWidth", 900);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoClienteDialog", options, null);
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.cliente = (Cliente) event.getObject();
            if(this.cliente != null)
            {
                this.garantiaCabecera.setCliente(this.cliente);
            }
            else
            {
                this.iniciarCliente();
            }
        }
    }
    
    public void eliminar(GarantiaDetalle parametro) {
        try {
            this.garantiaCabecera.getGarantiaDetalleList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void verNuevoIngreso(GarantiaDetalle detalle) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("detalle", detalle);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentHeight", 500);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoIngresoDialog", options, null);
    }
    
    public void onIngresoSelect(SelectEvent event) {
        GarantiaDetalle producto = (GarantiaDetalle) event.getObject();
        if((producto != null))
        {
            producto.setGarantiaCabecera(this.garantiaCabecera);
            producto.setEstado("1");
            this.garantiaCabecera.getGarantiaDetalleList().add(producto);
        }
    }   
    
    public void verBusquedaProductosStock(GarantiaDetalle detalle) {
        this.garantiaDetalleSlc = detalle;
        if(this.garantiaDetalleSlc.getServicioDetalleList() == null){
            this.garantiaDetalleSlc.setServicioDetalleList(new ArrayList<ServicioDetalle>());
        }
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
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            for(Producto producto : listaProductos){
                ServicioDetalle servicioDetalle  = new ServicioDetalle();
                servicioDetalle.setProductoServicio(producto);
                servicioDetalle.setCantidad(BigDecimal.ONE);
                servicioDetalle.setCosto(producto.getPvp());
                servicioDetalle.setTotal(producto.getPvp());
                servicioDetalle.setEstado(Boolean.FALSE);
                servicioDetalle.setGarantiaDetalle(this.garantiaDetalleSlc);
                this.garantiaDetalleSlc.getServicioDetalleList().add(servicioDetalle);
            }
            this.sumarTotales(this.garantiaDetalleSlc);
        }
    }
    
    public void onCellEditCantidad(ServicioDetalle event){
        event.setTotal(event.getCosto().multiply(event.getCantidad()));
        this.sumarTotales(event.getGarantiaDetalle());
    }
    
    public void sumarTotales(GarantiaDetalle detalle){
        detalle.setCosto(BigDecimal.ZERO);
        for(ServicioDetalle servicio : detalle.getServicioDetalleList()){
            detalle.setCosto(detalle.getCosto().add(servicio.getTotal()));
        }
    }
    
    public void eliminarServicio(GarantiaDetalle detalle,ServicioDetalle parametro) {
       try {
           detalle.getServicioDetalleList().remove(parametro);
           FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
       } catch (Exception e) {
           LOG.log(Level.SEVERE, "No se puede eliminar.", e);
           FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
       }
    }
    
    public void verBusquedaIngresos() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarIngresosDialog", options, null);
    }
    
    public void onIngresoCabeceraSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.garantiaCabecera = (GarantiaCabecera) event.getObject();
            this.cliente = this.garantiaCabecera.getCliente();
        }
    }
    
    public void facturarServicios(GarantiaDetalle ingreso) {
        if(ingreso.getServicioDetalleList() != null)
        {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("facturaVenta.xhtml?ingreso=" + ingreso.getCodigo());
            } catch (IOException ex) {
                Logger.getLogger(PedidoVentaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
             FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("escojaServicios"));
        }
    }

    public GarantiaCabecera getGarantiaCabecera() {
        return garantiaCabecera;
    }

    public void setGarantiaCabecera(GarantiaCabecera garantiaCabecera) {
        this.garantiaCabecera = garantiaCabecera;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    
}
