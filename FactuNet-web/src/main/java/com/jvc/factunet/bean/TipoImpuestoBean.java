package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Impuesto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoImpuestoServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@Named(value = "tipoImpuestoBean")
@ViewScoped
public class TipoImpuestoBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(TipoImpuestoBean.class.getName());
    
    @EJB
    private TipoImpuestoServicio tipoImpuestoServicio;
    
    private List<Impuesto> lista;
    private Impuesto impuesto;
    private String buscarImpuesto;
    
    public TipoImpuestoBean() {
       this.lista=new ArrayList<>();
       this.impuesto=new Impuesto();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoImpuestoServicio.listar());
        this.buscarImpuesto = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.impuesto = new Impuesto();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoImpuestoServicio.listarNombre(this.buscarImpuesto));
        this.buscarImpuesto = StringUtils.EMPTY;
    }
    
    public void seleccionar(Impuesto parametro) {
        this.impuesto = parametro;
    }
    
    public void eliminar(Impuesto parametro) {
        try {
            this.tipoImpuestoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.impuesto.setNombre(this.impuesto.getNombre().trim().toUpperCase());
            if (this.impuesto.getId() != null) {
                this.tipoImpuestoServicio.actualizar(this.impuesto);
            } else {
                this.tipoImpuestoServicio.insertar(this.impuesto);
                this.lista.add(impuesto);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoImpuesto').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_IMPUESTO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Impuesto> getLista() {
        return lista;
    }

    public void setLista(List<Impuesto> lista) {
        this.lista = lista;
    }

    public Impuesto getTipodocumento() {
        return impuesto;
    }

    public void setTipodocumento(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public String getBuscarImpuesto() {
        return buscarImpuesto;
    }

    public void setBuscarImpuesto(String buscarImpuesto) {
        this.buscarImpuesto = buscarImpuesto;
    }
}
