package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.FacturaDetalle;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
//import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@ApplicationScoped
public class JasperReportUtil extends FacesUtils {
    private static final Logger LOG = Logger.getLogger(JasperReportUtil.class.getName());
    
    public final static String NOMBRE_BEAN;
    public final static String TIPO_PDF;
    public final static String TIPO_XLS;
    public final static String TIPO_HTML;
    public final static String PATH;
    
    //reportes generales
    public final static String PATH_SUBREPORTE_CABECERA;
    
    // reportes de inventario
    public final static String PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK;
    public final static String PATH_REPORTE_INVENTARIO_PROVEEDORES;
    public final static String PATH_REPORTE_INVENTARIO_TRANSPORTISTAS;
    public final static String PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK_TODOS;
    
    //reportes de rr-hh
    public final static String PATH_REPORTE_RECURSOS_EMPLEADOS;
    
    //reportes de mantenimiento
    public final static String PATH_REPORTE_LISTA_MARCAS;
    public final static String PATH_REPORTE_LISTA_UNIDAD_MEDIDA;
    public final static String PATH_REPORTE_LISTA_BANCOS;
    public final static String PATH_REPORTE_LISTA_TIPO_CUENTA;
    public final static String PATH_REPORTE_LISTA_FORMAS_PAGO;
    public final static String PATH_REPORTE_LISTA_TIPO_TARJETA;
    
    public final static String PATH_REPORTE_LISTA_SEXO;
    public final static String PATH_REPORTE_LISTA_CIUDAD;
    public final static String PATH_REPORTE_LISTA_ESTADO_CIVIL;
    public final static String PATH_REPORTE_LISTA_CARGOS;
    public final static String PATH_REPORTE_LISTA_TIPO_CLIENTE;
    public final static String PATH_REPORTE_LISTA_MOTIVO;
    
    public final static String PATH_REPORTE_LISTA_TIPO_DOCUMENTO;
    public final static String PATH_REPORTE_LISTA_TIPO_IDENTIFICACION;
    public final static String PATH_REPORTE_LISTA_TIPO_RETENCION;
    public final static String PATH_REPORTE_LISTA_TIPO_CONTACTO;
    public final static String PATH_REPORTE_LISTA_TIPO_VALIDACION;
    public final static String PATH_REPORTE_LISTA_TIPO_IMPUESTO;
    public final static String PATH_REPORTE_LISTA_TIPO_TARIFA;
    
    public final static String PATH_REPORTE_DOCUMENTO_TRANSACCION;
    public final static String PATH_REPORTE_DOCUMENTO_TRANSACCION_DARIO;
    public final static String PATH_REPORTE_DOCUMENTO_RETENCION;
    public final static String PATH_REPORTE_DOCUMENTO_CUENTA;
    public final static String PATH_REPORTE_DOCUMENTO_CUENTA_ABONO;
    public final static String PATH_REPORTE_DOCUMENTO_GUIA_REMISION;
    public final static String PATH_REPORTE_NOTA_ENTREGA;
    
    public final static String PATH_REPORTE_ARQUEO_CAJA_VENTAS;
    public final static String PATH_REPORTE_NOTAS_CREDITO;
    public final static String PATH_REPORTE_NOTAS_DEBITO;
    public final static String PATH_REPORTE_ARQUEO_CAJA_COMPRAS;
    public final static String PATH_REPORTE_ARQUEO_CAJA_RETENCIONES;
    public final static String PATH_REPORTE_ARQUEO_CAJA_RETENCIONES_COMPRAS;
    public final static String PATH_REPORTE_ARQUEO_CAJA_CUENTAS;
    public final static String PATH_REPORTE_ARQUEO_CAJA_PRODUCTOS;
    public final static String PATH_REPORTE_RETENCIONES_VENTAS;
    public final static String PATH_REPORTE_RETENCIONES_COMPRAS;
    
    public final static String PATH_REPORTE_CUENTAS_COBRAR;
    public final static String PATH_REPORTE_CUENTAS_PAGAR;
    public final static String PATH_REPORTE_TOTAL_CUENTAS_COBRAR;
    public final static String PATH_REPORTE_TOTAL_CUENTAS_PAGAR;
    
    public final static String PATH_REPORTE_FICHA_GENERAL_PERSONA;
    public final static String PATH_REPORTE_CLIENTES;
    public final static String PATH_REPORTE_CLIENTES_CUMPLEAÑOS;
    
    public final static String PATH_REPORTE_PEDIDO_BEAN;
    
    @Resource(name = "factunetDS")
    private DataSource factunetDS;
    
