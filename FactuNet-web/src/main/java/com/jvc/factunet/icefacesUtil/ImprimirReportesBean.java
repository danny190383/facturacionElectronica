package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.Reportes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class ImprimirReportesBean{
    
    
    private Map<String, Object> parametros;
    private List<Reportes> listaReportes;
    private String reporteSeleccionado;

    public ImprimirReportesBean() {
        this.parametros = new HashMap();
        this.listaReportes = new ArrayList<>();
    }
    
    public void generarReportePDF(Factura documento) {
        this.generarReporte(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF(Integer documento) {
        this.generarReporte(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF(Integer documento, Integer tipoDocumento) {
        this.generarReporte(JasperReportUtil.TIPO_PDF, documento, tipoDocumento);
    }
    
    public void generarReporteRetencionPDF(Integer documento) {
        this.generarReporteRetencion(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReporteCuentasPDF(Integer documento) {
        this.generarReporteCuentas(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReporteCuentasPDF(FacturaPago documento) {
        this.generarReporteCuentas(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReporteAbonoPDF(Integer documento) {
        this.generarReporteAbono(JasperReportUtil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF() {
        this.generarReporte(JasperReportUtil.TIPO_PDF);
    }
    
    public void generarReportePDFPrint() {
        this.generarReportePrint(JasperReportUtil.TIPO_PDF);
    }

    public void generarReporteXlS() {
        this.generarReporte(JasperReportUtil.TIPO_XLS);
    }

    public void generarReporteHTML() {
        this.generarReporte(JasperReportUtil.TIPO_HTML);
    }
    
    public void generarReportePrint(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporte(String tipoReporte, Factura documento) {
        
    }
    
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        
    }
    
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporteCuentas(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporteCuentas(String tipoReporte, FacturaPago documento) {
        
    }
    
    public void generarReporteAbono(String tipoReporte, Integer documento) {
        
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

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }
    
}
