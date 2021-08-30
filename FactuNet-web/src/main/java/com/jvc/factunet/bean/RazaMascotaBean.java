package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.RazaMascota;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.RazaMascotaServicio;
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
public class RazaMascotaBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(RazaMascotaBean.class.getName());
    
    @EJB
    private RazaMascotaServicio razaMascotaServicio;
    
    private List<RazaMascota> lista;
    private RazaMascota raza;
    private String buscarRazaMascota;
    
    public RazaMascotaBean() {
        this.lista = new ArrayList<>();
        this.raza = new RazaMascota();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.razaMascotaServicio.listar());
        this.buscarRazaMascota = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.raza = new RazaMascota();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.razaMascotaServicio.listarNombre(this.buscarRazaMascota));
        this.buscarRazaMascota = StringUtils.EMPTY;
    }
    
    public void seleccionar(RazaMascota parametro) {
        this.raza = parametro;
    }
  
    public void eliminar(RazaMascota parametro) {
        try {
            this.razaMascotaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.raza.setNombre(this.raza.getNombre().trim().toUpperCase());
            if (this.raza.getCodigo() != null) {
                this.razaMascotaServicio.actualizar(this.raza);
            } else {
                this.razaMascotaServicio.insertar(this.raza);
                this.lista.add(raza);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog002').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_SEXO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<RazaMascota> getLista() {
        return lista;
    }

    public void setLista(List<RazaMascota> lista) {
        this.lista = lista;
    }

    public RazaMascota getRaza() {
        return raza;
    }

    public void setRaza(RazaMascota raza) {
        this.raza = raza;
    }

    public String getBuscarRazaMascota() {
        return buscarRazaMascota;
    }

    public void setBuscarRazaMascota(String buscarRazaMascota) {
        this.buscarRazaMascota = buscarRazaMascota;
    }
}