    static {
        NOMBRE_BEAN = "jasperReportUtil";
        TIPO_PDF = "application/pdf";
        TIPO_XLS = "application/vnd.ms-excel";
        TIPO_HTML = "text/html";
        PATH = getServletContext().getRealPath("jrxml") + File.separator;
        
        PATH_SUBREPORTE_CABECERA = PATH + "cabecera.jasper";
        //
        PATH_REPORTE_INVENTARIO_PROVEEDORES = PATH + "inventario-proveedores.jasper";
        PATH_REPORTE_INVENTARIO_TRANSPORTISTAS = PATH + "inventario-transportistas.jasper";
        PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK = PATH + "inventario-producto-stock.jasper";
        PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK_TODOS = PATH + "inventario-producto-stock-todos.jasper";
        
        PATH_REPORTE_RECURSOS_EMPLEADOS = PATH + "recursos_empleados.jasper";
        
        //REPORTES DE MANTENIMIENTO
        PATH_REPORTE_LISTA_MARCAS = PATH + "lista-marcas.jasper";
        PATH_REPORTE_LISTA_UNIDAD_MEDIDA = PATH + "lista-unidad-medida.jasper";
        PATH_REPORTE_LISTA_BANCOS = PATH + "lista-bancos.jasper";
        PATH_REPORTE_LISTA_TIPO_CUENTA = PATH + "lista-tipo-cuenta.jasper";
        PATH_REPORTE_LISTA_FORMAS_PAGO = PATH + "lista-formas-pago.jasper";
        PATH_REPORTE_LISTA_TIPO_TARJETA = PATH + "lista-tipo-tarjeta.jasper";
        
        PATH_REPORTE_LISTA_SEXO = PATH + "lista-sexo.jasper";
        PATH_REPORTE_LISTA_CIUDAD = PATH + "lista-ciudad.jasper";
        PATH_REPORTE_LISTA_ESTADO_CIVIL = PATH + "lista-estado-civil.jasper";
        PATH_REPORTE_LISTA_CARGOS = PATH + "lista-cargos.jasper";
        PATH_REPORTE_LISTA_TIPO_CLIENTE = PATH + "lista-tipo-cliente.jasper";
        PATH_REPORTE_LISTA_MOTIVO = PATH + "lista-motivo.jasper";
        
        PATH_REPORTE_LISTA_TIPO_DOCUMENTO = PATH + "lista-tipo-documento.jasper";
        PATH_REPORTE_LISTA_TIPO_IDENTIFICACION = PATH + "lista-tipo-identificacion.jasper";
        PATH_REPORTE_LISTA_TIPO_RETENCION = PATH + "lista-tipo-retencion.jasper";
        PATH_REPORTE_LISTA_TIPO_CONTACTO = PATH + "lista-tipo-contacto.jasper";
        PATH_REPORTE_LISTA_TIPO_VALIDACION = PATH + "lista-tipo-validacion.jasper";
        PATH_REPORTE_LISTA_TIPO_IMPUESTO = PATH + "lista-impuestos.jasper";
        PATH_REPORTE_LISTA_TIPO_TARIFA = PATH + "lista-tarifas.jasper";
        
        PATH_REPORTE_DOCUMENTO_TRANSACCION = PATH + "documento-transaccion.jasper";
        PATH_REPORTE_DOCUMENTO_TRANSACCION_DARIO = PATH + "documento-transaccion-dario.jasper";
        PATH_REPORTE_DOCUMENTO_RETENCION = PATH + "documento-retencion.jasper";
        PATH_REPORTE_DOCUMENTO_CUENTA = PATH + "documento-cuenta.jasper";
        PATH_REPORTE_DOCUMENTO_CUENTA_ABONO = PATH + "documento-cuenta_abono.jasper";
        PATH_REPORTE_DOCUMENTO_GUIA_REMISION = PATH + "documento-guia-remision.jasper";
        PATH_REPORTE_NOTA_ENTREGA = PATH + "documento-nota-entrega.jasper";
         
        PATH_REPORTE_ARQUEO_CAJA_VENTAS = PATH + "facturacion-arqueoCaja.jasper";
        PATH_REPORTE_NOTAS_CREDITO = PATH + "facturacion-notasCredito.jasper";
        PATH_REPORTE_NOTAS_DEBITO = PATH + "facturacion-notasDebito.jasper";
        PATH_REPORTE_ARQUEO_CAJA_COMPRAS = PATH + "facturacion-arqueoCajaCompras.jasper";
        PATH_REPORTE_ARQUEO_CAJA_RETENCIONES = PATH + "facturacion-arqueoRetenciones.jasper";
        PATH_REPORTE_ARQUEO_CAJA_RETENCIONES_COMPRAS = PATH + "facturacion-arqueoRetencionesCompras.jasper";
        PATH_REPORTE_ARQUEO_CAJA_CUENTAS = PATH + "facturacion-arqueoCuentas.jasper";
        PATH_REPORTE_ARQUEO_CAJA_PRODUCTOS = PATH + "facturacion-arqueoCajaProductos.jasper";
        PATH_REPORTE_RETENCIONES_VENTAS = PATH + "facturacion-retencionesVenta.jasper";
        PATH_REPORTE_RETENCIONES_COMPRAS = PATH + "facturacion-retencionesCompras.jasper";
        
        PATH_REPORTE_CUENTAS_COBRAR = PATH + "facturacion-cuentas-cobrar.jasper";
        PATH_REPORTE_CUENTAS_PAGAR = PATH + "facturacion-cuentas-pagar.jasper";
        PATH_REPORTE_TOTAL_CUENTAS_COBRAR = PATH + "facturacion-total-cuentas-cobrar.jasper";
        PATH_REPORTE_TOTAL_CUENTAS_PAGAR = PATH + "facturacion-total-cuentas-pagar.jasper";
        
        PATH_REPORTE_FICHA_GENERAL_PERSONA = PATH + "ficha-general-persona.jasper";
        PATH_REPORTE_CLIENTES = PATH + "ventas-clientes.jasper";
        PATH_REPORTE_CLIENTES_CUMPLEAÑOS = PATH + "ventas-clientes-nacimiento.jasper";
        
        PATH_REPORTE_PEDIDO_BEAN = PATH + "pedidoTablet.jasper";
    }
    
