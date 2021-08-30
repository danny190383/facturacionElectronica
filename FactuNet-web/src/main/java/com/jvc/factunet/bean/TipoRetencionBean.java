package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoRetencion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoRetencionServicio;
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
public class TipoRetencionBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(TipoRetencionBean.class.getName());

    @EJB
    private TipoRetencionServicio tiporetencionServicio;
    
    private List<TipoRetencion> lista;
    private TipoRetencion tiporetencion;
    private String buscarTipoRetencion;
    private Boolean banActualizarInsertar;
    
    public TipoRetencionBean() {
       this.lista=new ArrayList<>();
       this.tiporetencion=new TipoRetencion();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tiporetencionServicio.listar());
        this.buscarTipoRetencion = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tiporetencion = new TipoRetencion();
        this.banActualizarInsertar = Boolean.TRUE;
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tiporetencionServicio.listarNombre(this.buscarTipoRetencion));
        this.buscarTipoRetencion = StringUtils.EMPTY;
    }
    
    
    public void seleccionar(TipoRetencion parametro) {
        this.tiporetencion = parametro;
        this.banActualizarInsertar = Boolean.FALSE;
    }
    
    
    public void eliminar(TipoRetencion parametro) {
        try {
            this.tiporetencionServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tiporetencion.setNombre(this.tiporetencion.getNombre().trim().toUpperCase());
            if (!this.banActualizarInsertar) {
                this.tiporetencionServicio.actualizar(this.tiporetencion);
            } else {
                this.tiporetencionServicio.insertar(this.tiporetencion);
                this.lista.add(tiporetencion);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoTipoRetencion').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_RETENCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public TipoRetencionServicio getTipoRetencionServicio() {
        return tiporetencionServicio;
    }

    public void setTiporetencionServicio(TipoRetencionServicio tiporetencionServicio) {
        this.tiporetencionServicio = tiporetencionServicio;
    }

    public List<TipoRetencion> getLista() {
        return lista;
    }

    public void setLista(List<TipoRetencion> lista) {
        this.lista = lista;
    }

    public TipoRetencion getTipoRetencion() {
        return tiporetencion;
    }

    public void setTiporetencion(TipoRetencion tiporetencion) {
        this.tiporetencion = tiporetencion;
    }

    public String getBuscarTipoRetencion() {
        return buscarTipoRetencion;
    }

    public void setBuscarTipoRetencion(String buscarTipoRetencion) {
        this.buscarTipoRetencion = buscarTipoRetencion;
    }

    
    
}
