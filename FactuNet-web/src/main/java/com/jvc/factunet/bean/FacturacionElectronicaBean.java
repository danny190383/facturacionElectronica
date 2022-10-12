package com.jvc.factunet.bean;

import com.jvc.factunet.consumo.ConsumoSRI;
import com.jvc.factunet.entidades.CabeceraFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.DetalleFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.DocumentoRetencion;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.NotaCredito;
import com.jvc.factunet.entidades.NotaDebito;
import com.jvc.factunet.entidades.Retencion;
import com.jvc.factunet.firmar.XAdESBESSignature;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.objetos.RespuestaAutorizacion;
import com.jvc.factunet.objetos.RespuestaRecepcion;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.EmailSenderThread;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

@Named(value = "facturacionElectronicaBean")
@ViewScoped
public class FacturacionElectronicaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(FacturacionElectronicaBean .class.getName());
    
    @EJB
    private ConsumoSRI consumoSRI;
    @EJB
    private DocumentosServicios documentosServicios;
    
    private List<Factura> listaDocumentos;
    private List<GuiaRemision> listaGuiasRemision;
    private List<DocumentoRetencion> listaDocumentoRetencion;
    private Empresa empresa;

    public FacturacionElectronicaBean() {
        this.listaDocumentos = new ArrayList<>();
        this.listaGuiasRemision = new ArrayList<>();
        this.listaDocumentoRetencion = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        this.empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    }
    
    public void verBusquedaDocumentoRetencion() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarDocumentosRetencionDialog", options, null);
    }
    
    public void onDocumentoRetencionSelect(SelectEvent event) {
        this.listaGuiasRemision.clear();
        this.listaDocumentos.clear();
        this.listaDocumentoRetencion.clear();
        if(event.getObject() != null)
        {
            this.listaDocumentoRetencion = (List) event.getObject();
        }
    }
    
    public void verBusquedaGuiasRemision() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarGuiasRemisionDialog", options, null);
    }
    
    public void onGuiasRemisionSelect(SelectEvent event) {
        this.listaGuiasRemision.clear();
        this.listaDocumentos.clear();
        this.listaDocumentoRetencion.clear();
        if(event.getObject() != null)
        {
            this.listaGuiasRemision = (List) event.getObject();
        }
    }
    
    public void verBusquedaFacturas() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoDocumento", 21);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 2);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasVentaDialog", options, null);
    }
    
    public void onFacturaSelect(SelectEvent event) {
        this.listaDocumentos.clear();
        this.listaGuiasRemision.clear();
        this.listaDocumentoRetencion.clear();
        if(event.getObject() != null)
        {
            List<Factura> listaTmp = (List) event.getObject();
            for(Factura factura : listaTmp){
                if(factura.getPuntoVenta().getFacturacionElectronica()){
                    this.listaDocumentos.add(factura);
                }
            }
        }
    }
    
    public void verBusquedaNotasCredito() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 2);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 500);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarNotasCreditoDialog", options, null);
    }
    
    public void verBusquedaNotasDebito() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoBusqueda", 2);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 500);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarNotasDebitoDialog", options, null);
    }
    
    public void procesarFacturasRecepcion(){
//        if(Objects.equals(this.empresa.getFacturacionElectronica(), Boolean.FALSE)){
//             FacesUtils.addErrorMessage("El consultorio no tiene activada facturación electrónica, revice Mi Perfil");
//             return;
//        }
        for(Factura documento : this.listaDocumentos){
            if(documento.getEstadoAutorizacionSri() == null || !documento.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    String archivoFirmar = null;
                    if(documento instanceof FacturaVenta){
                        archivoFirmar = generateFacturaVenta(documento);
                    }
                    if(documento instanceof NotaCredito){
                        archivoFirmar = generateNotaCredito(documento);
                    }
                    if(documento instanceof NotaDebito){
                        archivoFirmar = generateNotaDebito(documento);
                    }
                    if(archivoFirmar != null){
                        XAdESBESSignature procesoFirma = new XAdESBESSignature(archivoFirmar);
                        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa();
                        String nombreResultado = documento.getCodigoBarras() + "Firmado.xml";
                        procesoFirma.firmar(archivoFirmar, new ByteArrayInputStream(documento.getPuntoVenta().getFirmaElectronica()), documento.getPuntoVenta().getClaveFirma(), url, nombreResultado);
                        RespuestaRecepcion respuesta;
                        if(documento.getPuntoVenta().getAmbienteElectronica().equals("1")){
                            respuesta = consumoSRI.consumirRecepcionPruebas(url+nombreResultado); 
                        }
                        else
                        {
                            respuesta = consumoSRI.consumirRecepcionProduccion(url+nombreResultado); 
                        }
                        if(respuesta == null){
                            FacesUtils.addErrorMessage("Error de conexión con el SRI");
                            return;
                        }
                        documento.setEstadoSri(respuesta.getEstado()); 
                        documento.setIdentificadorErrorSri(respuesta.getIdentificadorError()); 
                        documento.setDescripcionErrorSri(respuesta.getDescripciónError());
                        documento.setDescripcionAdicionalErrorSri(respuesta.getDescripciónAdicionalError());
                        documento.setTipoMensajeSri(respuesta.getTipoMensaje()); 
                        documento.setFechaEnvioSri(new Date()); 
                        documento.setEmpleadoEnvioSri(((Login)FacesUtils.getManagedBean("login")).getEmpleado()); 
                        documento.setPathXmlFirmado(url+nombreResultado); 
                        this.documentosServicios.actualizarFactura(documento);
                    }
                    else
                    {
                        FacesUtils.addErrorMessage("Error al generar el archio del documento " + documento.getNumero()); 
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + documento.getNumero() + " ya está autorizado por el SRI" );
            }
        }
        for(GuiaRemision guia : this.listaGuiasRemision){
            if(guia.getEstadoAutorizacionSri() == null || !guia.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    String archivoFirmar = null;
                    archivoFirmar = generateGuiaRemision(guia);
                    if(archivoFirmar != null){
                        XAdESBESSignature procesoFirma = new XAdESBESSignature(archivoFirmar);
                        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa();
                        String nombreResultado = guia.getCodigoBarras() + "Firmado.xml";
                        procesoFirma.firmar(archivoFirmar, new ByteArrayInputStream(guia.getFactura().getPuntoVenta().getFirmaElectronica()), guia.getFactura().getPuntoVenta().getClaveFirma(), url, nombreResultado);
                        RespuestaRecepcion respuesta;
                        if(guia.getFactura().getPuntoVenta().getAmbienteElectronica().equals("1")){
                            respuesta = consumoSRI.consumirRecepcionPruebas(url+nombreResultado); 
                        }
                        else
                        {
                            respuesta = consumoSRI.consumirRecepcionProduccion(url+nombreResultado); 
                        }
                        if(respuesta == null){
                            FacesUtils.addErrorMessage("Error de conexión con el SRI");
                            return;
                        }
                        guia.setEstadoSri(respuesta.getEstado()); 
                        guia.setIdentificadorErrorSri(respuesta.getIdentificadorError()); 
                        guia.setDescripcionErrorSri(respuesta.getDescripciónError());
                        guia.setDescripcionAdicionalErrorSri(respuesta.getDescripciónAdicionalError());
                        guia.setTipoMensajeSri(respuesta.getTipoMensaje()); 
                        guia.setFechaEnvioSri(new Date()); 
                        guia.setEmpleadoEnvioSri(((Login)FacesUtils.getManagedBean("login")).getEmpleado()); 
                        guia.setPathXmlFirmado(url+nombreResultado); 
                        this.documentosServicios.actualizarGriaRemision(guia);
                    }
                    else
                    {
                        FacesUtils.addErrorMessage("Error al generar el archio del documento " + guia.getSecuencia()); 
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + guia.getSecuencia() + " ya está autorizado por el SRI" );
            }
        }
        for(DocumentoRetencion retencion : this.listaDocumentoRetencion){
            if(retencion.getEstadoAutorizacionSri() == null || !retencion.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    String archivoFirmar = null;
                    archivoFirmar = generateDocumentoRetencion(retencion);
                    if(archivoFirmar != null){
                        XAdESBESSignature procesoFirma = new XAdESBESSignature(archivoFirmar);
                        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa();
                        String nombreResultado = retencion.getCodigoBarras() + "Firmado.xml";
                        procesoFirma.firmar(archivoFirmar, new ByteArrayInputStream(retencion.getFactura().getPuntoVenta().getFirmaElectronica()), retencion.getFactura().getPuntoVenta().getClaveFirma(), url, nombreResultado);
                        RespuestaRecepcion respuesta;
                        if(retencion.getFactura().getPuntoVenta().getAmbienteElectronica().equals("1")){
                            respuesta = consumoSRI.consumirRecepcionPruebas(url+nombreResultado); 
                        }
                        else
                        {
                            respuesta = consumoSRI.consumirRecepcionProduccion(url+nombreResultado); 
                        }
                        if(respuesta == null){
                            FacesUtils.addErrorMessage("Error de conexión con el SRI");
                            return;
                        }
                        retencion.setEstadoSri(respuesta.getEstado()); 
                        retencion.setIdentificadorErrorSri(respuesta.getIdentificadorError()); 
                        retencion.setDescripcionErrorSri(respuesta.getDescripciónError());
                        retencion.setDescripcionAdicionalErrorSri(respuesta.getDescripciónAdicionalError());
                        retencion.setTipoMensajeSri(respuesta.getTipoMensaje()); 
                        retencion.setFechaEnvioSri(new Date()); 
                        retencion.setEmpleadoEnvioSri(((Login)FacesUtils.getManagedBean("login")).getEmpleado()); 
                        retencion.setPathXmlFirmado(url+nombreResultado); 
                        this.documentosServicios.actualizarDocumentoRetencion(retencion);
                    }
                    else
                    {
                        FacesUtils.addErrorMessage("Error al generar el archio del documento " + retencion.getNumero()); 
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + retencion.getNumero() + " ya está autorizado por el SRI" );
            }
        }
    }
    
    public void procesarFacturasAutorizacion(){
//        if(Objects.equals(this.empresa.getFacturacionElectronica(), Boolean.FALSE)){
//             FacesUtils.addErrorMessage("El consultorio no tiene activada facturación electrónica, revice Mi Perfil");
//             return;
//        }
        for(Factura factura : this.listaDocumentos){
            if(factura.getEstadoAutorizacionSri() == null || !factura.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    RespuestaAutorizacion respuesta;
                    if(factura.getPuntoVenta().getAmbienteElectronica().equals("1")){
                        respuesta = consumoSRI.consumirAutorizacionPruebas(factura.getCodigoBarras()); 
                    }
                    else
                    {
                        respuesta = consumoSRI.consumirAutorizacionProduccion(factura.getCodigoBarras()); 
                    }
                    if(respuesta == null){
                        FacesUtils.addErrorMessage("Error de conexión con el SRI");
                        return;
                    }
                    factura.setEstadoAutorizacionSri(respuesta.getEstado()); 
                    factura.setAmbienteAutorizacionSri(respuesta.getAmbiente()); 
                    factura.setIdentificadorErrorAutorizacionSri(respuesta.getIdentificadorError()); 
                    factura.setDescripcionErrorAutorizacionSri(respuesta.getMensajeError()); 
                    factura.setDescripcionAdicionalErrorAutorizacionSri(respuesta.getDescripciónAdicionalError());
                    factura.setTipoMensajeAutorizacionSri(respuesta.getTipoMensaje()); 
                    factura.setFechaAutorizacionSri(Fecha.formatoStringToDateF(respuesta.getFechaAutorizacion())); 
                    this.documentosServicios.actualizarFactura(factura);
                    if(respuesta.getEstado().equals("AUTORIZADO")){
                        String nombreDocumento = null;
                        if(factura instanceof FacturaVenta){
                            nombreDocumento = "Factura de Venta";
                        }
                        if(factura instanceof NotaCredito){
                            nombreDocumento = "Nota de Crédito";
                        }
                        if(factura instanceof NotaDebito){
                            nombreDocumento = "Nota de Débito";
                        }
                        if(factura.getCliente().getPersona().getEmail() != null){
                            envioFacturaEmail(factura.getPathXmlFirmado(), factura.getCliente().getPersona().getEmail(), factura.getCodigo(), nombreDocumento, 1);
                        }
                        else
                        {
                            envioFacturaEmail(factura.getPathXmlFirmado(), factura.getEmpresa().getEmail(), factura.getCodigo(), nombreDocumento, 1);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + factura.getNumero() + " ya está autorizado por el SRI" );
            }
        }
        for(GuiaRemision guia : this.listaGuiasRemision){
            if(guia.getEstadoAutorizacionSri() == null || !guia.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    RespuestaAutorizacion respuesta;
                    if(guia.getFactura().getPuntoVenta().getAmbienteElectronica().equals("1")){
                        respuesta = consumoSRI.consumirAutorizacionPruebas(guia.getCodigoBarras()); 
                    }
                    else
                    {
                        respuesta = consumoSRI.consumirAutorizacionProduccion(guia.getCodigoBarras()); 
                    }
                    if(respuesta == null){
                        FacesUtils.addErrorMessage("Error de conexión con el SRI");
                        return;
                    }
                    guia.setEstadoAutorizacionSri(respuesta.getEstado()); 
                    guia.setAmbienteAutorizacionSri(respuesta.getAmbiente()); 
                    guia.setIdentificadorErrorAutorizacionSri(respuesta.getIdentificadorError()); 
                    guia.setDescripcionErrorAutorizacionSri(respuesta.getMensajeError()); 
                    guia.setDescripcionAdicionalErrorAutorizacionSri(respuesta.getDescripciónAdicionalError());
                    guia.setTipoMensajeAutorizacionSri(respuesta.getTipoMensaje()); 
                    guia.setFechaAutorizacionSri(Fecha.formatoStringToDateF(respuesta.getFechaAutorizacion())); 
                    this.documentosServicios.actualizarGriaRemision(guia);
                    if(respuesta.getEstado().equals("AUTORIZADO")){
                        
                        if(guia.getFactura().getCliente().getPersona().getEmail() != null){
                            envioFacturaEmail(guia.getPathXmlFirmado(), guia.getFactura().getCliente().getPersona().getEmail(), guia.getCodigo(), "", 2);
                        }
                        else
                        {
                            envioFacturaEmail(guia.getPathXmlFirmado(), guia.getFactura().getEmpresa().getEmail(), guia.getCodigo(), "", 2);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + guia.getSecuencia() + " ya está autorizado por el SRI" );
            }
        }
        for(DocumentoRetencion retencion : this.listaDocumentoRetencion){
            if(retencion.getEstadoAutorizacionSri() == null || !retencion.getEstadoAutorizacionSri().equals("AUTORIZADO")) {
                try {
                    RespuestaAutorizacion respuesta;
                    if(retencion.getFactura().getPuntoVenta().getAmbienteElectronica().equals("1")){
                        respuesta = consumoSRI.consumirAutorizacionPruebas(retencion.getCodigoBarras()); 
                    }
                    else
                    {
                        respuesta = consumoSRI.consumirAutorizacionProduccion(retencion.getCodigoBarras()); 
                    }
                    if(respuesta == null){
                        FacesUtils.addErrorMessage("Error de conexión con el SRI");
                        return;
                    }
                    retencion.setEstadoAutorizacionSri(respuesta.getEstado()); 
                    retencion.setAmbienteAutorizacionSri(respuesta.getAmbiente()); 
                    retencion.setIdentificadorErrorAutorizacionSri(respuesta.getIdentificadorError()); 
                    retencion.setDescripcionErrorAutorizacionSri(respuesta.getMensajeError()); 
                    retencion.setDescripcionAdicionalErrorAutorizacionSri(respuesta.getDescripciónAdicionalError());
                    retencion.setTipoMensajeAutorizacionSri(respuesta.getTipoMensaje()); 
                    retencion.setFechaAutorizacionSri(Fecha.formatoStringToDateF(respuesta.getFechaAutorizacion())); 
                    this.documentosServicios.actualizarDocumentoRetencion(retencion);
                    if(respuesta.getEstado().equals("AUTORIZADO")){
                        
                        if(retencion.getFactura().getProveedor().getPersona().getEmail() != null){
                            envioFacturaEmail(retencion.getPathXmlFirmado(), retencion.getFactura().getProveedor().getPersona().getEmail(), retencion.getCodigo(), "", 3);
                        }
                        else
                        {
                            envioFacturaEmail(retencion.getPathXmlFirmado(), retencion.getFactura().getEmpresa().getEmail(), retencion.getCodigo(), "", 3);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FacturacionElectronicaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                FacesUtils.addWarningMessage("El documento número : " + retencion.getNumero() + " ya está autorizado por el SRI" );
            }
        }
    }
    
    public String generarReporte(Integer factura, String nombreDocumento) {
        Map<String, Object> parametros = new HashMap();
        try {
            parametros.put("factura", factura);
            parametros.put("nombreReporte", nombreDocumento);
            parametros.put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            return jasperBean.jasperReportFile(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION, JasperReportUtil.TIPO_PDF, parametros);
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
        return null;
    }
    
    public String generarReporteGuiaRemision(Integer guia) {
        Map<String, Object> parametros = new HashMap();
        try {
            parametros.put("guia", guia);
            parametros.put("nombreReporte", "GUIA DE REMISION");
            parametros.put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            return jasperBean.jasperReportFile(JasperReportUtil.PATH_REPORTE_DOCUMENTO_GUIA_REMISION, JasperReportUtil.TIPO_PDF, parametros);
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
        return null;
    }
    
    public String generarReporteDocumentoRetencion(Integer retencion) {
        Map<String, Object> parametros = new HashMap();
        try {
            parametros.put("factura", retencion);
            parametros.put("nombreReporte", "DOCUMENTO DE RETENCIÓN");
            parametros.put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            return jasperBean.jasperReportFile(JasperReportUtil.PATH_REPORTE_DOCUMENTO_RETENCION, JasperReportUtil.TIPO_PDF, parametros);
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
        return null;
    }
    
    public void enviarFacturaEmail(Factura factura){
        String nombreDocumento = null;
        if(factura instanceof FacturaVenta){
            nombreDocumento = "Factura de Venta";
        }
        if(factura instanceof NotaCredito){
            nombreDocumento = "Nota de Crédito";
        }
        if(factura instanceof NotaDebito){
            nombreDocumento = "Nota de Débito";
        }
        
        if(factura.getCliente().getPersona().getEmail() != null){
            envioFacturaEmail(factura.getPathXmlFirmado(), factura.getCliente().getPersona().getEmail(), factura.getCodigo(), nombreDocumento, 1);
        }
        else
        {
            envioFacturaEmail(factura.getPathXmlFirmado(), factura.getEmpresa().getEmail(), factura.getCodigo(), nombreDocumento, 1);
        }
    }
    
    public void envioFacturaEmail(String xml, String destinatarioFactura, Integer factura, String nombreDocumento, Integer tipo) {
        try {
            String asunto = "Documento Eléctronico ";
            String usuario = empresa.getEmail().trim();
            String password = empresa.getEmailClave().trim();
            String destinatario = destinatarioFactura;
            String mensaje = "Sistema FactuNet documento electrónico SRI";
            String archivo = "";
            if(tipo == 1){ // Documento
                archivo = this.generarReporte(factura, nombreDocumento);
            }
            if(tipo == 2){ // Guia Remision
                archivo = this.generarReporteGuiaRemision(factura);
            }
            if(tipo == 3){ // Documento Retencion
                archivo = this.generarReporteDocumentoRetencion(factura);
            }
            EmailSenderThread emailSenderThread = new EmailSenderThread(usuario, password, destinatario, asunto, mensaje, archivo, xml);
            emailSenderThread.sendMail();
        } catch (Exception e) {
            String danny = "";
        }
    }
    
    public String generateFacturaVenta(Factura documento) throws Exception{
        
        FacturaVenta factura = (FacturaVenta) documento;
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(Boolean.TRUE); 
        Element rootElement = document.createElement("factura");
        document.appendChild(rootElement);

        Attr attr = document.createAttribute("id");
        attr.setValue("comprobante");
        rootElement.setAttributeNode(attr);

        Attr attr2 = document.createAttribute("version");
        attr2.setValue("1.1.0");
        rootElement.setAttributeNode(attr2);

        //Main Node
        Element raiz = document.getDocumentElement();

        Element itemInfoTributariaNode = document.createElement("infoTributaria"); 

            Element ambiente = document.createElement("ambiente"); 
            Text ambienteValue = document.createTextNode(factura.getPuntoVenta().getAmbienteElectronica());
            ambiente.appendChild(ambienteValue);

            Element tipoEmision = document.createElement("tipoEmision"); 
            Text tipoEmisionValue = document.createTextNode("1");
            tipoEmision.appendChild(tipoEmisionValue);

            Element razonSocial = document.createElement("razonSocial"); 
            Text razonSocialValue = document.createTextNode(factura.getPuntoVenta().getRazonSocial().trim());
            razonSocial.appendChild(razonSocialValue);

            Element nombreComercial = document.createElement("nombreComercial"); 
            Text nombreComercialValue = document.createTextNode(factura.getPuntoVenta().getNombre().trim());
            nombreComercial.appendChild(nombreComercialValue);

            Element ruc = document.createElement("ruc"); 
            Text rucValue = document.createTextNode(factura.getPuntoVenta().getRuc());
            ruc.appendChild(rucValue);
            
            Element claveAcceso = document.createElement("claveAcceso"); 
            Text claveAccesoValue = document.createTextNode(factura.getCodigoBarras());
            claveAcceso.appendChild(claveAccesoValue);
            
            Element codDoc = document.createElement("codDoc"); 
            Text codDocValue = document.createTextNode("01");
            codDoc.appendChild(codDocValue);
            
            Element estab = document.createElement("estab"); 
            Text estabValue = document.createTextNode(factura.getEmpresa().getCodigoSri());
            estab.appendChild(estabValue);
            
            Element ptoEmi = document.createElement("ptoEmi"); 
            Text ptoEmiValue = document.createTextNode(factura.getPuntoVenta().getCodigoSri());
            ptoEmi.appendChild(ptoEmiValue);
            
            Element secuencial = document.createElement("secuencial"); 
            Text secuencialValue = document.createTextNode(this.generaSecuencia(factura.getNumero().toString()));
            secuencial.appendChild(secuencialValue);
            
            Element dirMatriz = document.createElement("dirMatriz"); 
            Text dirMatrizValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirMatriz.appendChild(dirMatrizValue);

        itemInfoTributariaNode.appendChild(ambiente);
        itemInfoTributariaNode.appendChild(tipoEmision);
        itemInfoTributariaNode.appendChild(razonSocial);
        itemInfoTributariaNode.appendChild(nombreComercial);
        itemInfoTributariaNode.appendChild(ruc);
        itemInfoTributariaNode.appendChild(claveAcceso);
        itemInfoTributariaNode.appendChild(codDoc);
        itemInfoTributariaNode.appendChild(estab);
        itemInfoTributariaNode.appendChild(ptoEmi);
        itemInfoTributariaNode.appendChild(secuencial);
        itemInfoTributariaNode.appendChild(dirMatriz);

        raiz.appendChild(itemInfoTributariaNode);
        
        Element infoFacturaNode = document.createElement("infoFactura"); 
        
            Element fechaEmision = document.createElement("fechaEmision"); 
            Text fechaEmisionValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFecha()));
            fechaEmision.appendChild(fechaEmisionValue);
            
            Element dirEstablecimiento = document.createElement("dirEstablecimiento"); 
            Text dirEstablecimientoValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirEstablecimiento.appendChild(dirEstablecimientoValue);
            
            Element obligadoContabilidad = document.createElement("obligadoContabilidad"); 
            Text obligadoContabilidadValue = document.createTextNode("NO");
            obligadoContabilidad.appendChild(obligadoContabilidadValue);
            
            Element tipoIdentificacionComprador = document.createElement("tipoIdentificacionComprador"); 
            Text tipoIdentificacionCompradorValue = document.createTextNode(factura.getCliente().getPersona().getTipoIdentificacion().getCodigoSRI());
            tipoIdentificacionComprador.appendChild(tipoIdentificacionCompradorValue);
            
            Element razonSocialComprador = document.createElement("razonSocialComprador"); 
            Text razonSocialCompradorValue = document.createTextNode((factura.getCliente().getPersona().getApellidos() + " " + factura.getCliente().getPersona().getNombres()).trim());
            razonSocialComprador.appendChild(razonSocialCompradorValue);
            
            Element identificacionComprador = document.createElement("identificacionComprador"); 
            Text identificacionCompradorValue = document.createTextNode(factura.getCliente().getPersona().getCedula());
            identificacionComprador.appendChild(identificacionCompradorValue);
            
            Element direccionComprador = document.createElement("direccionComprador"); 
            Text direccionCompradorValue = document.createTextNode(factura.getCliente().getPersona().getDireccion().equals("") ? " " : factura.getCliente().getPersona().getDireccion().trim());
            direccionComprador.appendChild(direccionCompradorValue);
            
            Element totalSinImpuestos = document.createElement("totalSinImpuestos"); 
            Text totalSinImpuestosValue = document.createTextNode(factura.getSubtotal().toString());
            totalSinImpuestos.appendChild(totalSinImpuestosValue);
            
            Element totalDescuento = document.createElement("totalDescuento"); 
            Text totalDescuentoValue = document.createTextNode(factura.getDescuento().toString());
            totalDescuento.appendChild(totalDescuentoValue);
            
            Element totalConImpuestos = document.createElement("totalConImpuestos"); 
            
            for(CabeceraFacturaImpuestoTarifa impuestosFactura : factura.getCabeceraFacturaImpuestoTarifaList()){
                    
                Element totalImpuesto = document.createElement("totalImpuesto"); 
            
                    Element codigo = document.createElement("codigo"); 
                    Text codigoValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getImpuesto().getCodigoSri());
                    codigo.appendChild(codigoValue);
                    
                    Element codigoPorcentaje = document.createElement("codigoPorcentaje"); 
                    Text codigoPorcentajeValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getCodigoSri());
                    codigoPorcentaje.appendChild(codigoPorcentajeValue);
                    
                    Element baseImponible = document.createElement("baseImponible"); 
                    Text baseImponibleValue = document.createTextNode(impuestosFactura.getBaseImponible().toString());
                    baseImponible.appendChild(baseImponibleValue);
                    
                    Element tarifa = document.createElement("tarifa"); 
                    Text tarifaValue = document.createTextNode(String.valueOf(impuestosFactura.getPorcentaje()));
                    tarifa.appendChild(tarifaValue);
                    
                    Element valor = document.createElement("valor"); 
                    Text valorValue = document.createTextNode(impuestosFactura.getValor().toString());
                    valor.appendChild(valorValue);
                    
                    totalImpuesto.appendChild(codigo);
                    totalImpuesto.appendChild(codigoPorcentaje);
                    totalImpuesto.appendChild(baseImponible);
                    totalImpuesto.appendChild(tarifa);
                    totalImpuesto.appendChild(valor);
            
                totalConImpuestos.appendChild(totalImpuesto);
            }
            Element propina = document.createElement("propina"); 
            Text propinaValue = document.createTextNode("0.00");
            propina.appendChild(propinaValue);
            
            Element importeTotal = document.createElement("importeTotal"); 
            Text importeTotalValue = document.createTextNode(factura.getTotal().toString());
            importeTotal.appendChild(importeTotalValue);
            
            Element moneda = document.createElement("moneda"); 
            Text monedaValue = document.createTextNode(factura.getEmpresa().getMoneda());
            moneda.appendChild(monedaValue);

            infoFacturaNode.appendChild(fechaEmision);
            infoFacturaNode.appendChild(dirEstablecimiento);
            infoFacturaNode.appendChild(obligadoContabilidad);
            infoFacturaNode.appendChild(tipoIdentificacionComprador);
            infoFacturaNode.appendChild(razonSocialComprador);
            infoFacturaNode.appendChild(identificacionComprador);
            infoFacturaNode.appendChild(direccionComprador);
            infoFacturaNode.appendChild(totalSinImpuestos);
            infoFacturaNode.appendChild(totalDescuento);
            infoFacturaNode.appendChild(totalConImpuestos);
            infoFacturaNode.appendChild(propina);
            infoFacturaNode.appendChild(importeTotal);
            infoFacturaNode.appendChild(moneda);
            
            Element pagos = document.createElement("pagos"); 
             
                for(FacturaPago pagoFactua : factura.getFacturaPagoList()){
                    
                    Element pago = document.createElement("pago"); 
                    
                    Element formaPago = document.createElement("formaPago"); 
                    Text formaPagoValue;
                    if(pagoFactua.getCuentaFacturaList() == null || pagoFactua.getCuentaFacturaList().isEmpty()){
                        
                        formaPagoValue = document.createTextNode(pagoFactua.getFormaPago().getCodigoSRI());
                    }
                    else
                    {
                        formaPagoValue = document.createTextNode(pagoFactua.getCuentaFacturaList().get(0).getFormaPago().getCodigoSRI());
                    }
                    formaPago.appendChild(formaPagoValue);
                    
                    Element total = document.createElement("total"); 
                    Text totalValue = document.createTextNode(pagoFactua.getValor().toString());
                    total.appendChild(totalValue);
            
                    pago.appendChild(formaPago);
                    pago.appendChild(total);

                    pagos.appendChild(pago);
                }
                
            infoFacturaNode.appendChild(pagos); 
            
        raiz.appendChild(infoFacturaNode);
        
        Element detalles = document.createElement("detalles"); 
        
            for(FacturaDetalle detalleFactura : factura.getFacturaDetalleList()){

                Element detalle = document.createElement("detalle"); 

                Element codigoPrincipal = document.createElement("codigoPrincipal"); 
                Text codigoPrincipalValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                codigoPrincipal.appendChild(codigoPrincipalValue);

                Element codigoAuxiliar = document.createElement("codigoAuxiliar"); 
                Text codigoAuxiliarValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                codigoAuxiliar.appendChild(codigoAuxiliarValue);
                
                Element descripcion = document.createElement("descripcion"); 
                Text descripcionValue = document.createTextNode(detalleFactura.getProductoServicio().getNombre());
                descripcion.appendChild(descripcionValue);
                
                Element cantidad = document.createElement("cantidad"); 
                Text cantidadValue = document.createTextNode(detalleFactura.getCantidad().toString());
                cantidad.appendChild(cantidadValue);
                
                Element precioUnitario = document.createElement("precioUnitario"); 
                Text precioUnitarioValue = document.createTextNode(detalleFactura.getPrecioVentaUnitario().toString());
                precioUnitario.appendChild(precioUnitarioValue);
                
                Element descuento = document.createElement("descuento"); 
                Text descuentoValue = document.createTextNode(detalleFactura.getValorDescuento().toString());
                descuento.appendChild(descuentoValue);
                
                Element precioTotalSinImpuesto = document.createElement("precioTotalSinImpuesto"); 
                Text precioTotalSinImpuestoValue = document.createTextNode(detalleFactura.getSubtotalConDescuento().toString());
                precioTotalSinImpuesto.appendChild(precioTotalSinImpuestoValue);

                detalle.appendChild(codigoPrincipal);
                detalle.appendChild(codigoAuxiliar);
                detalle.appendChild(descripcion);
                detalle.appendChild(cantidad);
                detalle.appendChild(precioUnitario);
                detalle.appendChild(descuento);
                detalle.appendChild(precioTotalSinImpuesto);
                
                Element impuestos = document.createElement("impuestos"); 
            
                for(DetalleFacturaImpuestoTarifa impuestosDetalle : detalleFactura.getDetalleFacturaImpuestoTarifaList()){
                    
               
                        Element impuesto = document.createElement("impuesto"); 

                        Element codigoD = document.createElement("codigo"); 
                        Text codigoValueD = document.createTextNode(impuestosDetalle.getImpuestoTarifa().getImpuesto().getCodigoSri());
                        codigoD.appendChild(codigoValueD);

                        Element codigoPorcentajeD = document.createElement("codigoPorcentaje"); 
                        Text codigoPorcentajeValueD = document.createTextNode(impuestosDetalle.getImpuestoTarifa().getCodigoSri());
                        codigoPorcentajeD.appendChild(codigoPorcentajeValueD);
                        
                        Element tarifaD = document.createElement("tarifa"); 
                        Text tarifaValueD = document.createTextNode(impuestosDetalle.getPorcentaje().toString());
                        tarifaD.appendChild(tarifaValueD);

                        Element baseImponibleD = document.createElement("baseImponible"); 
                        Text baseImponibleValueD = document.createTextNode(impuestosDetalle.getBaseImponible().toString());
                        baseImponibleD.appendChild(baseImponibleValueD);

                        Element valorD = document.createElement("valor"); 
                        Text valorValueD = document.createTextNode(impuestosDetalle.getValor().toString());
                        valorD.appendChild(valorValueD);

                        impuesto.appendChild(codigoD);
                        impuesto.appendChild(codigoPorcentajeD);
                        impuesto.appendChild(tarifaD);
                        impuesto.appendChild(baseImponibleD);
                        impuesto.appendChild(valorD);
            
                        impuestos.appendChild(impuesto);
                    }
                
                    detalle.appendChild(impuestos);
               
                detalles.appendChild(detalle);
            }
        
        raiz.appendChild(detalles);
        
        Element infoAdicional = document.createElement("infoAdicional"); 
        
            Element campoAdicional1  = document.createElement("campoAdicional"); 
            Text campoAdicional1Value = document.createTextNode(factura.getCliente().getPersona().getDireccion() == null ? "S/D" : factura.getCliente().getPersona().getDireccion());
            campoAdicional1.appendChild(campoAdicional1Value);
            
            Attr attrNombre1 = document.createAttribute("nombre");
            attrNombre1.setValue("Dirección");
            campoAdicional1.setAttributeNode(attrNombre1);
            
            Element campoAdicional2  = document.createElement("campoAdicional"); 
            Text campoAdicional2Value = document.createTextNode(factura.getCliente().getPersona().getEmail() == null ? "S/D" : factura.getCliente().getPersona().getEmail());
            campoAdicional2.appendChild(campoAdicional2Value);
            
            Attr attrNombre2 = document.createAttribute("nombre");
            attrNombre2.setValue("Email");
            campoAdicional2.setAttributeNode(attrNombre2);
            
            infoAdicional.appendChild(campoAdicional1);
            infoAdicional.appendChild(campoAdicional2);
            
        raiz.appendChild(infoAdicional);
    
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa() + factura.getCodigoBarras() + ".xml";
        Result result = new StreamResult(new File(url)); 
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        
        return url;
    }
    
    public String generateNotaCredito(Factura documento) throws Exception{
        
        NotaCredito factura = (NotaCredito) documento;
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(Boolean.TRUE); 
        Element rootElement = document.createElement("notaCredito");
        document.appendChild(rootElement);

        Attr attr = document.createAttribute("id");
        attr.setValue("comprobante");
        rootElement.setAttributeNode(attr);

        Attr attr2 = document.createAttribute("version");
        attr2.setValue("1.1.0");
        rootElement.setAttributeNode(attr2);

        //Main Node
        Element raiz = document.getDocumentElement();

        Element itemInfoTributariaNode = document.createElement("infoTributaria"); 

            Element ambiente = document.createElement("ambiente"); 
            Text ambienteValue = document.createTextNode(factura.getPuntoVenta().getAmbienteElectronica());
            ambiente.appendChild(ambienteValue);

            Element tipoEmision = document.createElement("tipoEmision"); 
            Text tipoEmisionValue = document.createTextNode("1");
            tipoEmision.appendChild(tipoEmisionValue);

            Element razonSocial = document.createElement("razonSocial"); 
            Text razonSocialValue = document.createTextNode(factura.getPuntoVenta().getRazonSocial().trim());
            razonSocial.appendChild(razonSocialValue);

            Element nombreComercial = document.createElement("nombreComercial"); 
            Text nombreComercialValue = document.createTextNode(factura.getPuntoVenta().getNombre().trim());
            nombreComercial.appendChild(nombreComercialValue);

            Element ruc = document.createElement("ruc"); 
            Text rucValue = document.createTextNode(factura.getPuntoVenta().getRuc());
            ruc.appendChild(rucValue);
            
            Element claveAcceso = document.createElement("claveAcceso"); 
            Text claveAccesoValue = document.createTextNode(factura.getCodigoBarras());
            claveAcceso.appendChild(claveAccesoValue);
            
            Element codDoc = document.createElement("codDoc"); 
            Text codDocValue = document.createTextNode("04");
            codDoc.appendChild(codDocValue);
            
            Element estab = document.createElement("estab"); 
            Text estabValue = document.createTextNode(factura.getEmpresa().getCodigoSri());
            estab.appendChild(estabValue);
            
            Element ptoEmi = document.createElement("ptoEmi"); 
            Text ptoEmiValue = document.createTextNode(((FacturaVenta)factura.getDocumentoRelacionado()).getPuntoVenta().getCodigoSri());
            ptoEmi.appendChild(ptoEmiValue);
            
            Element secuencial = document.createElement("secuencial"); 
            Text secuencialValue = document.createTextNode(this.generaSecuencia(factura.getNumero().toString()));
            secuencial.appendChild(secuencialValue);
            
            Element dirMatriz = document.createElement("dirMatriz"); 
            Text dirMatrizValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirMatriz.appendChild(dirMatrizValue);

        itemInfoTributariaNode.appendChild(ambiente);
        itemInfoTributariaNode.appendChild(tipoEmision);
        itemInfoTributariaNode.appendChild(razonSocial);
        itemInfoTributariaNode.appendChild(nombreComercial);
        itemInfoTributariaNode.appendChild(ruc);
        itemInfoTributariaNode.appendChild(claveAcceso);
        itemInfoTributariaNode.appendChild(codDoc);
        itemInfoTributariaNode.appendChild(estab);
        itemInfoTributariaNode.appendChild(ptoEmi);
        itemInfoTributariaNode.appendChild(secuencial);
        itemInfoTributariaNode.appendChild(dirMatriz);

        raiz.appendChild(itemInfoTributariaNode);
        
        Element infoFacturaNode = document.createElement("infoNotaCredito"); 
        
            Element fechaEmision = document.createElement("fechaEmision"); 
            Text fechaEmisionValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFecha()));
            fechaEmision.appendChild(fechaEmisionValue);
            
            Element dirEstablecimiento = document.createElement("dirEstablecimiento"); 
            Text dirEstablecimientoValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirEstablecimiento.appendChild(dirEstablecimientoValue);
            
            Element tipoIdentificacionComprador = document.createElement("tipoIdentificacionComprador"); 
            Text tipoIdentificacionCompradorValue = document.createTextNode(factura.getDocumentoRelacionado().getCliente().getPersona().getTipoIdentificacion().getCodigoSRI());
            tipoIdentificacionComprador.appendChild(tipoIdentificacionCompradorValue);
            
            Element razonSocialComprador = document.createElement("razonSocialComprador"); 
            Text razonSocialCompradorValue = document.createTextNode((factura.getDocumentoRelacionado().getCliente().getPersona().getApellidos() + " " + factura.getDocumentoRelacionado().getCliente().getPersona().getNombres()).trim());
            razonSocialComprador.appendChild(razonSocialCompradorValue);
            
            Element identificacionComprador = document.createElement("identificacionComprador"); 
            Text identificacionCompradorValue = document.createTextNode(factura.getDocumentoRelacionado().getCliente().getPersona().getCedula());
            identificacionComprador.appendChild(identificacionCompradorValue);
            
            Element obligadoContabilidad = document.createElement("obligadoContabilidad"); 
            Text obligadoContabilidadValue = document.createTextNode("NO");
            obligadoContabilidad.appendChild(obligadoContabilidadValue);
            
            Element codDocModificado = document.createElement("codDocModificado"); 
            Text codDocModificadoValue = document.createTextNode("01");
            codDocModificado.appendChild(codDocModificadoValue);
            
            Element numDocModificado = document.createElement("numDocModificado"); 
            Text numDocModificadoValue = document.createTextNode(factura.getDocumentoRelacionado().getEmpresa().getCodigoSri()+"-"+((FacturaVenta)factura.getDocumentoRelacionado()).getPuntoVenta().getCodigoSri()+"-"+this.generaSecuencia(factura.getDocumentoRelacionado().getNumero().toString()));
            numDocModificado.appendChild(numDocModificadoValue);
            
            Element fechaEmisionDocSustento = document.createElement("fechaEmisionDocSustento"); 
            Text fechaEmisionDocSustentoValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getDocumentoRelacionado().getFecha()));
            fechaEmisionDocSustento.appendChild(fechaEmisionDocSustentoValue);
            
            Element totalSinImpuestos = document.createElement("totalSinImpuestos"); 
            Text totalSinImpuestosValue = document.createTextNode(factura.getSubtotal().toString());
            totalSinImpuestos.appendChild(totalSinImpuestosValue);
            
            Element valorModificacion = document.createElement("valorModificacion"); 
            Text valorModificacionValue = document.createTextNode(factura.getTotal().toString());
            valorModificacion.appendChild(valorModificacionValue);
            
            Element moneda = document.createElement("moneda"); 
            Text monedaValue = document.createTextNode(factura.getEmpresa().getMoneda());
            moneda.appendChild(monedaValue);
            
            Element totalConImpuestos = document.createElement("totalConImpuestos"); 
            
            for(CabeceraFacturaImpuestoTarifa impuestosFactura : factura.getCabeceraFacturaImpuestoTarifaList()){
                    
                Element totalImpuesto = document.createElement("totalImpuesto"); 
            
                    Element codigo = document.createElement("codigo"); 
                    Text codigoValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getImpuesto().getCodigoSri());
                    codigo.appendChild(codigoValue);
                    
                    Element codigoPorcentaje = document.createElement("codigoPorcentaje"); 
                    Text codigoPorcentajeValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getCodigoSri());
                    codigoPorcentaje.appendChild(codigoPorcentajeValue);
                    
                    Element baseImponible = document.createElement("baseImponible"); 
                    Text baseImponibleValue = document.createTextNode(impuestosFactura.getBaseImponible().toString());
                    baseImponible.appendChild(baseImponibleValue);
                    
                    Element valor = document.createElement("valor"); 
                    Text valorValue = document.createTextNode(impuestosFactura.getValor().toString());
                    valor.appendChild(valorValue);
                    
                    totalImpuesto.appendChild(codigo);
                    totalImpuesto.appendChild(codigoPorcentaje);
                    totalImpuesto.appendChild(baseImponible);
                    totalImpuesto.appendChild(valor);
            
                totalConImpuestos.appendChild(totalImpuesto);
            }
            
            Element motivo = document.createElement("motivo"); 
            Text motivoValue = document.createTextNode(factura.getTipoNota() == 1 ? "Devolución total" : 
                                                       factura.getTipoNota() == 2 ? "Devolución parcial" : 
                                                       factura.getTipoNota() == 3 ? "Descuento adicional" :
                                                       factura.getTipoNota() == 4 ? "Diferimiento de precio" : "Sin especificar");
            motivo.appendChild(motivoValue);
            
            infoFacturaNode.appendChild(fechaEmision);
            infoFacturaNode.appendChild(dirEstablecimiento);
            infoFacturaNode.appendChild(tipoIdentificacionComprador);
            infoFacturaNode.appendChild(razonSocialComprador);
            infoFacturaNode.appendChild(identificacionComprador);
            infoFacturaNode.appendChild(obligadoContabilidad);
            infoFacturaNode.appendChild(codDocModificado);
            infoFacturaNode.appendChild(numDocModificado);
            infoFacturaNode.appendChild(fechaEmisionDocSustento);
            infoFacturaNode.appendChild(totalSinImpuestos);
            infoFacturaNode.appendChild(valorModificacion);
            infoFacturaNode.appendChild(moneda);
            infoFacturaNode.appendChild(totalConImpuestos);
            infoFacturaNode.appendChild(motivo);
            
        raiz.appendChild(infoFacturaNode);
        
        Element detalles = document.createElement("detalles"); 
        
            for(FacturaDetalle detalleFactura : factura.getFacturaDetalleList()){

                Element detalle = document.createElement("detalle"); 

                Element codigoPrincipal = document.createElement("codigoInterno"); 
                Text codigoPrincipalValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                codigoPrincipal.appendChild(codigoPrincipalValue);

                Element codigoAuxiliar = document.createElement("codigoAdicional"); 
                Text codigoAuxiliarValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                codigoAuxiliar.appendChild(codigoAuxiliarValue);
                
                Element descripcion = document.createElement("descripcion"); 
                Text descripcionValue = document.createTextNode(detalleFactura.getProductoServicio().getNombre());
                descripcion.appendChild(descripcionValue);
                
                Element cantidad = document.createElement("cantidad"); 
                Text cantidadValue = document.createTextNode(detalleFactura.getCantidad().toString());
                cantidad.appendChild(cantidadValue);
                
                Element precioUnitario = document.createElement("precioUnitario"); 
                Text precioUnitarioValue = document.createTextNode(detalleFactura.getPrecioVentaUnitario().toString());
                precioUnitario.appendChild(precioUnitarioValue);
                
                Element descuento = document.createElement("descuento"); 
                Text descuentoValue = document.createTextNode(detalleFactura.getValorDescuento().toString());
                descuento.appendChild(descuentoValue);
                
                Element precioTotalSinImpuesto = document.createElement("precioTotalSinImpuesto"); 
                Text precioTotalSinImpuestoValue = document.createTextNode(detalleFactura.getSubtotalConDescuento().toString());
                precioTotalSinImpuesto.appendChild(precioTotalSinImpuestoValue);

                detalle.appendChild(codigoPrincipal);
                detalle.appendChild(codigoAuxiliar);
                detalle.appendChild(descripcion);
                detalle.appendChild(cantidad);
                detalle.appendChild(precioUnitario);
                detalle.appendChild(descuento);
                detalle.appendChild(precioTotalSinImpuesto);
                
                Element impuestos = document.createElement("impuestos"); 
            
                for(DetalleFacturaImpuestoTarifa impuestosDetalle : detalleFactura.getDetalleFacturaImpuestoTarifaList()){
                    
               
                        Element impuesto = document.createElement("impuesto"); 

                        Element codigoD = document.createElement("codigo"); 
                        Text codigoValueD = document.createTextNode(impuestosDetalle.getImpuestoTarifa().getImpuesto().getCodigoSri());
                        codigoD.appendChild(codigoValueD);

                        Element codigoPorcentajeD = document.createElement("codigoPorcentaje"); 
                        Text codigoPorcentajeValueD = document.createTextNode(impuestosDetalle.getImpuestoTarifa().getCodigoSri());
                        codigoPorcentajeD.appendChild(codigoPorcentajeValueD);
                        
                        Element tarifaD = document.createElement("tarifa"); 
                        Text tarifaValueD = document.createTextNode(impuestosDetalle.getPorcentaje().toString());
                        tarifaD.appendChild(tarifaValueD);

                        Element baseImponibleD = document.createElement("baseImponible"); 
                        Text baseImponibleValueD = document.createTextNode(impuestosDetalle.getBaseImponible().toString());
                        baseImponibleD.appendChild(baseImponibleValueD);

                        Element valorD = document.createElement("valor"); 
                        Text valorValueD = document.createTextNode(impuestosDetalle.getValor().toString());
                        valorD.appendChild(valorValueD);

                        impuesto.appendChild(codigoD);
                        impuesto.appendChild(codigoPorcentajeD);
                        impuesto.appendChild(tarifaD);
                        impuesto.appendChild(baseImponibleD);
                        impuesto.appendChild(valorD);
            
                        impuestos.appendChild(impuesto);
                    }
                
                    detalle.appendChild(impuestos);
               
                detalles.appendChild(detalle);
            }
        
        raiz.appendChild(detalles);
        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa() + factura.getCodigoBarras() + ".xml";
        Result result = new StreamResult(new File(url)); 
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        
        return url;
    }
    
    public String generateNotaDebito(Factura documento) throws Exception{
        
        NotaDebito factura = (NotaDebito) documento;
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(Boolean.TRUE); 
        Element rootElement = document.createElement("notaDebito");
        document.appendChild(rootElement);

        Attr attr = document.createAttribute("id");
        attr.setValue("comprobante");
        rootElement.setAttributeNode(attr);

        Attr attr2 = document.createAttribute("version");
        attr2.setValue("1.0.0");
        rootElement.setAttributeNode(attr2);

        //Main Node
        Element raiz = document.getDocumentElement();

        Element itemInfoTributariaNode = document.createElement("infoTributaria"); 

            Element ambiente = document.createElement("ambiente"); 
            Text ambienteValue = document.createTextNode(factura.getPuntoVenta().getAmbienteElectronica());
            ambiente.appendChild(ambienteValue);

            Element tipoEmision = document.createElement("tipoEmision"); 
            Text tipoEmisionValue = document.createTextNode("1");
            tipoEmision.appendChild(tipoEmisionValue);

            Element razonSocial = document.createElement("razonSocial"); 
            Text razonSocialValue = document.createTextNode(factura.getPuntoVenta().getRazonSocial().trim());
            razonSocial.appendChild(razonSocialValue);

            Element nombreComercial = document.createElement("nombreComercial"); 
            Text nombreComercialValue = document.createTextNode(factura.getPuntoVenta().getNombre().trim());
            nombreComercial.appendChild(nombreComercialValue);

            Element ruc = document.createElement("ruc"); 
            Text rucValue = document.createTextNode(factura.getPuntoVenta().getRuc());
            ruc.appendChild(rucValue);
            
            Element claveAcceso = document.createElement("claveAcceso"); 
            Text claveAccesoValue = document.createTextNode(factura.getCodigoBarras());
            claveAcceso.appendChild(claveAccesoValue);
            
            Element codDoc = document.createElement("codDoc"); 
            Text codDocValue = document.createTextNode("05");
            codDoc.appendChild(codDocValue);
            
            Element estab = document.createElement("estab"); 
            Text estabValue = document.createTextNode(factura.getEmpresa().getCodigoSri());
            estab.appendChild(estabValue);
            
            Element ptoEmi = document.createElement("ptoEmi"); 
            Text ptoEmiValue = null;
            if(factura.getDocumentoRelacionado() instanceof FacturaVenta){
                ptoEmiValue = document.createTextNode(((FacturaVenta)factura.getDocumentoRelacionado()).getPuntoVenta().getCodigoSri());
            }
            if(factura.getDocumentoRelacionado() instanceof NotaCredito){
                ptoEmiValue = document.createTextNode(((FacturaVenta)(((NotaCredito)factura.getDocumentoRelacionado())).getDocumentoRelacionado()).getPuntoVenta().getCodigoSri());
            }
            ptoEmi.appendChild(ptoEmiValue);
            
            Element secuencial = document.createElement("secuencial"); 
            Text secuencialValue = document.createTextNode(this.generaSecuencia(factura.getNumero().toString()));
            secuencial.appendChild(secuencialValue);
            
            Element dirMatriz = document.createElement("dirMatriz"); 
            Text dirMatrizValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirMatriz.appendChild(dirMatrizValue);

        itemInfoTributariaNode.appendChild(ambiente);
        itemInfoTributariaNode.appendChild(tipoEmision);
        itemInfoTributariaNode.appendChild(razonSocial);
        itemInfoTributariaNode.appendChild(nombreComercial);
        itemInfoTributariaNode.appendChild(ruc);
        itemInfoTributariaNode.appendChild(claveAcceso);
        itemInfoTributariaNode.appendChild(codDoc);
        itemInfoTributariaNode.appendChild(estab);
        itemInfoTributariaNode.appendChild(ptoEmi);
        itemInfoTributariaNode.appendChild(secuencial);
        itemInfoTributariaNode.appendChild(dirMatriz);

        raiz.appendChild(itemInfoTributariaNode);
        
        Element infoFacturaNode = document.createElement("infoNotaDebito"); 
        
            Element fechaEmision = document.createElement("fechaEmision"); 
            Text fechaEmisionValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFecha()));
            fechaEmision.appendChild(fechaEmisionValue);
            
            Element dirEstablecimiento = document.createElement("dirEstablecimiento"); 
            Text dirEstablecimientoValue = document.createTextNode(factura.getPuntoVenta().getDireccion().trim());
            dirEstablecimiento.appendChild(dirEstablecimientoValue);
            
            Element tipoIdentificacionComprador = document.createElement("tipoIdentificacionComprador"); 
            Text tipoIdentificacionCompradorValue = document.createTextNode(factura.getCliente().getPersona().getTipoIdentificacion().getCodigoSRI());
            tipoIdentificacionComprador.appendChild(tipoIdentificacionCompradorValue);
            
            Element razonSocialComprador = document.createElement("razonSocialComprador"); 
            Text razonSocialCompradorValue = document.createTextNode((factura.getCliente().getPersona().getApellidos() + " " + factura.getCliente().getPersona().getNombres()).trim());
            razonSocialComprador.appendChild(razonSocialCompradorValue);
            
            Element identificacionComprador = document.createElement("identificacionComprador"); 
            Text identificacionCompradorValue = document.createTextNode(factura.getCliente().getPersona().getCedula());
            identificacionComprador.appendChild(identificacionCompradorValue);
            
            Element obligadoContabilidad = document.createElement("obligadoContabilidad"); 
            Text obligadoContabilidadValue = document.createTextNode("NO");
            obligadoContabilidad.appendChild(obligadoContabilidadValue);
            
            Element codDocModificado = document.createElement("codDocModificado"); 
            Text codDocModificadoValue = null;
            if(factura.getDocumentoRelacionado() instanceof FacturaVenta){
                codDocModificadoValue = document.createTextNode("01");
            }
            if(factura.getDocumentoRelacionado() instanceof NotaCredito){
                codDocModificadoValue = document.createTextNode("05");
            }
            codDocModificado.appendChild(codDocModificadoValue);
            
            Element numDocModificado = document.createElement("numDocModificado"); 
            String numeroDocumentoPV = "";
            if(factura.getDocumentoRelacionado() instanceof FacturaVenta){
                numeroDocumentoPV = ((FacturaVenta)factura.getDocumentoRelacionado()).getPuntoVenta().getCodigoSri();
            }
            if(factura.getDocumentoRelacionado() instanceof NotaCredito){
                numeroDocumentoPV = ((FacturaVenta)(((NotaCredito)factura.getDocumentoRelacionado())).getDocumentoRelacionado()).getPuntoVenta().getCodigoSri();
            }
            Text numDocModificadoValue = document.createTextNode(factura.getDocumentoRelacionado().getEmpresa().getCodigoSri()+"-"+numeroDocumentoPV+"-"+this.generaSecuencia(factura.getDocumentoRelacionado().getNumero().toString()));
            numDocModificado.appendChild(numDocModificadoValue);
            
            Element fechaEmisionDocSustento = document.createElement("fechaEmisionDocSustento"); 
            Text fechaEmisionDocSustentoValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getDocumentoRelacionado().getFecha()));
            fechaEmisionDocSustento.appendChild(fechaEmisionDocSustentoValue);
            
            Element totalSinImpuestos = document.createElement("totalSinImpuestos"); 
            Text totalSinImpuestosValue = document.createTextNode(factura.getSubtotal().toString());
            totalSinImpuestos.appendChild(totalSinImpuestosValue);
            
            
            Element totalConImpuestos = document.createElement("impuestos"); 
            
            for(CabeceraFacturaImpuestoTarifa impuestosFactura : factura.getCabeceraFacturaImpuestoTarifaList()){
                    
                Element totalImpuesto = document.createElement("impuesto"); 
            
                    Element codigo = document.createElement("codigo"); 
                    Text codigoValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getImpuesto().getCodigoSri());
                    codigo.appendChild(codigoValue);
                    
                    Element codigoPorcentaje = document.createElement("codigoPorcentaje"); 
                    Text codigoPorcentajeValue = document.createTextNode(impuestosFactura.getImpuestoTarifa().getCodigoSri());
                    codigoPorcentaje.appendChild(codigoPorcentajeValue);
                    
                    Element tarifa = document.createElement("tarifa"); 
                    Text tarifaValue = document.createTextNode(String.valueOf(impuestosFactura.getPorcentaje()));
                    tarifa.appendChild(tarifaValue);
                    
                    Element baseImponible = document.createElement("baseImponible"); 
                    Text baseImponibleValue = document.createTextNode(impuestosFactura.getBaseImponible().toString());
                    baseImponible.appendChild(baseImponibleValue);
                    
                    
                    Element valor = document.createElement("valor"); 
                    Text valorValue = document.createTextNode(impuestosFactura.getValor().toString());
                    valor.appendChild(valorValue);
                    
                    totalImpuesto.appendChild(codigo);
                    totalImpuesto.appendChild(codigoPorcentaje);
                    totalImpuesto.appendChild(tarifa);
                    totalImpuesto.appendChild(baseImponible);
                    totalImpuesto.appendChild(valor);
            
                totalConImpuestos.appendChild(totalImpuesto);
            }
            
            
            Element importeTotal = document.createElement("valorTotal"); 
            Text importeTotalValue = document.createTextNode(factura.getTotal().toString());
            importeTotal.appendChild(importeTotalValue);

            infoFacturaNode.appendChild(fechaEmision);
            infoFacturaNode.appendChild(dirEstablecimiento);
            infoFacturaNode.appendChild(tipoIdentificacionComprador);
            infoFacturaNode.appendChild(razonSocialComprador);
            infoFacturaNode.appendChild(identificacionComprador);
            infoFacturaNode.appendChild(obligadoContabilidad);
            infoFacturaNode.appendChild(codDocModificado);
            infoFacturaNode.appendChild(numDocModificado);
            infoFacturaNode.appendChild(fechaEmisionDocSustento);
            infoFacturaNode.appendChild(totalSinImpuestos);
            infoFacturaNode.appendChild(totalConImpuestos);
            infoFacturaNode.appendChild(importeTotal);
            
            Element pagos = document.createElement("pagos"); 
             
                for(FacturaPago pagoFactua : factura.getFacturaPagoList()){
                    
                    Element pago = document.createElement("pago"); 
                    
                    Element formaPago = document.createElement("formaPago"); 
                    Text formaPagoValue;
                    if(pagoFactua.getCuentaFacturaList() == null || pagoFactua.getCuentaFacturaList().isEmpty()){
                        
                        formaPagoValue = document.createTextNode(pagoFactua.getFormaPago().getCodigoSRI());
                    }
                    else
                    {
                        formaPagoValue = document.createTextNode(pagoFactua.getCuentaFacturaList().get(0).getFormaPago().getCodigoSRI());
                    }
                    formaPago.appendChild(formaPagoValue);
                    
                    Element total = document.createElement("total"); 
                    Text totalValue = document.createTextNode(pagoFactua.getValor().toString());
                    total.appendChild(totalValue);
            
                    pago.appendChild(formaPago);
                    pago.appendChild(total);

                    pagos.appendChild(pago);
                }
                
            infoFacturaNode.appendChild(pagos); 
            
        raiz.appendChild(infoFacturaNode);
        
        Element detalles = document.createElement("motivos"); 
        
            for(FacturaDetalle detalleFactura : factura.getFacturaDetalleList()){

                Element detalle = document.createElement("motivo"); 

                Element descripcion = document.createElement("razon"); 
                Text descripcionValue = document.createTextNode(detalleFactura.getProductoServicio().getNombre());
                descripcion.appendChild(descripcionValue);
                
                Element precioTotalSinImpuesto = document.createElement("valor"); 
                Text precioTotalSinImpuestoValue = document.createTextNode(detalleFactura.getSubtotalConDescuento().toString());
                precioTotalSinImpuesto.appendChild(precioTotalSinImpuestoValue);

                detalle.appendChild(descripcion);
                detalle.appendChild(precioTotalSinImpuesto);
                
                detalles.appendChild(detalle);
            }
        
        raiz.appendChild(detalles);
        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa() + factura.getCodigoBarras() + ".xml";
        Result result = new StreamResult(new File(url)); 
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        
        return url;
    }
    
    public String generateGuiaRemision(GuiaRemision factura) throws Exception{
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(Boolean.TRUE); 
        Element rootElement = document.createElement("guiaRemision");
        document.appendChild(rootElement);

        Attr attr = document.createAttribute("id");
        attr.setValue("comprobante");
        rootElement.setAttributeNode(attr);

        Attr attr2 = document.createAttribute("version");
        attr2.setValue("1.1.0");
        rootElement.setAttributeNode(attr2);

        //Main Node
        Element raiz = document.getDocumentElement();

        Element itemInfoTributariaNode = document.createElement("infoTributaria"); 

            Element ambiente = document.createElement("ambiente"); 
            Text ambienteValue = document.createTextNode(factura.getFactura().getPuntoVenta().getAmbienteElectronica());
            ambiente.appendChild(ambienteValue);

            Element tipoEmision = document.createElement("tipoEmision"); 
            Text tipoEmisionValue = document.createTextNode("1");
            tipoEmision.appendChild(tipoEmisionValue);

            Element razonSocial = document.createElement("razonSocial"); 
            Text razonSocialValue = document.createTextNode(factura.getFactura().getPuntoVenta().getRazonSocial().trim());
            razonSocial.appendChild(razonSocialValue);

            Element nombreComercial = document.createElement("nombreComercial"); 
            Text nombreComercialValue = document.createTextNode(factura.getFactura().getPuntoVenta().getNombre().trim());
            nombreComercial.appendChild(nombreComercialValue);

            Element ruc = document.createElement("ruc"); 
            Text rucValue = document.createTextNode(factura.getFactura().getPuntoVenta().getRuc());
            ruc.appendChild(rucValue);
            
            Element claveAcceso = document.createElement("claveAcceso"); 
            Text claveAccesoValue = document.createTextNode(factura.getCodigoBarras());
            claveAcceso.appendChild(claveAccesoValue);
            
            Element codDoc = document.createElement("codDoc"); 
            Text codDocValue = document.createTextNode("06");
            codDoc.appendChild(codDocValue);
            
            Element estab = document.createElement("estab"); 
            Text estabValue = document.createTextNode(factura.getFactura().getEmpresa().getCodigoSri());
            estab.appendChild(estabValue);
            
            Element ptoEmi = document.createElement("ptoEmi"); 
            Text ptoEmiValue = document.createTextNode(factura.getFactura().getPuntoVenta().getCodigoSri());
            ptoEmi.appendChild(ptoEmiValue);
            
            Element secuencial = document.createElement("secuencial"); 
            Text secuencialValue = document.createTextNode(this.generaSecuencia(factura.getSecuencia().toString()));
            secuencial.appendChild(secuencialValue);
            
            Element dirMatriz = document.createElement("dirMatriz"); 
            Text dirMatrizValue = document.createTextNode(factura.getFactura().getPuntoVenta().getDireccion().trim());
            dirMatriz.appendChild(dirMatrizValue);

        itemInfoTributariaNode.appendChild(ambiente);
        itemInfoTributariaNode.appendChild(tipoEmision);
        itemInfoTributariaNode.appendChild(razonSocial);
        itemInfoTributariaNode.appendChild(nombreComercial);
        itemInfoTributariaNode.appendChild(ruc);
        itemInfoTributariaNode.appendChild(claveAcceso);
        itemInfoTributariaNode.appendChild(codDoc);
        itemInfoTributariaNode.appendChild(estab);
        itemInfoTributariaNode.appendChild(ptoEmi);
        itemInfoTributariaNode.appendChild(secuencial);
        itemInfoTributariaNode.appendChild(dirMatriz);

        raiz.appendChild(itemInfoTributariaNode);
        
        Element infoFacturaNode = document.createElement("infoGuiaRemision"); 
        
            Element dirEstablecimiento = document.createElement("dirEstablecimiento"); 
            Text dirEstablecimientoValue = document.createTextNode(factura.getFactura().getPuntoVenta().getDireccion().trim());
            dirEstablecimiento.appendChild(dirEstablecimientoValue);
            
            Element dirPartida = document.createElement("dirPartida"); 
            Text dirPartidaValue = document.createTextNode(factura.getTransportista().getPersona().getDireccion().trim());
            dirPartida.appendChild(dirPartidaValue);
            
            Element razonSocialTransportista = document.createElement("razonSocialTransportista"); 
            Text razonSocialTransportistaValue = document.createTextNode(factura.getTransportista().getPersona().getNombres().trim());
            razonSocialTransportista.appendChild(razonSocialTransportistaValue);
            
            Element tipoIdentificacionTransportista = document.createElement("tipoIdentificacionTransportista"); 
            Text tipoIdentificacionTransportistaValue = document.createTextNode(factura.getTransportista().getPersona().getTipoIdentificacion().getCodigoSRI());
            tipoIdentificacionTransportista.appendChild(tipoIdentificacionTransportistaValue);
            
            Element rucTransportista = document.createElement("rucTransportista"); 
            Text rucTransportistaValue = document.createTextNode(factura.getTransportista().getPersona().getCedula().trim());
            rucTransportista.appendChild(rucTransportistaValue);
            
            Element obligadoContabilidad = document.createElement("obligadoContabilidad"); 
            Text obligadoContabilidadValue = document.createTextNode("NO");
            obligadoContabilidad.appendChild(obligadoContabilidadValue);
            
            Element fechaIniTransporte = document.createElement("fechaIniTransporte"); 
            Text fechaIniTransporteValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFechaEnvio()));
            fechaIniTransporte.appendChild(fechaIniTransporteValue);
            
            Element fechaFinTransporte = document.createElement("fechaFinTransporte"); 
            Text fechaFinTransporteValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFechaRecepcion()));
            fechaFinTransporte.appendChild(fechaFinTransporteValue);

            Element placa = document.createElement("placa"); 
            Text placaValue = document.createTextNode(factura.getPlaca().trim());
            placa.appendChild(placaValue);
            
            infoFacturaNode.appendChild(dirEstablecimiento);
            infoFacturaNode.appendChild(dirPartida);
            infoFacturaNode.appendChild(razonSocialTransportista);
            infoFacturaNode.appendChild(tipoIdentificacionTransportista);
            infoFacturaNode.appendChild(rucTransportista);
            infoFacturaNode.appendChild(obligadoContabilidad);
            infoFacturaNode.appendChild(fechaIniTransporte);
            infoFacturaNode.appendChild(fechaFinTransporte);
            infoFacturaNode.appendChild(placa); 
            
        raiz.appendChild(infoFacturaNode);
        
        Element destinatarios = document.createElement("destinatarios"); 
            Element destinatario = document.createElement("destinatario"); 
            
            Element identificacionDestinatario = document.createElement("identificacionDestinatario"); 
            Text identificacionDestinatarioValue = document.createTextNode(factura.getDestinatario().getPersona().getCedula().trim());
            identificacionDestinatario.appendChild(identificacionDestinatarioValue);
            
            Element razonSocialDestinatario = document.createElement("razonSocialDestinatario"); 
            Text razonSocialDestinatarioValue = document.createTextNode(factura.getDestinatario().getPersona().getApellidos().trim() + " " + factura.getDestinatario().getPersona().getNombres().trim());
            razonSocialDestinatario.appendChild(razonSocialDestinatarioValue);
            
            Element dirDestinatario = document.createElement("dirDestinatario"); 
            Text dirDestinatarioValue = document.createTextNode(factura.getDestinatario().getPersona().getDireccion().trim());
            dirDestinatario.appendChild(dirDestinatarioValue);
            
            Element motivoTraslado = document.createElement("motivoTraslado"); 
            Text motivoTrasladoValue = document.createTextNode(factura.getMotivo().getNombre());
            motivoTraslado.appendChild(motivoTrasladoValue);
            
            Element ruta = document.createElement("ruta"); 
            Text rutaValue = document.createTextNode(factura.getRuta().trim());
            ruta.appendChild(rutaValue);
            
            Element codDocSustento = document.createElement("codDocSustento"); 
            Text codDocSustentoValue = document.createTextNode("01");
            codDocSustento.appendChild(codDocSustentoValue);
            
            Element numDocSustento = document.createElement("numDocSustento"); 
            Text numDocSustentoValue = document.createTextNode(factura.getFactura().getEmpresa().getCodigoSri()+"-"+factura.getFactura().getPuntoVenta().getCodigoSri()+"-"+this.generaSecuencia(factura.getFactura().getNumero().toString()));
            numDocSustento.appendChild(numDocSustentoValue);
            
            Element numAutDocSustento = document.createElement("numAutDocSustento"); 
            Text numAutDocSustentoValue = document.createTextNode(factura.getFactura().getCodigoBarras().trim());
            numAutDocSustento.appendChild(numAutDocSustentoValue);
            
            Element fechaEmisionDocSustento = document.createElement("fechaEmisionDocSustento"); 
            Text fechaEmisionDocSustentoValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFactura().getFecha()));
            fechaEmisionDocSustento.appendChild(fechaEmisionDocSustentoValue);
            
            Element detalles = document.createElement("detalles"); 
        
                for(FacturaDetalle detalleFactura : factura.getFactura().getFacturaDetalleList()){

                    Element detalle = document.createElement("detalle"); 

                    Element codigoPrincipal = document.createElement("codigoInterno"); 
                    Text codigoPrincipalValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                    codigoPrincipal.appendChild(codigoPrincipalValue);

                    Element codigoAuxiliar = document.createElement("codigoAdicional"); 
                    Text codigoAuxiliarValue = document.createTextNode(detalleFactura.getProductoServicio().getCodigo().toString());
                    codigoAuxiliar.appendChild(codigoAuxiliarValue);

                    Element descripcion = document.createElement("descripcion"); 
                    Text descripcionValue = document.createTextNode(detalleFactura.getProductoServicio().getNombre());
                    descripcion.appendChild(descripcionValue);

                    Element cantidad = document.createElement("cantidad"); 
                    Text cantidadValue = document.createTextNode(detalleFactura.getCantidad().toString());
                    cantidad.appendChild(cantidadValue);


                    detalle.appendChild(codigoPrincipal);
                    detalle.appendChild(codigoAuxiliar);
                    detalle.appendChild(descripcion);
                    detalle.appendChild(cantidad);

                    detalles.appendChild(detalle);
                }
                
                destinatario.appendChild(identificacionDestinatario);
                destinatario.appendChild(razonSocialDestinatario);
                destinatario.appendChild(dirDestinatario);
                destinatario.appendChild(motivoTraslado);
                destinatario.appendChild(ruta);
                destinatario.appendChild(codDocSustento);
                destinatario.appendChild(numDocSustento);
                destinatario.appendChild(numAutDocSustento);
                destinatario.appendChild(fechaEmisionDocSustento);
                destinatario.appendChild(detalles);
                
            destinatarios.appendChild(destinatario);  
         raiz.appendChild(destinatarios);       
        
        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa() + factura.getCodigoBarras() + ".xml";
        Result result = new StreamResult(new File(url)); 
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        
        return url;
    }
    
    public String generateDocumentoRetencion(DocumentoRetencion factura) throws Exception{
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();
        document.setXmlVersion("1.0");
        document.setXmlStandalone(Boolean.TRUE); 
        Element rootElement = document.createElement("comprobanteRetencion");
        document.appendChild(rootElement);

        Attr attr = document.createAttribute("id");
        attr.setValue("comprobante");
        rootElement.setAttributeNode(attr);

        Attr attr2 = document.createAttribute("version");
        attr2.setValue("1.0.0");
        rootElement.setAttributeNode(attr2);

        //Main Node
        Element raiz = document.getDocumentElement();

        Element itemInfoTributariaNode = document.createElement("infoTributaria"); 

            Element ambiente = document.createElement("ambiente"); 
            Text ambienteValue = document.createTextNode(factura.getFactura().getPuntoVenta().getAmbienteElectronica());
            ambiente.appendChild(ambienteValue);

            Element tipoEmision = document.createElement("tipoEmision"); 
            Text tipoEmisionValue = document.createTextNode("1");
            tipoEmision.appendChild(tipoEmisionValue);

            Element razonSocial = document.createElement("razonSocial"); 
            Text razonSocialValue = document.createTextNode(factura.getFactura().getPuntoVenta().getRazonSocial().trim());
            razonSocial.appendChild(razonSocialValue);

            Element nombreComercial = document.createElement("nombreComercial"); 
            Text nombreComercialValue = document.createTextNode(factura.getFactura().getPuntoVenta().getNombre().trim());
            nombreComercial.appendChild(nombreComercialValue);

            Element ruc = document.createElement("ruc"); 
            Text rucValue = document.createTextNode(factura.getFactura().getPuntoVenta().getRuc());
            ruc.appendChild(rucValue);
            
            Element claveAcceso = document.createElement("claveAcceso"); 
            Text claveAccesoValue = document.createTextNode(factura.getCodigoBarras());
            claveAcceso.appendChild(claveAccesoValue);
            
            Element codDoc = document.createElement("codDoc"); 
            Text codDocValue = document.createTextNode("07");
            codDoc.appendChild(codDocValue);
            
            Element estab = document.createElement("estab"); 
            Text estabValue = document.createTextNode(factura.getFactura().getEmpresa().getCodigoSri());
            estab.appendChild(estabValue);
            
            Element ptoEmi = document.createElement("ptoEmi"); 
            Text ptoEmiValue = document.createTextNode(factura.getFactura().getEmpleado().getPuntoVenta().getCodigoSri());
            ptoEmi.appendChild(ptoEmiValue);
            
            Element secuencial = document.createElement("secuencial"); 
            Text secuencialValue = document.createTextNode(this.generaSecuencia(factura.getNumero().toString()));
            secuencial.appendChild(secuencialValue);
            
            Element dirMatriz = document.createElement("dirMatriz"); 
            Text dirMatrizValue = document.createTextNode(factura.getFactura().getPuntoVenta().getDireccion().trim());
            dirMatriz.appendChild(dirMatrizValue);

        itemInfoTributariaNode.appendChild(ambiente);
        itemInfoTributariaNode.appendChild(tipoEmision);
        itemInfoTributariaNode.appendChild(razonSocial);
        itemInfoTributariaNode.appendChild(nombreComercial);
        itemInfoTributariaNode.appendChild(ruc);
        itemInfoTributariaNode.appendChild(claveAcceso);
        itemInfoTributariaNode.appendChild(codDoc);
        itemInfoTributariaNode.appendChild(estab);
        itemInfoTributariaNode.appendChild(ptoEmi);
        itemInfoTributariaNode.appendChild(secuencial);
        itemInfoTributariaNode.appendChild(dirMatriz);

        raiz.appendChild(itemInfoTributariaNode);
        
        Element infoFacturaNode = document.createElement("infoCompRetencion"); 
        
            Element fechaEmision = document.createElement("fechaEmision"); 
            Text fechaEmisionValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFecha()));
            fechaEmision.appendChild(fechaEmisionValue);
        
            Element dirEstablecimiento = document.createElement("dirEstablecimiento"); 
            Text dirEstablecimientoValue = document.createTextNode(factura.getFactura().getPuntoVenta().getDireccion().trim());
            dirEstablecimiento.appendChild(dirEstablecimientoValue);
            
            Element obligadoContabilidad = document.createElement("obligadoContabilidad"); 
            Text obligadoContabilidadValue = document.createTextNode("NO");
            obligadoContabilidad.appendChild(obligadoContabilidadValue);
            
            Element tipoIdentificacionSujetoRetenido = document.createElement("tipoIdentificacionSujetoRetenido"); 
            Text tipoIdentificacionSujetoRetenidoValue = document.createTextNode(factura.getFactura().getProveedor().getPersona().getTipoIdentificacion().getCodigoSRI());
            tipoIdentificacionSujetoRetenido.appendChild(tipoIdentificacionSujetoRetenidoValue);
            
            Element razonSocialSujetoRetenido = document.createElement("razonSocialSujetoRetenido"); 
            Text razonSocialSujetoRetenidoValue = document.createTextNode(factura.getFactura().getProveedor().getPersona().getNombres().trim() + " " + factura.getFactura().getProveedor().getPersona().getApellidos());
            razonSocialSujetoRetenido.appendChild(razonSocialSujetoRetenidoValue);
            
            Element identificacionSujetoRetenido = document.createElement("identificacionSujetoRetenido"); 
            Text identificacionSujetoRetenidoValue = document.createTextNode(factura.getFactura().getProveedor().getPersona().getCedula().trim());
            identificacionSujetoRetenido.appendChild(identificacionSujetoRetenidoValue);

            Element periodoFiscal = document.createElement("periodoFiscal"); 
            String mesPeriodo = String.valueOf(Fecha.getMes(factura.getFecha()));
            if(mesPeriodo.length() == 1){
                mesPeriodo = "0" + mesPeriodo;
            }
            Text periodoFiscalValue = document.createTextNode( mesPeriodo + "/" + String.valueOf(Fecha.getAnio(factura.getFecha())));
            periodoFiscal.appendChild(periodoFiscalValue);
            
            infoFacturaNode.appendChild(fechaEmision);
            infoFacturaNode.appendChild(dirEstablecimiento);
            infoFacturaNode.appendChild(obligadoContabilidad);
            infoFacturaNode.appendChild(tipoIdentificacionSujetoRetenido);
            infoFacturaNode.appendChild(razonSocialSujetoRetenido);
            infoFacturaNode.appendChild(identificacionSujetoRetenido);
            infoFacturaNode.appendChild(periodoFiscal);
            
        raiz.appendChild(infoFacturaNode);
        
        Element impuestos = document.createElement("impuestos"); 

            for(Retencion retencionDetalle : factura.getRetencionList()){

                Element impuesto = document.createElement("impuesto"); 

                Element codigo = document.createElement("codigo"); 
                Text codigoValue = document.createTextNode(retencionDetalle.getTipoRetencion().getCalculadoCon().equals("1") ? "2" : "1" );
                codigo.appendChild(codigoValue);

                Element codigoRetencion = document.createElement("codigoRetencion"); 
                Text codigoRetencionValue = document.createTextNode(retencionDetalle.getTipoRetencion().getCodigoImpuesto());
                codigoRetencion.appendChild(codigoRetencionValue);

                Element baseImponible = document.createElement("baseImponible"); 
                Text baseImponibleValue = document.createTextNode(retencionDetalle.getBaseImponible().toString());
                baseImponible.appendChild(baseImponibleValue);

                Element porcentajeRetener = document.createElement("porcentajeRetener"); 
                Text porcentajeRetenerValue = document.createTextNode(retencionDetalle.getTipoRetencion().getValor().toString());
                porcentajeRetener.appendChild(porcentajeRetenerValue);

                Element valorRetenido = document.createElement("valorRetenido"); 
                Text valorRetenidoValue = document.createTextNode(retencionDetalle.getValor().toString());
                valorRetenido.appendChild(valorRetenidoValue);

                Element codDocSustento = document.createElement("codDocSustento"); 
                Text codDocSustentoValue = null;
                if(retencionDetalle.getDocumentoRetencion().getFactura() instanceof FacturaCompra){
                    codDocSustentoValue = document.createTextNode("01");
                }
                if(retencionDetalle.getDocumentoRetencion().getFactura() instanceof NotaDebito){
                    codDocSustentoValue = document.createTextNode("05");
                }
                codDocSustento.appendChild(codDocSustentoValue);

                Element numDocSustento = document.createElement("numDocSustento"); 
                String numeroSustento1 = retencionDetalle.getDocumentoRetencion().getFactura().getObservacion().replace("-", "");
                String numeroSustento2 = this.generaSecuencia(retencionDetalle.getDocumentoRetencion().getFactura().getNumero().toString());
                Text numDocSustentoValue = document.createTextNode(numeroSustento1.concat(numeroSustento2)); 
                numDocSustento.appendChild(numDocSustentoValue);

                Element fechaEmisionDocSustento = document.createElement("fechaEmisionDocSustento"); 
                Text fechaEmisionDocSustentoValue = document.createTextNode(Fecha.formatoDateStringF0(factura.getFactura().getFecha()));
                fechaEmisionDocSustento.appendChild(fechaEmisionDocSustentoValue);
                
                impuesto.appendChild(codigo);
                impuesto.appendChild(codigoRetencion);
                impuesto.appendChild(baseImponible);
                impuesto.appendChild(porcentajeRetener);
                impuesto.appendChild(valorRetenido);
                impuesto.appendChild(codDocSustento);
                impuesto.appendChild(numDocSustento);
                impuesto.appendChild(fechaEmisionDocSustento);

                impuestos.appendChild(impuesto);  
            }
                
        raiz.appendChild(impuestos);       
        
        Element infoAdicional = document.createElement("infoAdicional"); 
        
            Element campoAdicional1  = document.createElement("campoAdicional"); 
            Text campoAdicional1Value = document.createTextNode(factura.getFactura().getProveedor().getPersona().getDireccion() == null ? "S/D" : factura.getFactura().getProveedor().getPersona().getDireccion());
            campoAdicional1.appendChild(campoAdicional1Value);
            
            Attr attrNombre1 = document.createAttribute("nombre");
            attrNombre1.setValue("Dirección");
            campoAdicional1.setAttributeNode(attrNombre1);
            
            Element campoAdicional2  = document.createElement("campoAdicional"); 
            Text campoAdicional2Value = document.createTextNode(factura.getFactura().getProveedor().getPersona().getEmail() == null ? "S/D" : factura.getFactura().getProveedor().getPersona().getEmail());
            campoAdicional2.appendChild(campoAdicional2Value);
            
            Attr attrNombre2 = document.createAttribute("nombre");
            attrNombre2.setValue("Email");
            campoAdicional2.setAttributeNode(attrNombre2);
            
            Element campoAdicional3  = document.createElement("campoAdicional"); 
            Text campoAdicional3Value = document.createTextNode(factura.getFactura().getProveedor().getPersona().getTelefono() == null ? "S/D" : factura.getFactura().getProveedor().getPersona().getTelefono());
            campoAdicional3.appendChild(campoAdicional3Value);
            
            Attr attrNombre3 = document.createAttribute("nombre");
            attrNombre3.setValue("Teléfono");
            campoAdicional3.setAttributeNode(attrNombre3);
            
            infoAdicional.appendChild(campoAdicional1);
            infoAdicional.appendChild(campoAdicional3);
            infoAdicional.appendChild(campoAdicional2);
            
        raiz.appendChild(infoAdicional);
        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        String url = ((Login)FacesUtils.getManagedBean("login")).getPathEmpresa() + factura.getCodigoBarras() + ".xml";
        Result result = new StreamResult(new File(url)); 
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        
        return url;
    }
    
    char[] temp=new char[]{ '0' };
    public String generaSecuencia(String numero){
        for(int i = numero.length() ; i<9 ; i++){
            numero = temp[0] + numero;
        }
        return numero;
    }
    
    public void eliminarFactura(int index){
        this.listaDocumentos.remove(index);
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
    }
    
    public void eliminarGuia(int index){
        this.listaGuiasRemision.remove(index);
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
    }
    
    public void eliminarRetencion(int index){
        this.listaDocumentoRetencion.remove(index);
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
    }

    public List<Factura> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Factura> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public List<GuiaRemision> getListaGuiasRemision() {
        return listaGuiasRemision;
    }

    public void setListaGuiasRemision(List<GuiaRemision> listaGuiasRemision) {
        this.listaGuiasRemision = listaGuiasRemision;
    }

    public List<DocumentoRetencion> getListaDocumentoRetencion() {
        return listaDocumentoRetencion;
    }

    public void setListaDocumentoRetencion(List<DocumentoRetencion> listaDocumentoRetencion) {
        this.listaDocumentoRetencion = listaDocumentoRetencion;
    }
}
