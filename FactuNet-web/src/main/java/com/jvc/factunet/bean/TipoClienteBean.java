package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoCliente;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoClienteServicio;
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
public class TipoClienteBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(TipoClienteBean.class.getName());
    
    @EJB
    private TipoClienteServicio tipoclienteServicio;
    
    private List<TipoCliente> lista;
    private TipoCliente tipocliente;
    private String buscartipocliente;
    
    public TipoClienteBean() {
        this.lista = new ArrayList<>();
        this.tipocliente = new TipoCliente();  
    }
    
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoclienteServicio.listar());
        this.buscartipocliente = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tipocliente = new TipoCliente();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoclienteServicio.listarNombre(this.buscartipocliente));
        this.buscartipocliente = StringUtils.EMPTY;
    }
    
    public void seleccionar(TipoCliente parametro) {
        this.tipocliente = parametro;
    }
  
    public void eliminar(TipoCliente parametro) {
        try {
            this.tipoclienteServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipocliente.setNombre(this.tipocliente.getNombre().trim().toUpperCase());
            if (this.tipocliente.getCodigo() != null) {
                this.tipoclienteServicio.actualizar(this.tipocliente);
            } else {
                this.tipoclienteServicio.insertar(this.tipocliente);
                this.lista.add(tipocliente);
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_CLIENTE,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoCliente> getLista() {
        return lista;
    }

    public void setLista(List<TipoCliente> lista) {
        this.lista = lista;
    }

    public TipoCliente getTipocliente() {
        return tipocliente;
    }

    public void setTipocliente(TipoCliente tipocliente) {
        this.tipocliente = tipocliente;
    }

    public String getBuscartipocliente() {
        return buscartipocliente;
    }

    public void setBuscartipocliente(String buscartipocliente) {
        this.buscartipocliente = buscartipocliente;
    }
}
