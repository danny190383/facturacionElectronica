package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.ProveedorServicio;
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
public class BuscarProveedoresBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarProveedoresBean.class.getName());

    @EJB
    private ProveedorServicio proveedorServicio;

    private LazyDataModel<Proveedor> lazyModel;
    private final Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
   
    public BuscarProveedoresBean() {
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Proveedor>()
            {
                @Override
                public List<Proveedor> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
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
                            if(meta.getFilterField().equals("persona.ciudad.nombre"))
                            {ciudad=(String)meta.getFilterValue();}
                        }
                        List<Proveedor> result = proveedorServicio.listarBuscar(nombre,ciudad,ruc,empresa, pageSize, first);
                        lazyModel.setRowCount(proveedorServicio.contar(nombre,ciudad,ruc,empresa).intValue());
                        return result;
                    }
                    else
                    {
                        List<Proveedor> result = proveedorServicio.listar(empresa, pageSize, first);
                        lazyModel.setRowCount(proveedorServicio.contar(empresa).intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Proveedor event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void eliminar(Proveedor parametro) {
        try {
            this.proveedorServicio.eliminar(parametro);
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
            super.getParametros().put("tipo", 2);
            super.getParametros().put("nombreReporte", "Ficha Proveedor");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_INVENTARIO_PROVEEDORES,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public LazyDataModel<Proveedor> getLazyModel() {
        return lazyModel;
    }
    
}
