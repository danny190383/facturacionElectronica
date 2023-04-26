package com.jvc.factunet.print;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.PuntoVenta;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.Doc;
import javax.print.PrintException;

@ManagedBean
@ApplicationScoped
public class Ticket {
  
  private String contentTicket = "\n"+
                                 "\n"+
                                 "\n"+
                                 "         {{numero}}\n"+
                                 "{{mesa}}\n"+
                                 "{{ciudadFecha}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "DIRECCION: {{direccion}}\n"+
                                 "TELEFONO: {{telefono}}\n"+
                                 "RUC: {{cedula}}\n"+
                                 "-------------------------------------\n"+
                                 "CANT. DESCRIPCION        V.UNIT. V.TOTAL\n"+
                                 "--------------------------------------\n"+
                                 "{{items}}"+
                                 "--------------------------------------\n"+
                                 "              SUBTOTAL:        {{subTotal}}\n"+
                                 "              IVA tarifa 0%:   {{siniva}}\n"+
                                 "              IVA tarifa 12%:  {{coniva}}\n"+
                                 "              IMPORTE DEL IVA: {{iva}}\n"+
                                 "              SUMA TOTAL:      {{total}}\n"+
                                 ". \n"+
                                 ". \n"+
                                 ". \n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 " \n";
  
  private String contentTicketRise = "\n"+
                                     "\n"+
                                     "                   {{numero}}\n"+
                                     "{{mesa}}\n"+
                                     "{{ciudadFecha}}\n"+
                                     "CLIENTE: {{cliente}}\n"+
                                     "DIRECCION: {{direccion}}\n"+
                                     "TELEFONO: {{telefono}}\n"+
                                     "RUC: {{cedula}}\n"+
                                     "-------------------------------------\n"+
                                     "CANT. DESCRIPCION        V.UNIT. V.TOTAL\n"+
                                     "--------------------------------------\n"+
                                     "{{items}}"+
                                     "--------------------------------------\n"+
                                     "              TOTAL:       {{total}}\n"+
                                     ". \n"+
                                     ". \n"+
                                     "\n"+
                                     "\n"+
                                     "\n"+
                                     "\n"+
                                     "\n";

     private String contentTicketComprobante = 
                                 "\n"+
                                 "\n"+
                                 "--------------------------------\n"+
                                 "{{empresa}}\n"+
                                 "{{razon_social}}\n"+
                                 "{{ruc}}\n"+
                                 "{{descripcion}}\n"+
                                 "{{telefono_empresa}}\n"+
                                 "{{correo_empresa}}\n"+
                                 "{{direccion_empresa}}\n"+
                                 "{{ciudad_empresa}}\n"+
                                 "--------------------------------\n"+
                                 "FACTURA Nº: {{numero}}\n"+
                                 "Nº AUTORIZACION / CLAVE ACCESO:\n"+
                                 "{{clave_acceso}}\n"+
                                 "--------------------------------\n"+
                                 "CIUDAD/FECHA: {{ciudadFecha}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "DIRECCION: {{direccion}}\n"+
                                 "TELEFONO: {{telefono}}\n"+
                                 "CORREO: {{correo_cliente}}\n"+
                                 "CI/RUC: {{cedula}}\n"+
                                 "--------------------------------\n"+
                                 "CANT. DESCRIPCION        V.TOTAL\n"+
                                 "--------------------------------\n"+
                                 "{{items}}"+
                                 "--------------------------------\n"+
                                 "   SUBTOTAL:        {{subTotal}}\n"+
                                 "   DESCUENTO:       {{descuento}}\n"+
                                 "   IMPORTE DEL IVA: {{iva}}\n"+
                                 "   SUMA TOTAL:      {{total}}\n"+
                                 "\n"+
                                 "--------------------------------\n"+
                                 "REVICE SU FACTURA ELECTRÓNICA \n"+
                                 "INGRESANDO A SU CORREO        \n"+
                                 "\n"+
                                 "ESTE DOCUMENTO NO TIENE VALIDEZ \n"+
                                 "TRIBUTARIA \n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n";
    
