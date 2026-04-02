package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Anio;
import com.jvc.factunet.entidades.CobrosServicio;
import com.jvc.factunet.entidades.Controles;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ServicioPersona;
import com.jvc.factunet.enumeracion.EnumMesAnio;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.AnioServicio;
import com.jvc.factunet.servicios.ClienteServicioMaestroServicio;
import com.jvc.factunet.servicios.ServicioCobrosMaestroServicio;
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
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class ServicioCobrosMaestroBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(ServicioCobrosMaestroBean.class.getName());

    @EJB
    private ServicioCobrosMaestroServicio servicioCobrosMaestroServicio;
    @EJB
    private ClienteServicioMaestroServicio clienteServicioMaestroServicio;
    @EJB
    private AnioServicio anioServicio;
    
    private LazyDataModel<CobrosServicio> lazyModel;
    private Integer servicioPersonaParam;
    private CobrosServicio cobrosServicio;
    private ServicioPersona servicioPersona;
    private List<SelectItem> mesesAnio;
    private List<Anio> listaAnio;

    public ServicioCobrosMaestroBean() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if(request.getParameter("servicioPersonaParam") != null)
        {
            servicioPersonaParam = new Integer(request.getParameter("servicioPersonaParam"));
            servicioPersona = new ServicioPersona();
            cobrosServicio = new CobrosServicio();
            this.listaAnio = new ArrayList<>();
        }
    }
    
    @PostConstruct
    private void init()
    {
        this.cargarDatos();
        try {
            this.lazyModel = new LazyDataModel<CobrosServicio>()
            {
                @Override
                public List<CobrosServicio> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    List<CobrosServicio> result = servicioCobrosMaestroServicio.listar(servicioPersonaParam,pageSize, first);
                    lazyModel.setRowCount(servicioCobrosMaestroServicio.contar(servicioPersonaParam).intValue());
                    return result;
                }
                
                @Override
                public CobrosServicio getRowData(String rowKey) {
                    List<CobrosServicio> lista = (List<CobrosServicio>) getWrappedData();
                    for(CobrosServicio car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(CobrosServicio car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            String error = "error";
        }
    }
    
    public void cargarDatos(){
        servicioPersona = clienteServicioMaestroServicio.buscar(servicioPersonaParam);
        this.listaAnio = anioServicio.listar();
    }
    
    public void nuevo() {
        cobrosServicio = new CobrosServicio();
        cobrosServicio.setServicioPersona(servicioPersona); 
        cobrosServicio.setFechaRegistro(new Date()); 
        cobrosServicio.setEstado("1");
        cobrosServicio.setPersona(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPersona()); 
        cobrosServicio.setServicio(servicioPersona.getServicio()); 
        if(servicioPersona.getCobroAutomatico()){
            cobrosServicio.setValor(servicioPersona.getServicio().getPvp()); 
            cobrosServicio.setLectura(BigDecimal.ZERO); 
        }else{
            BigDecimal lecturaA = this.servicioCobrosMaestroServicio.obtenerUltimaLectura(servicioPersona.getServicio().getCodigo());
            if(lecturaA != null){
                cobrosServicio.setLecturaAnterior(lecturaA); 
            }else{
                cobrosServicio.setLecturaAnterior(BigDecimal.ZERO); 
            }
        } 
    }
    
    public void calcularLectura(){
        if (cobrosServicio.getLecturaAnterior() != null 
                && cobrosServicio.getLecturaActual() != null
                && cobrosServicio.getLecturaAnterior().compareTo(BigDecimal.ZERO) > 0
                && cobrosServicio.getLecturaActual().compareTo(BigDecimal.ZERO) > 0
                && cobrosServicio.getLecturaActual().compareTo(cobrosServicio.getLecturaAnterior()) > 0) {

            cobrosServicio.setLectura(
                cobrosServicio.getLecturaActual().subtract(cobrosServicio.getLecturaAnterior())
            );
        }else {
            cobrosServicio.setLectura(BigDecimal.ZERO);
        }
    }
    
    public void seleccionar(CobrosServicio parametro) {
        this.cobrosServicio = parametro;
    }
    
    public List<SelectItem> getMesesAnio() {
        if (mesesAnio == null) {
            mesesAnio = new ArrayList<>();
            for (EnumMesAnio mes : EnumMesAnio.values()) {
                mesesAnio.add(new SelectItem(mes.name(), mes.getEtiqueta()));
            }
        }
        return mesesAnio;
    }
    
    public void guardar() {
        try {
            if (this.cobrosServicio.getCodigo() != null) {
                this.servicioCobrosMaestroServicio.actualizar(this.cobrosServicio);
            } else {
                this.servicioCobrosMaestroServicio.insertar(this.cobrosServicio);
                this.init();
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoCobro').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            this.init();
        }
    }
     
    public void verBusquedaProductosStock() {
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
            this.cobrosServicio.setServicio(listaProductos.get(0)); 
            this.cobrosServicio.setValor(this.cobrosServicio.getServicio().getPvp()); 
        }
    }
    
    public void eliminarCobro(CobrosServicio cobrosServicio){
        try {
            servicioCobrosMaestroServicio.eliminar(cobrosServicio); 
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void calcularAgua(){
        Controles baseAgua = ((Login)FacesUtils.getManagedBean("login")).getBaseAgua();
        if(baseAgua != null){
            BigDecimal base = new BigDecimal(baseAgua.getDetalle());
            if(this.cobrosServicio.getLectura().compareTo(base) < 0){
                this.cobrosServicio.setValor(baseAgua.getValor()); 
            }else{
                BigDecimal diferencia = this.cobrosServicio.getLectura().subtract(base);
                BigDecimal valorTotal = diferencia.multiply(this.cobrosServicio.getServicio().getPvp());
                this.cobrosServicio.setValor(baseAgua.getValor().add(valorTotal));  
            }
        }
    }
    
    public void regresar() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("clienteSericioMaestro.xhtml" );
    }
    
    public LazyDataModel<CobrosServicio> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<CobrosServicio> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ServicioPersona getServicioPersona() {
        return servicioPersona;
    }

    public void setServicioPersona(ServicioPersona servicioPersona) {
        this.servicioPersona = servicioPersona;
    }

    public CobrosServicio getCobrosServicio() {
        return cobrosServicio;
    }

    public void setCobrosServicio(CobrosServicio cobrosServicio) {
        this.cobrosServicio = cobrosServicio;
    }

    public List<Anio> getListaAnio() {
        return listaAnio;
    }

    public void setListaAnio(List<Anio> listaAnio) {
        this.listaAnio = listaAnio;
    }
   
}
