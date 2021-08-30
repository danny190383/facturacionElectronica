package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.SexoMascota;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.SexoMascotaServicio;
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
public class SexoMascotaBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(SexoMascotaBean.class.getName());
    
    @EJB
    private SexoMascotaServicio sexoMascotaServicio;
    
    private List<SexoMascota> lista;
    private SexoMascota sexo;
    private String buscarSexoMascota;
    
    public SexoMascotaBean() {
        this.lista = new ArrayList<>();
        this.sexo = new SexoMascota();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.sexoMascotaServicio.listar());
        this.buscarSexoMascota = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.sexo = new SexoMascota();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.sexoMascotaServicio.listarNombre(this.buscarSexoMascota));
        this.buscarSexoMascota = StringUtils.EMPTY;
    }
    
    public void seleccionar(SexoMascota parametro) {
        this.sexo = parametro;
    }
  
    public void eliminar(SexoMascota parametro) {
        try {
            this.sexoMascotaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.sexo.setNombre(this.sexo.getNombre().trim().toUpperCase());
            if (this.sexo.getCodigo() != null) {
                this.sexoMascotaServicio.actualizar(this.sexo);
            } else {
                this.sexoMascotaServicio.insertar(this.sexo);
                this.lista.add(sexo);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog001').hide();");
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

    public List<SexoMascota> getLista() {
        return lista;
    }

    public void setLista(List<SexoMascota> lista) {
        this.lista = lista;
    }

    public SexoMascota getSexo() {
        return sexo;
    }

    public void setSexo(SexoMascota sexo) {
        this.sexo = sexo;
    }

    public String getBuscarSexoMascota() {
        return buscarSexoMascota;
    }

    public void setBuscarSexoMascota(String buscarSexoMascota) {
        this.buscarSexoMascota = buscarSexoMascota;
    }
}
