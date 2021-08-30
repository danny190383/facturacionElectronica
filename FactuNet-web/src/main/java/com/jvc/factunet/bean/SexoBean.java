package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Sexo;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.SexoServicio;
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
public class SexoBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(SexoBean.class.getName());
    
    @EJB
    private SexoServicio sexoServicio;
    
    private List<Sexo> lista;
    private Sexo sexo;
    private String buscarSexo;
    
    public SexoBean() {
        this.lista = new ArrayList<>();
        this.sexo = new Sexo();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.sexoServicio.listar());
        this.buscarSexo = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.sexo = new Sexo();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.sexoServicio.listarNombre(this.buscarSexo));
        this.buscarSexo = StringUtils.EMPTY;
    }
    
    public void seleccionar(Sexo parametro) {
        this.sexo = parametro;
    }
  
    public void eliminar(Sexo parametro) {
        try {
            this.sexoServicio.eliminar(parametro);
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
                this.sexoServicio.actualizar(this.sexo);
            } else {
                this.sexoServicio.insertar(this.sexo);
                this.lista.add(sexo);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog005').hide();");
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

    public List<Sexo> getLista() {
        return lista;
    }

    public void setLista(List<Sexo> lista) {
        this.lista = lista;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getBuscarSexo() {
        return buscarSexo;
    }

    public void setBuscarSexo(String buscarSexo) {
        this.buscarSexo = buscarSexo;
    }
}
