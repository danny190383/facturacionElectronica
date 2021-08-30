package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Motivo;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.MotivoServicio;
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
public class MotivoBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(MotivoBean.class.getName());
    
    @EJB
    private MotivoServicio motivoServicio;
    
    private List<Motivo> lista;
    private Motivo motivo;
    private String buscarMotivo;
    
    public MotivoBean() {
        this.lista = new ArrayList<>();
        this.motivo = new Motivo();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.motivoServicio.listar());
        this.buscarMotivo = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.motivo = new Motivo();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.motivoServicio.listarNombre(this.buscarMotivo));
        this.buscarMotivo = StringUtils.EMPTY;
    }
    
    public void seleccionar(Motivo parametro) {
        this.motivo = parametro;
    }
  
    public void eliminar(Motivo parametro) {
        try {
            this.motivoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.motivo.setNombre(this.motivo.getNombre().trim().toUpperCase());
            if (this.motivo.getCodigo() != null) {
                this.motivoServicio.actualizar(this.motivo);
            } else {
                this.motivoServicio.insertar(this.motivo);
                this.lista.add(motivo);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog0005').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_MOTIVO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Motivo> getLista() {
        return lista;
    }

    public void setLista(List<Motivo> lista) {
        this.lista = lista;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public String getBuscarMotivo() {
        return buscarMotivo;
    }

    public void setBuscarMotivo(String buscarMotivo) {
        this.buscarMotivo = buscarMotivo;
    }
}
