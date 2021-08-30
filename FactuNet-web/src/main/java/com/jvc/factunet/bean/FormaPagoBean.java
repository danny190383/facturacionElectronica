package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.FormaPago;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.FormaPagoServicio;
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
public class FormaPagoBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(FormaPagoBean.class.getName());
    
    @EJB
    private FormaPagoServicio formaPagoServicio;
    
    private List<FormaPago> lista;
    private FormaPago formaPago;
    private String buscarFormaPago;
    
    public FormaPagoBean() {
       this.lista=new ArrayList<>();
       this.formaPago=new FormaPago();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.formaPagoServicio.listar());
        this.buscarFormaPago = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.formaPago = new FormaPago();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.formaPagoServicio.listarNombre(this.buscarFormaPago));
        this.buscarFormaPago = StringUtils.EMPTY;
    }
    
    
    public void seleccionar(FormaPago parametro) {
        this.formaPago = parametro;
    }
    
    
    public void eliminar(FormaPago parametro) {
        try {
            this.formaPagoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.formaPago.setNombre(this.formaPago.getNombre().trim().toUpperCase());
            if (this.formaPago.getCodigo() != null) {
                this.formaPagoServicio.actualizar(this.formaPago);
            } else {
                this.formaPagoServicio.insertar(this.formaPago);
                this.lista.add(formaPago);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog007').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_FORMAS_PAGO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<FormaPago> getLista() {
        return lista;
    }

    public void setLista(List<FormaPago> lista) {
        this.lista = lista;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getBuscarFormaPago() {
        return buscarFormaPago;
    }

    public void setBuscarFormaPago(String buscarFormaPago) {
        this.buscarFormaPago = buscarFormaPago;
    }
    
}
