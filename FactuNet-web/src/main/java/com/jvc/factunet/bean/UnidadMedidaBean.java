package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.UnidadMedida;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.UnidadMedidaServicio;
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
public class UnidadMedidaBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(UnidadMedidaBean.class.getName());

    @EJB
    private UnidadMedidaServicio unidadmedidaServicio;
    private List<UnidadMedida> lista;
    private UnidadMedida unidadmedida;
    private String buscarUnidadMedida;
    
    public UnidadMedidaBean() {
       this.lista=new ArrayList<>();
       this.unidadmedida=new UnidadMedida();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.unidadmedidaServicio.listar());
        this.buscarUnidadMedida = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.unidadmedida = new UnidadMedida();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.unidadmedidaServicio.listarNombre(this.buscarUnidadMedida));
        this.buscarUnidadMedida = StringUtils.EMPTY;
    }
    
    public void seleccionar(UnidadMedida parametro) {
        this.unidadmedida = parametro;
    }
    
    public void eliminar(UnidadMedida parametro) {
        try {
            this.unidadmedidaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.unidadmedida.setNombre(this.unidadmedida.getNombre().trim().toUpperCase());
            if (this.unidadmedida.getCodigo() != null) {
                this.unidadmedidaServicio.actualizar(this.unidadmedida);
            } else {
                this.unidadmedidaServicio.insertar(this.unidadmedida);
                this.lista.add(unidadmedida);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog012').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_UNIDAD_MEDIDA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public UnidadMedidaServicio getUnidadmedidaServicio() {
        return unidadmedidaServicio;
    }

    public void setUnidadmedidaServicio(UnidadMedidaServicio unidadmedidaServicio) {
        this.unidadmedidaServicio = unidadmedidaServicio;
    }

    public List<UnidadMedida> getLista() {
        return lista;
    }

    public void setLista(List<UnidadMedida> lista) {
        this.lista = lista;
    }

    public UnidadMedida getUnidadmedida() {
        return unidadmedida;
    }

    public void setUnidadmedida(UnidadMedida unidadmedida) {
        this.unidadmedida = unidadmedida;
    }

    public String getBuscarUnidadMedida() {
        return buscarUnidadMedida;
    }

    public void setBuscarUnidadMedida(String buscarUnidadMedida) {
        this.buscarUnidadMedida = buscarUnidadMedida;
    }
}
