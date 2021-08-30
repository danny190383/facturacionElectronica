package com.jvc.factunet.consumo;

import com.jvc.factunet.objetos.RespuestaAutorizacion;
import com.jvc.factunet.objetos.RespuestaRecepcion;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.w3c.dom.Document;

@LocalBean
@Stateless
public class ProcesoConsumoWebService {
    
    @EJB
    private XML_Utilitis xml_utilidades;
    
    public RespuestaRecepcion sendPostSoapRecepcion(String urlWebServices, String method, String host, String getEncodeXML, Proxy proxy){    
        try {
            URL oURL = new URL(urlWebServices);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection(proxy);
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "");
            con.setRequestProperty("Host", host);     
            OutputStream reqStreamOut = con.getOutputStream();
            reqStreamOut.write(getEncodeXML.getBytes());
            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream(), "UTF8"));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) 
                sb.append(line);
            RespuestaRecepcion respuesta = getEstadoPostSoapRecepcion(xml_utilidades.convertStringToDocument(sb.toString()),
                              "RespuestaRecepcionComprobante",
                              "estado");//está extrae la data de los nodos en un archivo XML
            con.disconnect();
            return respuesta;
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    } 
    
    public RespuestaRecepcion getEstadoPostSoapRecepcion(Document doc, String nodoRaiz, String nodoElemento){
        RespuestaRecepcion respuesta = new RespuestaRecepcion();
        respuesta.setEstado(xml_utilidades.getNodes(nodoRaiz, nodoElemento, doc));
        if(respuesta.getEstado().equals("DEVUELTA")){
           respuesta.setClaveAccceso(xml_utilidades.getNodes("comprobante","claveAcceso", doc)); 
           System.out.println("Clave de Accceso: " + respuesta.getClaveAccceso());
           respuesta.setIdentificadorError(xml_utilidades.getNodes("mensaje","identificador", doc));
           System.out.println("Identificador Error: " + respuesta.getIdentificadorError());
           respuesta.setDescripciónError(xml_utilidades.getNodes("mensaje","mensaje", doc)); 
           System.out.println("Descripción Error: " + respuesta.getDescripciónError());
           respuesta.setDescripciónAdicionalError(xml_utilidades.getNodes("mensaje","informacionAdicional", doc)); 
           System.out.println("Descripción Adicional Error: " + respuesta.getDescripciónAdicionalError());
           respuesta.setTipoMensaje(xml_utilidades.getNodes("mensaje","tipo", doc));
           System.out.println("Tipo mensaje: " + respuesta.getTipoMensaje()); 
        }
        else if(respuesta.getEstado().equals("RECIBIDA"))
        {
            System.out.println("RECIBIDA");           
        }
        return respuesta;
    }
    
    public RespuestaAutorizacion sendPostSoapAutorizacion(String urlWebServices, String method, String host, String claveAcceso, Proxy proxy){    
        try {
            URL oURL = new URL(urlWebServices);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection(proxy);
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", "");
            con.setRequestProperty("Host", host); 
            OutputStream reqStreamOut = con.getOutputStream();
            reqStreamOut.write(claveAcceso.getBytes());
            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream(), "UTF8"));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) 
                sb.append(line);
            RespuestaAutorizacion respuesta = getEstadoPostSoapAutorizacion(xml_utilidades.convertStringToDocument(sb.toString()),
                              "autorizacion",
                              "estado");//está extrae la data de los nodos en un archivo XML
            con.disconnect();
            return respuesta;
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
        
    public RespuestaAutorizacion getEstadoPostSoapAutorizacion(Document doc, String nodoRaiz, String nodoElemento){
        RespuestaAutorizacion respuesta = new RespuestaAutorizacion();
        respuesta.setEstado(xml_utilidades.getNodes(nodoRaiz, nodoElemento, doc));
//        if(respuesta.getEstado().equals("AUTORIZADO")){
           respuesta.setFechaAutorizacion(xml_utilidades.getNodes("autorizacion","fechaAutorizacion", doc)); 
           System.out.println("Fecha de Autorizacion: " + respuesta.getFechaAutorizacion());
           respuesta.setAmbiente(xml_utilidades.getNodes("autorizacion","ambiente", doc));
           System.out.println("Ambiente: " + respuesta.getAmbiente());
//        }
//        else 
//        {
           respuesta.setIdentificadorError(xml_utilidades.getNodes("mensaje","identificador", doc)); 
           System.out.println("Identificador Error: " + respuesta.getIdentificadorError());
           respuesta.setMensajeError(xml_utilidades.getNodes("mensaje","mensaje", doc)); 
           System.out.println("Descripción Error: " + respuesta.getMensajeError());     
           respuesta.setDescripciónAdicionalError(xml_utilidades.getNodes("mensaje","informacionAdicional", doc)); 
           System.out.println("Descripción Adicional Error: " + respuesta.getDescripciónAdicionalError());
           respuesta.setTipoMensaje(xml_utilidades.getNodes("mensaje","tipo", doc));
           System.out.println("Tipo mensaje: " + respuesta.getTipoMensaje());
//        }
        return respuesta;
    }
    
}
