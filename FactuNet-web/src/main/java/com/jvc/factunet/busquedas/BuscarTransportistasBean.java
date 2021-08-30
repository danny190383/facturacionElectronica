package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Transportista;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.TransportistaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarTransportistasBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarTransportistasBean.class.getName());

    @EJB
    private TransportistaServicio transportistaServicio;

    private LazyDataModel<Transportista> lazyModel;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
   
    public BuscarTransportistasBean() {
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Transportista>()
            {
                @Override
                public List<Transportista> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String ciudad = StringUtils.EMPTY;
                        String ruc = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {
                            if(meta.getFilterField().equals("persona.cedula"))
                            {ruc=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombre=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.apellidos"))
                            {ruc=(String)meta.getFilterValue();}
                        }
                        List<Transportista> result = transportistaServicio.listarBuscar(nombre,ciudad,ruc,empresa, pageSize, first);
                        lazyModel.setRowCount(transportistaServicio.contar(nombre,ciudad,ruc,empresa).intValue());
                        return result;
                    }
                    else
                    {
                        List<Transportista> result = transportistaServicio.listar(empresa, pageSize, first);
                        lazyModel.setRowCount(transportistaServicio.contar(empresa).intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Transportista event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void eliminar(Transportista parametro) {
        try {
            this.transportistaServicio.eliminar(parametro);
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer persona) {
        try {
            super.getParametros().put("empresa", this.empresa);
            super.getParametros().put("persona", persona);
            super.getParametros().put("tipo", 3);
            super.getParametros().put("nombreReporte", "Ficha Transportista");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", this.empresa);
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_INVENTARIO_TRANSPORTISTAS,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public LazyDataModel<Transportista> getLazyModel() {
        return lazyModel;
    }
    
}
