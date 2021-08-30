package com.jvc.factunet.consumo;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Stateless
public class XML_Utilitis {
    
//    Está función toma la ruta del documento firmado y lo 
//    convierte en un objeto Document, esto es para que pueda ser manejado más rápidamente 
//    en la ejecución de nuestro programa, lo único que nos solicita como parámetro es la 
//    ruta del documento.
    public Document getDoc(String dir) throws ParserConfigurationException, 
        IOException, org.xml.sax.SAXException{
        File fXmlFile = new File(dir);	
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = (Document) dBuilder.parse(fXmlFile);
        return doc;
    }
    
//    Esta función convierte en String el objeto Document,
//    para que podamos obtener parámetros, o escribir nuevos argumentos. 
//    A está función enviamos como parámetro un objeto Document.
    public String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource((Node) doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
    
 //     Esta función realiza lo contrario, vuelve el objeto String a un tipo Document
    public Document convertStringToDocument(String xmlStr) {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
           DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
 
// Esta función convierte en Base64 el objeto Document que contendría el XML, 
// proceso importante para el envío y recepción de datos en un Web Services
    public String converBase64(Document doc) throws UnsupportedEncodingException{
        String str = convertDocumentToString(doc);
        String bytesEncoded = DatatypeConverter.printBase64Binary(str.getBytes("UTF-8"));
        return bytesEncoded;
    }
 
// Esta función obtiene los datos jerarquicamente desde un archivo XML, 
// en este caso se encontraría embebido en un objeto Document, 
// adicional mente se encuentran dos parámetros que debemos de ingresar el rootNodo y el infoNodo 
// el cual nos devolverá la data dentro de las etiquetas, por ejemplo:
//
//<mensaje>
//<data>hola</data>
//</mensaje>
// En este caso el root es <mensaje> y el nodo a obtener es <data> nos devolverá 
// como String: hola, esa es la manera de funcionar de la función getNodes(), a continuación.
    public String getNodes(String rootNodo, String infoNodo, Document doc){
        String resultNodo = null;          
        Element docEle = doc.getDocumentElement(); 
        NodeList studentList = docEle.getElementsByTagName(rootNodo);
        if(studentList.getLength()>0){
            Node node = studentList.item(0);            
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                   Element e = (Element) node;
                   NodeList nodeList = e.getElementsByTagName(infoNodo);
                   if(nodeList.item(0) != null){
                      resultNodo = nodeList.item(0).getChildNodes().item(0).getNodeValue();
                   }
              }
        }
        return resultNodo == null ? null : resultNodo.toString();
    }

//Esta función podría inclusive reemplazar a la función anterior y 
//los que hace es coleccionar de una estructura XML repetitiva el último tag, 
//esto se puede aprender de mejor manera en el proceso.
    public String getLastNode(String pathLevelXML, String nodo, Document doc) throws XPathExpressionException{
        //Ejemplo: //RespuestaAutorizacionComprobante/autorizaciones/autorizacion[last()]/estado
        String pathFull = pathLevelXML + nodo;
        XPath xpath = XPathFactory.newInstance().newXPath();
        return xpath.evaluate(pathFull, doc);
    }
 
    public java.net.Proxy setProxy(String ip, int port){
        java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        return proxy;
    }
  
//  Esta función enviará al servicio web de forma parametrizada  
//  los comprobantes que queremos enviar al web services para que pueda 
//  recibir el comprobante, como se puede observar es una estructura 
//  basada en SOAP a la cual se le envian los parámetros en el tag 
//  <ec:validarComprobante></ec:validarComprobante>, está es la forma como el software SoapUI, lo hace
    public String formatSendPost(String bytesEncodeBase64){
        String xml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.recepcion'>"+
        "<soapenv:Header/>"+
        "<soapenv:Body>"+
        "<ec:validarComprobante>"+
        "<xml>"+bytesEncodeBase64+"</xml>"+
        "</ec:validarComprobante>"+
        "</soapenv:Body>"+
        "</soapenv:Envelope>";
        return xml;
    }
    
    
    public String formatSendPostAutorizacion(String claveAcceso){
        String xml = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ec='http://ec.gob.sri.ws.autorizacion'>"+
        "<soapenv:Header/>"+
        "<soapenv:Body>"+
        "<ec:autorizacionComprobante>"+
        "<claveAccesoComprobante>"+claveAcceso+"</claveAccesoComprobante>"+
        "</ec:autorizacionComprobante>"+
        "</soapenv:Body>"+
        "</soapenv:Envelope>";
        return xml;
    }
}
