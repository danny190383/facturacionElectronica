package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoDocumento;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoDocumentoServicio;
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
public class TipoDocumentoBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(TipoDocumentoBean.class.getName());
    
    @EJB
    private TipoDocumentoServicio tipodocumentoServicio;
    
    private List<TipoDocumento> lista;
    private TipoDocumento tipodocumento;
    private String buscarTipoDocumento;
    
    public TipoDocumentoBean() {
       this.lista=new ArrayList<>();
       this.tipodocumento=new TipoDocumento();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipodocumentoServicio.listar());
        this.buscarTipoDocumento = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.tipodocumento = new TipoDocumento();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipodocumentoServicio.listarNombre(this.buscarTipoDocumento));
        this.buscarTipoDocumento = StringUtils.EMPTY;
    }
    
    public void seleccionar(TipoDocumento parametro) {
        this.tipodocumento = parametro;
    }
    
    public void eliminar(TipoDocumento parametro) {
        try {
            this.tipodocumentoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.tipodocumento.setNombre(this.tipodocumento.getNombre().trim().toUpperCase());
            if (this.tipodocumento.getCodigo() != null) {
                this.tipodocumentoServicio.actualizar(this.tipodocumento);
            } else {
                this.tipodocumentoServicio.insertar(this.tipodocumento);
                this.lista.add(tipodocumento);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoTipoDocumento').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_DOCUMENTO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoDocumento> getLista() {
        return lista;
    }

    public void setLista(List<TipoDocumento> lista) {
        this.lista = lista;
    }

    public TipoDocumento getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(TipoDocumento tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getBuscarTipoDocumento() {
        return buscarTipoDocumento;
    }

    public void setBuscarTipoDocumento(String buscarTipoDocumento) {
        this.buscarTipoDocumento = buscarTipoDocumento;
    }
}
