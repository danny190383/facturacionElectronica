package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoIdentificacion;
import com.jvc.factunet.entidades.TipoValidacion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoIdentificacionServicio;
import com.jvc.factunet.servicios.TipoValidacionServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class TipoIdentificacionBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(TipoIdentificacionBean.class.getName());
    
    @EJB
    private TipoIdentificacionServicio tipoIdentificacionServicio;
    @EJB
    private TipoValidacionServicio tipoValidacionServicio;
    
    
    private List<TipoIdentificacion> lista;
    private TipoIdentificacion tipoIdentificacion;
    private String buscar;
    private List<TipoValidacion> listaValidacion;

    public TipoIdentificacionBean() {
        this.lista=new ArrayList<>();
        this.listaValidacion=new ArrayList<>();
        this.tipoIdentificacion = new TipoIdentificacion();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoIdentificacionServicio.listar());
        this.listaValidacion.addAll(this.tipoValidacionServicio.listar());
        this.buscar = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tipoIdentificacion = new TipoIdentificacion();
        this.tipoIdentificacion.setTipoValidacion(this.listaValidacion.get(0));
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoIdentificacionServicio.listarNombre(this.buscar));
        this.buscar = StringUtils.EMPTY;
    }
    
    public void seleccionar(TipoIdentificacion parametro) {
        this.tipoIdentificacion = parametro;
    }
    
    public void eliminar(TipoIdentificacion parametro) {
        try {
            this.tipoIdentificacionServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipoIdentificacion.setNombre(this.tipoIdentificacion.getNombre().trim().toUpperCase());
            if (this.tipoIdentificacion.getCodigo() != null) {
                this.tipoIdentificacionServicio.actualizar(this.tipoIdentificacion);
            } else {
                this.tipoIdentificacionServicio.insertar(this.tipoIdentificacion);
                this.lista.add(tipoIdentificacion);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoTipoIdentificacion').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            this.init();
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_IDENTIFICACION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoIdentificacion> getLista() {
        return lista;
    }

    public void setLista(List<TipoIdentificacion> lista) {
        this.lista = lista;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public List<TipoValidacion> getListaValidacion() {
        return listaValidacion;
    }

    public void setListaValidacion(List<TipoValidacion> listaValidacion) {
        this.listaValidacion = listaValidacion;
    }
}
