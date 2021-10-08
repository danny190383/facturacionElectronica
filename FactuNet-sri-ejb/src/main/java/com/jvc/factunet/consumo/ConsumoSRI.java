package com.jvc.factunet.consumo;

import com.jvc.factunet.objetos.RespuestaAutorizacion;
import com.jvc.factunet.objetos.RespuestaRecepcion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

@Stateless
public class ConsumoSRI {
    
    @EJB
    private ProcesoConsumoWebService procesoConsumoWebService;
    @EJB
    private XML_Utilitis xml_Utilitis;
    
    public RespuestaRecepcion consumirRecepcionPruebas(String url) throws ParserConfigurationException, UnsupportedEncodingException, IOException, org.xml.sax.SAXException{
        try {
            Document document = xml_Utilitis.getDoc(url);
            String codifocado = xml_Utilitis.converBase64(document);
            String xml = xml_Utilitis.formatSendPost(codifocado);
            return this.procesoConsumoWebService.sendPostSoapRecepcion("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl",
                                                       "POST", 
                                                       "celcer.sri.gob.ec", 
                                                       xml, 
                                                       Proxy.NO_PROXY);
        } catch (Exception e) {
            return null;
        }
    }
    
    public RespuestaAutorizacion consumirAutorizacionPruebas(String claveAcceso) throws ParserConfigurationException, UnsupportedEncodingException, IOException, org.xml.sax.SAXException{
        try {
            String xml = xml_Utilitis.formatSendPostAutorizacion(claveAcceso);
            return this.procesoConsumoWebService.sendPostSoapAutorizacion("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl",
                                                       "POST", 
                                                       "celcer.sri.gob.ec", 
                                                       xml, 
                                                       Proxy.NO_PROXY);
        } catch (Exception e) {
            return null;
        }
    }
    
    public RespuestaRecepcion consumirRecepcionProduccion(String url) throws ParserConfigurationException, UnsupportedEncodingException, IOException, org.xml.sax.SAXException{
        try {
            Document document = xml_Utilitis.getDoc(url);
            String codifocado = xml_Utilitis.converBase64(document);
            String xml = xml_Utilitis.formatSendPost(codifocado);
            return this.procesoConsumoWebService.sendPostSoapRecepcion("https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl",
                                                       "POST", 
                                                       "cel.sri.gob.ec", 
                                                       xml, 
                                                       Proxy.NO_PROXY);
        } catch (Exception e) {
            return null;
        }
    }
    
    public RespuestaAutorizacion consumirAutorizacionProduccion(String claveAcceso) throws ParserConfigurationException, UnsupportedEncodingException, IOException, org.xml.sax.SAXException{
        try {
            String xml = xml_Utilitis.formatSendPostAutorizacion(claveAcceso);
            return this.procesoConsumoWebService.sendPostSoapAutorizacion("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl",
                                                       "POST", 
                                                       "cel.sri.gob.ec", 
                                                       xml, 
                                                       Proxy.NO_PROXY);
        } catch (Exception e) {
            return null;
        }
    }
}
