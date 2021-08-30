package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.EstadoCivil;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.EstadoCivilServicio;
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
public class EstadoCivilBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(EstadoCivilBean.class.getName());
    
    @EJB
    private EstadoCivilServicio estadocivilServicio;
    
    private List<EstadoCivil> lista;
    private EstadoCivil estadocivil;
    private String buscarestadocivil;
    
    public EstadoCivilBean() {
        this.lista = new ArrayList<>();
        this.estadocivil = new EstadoCivil();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.estadocivilServicio.listar());
        this.buscarestadocivil = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.estadocivil = new EstadoCivil();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.estadocivilServicio.listarNombre(this.buscarestadocivil));
        this.buscarestadocivil = StringUtils.EMPTY;
    }
    
    public void seleccionar(EstadoCivil parametro) {
        this.estadocivil = parametro;
    }
  
    public void eliminar(EstadoCivil parametro) {
        try {
            this.estadocivilServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.estadocivil.setNombre(this.estadocivil.getNombre().trim().toUpperCase());
            if (this.estadocivil.getCodigo() != null) {
                this.estadocivilServicio.actualizar(this.estadocivil);
            } else {
                this.estadocivilServicio.insertar(this.estadocivil);
                this.lista.add(estadocivil);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoEstadoCivil').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_ESTADO_CIVIL,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<EstadoCivil> getLista() {
        return lista;
    }

    public void setLista(List<EstadoCivil> lista) {
        this.lista = lista;
    }

    public EstadoCivil getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(EstadoCivil estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getBuscarestadocivil() {
        return buscarestadocivil;
    }

    public void setBuscarestadocivil(String buscarestadocivil) {
        this.buscarestadocivil = buscarestadocivil;
    }
    
}
