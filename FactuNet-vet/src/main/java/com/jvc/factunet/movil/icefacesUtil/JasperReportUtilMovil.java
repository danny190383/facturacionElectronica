package com.jvc.factunet.movil.icefacesUtil;

import com.jvc.factunet.entidades.FacturaDetalle;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

@ManagedBean
@ApplicationScoped
public class JasperReportUtilMovil extends FacesUtilsMovil {
    private static final Logger LOG = Logger.getLogger(JasperReportUtilMovil.class.getName());
    
    public final static String NOMBRE_BEAN;
    public final static String TIPO_PDF;
    public final static String TIPO_XLS;
    public final static String TIPO_HTML;
    public final static String PATH;
    
    public final static String PATH_REPORTE_PEDIDO_BEAN;
    
    @Resource(name = "factunetDS")
    private DataSource factunetDS;
    
    static {
        NOMBRE_BEAN = "jasperReportUtilMovil";
        TIPO_PDF = "application/pdf";
        TIPO_XLS = "application/vnd.ms-excel";
        TIPO_HTML = "text/html";
        PATH = getServletContext().getRealPath("jrxmlMovil") + File.separator;
        
        PATH_REPORTE_PEDIDO_BEAN = PATH + "pedidoTablet.jasper";
       
    }
    
    public Boolean jasperReportPrint(final String urlReporte, String tipo, String impresora, Map<String, Object> params) throws ClassNotFoundException {
        return generarReporte(urlReporte, tipo, params, impresora);
    }
    
    public Boolean jasperReportPrintBean(final String urlReporte,Map<String, Object> params, List<FacturaDetalle> lista, String impresora) throws ClassNotFoundException, IOException {
        return generarReporteBean(urlReporte, params, lista, impresora);
    }
    
    public Boolean generarReporteBean(final String urlReporte,Map<String, Object> params, List<FacturaDetalle> lista,String impresora) throws ClassNotFoundException, IOException {
        try {
            InputStream inputStream = new FileInputStream(urlReporte);
            if (inputStream == null) {
                throw new ClassNotFoundException("Archivo " + urlReporte + " no se encontro");
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream,params,new JRBeanCollectionDataSource(lista));
            if(this.PrintReportToPrinter2(jasperPrint, impresora)){
                    return Boolean.TRUE;
            }
        } catch (FileNotFoundException | ClassNotFoundException | JRException  ex) {
            LOG.log(Level.SEVERE, "Error al ejecutar la sentecia sql del reporte. ", ex);
        }
        return Boolean.FALSE;
    }

    public Boolean generarReporte(final String urlReporte, String tipo, Map<String, Object> params, String impresora){
     
        try {
//            ExternalContext econtext = FacesUtilsMovil.getExternalContext();
//            FacesContext fcontext = super.getFacesContext();
            Connection conn = this.factunetDS.getConnection();
            try {
//                JRExporter exporter = null;
                InputStream inputStream = new FileInputStream(urlReporte);
                if (inputStream == null) {
                    throw new ClassNotFoundException("Archivo " + urlReporte + " no se encontro");
                }
                JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, conn);
//                HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
//                response.setContentType(tipo);
                if ("application/pdf".equals(tipo)) {
//                    exporter = new JRPdfExporter();
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                    if(this.PrintReportToPrinter2(jasperPrint, impresora)){
//                        fcontext.responseComplete();
                        conn.close();
                        return Boolean.TRUE;
                    }
                }
//                fcontext.responseComplete();
                conn.close();
                return Boolean.FALSE;
            } catch (Exception ex) {
                conn.close();
                LOG.log(Level.SEVERE, "Error al generar el reporte. ", ex);
                return Boolean.FALSE;
            } 
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al ejecutar la sentecia sql del reporte. ", ex);
            return Boolean.FALSE;
        }
    }
    
    public Boolean PrintReportToPrinter2(JasperPrint jp, String impresora) throws JRException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
        printerJob.defaultPage(pageFormat);
        int selectedService = 0;
        AttributeSet attributeSet = new HashPrintServiceAttributeSet(new PrinterName(impresora, null));
        PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, attributeSet);
        try {
            printerJob.setPrintService(printService[selectedService]);
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error de driver. ");
            return Boolean.FALSE;
        }
        JRPrintServiceExporter exporter;
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
        printRequestAttributeSet.add(new Copies(1));
        exporter = new JRPrintServiceExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService[selectedService]);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService[selectedService].getAttributes());
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        try {
            exporter.exportReport();
            return Boolean.TRUE;
        } catch (JRException ex) {
            LOG.log(Level.SEVERE, "Error de driver. ");
            return Boolean.FALSE;
        }
    }
    
    public Boolean PrintReportToPrinter(JasperPrint jp, String impresora) {
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(new Copies(1));
        PrinterName printerName = new PrinterName(impresora, null); 
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(printerName);
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        try {
            exporter.exportReport();
            return Boolean.TRUE;
        } catch (JRException ex) {
            LOG.log(Level.SEVERE, "Error de driver. ");
            return Boolean.FALSE;
        }
    }
}