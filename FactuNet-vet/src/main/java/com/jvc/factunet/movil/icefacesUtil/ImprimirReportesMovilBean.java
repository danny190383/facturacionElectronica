package com.jvc.factunet.movil.icefacesUtil;

import com.jvc.factunet.entidades.Reportes;
import com.jvc.factunet.servicios.ReportesServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;

@LocalBean
public class ImprimirReportesMovilBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ImprimirReportesMovilBean.class.getName());
    
    @EJB
    public ReportesServicio reportesServicio;
    
    public Map<String, Object> parametros = new HashMap<String, Object>();
    private List<Reportes> listaReportes;
    private String reporteSeleccionado;

    public ImprimirReportesMovilBean() {
        this.listaReportes = new ArrayList<>();
    }
    
    public void generarReportePDF(Integer documento) {
        this.generarReporte(JasperReportUtilMovil.TIPO_PDF, documento);
    }
    
    public void generarReporteRetencionPDF(Integer documento) {
        this.generarReporteRetencion(JasperReportUtilMovil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF() {
        this.generarReporte(JasperReportUtilMovil.TIPO_PDF);
    }
    
    public void generarReportePDFPrint() {
        this.generarReportePrint(JasperReportUtilMovil.TIPO_PDF);
    }

    public void generarReporteXlS() {
        this.generarReporte(JasperReportUtilMovil.TIPO_XLS);
    }

    public void generarReporteHTML() {
        this.generarReporte(JasperReportUtilMovil.TIPO_HTML);
    }
    
    public void generarReportePrint(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        
    }
    
    public List<Reportes> getListaReportes() {
        return listaReportes;
    }

    public void setListaReportes(List<Reportes> listaReportes) {
        this.listaReportes = listaReportes;
    }

    public String getReporteSeleccionado() {
        return reporteSeleccionado;
    }

    public void setReporteSeleccionado(String reporteSeleccionado) {
        this.reporteSeleccionado = reporteSeleccionado;
    }
}