    public Ticket(String numero, String ciudadFecha, String cliente, String direccion, String telefono, String cedula,String items, String subTotal, String siniva, String coniva, String iva, String total,String mesa) {
      this.contentTicket = this.contentTicket.replace("{{numero}}", numero);
      this.contentTicket = this.contentTicket.replace("{{ciudadFecha}}", ciudadFecha);
      this.contentTicket = this.contentTicket.replace("{{cliente}}", cliente);
      this.contentTicket = this.contentTicket.replace("{{direccion}}", direccion);
      this.contentTicket = this.contentTicket.replace("{{telefono}}", telefono);
      this.contentTicket = this.contentTicket.replace("{{cedula}}", cedula);
      this.contentTicket = this.contentTicket.replace("{{items}}", items);
      this.contentTicket = this.contentTicket.replace("{{subTotal}}", subTotal);
      this.contentTicket = this.contentTicket.replace("{{siniva}}", siniva);
      this.contentTicket = this.contentTicket.replace("{{coniva}}", coniva);
      this.contentTicket = this.contentTicket.replace("{{iva}}", iva);
      this.contentTicket = this.contentTicket.replace("{{total}}", total);
      this.contentTicket = this.contentTicket.replace("{{mesa}}", mesa);
    }
    
    public Ticket(String numero, String ciudadFecha, String cliente, String direccion, String telefono, String cedula, String items, String total,String mesa) {
      this.contentTicketRise = this.contentTicketRise.replace("{{numero}}", numero);
      this.contentTicketRise = this.contentTicketRise.replace("{{ciudadFecha}}", ciudadFecha);
      this.contentTicketRise = this.contentTicketRise.replace("{{cliente}}", cliente);
      this.contentTicketRise = this.contentTicketRise.replace("{{direccion}}", direccion);
      this.contentTicketRise = this.contentTicketRise.replace("{{telefono}}", telefono);
      this.contentTicketRise = this.contentTicketRise.replace("{{cedula}}", cedula);
      this.contentTicketRise = this.contentTicketRise.replace("{{items}}", items);
      this.contentTicketRise = this.contentTicketRise.replace("{{total}}", total);
      this.contentTicketRise = this.contentTicketRise.replace("{{mesa}}", mesa);
    }
    
    public Ticket(Empresa empresa,PuntoVenta punto, String numero, String clave, String ciudadFecha, String cliente, String direccion, String telefono,String cedula, String correo,String items, String subTotal,String iva, String total, String descuento) {
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{empresa}}", empresa.getNombreAbreviado());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{razon_social}}", punto.getRazonSocial());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{ruc}}", punto.getRuc());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{descripcion}}", empresa.getDescripcion());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{telefono_empresa}}", empresa.getTelefono1());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{correo_empresa}}", empresa.getEmail());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{direccion_empresa}}", punto.getDireccion());
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{ciudad_empresa}}", empresa.getCiudad().getNombre());
      
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{numero}}", numero);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{clave_acceso}}", clave);
      
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{ciudadFecha}}", ciudadFecha);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{cliente}}", cliente);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{direccion}}", direccion);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{telefono}}", telefono);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{cedula}}", cedula);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{correo_cliente}}", correo);
      
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{items}}", items);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{subTotal}}", subTotal);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{iva}}", iva);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{total}}", total);
      this.contentTicketComprobante = this.contentTicketComprobante.replace("{{descuento}}", descuento);
    }
    
    public void print(String impresora, String rise) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        byte[] bytes;
      switch (rise) {
          case "2":
              bytes = this.contentTicket.getBytes();
              break;
          case "1":
              bytes = this.contentTicketRise.getBytes();
              break;
          default:
              bytes = this.contentTicketComprobante.getBytes();
              break;
      }
        Doc doc = new SimpleDoc(bytes,flavor,null);
        DocPrintJob job = null;
        DocPrintJob jobCorte = null;
        DocPrintJob jobOpenCajon = null;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(impresora)) {
                    job = services[i].createPrintJob();
                    jobCorte = services[i].createPrintJob();
                    jobOpenCajon = services[i].createPrintJob();
                    System.out.println(i + ": " + services[i].getName());
                    break;
                }
            }
        }
        if(job != null)
        {
            try {
                
                //imprimir tiket
                job.print(doc, null);
                //open cajon
                byte[] bytesOpen = {27, 112, 0};
                DocFlavor flavorOpen = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc docOpen = new SimpleDoc(bytesOpen, flavorOpen, null);
                jobOpenCajon.print(docOpen, null);
                //corte de papel
                byte[] bytesCorte = {27, 109, 1};
                DocFlavor flavorCorte = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc docCorte = new SimpleDoc(bytesCorte, flavorCorte, null);
                jobCorte.print(docCorte, null);
            } catch (PrintException ex) {
                System.out.println("Error de driver o conexion");
            }
        }
        else
        {
            System.out.println("Impresora no encontrada");
        }
        
    }
}
