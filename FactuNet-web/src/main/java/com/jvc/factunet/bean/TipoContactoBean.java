package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoContacto;
import com.jvc.factunet.entidades.TipoValidacion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoContactoServicio;
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
public class TipoContactoBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(TipoContactoBean.class.getName());
    
    @EJB
    private TipoContactoServicio tipoContactoServicio;
    @EJB
    private TipoValidacionServicio tipoValidacionServicio;
    
    private List<TipoContacto> lista;
    private TipoContacto tipoContacto;
    private String buscar;
    private List<TipoValidacion> listaValidacion;

    public TipoContactoBean() {
        this.lista=new ArrayList<>();
        this.listaValidacion=new ArrayList<>();
        this.tipoContacto = new TipoContacto();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoContactoServicio.listar());
        this.listaValidacion.addAll(this.tipoValidacionServicio.listar());
        this.buscar = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tipoContacto = new TipoContacto();
        this.tipoContacto.setTipoValidacion(this.listaValidacion.get(0));
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoContactoServicio.listarNombre(this.buscar));
        this.buscar = StringUtils.EMPTY;
    }
    
    public void seleccionar(TipoContacto parametro) {
        this.tipoContacto = parametro;
    }
    
    public void eliminar(TipoContacto parametro) {
        try {
            this.tipoContactoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipoContacto.setNombre(this.tipoContacto.getNombre().trim().toUpperCase());
            if (this.tipoContacto.getCodigo() != null) {
                this.tipoContactoServicio.actualizar(this.tipoContacto);
            } else {
                this.tipoContactoServicio.insertar(this.tipoContacto);
                this.lista.add(tipoContacto);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoTipoContacto').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_CONTACTO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoContacto> getLista() {
        return lista;
    }

    public void setLista(List<TipoContacto> lista) {
        this.lista = lista;
    }

    public TipoContacto getTipoContacto() {
        return tipoContacto;
    }

    public void setTipoContacto(TipoContacto tipoContacto) {
        this.tipoContacto = tipoContacto;
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
