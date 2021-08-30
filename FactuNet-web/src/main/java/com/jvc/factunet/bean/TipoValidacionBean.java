package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoValidacion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
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
public class TipoValidacionBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(TipoValidacionBean.class.getName());
    
    @EJB
    private TipoValidacionServicio tipoValidacionServicio;
    
    private List<TipoValidacion> lista;
    private TipoValidacion tipoValidacion;
    private String buscar;
    
    public TipoValidacionBean() {
        this.lista=new ArrayList<>();
        this.tipoValidacion = new TipoValidacion();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoValidacionServicio.listar());
        this.buscar = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tipoValidacion = new TipoValidacion();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoValidacionServicio.listarNombre(this.buscar));
        this.buscar = StringUtils.EMPTY;
    }
    
    public void seleccionar(TipoValidacion parametro) {
        this.tipoValidacion = parametro;
    }
    
    public void eliminar(TipoValidacion parametro) {
        try {
            this.tipoValidacionServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipoValidacion.setNombre(this.tipoValidacion.getNombre().trim().toUpperCase());
            if (this.tipoValidacion.getCodigo() != null) {
                this.tipoValidacionServicio.actualizar(this.tipoValidacion);
            } else {
                this.tipoValidacionServicio.insertar(this.tipoValidacion);
                this.lista.add(tipoValidacion);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoTipoValidacion').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_VALIDACION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoValidacion> getLista() {
        return lista;
    }

    public void setLista(List<TipoValidacion> lista) {
        this.lista = lista;
    }

    public TipoValidacion getTipoValidacion() {
        return tipoValidacion;
    }

    public void setTipoValidacion(TipoValidacion tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }
}