    public void jasperReport(final String urlReporte, String tipo, final String nombrePersona, Map<String, Object> params) throws ClassNotFoundException {
        generarReporte(urlReporte, tipo, params, false,false,null);
    }
    
    public String jasperReportFile(final String urlReporte, String tipo, Map<String, Object> params) throws ClassNotFoundException {
        return generarReporte(urlReporte, tipo, params, true, false, null);
    }
    
    public String jasperReportPrint(final String urlReporte, String tipo, String impresora, Map<String, Object> params) throws ClassNotFoundException {
        return generarReporte(urlReporte, tipo, params, false, true, impresora);
    }
    
    public void jasperReportBean(final String urlReporte, Map<String, Object> params, List<FacturaDetalle> lista, String tipo) throws ClassNotFoundException, IOException {
        generarReporteBean(urlReporte, params, lista,tipo);
    }
    
    public void jasperReportPrintBean(final String urlReporte,Map<String, Object> params, List<FacturaDetalle> lista, String impresora) throws ClassNotFoundException, IOException {
        generarReportePrintBean(urlReporte, params, lista, impresora);
    }
    
    public void generarReportePrintBean(final String urlReporte,Map<String, Object> params, List<FacturaDetalle> lista,String impresora) throws ClassNotFoundException, IOException {
        try {
//            JRExporter exporter = null;
//            FacesContext fcontext = super.getFacesContext();
//            ExternalContext econtext = FacesUtilsMovil.getExternalContext();
            InputStream inputStream = new FileInputStream(urlReporte);
            if (inputStream == null) {
                throw new ClassNotFoundException("Archivo " + urlReporte + " no se encontro");
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream,params,new JRBeanCollectionDataSource(lista));
            this.PrintReportToPrinter2(jasperPrint, impresora);
//            fcontext.responseComplete();
//            conn.close();
//            HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
//            response.setContentType(tipo);
//            if ("application/pdf".equals(tipo)) {
//                    exporter = new JRPdfExporter();
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
//            }
//            if (exporter != null) {
//                exporter.exportReport();
//            }
//            fcontext.responseComplete();
        } catch (FileNotFoundException | ClassNotFoundException | JRException  ex) {
            LOG.log(Level.SEVERE, "Error al ejecutar la sentecia sql del reporte. ", ex);
        }
    }
    
    public void generarReporteBean(final String urlReporte,Map<String, Object> params, List<FacturaDetalle> lista,String tipo) throws ClassNotFoundException, IOException {
        try {
            JRExporter exporter = null;
            FacesContext fcontext = super.getFacesContext();
            ExternalContext econtext = FacesUtils.getExternalContext();
            InputStream inputStream = new FileInputStream(urlReporte);
            if (inputStream == null) {
                throw new ClassNotFoundException("Archivo " + urlReporte + " no se encontro");
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream,params,new JRBeanCollectionDataSource(lista));
            HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
            response.setContentType(tipo);
            if ("application/pdf".equals(tipo)) {
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            }
            if (exporter != null) {
                exporter.exportReport();
            }
            fcontext.responseComplete();
        } catch (FileNotFoundException | ClassNotFoundException | JRException  ex) {
            LOG.log(Level.SEVERE, "Error al ejecutar la sentecia sql del reporte. ", ex);
        }
    }

