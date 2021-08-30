package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.Reportes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.LocalBean;

@LocalBean
public class ImprimirReportesPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ImprimirReportesPedidosBean.class.getName());
    
    private Map<String, Object> parametros = new HashMap<String, Object>();
    private List<Reportes> listaReportes;
    private String reporteSeleccionado;

    public ImprimirReportesPedidosBean() {
        this.listaReportes = new ArrayList<>();
    }
    
    public void generarReportePDF(Integer documento) {
        this.generarReporte(JasperReporPedidostUtil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF(Integer documento, Integer tipoDocumento) {
        this.generarReporte(JasperReporPedidostUtil.TIPO_PDF, documento, tipoDocumento);
    }
    
    public void generarReporteRetencionPDF(Integer documento) {
        this.generarReporteRetencion(JasperReporPedidostUtil.TIPO_PDF, documento);
    }
    
    public void generarReporteCuentasPDF(Integer documento) {
        this.generarReporteCuentas(JasperReporPedidostUtil.TIPO_PDF, documento);
    }
    
    public void generarReporteAbonoPDF(Integer documento) {
        this.generarReporteAbono(JasperReporPedidostUtil.TIPO_PDF, documento);
    }
    
    public void generarReportePDF() {
        this.generarReporte(JasperReporPedidostUtil.TIPO_PDF);
    }
    
    public void generarReportePDFPrint() {
        this.generarReportePrint(JasperReporPedidostUtil.TIPO_PDF);
    }

    public void generarReporteXlS() {
        this.generarReporte(JasperReporPedidostUtil.TIPO_XLS);
    }

    public void generarReporteHTML() {
        this.generarReporte(JasperReporPedidostUtil.TIPO_HTML);
    }
    
    public void generarReportePrint(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte) {
        
    }
    
    public void generarReporte(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        
    }
    
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        
    }
    
    public void generarReporteCuentas(String tipoReporte, Integer documento) {
        
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
