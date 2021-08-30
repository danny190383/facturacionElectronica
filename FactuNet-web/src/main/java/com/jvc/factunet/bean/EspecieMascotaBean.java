package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.EspecieMascota;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.EspecieMascotaServicio;
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
public class EspecieMascotaBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(EspecieMascotaBean.class.getName());
    
    @EJB
    private EspecieMascotaServicio especieMascotaServicio;
    
    private List<EspecieMascota> lista;
    private EspecieMascota especie;
    private String buscarEspecieMascota;
    
    public EspecieMascotaBean() {
        this.lista = new ArrayList<>();
        this.especie = new EspecieMascota();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.especieMascotaServicio.listar());
        this.buscarEspecieMascota = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.especie = new EspecieMascota();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.especieMascotaServicio.listarNombre(this.buscarEspecieMascota));
        this.buscarEspecieMascota = StringUtils.EMPTY;
    }
    
    public void seleccionar(EspecieMascota parametro) {
        this.especie = parametro;
    }
  
    public void eliminar(EspecieMascota parametro) {
        try {
            this.especieMascotaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.especie.setNombre(this.especie.getNombre().trim().toUpperCase());
            if (this.especie.getCodigo() != null) {
                this.especieMascotaServicio.actualizar(this.especie);
            } else {
                this.especieMascotaServicio.insertar(this.especie);
                this.lista.add(especie);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog003').hide();");
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

    public List<EspecieMascota> getLista() {
        return lista;
    }

    public void setLista(List<EspecieMascota> lista) {
        this.lista = lista;
    }

    public EspecieMascota getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieMascota especie) {
        this.especie = especie;
    }

    public String getBuscarEspecieMascota() {
        return buscarEspecieMascota;
    }

    public void setBuscarEspecieMascota(String buscarEspecieMascota) {
        this.buscarEspecieMascota = buscarEspecieMascota;
    }
}
