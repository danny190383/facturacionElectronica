package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoTarjeta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoTarjetaServicio;
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
public class TipoTarjetaBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(TipoTarjetaBean.class.getName());
    
    @EJB
    private TipoTarjetaServicio tipoTarjetaServicio;
    
    private List<TipoTarjeta> lista;
    private TipoTarjeta tipoTarjeta;
    private String buscarTipoTarjeta;
    
    public TipoTarjetaBean() {
       this.lista=new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoTarjetaServicio.listar());
        this.buscarTipoTarjeta = StringUtils.EMPTY;
        this.nuevo();
    }
    
    public void nuevo() {
        this.tipoTarjeta = new TipoTarjeta();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoTarjetaServicio.listarNombre(this.buscarTipoTarjeta));
        this.buscarTipoTarjeta = StringUtils.EMPTY;
    }
    
    
    public void seleccionar(TipoTarjeta parametro) {
        this.tipoTarjeta = parametro;
    }
    
    
    public void eliminar(TipoTarjeta parametro) {
        try {
            this.tipoTarjetaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipoTarjeta.setNombre(this.tipoTarjeta.getNombre().trim().toUpperCase());
            if (this.tipoTarjeta.getCodigo() != null) {
                this.tipoTarjetaServicio.actualizar(this.tipoTarjeta);
            } else {
                this.tipoTarjetaServicio.insertar(this.tipoTarjeta);
                this.lista.add(tipoTarjeta);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog008').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_TARJETA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoTarjeta> getLista() {
        return lista;
    }

    public void setLista(List<TipoTarjeta> lista) {
        this.lista = lista;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getBuscarTipoTarjeta() {
        return buscarTipoTarjeta;
    }

    public void setBuscarTipoTarjeta(String buscarTipoTarjeta) {
        this.buscarTipoTarjeta = buscarTipoTarjeta;
    }
}
