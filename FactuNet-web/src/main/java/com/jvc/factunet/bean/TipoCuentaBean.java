package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoCuenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoCuentaServicio;
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
public class TipoCuentaBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(TipoCuentaBean.class.getName());
    
    @EJB
    private TipoCuentaServicio tipoCuentaServicio;
    
    private List<TipoCuenta> lista;
    private TipoCuenta marca;
    private String buscarTipoCuenta;
    
    public TipoCuentaBean() {
       this.lista=new ArrayList<>();
       this.marca=new TipoCuenta();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.tipoCuentaServicio.listar());
        this.buscarTipoCuenta = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.marca = new TipoCuenta();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoCuentaServicio.listarNombre(this.buscarTipoCuenta));
        this.buscarTipoCuenta = StringUtils.EMPTY;
    }
    
    
    public void seleccionar(TipoCuenta parametro) {
        this.marca = parametro;
    }
    
    
    public void eliminar(TipoCuenta parametro) {
        try {
            this.tipoCuentaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.marca.setNombre(this.marca.getNombre().trim().toUpperCase());
            if (this.marca.getCodigo() != null) {
                this.tipoCuentaServicio.actualizar(this.marca);
            } else {
                this.tipoCuentaServicio.insertar(this.marca);
                this.lista.add(marca);
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
            super.getParametros().put("empresa", ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_CUENTA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoCuenta> getLista() {
        return lista;
    }

    public void setLista(List<TipoCuenta> lista) {
        this.lista = lista;
    }

    public TipoCuenta getTipoCuenta() {
        return marca;
    }

    public void setTipoCuenta(TipoCuenta marca) {
        this.marca = marca;
    }

    public String getBuscarTipoCuenta() {
        return buscarTipoCuenta;
    }

    public void setBuscarTipoCuenta(String buscarTipoCuenta) {
        this.buscarTipoCuenta = buscarTipoCuenta;
    }
    
}