    public String generarReporte(final String urlReporte, String tipo, Map<String, Object> params, boolean generarArchivo, boolean print, String impresora) throws ClassNotFoundException {
        String archivoGenerado = "";
        try {
            ExternalContext econtext = FacesUtils.getExternalContext();
            FacesContext fcontext = super.getFacesContext();
            Connection conn = this.factunetDS.getConnection();
            try {
                JRExporter exporter = null;
                InputStream inputStream = new FileInputStream(urlReporte);
                if (inputStream == null) {
                    throw new ClassNotFoundException("Archivo " + urlReporte + " no se encontrÃ³");
                }
                JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, conn);
                HttpServletResponse response = (HttpServletResponse) econtext.getResponse();
                response.setContentType(tipo);
                //            response.setHeader("Content-Disposition", "attachment; filename=\"actividades" + nombrePersona + ".pdf\";");
                //            response.setHeader("Cache-Control", "no-cache");
                //            response.setHeader("Pragma", "no-cache");
                //            response.setDateHeader("Expires", 0);
                if ("application/pdf".equals(tipo)) {
                    exporter = new JRPdfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    if(print){
                        try {
//                            Date d = new Date();
//                            archivoGenerado = getServletContext().getRealPath("/") + String.valueOf(d.getTime()) + ".pdf";
//                            File file = new File(archivoGenerado);
//                            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//                            exporter.exportReport();
                            this.PrintReportToPrinter2(jasperPrint,impresora);
                            exporter = null;
                        } catch (JRException e) {
                            LOG.log(Level.SEVERE, "Error de driver. ");
                            exporter = null;
                        }
                    }
                    else if (generarArchivo)
                    {
                        Date d = new Date();
                        archivoGenerado = getServletContext().getRealPath("/") + String.valueOf(d.getTime()) + ".pdf";
                        File file = new File(archivoGenerado);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
                    }
                    else
                    {
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                    }
                }
                if ("text/html".equals(tipo)) {
//                    exporter = new JRHtmlExporter();
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
//                    exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
//                    exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
                    // System.out.println("Exportando a HTML");
                }
                if ("application/rtf".equals(tipo)) {
                    exporter = new JRRtfExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                    // System.out.println("Exportando a RTF");
                }
                if ("application/csv".equals(tipo)) {
                    exporter = new JRCsvExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                    // System.out.println("Exportando a CSV");
                }
                if ("application/vnd.ms-excel".equals(tipo)) {
                    exporter = new JRXlsExporter();

                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
                    exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
                    exporter.setParameter(JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
                    exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                }
                if (exporter != null) {
                    exporter.exportReport();
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error al generar el reporte. ", ex);
            } finally {
                conn.close();
            }
            if (!generarArchivo)
            {
                fcontext.responseComplete();
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al ejecutar la sentecia sql del reporte. ", ex);
        }
        return archivoGenerado;
    }
    
    
//    public void llamarPrint(final String urlReporte, String impresora,Map<String, Object> params) throws SQLException{
//        JasperDesign jsd;
//         Connection con = this.factunetDS.getConnection();
//        try {
//            jsd = JRXmlLoader.load(urlReporte);
//            JasperReport jr = JasperCompileManager.compileReport(jsd);
//            JasperPrint jp = JasperFillManager.fillReport(jr, params, con);
//            PrintReportToPrinter(jp, impresora);
//        } catch (JRException ex) {
//            Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }finally {
//            try {
//                con.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
    public void PrintReportToPrinter(JasperPrint jp, String impresora) throws JRException {
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
        exporter.exportReport();
    }
    
    public void PrintReportToPrinter2(JasperPrint jp, String impresora) throws JRException {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
            printerJob.defaultPage(pageFormat);

            int selectedService = 0;

            AttributeSet attributeSet = new HashPrintServiceAttributeSet(new PrinterName(impresora, null));

            PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, attributeSet);

            try {
                printerJob.setPrintService(printService[selectedService]);

            } catch (Exception e) {
                System.out.println(e);
            }
            JRPrintServiceExporter exporter;
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(MediaSizeName.NA_LETTER);
            printRequestAttributeSet.add(new Copies(1));
            // these are deprecated
            exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
    
    public void PrintReportToPrinterFile(JasperPrint jp,String file, String impresora) throws JRException, URISyntaxException {
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(new Copies(1));
        printRequestAttributeSet.add(new Destination(new java.net.URI(file)));
        PrinterName printerName = new PrinterName(impresora, null); 
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(printerName);
        JRExporter exporter = new JRPrintServiceExporter(); 
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        exporter.exportReport();
    }
    
    public final static JasperReport subReport(String nombreSubreporte) throws JRException
    {
        return (JasperReport)JRLoader.loadObjectFromFile(nombreSubreporte);
    }
}