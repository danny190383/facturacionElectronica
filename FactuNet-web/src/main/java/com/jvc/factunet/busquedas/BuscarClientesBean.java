package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ServicioPersona;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.CuentasFacturaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarClientesBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarClientesBean.class.getName());

    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;

    private LazyDataModel<Cliente> lazyModel;
    private Cliente cliente;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
   
    public BuscarClientesBean() {
        this.cliente = new Cliente();
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Cliente>()
            {
                @Override
                public List<Cliente> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {        
                            if(meta.getFilterField().equals("persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                        }
                        List<Cliente> result = clienteServicio.listarBuscar(nombres,cedula,empresa.getCodigo(), pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar(nombres,cedula,empresa.getCodigo()).intValue());
                        return result;
                    }
                    else
                    {
                        List<Cliente> result = clienteServicio.listar(empresa.getCodigo(), pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar(empresa.getCodigo()).intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Cliente event) {
        List<CuentaFactura> listaCuentas = this.cuentasFacturaServicio.buscarPendientesCliente(event.getCodigo(), this.empresa.getCodigo());
        this.cliente = event;
        if(!listaCuentas.isEmpty()){
            this.cliente.setTotalDeuda(BigDecimal.ZERO);
            for(CuentaFactura cuenta : listaCuentas){
                this.cliente.setTotalDeuda(this.cliente.getTotalDeuda().add(cuenta.getSaldo()));
            }
            PrimeFaces.current().executeScript("PF('dlgRestriccion').show();");
        }
        else
        {
            PrimeFaces.current().dialog().closeDynamic(event);
        }
    }
    
    public void cerrarSeleccionar(){
        PrimeFaces.current().dialog().closeDynamic(this.cliente);
    }
    
    public void refrescarLista(){
        this.init();
    }
    
    public void eliminar(Cliente parametro) {
        try {
            this.clienteServicio.eliminar(parametro);
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer persona) {
        try {
            super.getParametros().put("empresa", this.empresa);
            super.getParametros().put("persona", persona);
            super.getParametros().put("tipo", 1);
            super.getParametros().put("nombreReporte", "Ficha Cliente");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void verNuevoServicioPersona(ServicioPersona servicioPersona) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicioPersona", servicioPersona);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/ventas/extras/nuevoServicioPersonaDialog", options, null);
    }
    
    public void onServicioPersonaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            try {
                ServicioPersona servicioPersona = (ServicioPersona) event.getObject();
                this.clienteServicio.actualizar(servicioPersona);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                this.init();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se puede guardar.", e);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public void verBusquedaProductosStock(Cliente persona) {
        this.cliente = persona;
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
        if (event.getObject() != null) {
            List<Producto> listaProductosSeleccionados = (List<Producto>) event.getObject();
            
            if (this.cliente.getPersona().getServicioPersonaList() == null) {
                this.cliente.getPersona().setServicioPersonaList(new ArrayList<>());
            }

            boolean seAgregoAlguno = false; 

            for (Producto producto : listaProductosSeleccionados) {
                boolean yaExiste = this.cliente.getPersona().getServicioPersonaList().stream()
                        .anyMatch(sp -> sp.getServicio().equals(producto));

                if (yaExiste) {
                    FacesUtils.addWarningMessage("El servicio '" + producto.getNombre() + "' ya está incluido.");
                } else {
                    ServicioPersona servicioPersona = new ServicioPersona();
                    servicioPersona.setPersona(this.cliente.getPersona());
                    servicioPersona.setServicio(producto);
                    servicioPersona.setEstado(Boolean.TRUE);
                    servicioPersona.setRequiereLectura(Boolean.TRUE);
                    servicioPersona.setCobroAutomatico(Boolean.FALSE);
                    
                    this.cliente.getPersona().getServicioPersonaList().add(servicioPersona);
                    seAgregoAlguno = true; 
                }
            }

            
            if (seAgregoAlguno) {
                try {
                    this.clienteServicio.actualizar(cliente);
                    this.init(); 
                    FacesUtils.addInfoMessage("Servicios agregados correctamente."); 
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "No se puede guardar la actualización de servicios.", e);
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoGuardado"));
                }
            }
        }
    }
     
//    public void onProductoSelect(SelectEvent event) {
//        if(event.getObject() != null)
//        {
//           List<Producto> listaProductos = (List) event.getObject();
//           if(this.cliente.getPersona().getServicioPersonaList() == null){
//               this.cliente.getPersona().setServicioPersonaList(new ArrayList<>());
//           }
//           for(Producto producto : listaProductos){
//               ServicioPersona servicioPersona = new ServicioPersona();
//               servicioPersona.setPersona(this.cliente.getPersona());
//               servicioPersona.setServicio(producto);
//               servicioPersona.setEstado(Boolean.TRUE);
//               servicioPersona.setRequiereLectura(Boolean.TRUE);
//               servicioPersona.setCobroAutomatico(Boolean.FALSE); 
//               this.cliente.getPersona().getServicioPersonaList().add(servicioPersona);
//           }
//            try {
//                this.clienteServicio.actualizar(cliente);
//                this.init();
//            } catch (Exception e) {
//                LOG.log(Level.SEVERE, "No se puede guardar.", e);
//                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
//            }
//        }
//    }
    
    public void eliminarServicio(Cliente cliente, int index) {
        try {
            cliente.getPersona().getServicioPersonaList().remove(index);
            this.clienteServicio.actualizar(cliente);
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void verNuevoCliente(Cliente cliente) {
        if(cliente != null)
        {
            if(cliente.getCodigo() != null){
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", cliente.getPersona().getCodigo());
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
        if(empresa.getClienteCompleto().equals("1")){
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
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.init();
        }
    }
    
    public LazyDataModel<Cliente> getLazyModel() {
        return lazyModel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
