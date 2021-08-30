package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Ciudad;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.CiudadServicio;
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
public class CiudadBean extends ImprimirReportesBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(CiudadBean.class.getName());
    
    @EJB
    private CiudadServicio ciudadServicio;
    
    private List<Ciudad> lista;
    private Ciudad ciudad;
    private String buscarCiudad;
    
    public CiudadBean() {
        this.lista = new ArrayList<>();
        this.ciudad = new Ciudad();
    }
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.ciudadServicio.listar());
        this.buscarCiudad = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.ciudad = new Ciudad();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.ciudadServicio.listarNombre(this.buscarCiudad));
        this.buscarCiudad = StringUtils.EMPTY;
    }
    
    public void seleccionar(Ciudad parametro) {
        this.ciudad = parametro;
    }
  
    public void eliminar(Ciudad parametro) {
        try {
            this.ciudadServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.ciudad.setNombre(this.ciudad.getNombre().trim().toUpperCase());
            if (this.ciudad.getCodigo() != null) {
                this.ciudadServicio.actualizar(this.ciudad);
            } else {
                this.ciudadServicio.insertar(this.ciudad);
                this.lista.add(ciudad);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoCiudad').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            this.init();
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPersona().getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_CIUDAD,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Ciudad> getLista() {
        return lista;
    }

    public void setLista(List<Ciudad> lista) {
        this.lista = lista;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getBuscarCiudad() {
        return buscarCiudad;
    }

    public void setBuscarCiudad(String buscarCiudad) {
        this.buscarCiudad = buscarCiudad;
    }
    
    

}
