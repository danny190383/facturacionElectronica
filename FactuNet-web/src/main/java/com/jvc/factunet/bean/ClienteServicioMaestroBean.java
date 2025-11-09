package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Anio;
import com.jvc.factunet.entidades.CobrosServicio;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class ClienteServicioMaestroBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(ClienteServicioMaestroBean.class.getName());

    @EJB
    private ClienteServicioMaestroServicio clienteServicioMaestroServicio;
    @EJB
    private AnioServicio anioServicio;
    @EJB
    private ServicioCobrosMaestroServicio servicioCobrosMaestroServicio;
    
    private LazyDataModel<ServicioPersona> lazyModel;
    private CobrosServicio cobrosServicio;
    private List<Anio> listaAnio;
    private List<SelectItem> mesesAnio;

    public ClienteServicioMaestroBean() {
        this.cobrosServicio = new CobrosServicio();
        this.listaAnio = new ArrayList<>();
    }
    
    @PostConstruct
    private void init()
    {
        this.cargarDatos();
        try {
            this.lazyModel = new LazyDataModel<ServicioPersona>()
            {
                @Override
                public List<ServicioPersona> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        String servicio = StringUtils.EMPTY;
                        String servicioCodigo = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {        
                            if(meta.getFilterField().equals("persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("servicio.nombre"))
                            {servicio=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("servicio.codigoBarras"))
                            {servicioCodigo=(String)meta.getFilterValue();}
                        }
                        List<ServicioPersona> result = clienteServicioMaestroServicio.listarBuscar(nombres,cedula,servicio,servicioCodigo, pageSize, first);
                        lazyModel.setRowCount(clienteServicioMaestroServicio.contar(nombres,cedula,servicio,servicioCodigo).intValue());
                        return result;
                    }
                    else
                    {
                        List<ServicioPersona> result = clienteServicioMaestroServicio.listar(pageSize, first);
                        lazyModel.setRowCount(clienteServicioMaestroServicio.contar().intValue());
                        return result;
                    }
                }
                
                @Override
                public ServicioPersona getRowData(String rowKey) {
                    List<ServicioPersona> lista = (List<ServicioPersona>) getWrappedData();
                    for(ServicioPersona car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ServicioPersona car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void cargarDatos(){
        this.listaAnio = this.anioServicio.listar();
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
    
    public void nuevo() {
        cobrosServicio = new CobrosServicio();
        cobrosServicio.setFechaRegistro(new Date()); 
        cobrosServicio.setEstado("1");
        cobrosServicio.setPersona(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPersona());     
    }
    
     public void guardarAutomaticos() {
        
        List<ServicioPersona> listaAutomaticosAcivos = clienteServicioMaestroServicio.listarAutomaticosActios();
        for(ServicioPersona servicioPersonaAutomatico : listaAutomaticosAcivos) {
            try {
                CobrosServicio cobroServicioInsert = new CobrosServicio();
                cobroServicioInsert.setEstado(cobrosServicio.getEstado());
                cobroServicioInsert.setFechaRegistro(new Date());
                cobroServicioInsert.setPersona(cobrosServicio.getPersona());
                cobroServicioInsert.setAnio(cobrosServicio.getAnio());
                cobroServicioInsert.setMes(cobrosServicio.getMes());
                cobroServicioInsert.setServicioPersona(servicioPersonaAutomatico);
                cobroServicioInsert.setServicio(servicioPersonaAutomatico.getServicio());
                cobroServicioInsert.setValor(servicioPersonaAutomatico.getServicio().getPvp()); 
                cobroServicioInsert.setLectura(BigDecimal.ZERO); 
                this.servicioCobrosMaestroServicio.insertar(cobroServicioInsert);
            } catch (Exception e) {
                
            }
        }
        this.init();
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        PrimeFaces.current().executeScript("PF('dialogoCobro').hide();");
    }
    
    public void verCobros(Integer servicioPersonaParam) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("servicioCobrosMaestro.xhtml?servicioPersonaParam=" + servicioPersonaParam);
    }
            
    public LazyDataModel<ServicioPersona> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ServicioPersona> lazyModel) {
        this.lazyModel = lazyModel;
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
