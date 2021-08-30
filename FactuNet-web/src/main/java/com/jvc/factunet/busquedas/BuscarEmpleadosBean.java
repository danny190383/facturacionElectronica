package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.EmpleadoServicio;
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
public class BuscarEmpleadosBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarEmpleadosBean.class.getName());
    
    @EJB
    private EmpleadoServicio empleadoServicio;

    private LazyDataModel<Empleado> lazyModel;
    private final Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
   
    public BuscarEmpleadosBean() {
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Empleado>()
            {
                @Override
                public List<Empleado> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String apellidos = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {       
                            if(meta.getFilterField().equals("persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.apellidos"))
                            {apellidos=(String)meta.getFilterValue();}
                        }
                        List<Empleado> result;
                        if(empresa == 1){
                            result = empleadoServicio.listarBuscarTodos(nombres,apellidos,cedula, pageSize, first);
                            lazyModel.setRowCount(empleadoServicio.contarTodos(nombres,apellidos,cedula).intValue());
                        }
                        else
                        {
                            result = empleadoServicio.listarBuscar(nombres,apellidos,cedula,empresa, pageSize, first);
                            lazyModel.setRowCount(empleadoServicio.contar(nombres,apellidos,cedula,empresa).intValue());
                        }
                        return result;
                    }
                    else
                    {
                        List<Empleado> result;
                        if(empresa == 1){
                            result = empleadoServicio.listarTodos(pageSize, first);
                            lazyModel.setRowCount(empleadoServicio.contarTodos().intValue());
                        }
                        else
                        {
                             result = empleadoServicio.listar(empresa, pageSize, first);
                            lazyModel.setRowCount(empleadoServicio.contar(empresa).intValue());
                        }
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Empleado event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void eliminar(Empleado parametro) {
        try {
            this.empleadoServicio.eliminar(parametro);
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
            super.getParametros().put("tipo", 4);
            super.getParametros().put("nombreReporte", "Ficha Empleado");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_RECURSOS_EMPLEADOS,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public LazyDataModel<Empleado> getLazyModel() {
        return lazyModel;
    }
}
