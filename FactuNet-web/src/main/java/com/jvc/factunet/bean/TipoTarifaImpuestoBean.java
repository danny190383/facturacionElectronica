package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Impuesto;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TipoImpuestoServicio;
import com.jvc.factunet.servicios.TipoTarifaImpuestoServicio;
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

@Named(value = "tipoTarifaImpuestoBean")
@ViewScoped
public class TipoTarifaImpuestoBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(TipoTarifaImpuestoBean.class.getName());
    
    @EJB
    private TipoTarifaImpuestoServicio tipoTarifaImpuestoServicio;
    @EJB
    private TipoImpuestoServicio tipoImpuestoServicio;
    
    private List<ImpuestoTarifa> lista;
    private ImpuestoTarifa impuestoTarifa;
    private String buscarImpuesto;
    private List<Impuesto> listaImpuestos;
    
    public TipoTarifaImpuestoBean() {
       this.lista=new ArrayList<>();
       this.listaImpuestos = new ArrayList<>();
       this.impuestoTarifa=new ImpuestoTarifa();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaImpuestos.addAll(this.tipoImpuestoServicio.listar());
        this.lista.clear();
        this.lista.addAll(this.tipoTarifaImpuestoServicio.listar());
        this.buscarImpuesto = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.impuestoTarifa = new ImpuestoTarifa();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.tipoTarifaImpuestoServicio.listarNombre(this.buscarImpuesto));
        this.buscarImpuesto = StringUtils.EMPTY;
    }
    
    public void seleccionar(ImpuestoTarifa parametro) {
        this.impuestoTarifa = parametro;
    }
    
    public void eliminar(ImpuestoTarifa parametro) {
        try {
            this.tipoTarifaImpuestoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.impuestoTarifa.setDescripcion(this.impuestoTarifa.getDescripcion().trim().toUpperCase());
            if (this.impuestoTarifa.getId() != null) {
                this.tipoTarifaImpuestoServicio.actualizar(this.impuestoTarifa);
            } else {
                this.tipoTarifaImpuestoServicio.insertar(this.impuestoTarifa);
                this.lista.add(impuestoTarifa);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoImpuestoTarifa').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_TIPO_TARIFA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<ImpuestoTarifa> getLista() {
        return lista;
    }

    public void setLista(List<ImpuestoTarifa> lista) {
        this.lista = lista;
    }

    public ImpuestoTarifa getImpuestoTarifa() {
        return impuestoTarifa;
    }

    public void setImpuestoTarifa(ImpuestoTarifa impuestoTarifa) {
        this.impuestoTarifa = impuestoTarifa;
    }

    public String getBuscarImpuesto() {
        return buscarImpuesto;
    }

    public void setBuscarImpuesto(String buscarImpuesto) {
        this.buscarImpuesto = buscarImpuesto;
    }

    public List<Impuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<Impuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }
}
