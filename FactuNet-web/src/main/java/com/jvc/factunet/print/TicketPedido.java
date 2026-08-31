package com.jvc.factunet.print;

import java.io.UnsupportedEncodingException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

@ManagedBean
@ApplicationScoped
public class TicketPedido {
    
    private final String ESC = "\u001B";
    private final String INITIALIZE = ESC + "@";
    
    private final String FONT_SIZE_BIG = ESC + "!" + "\u0018"; 
    private final String FONT_SIZE_NORMAL = ESC + "!" + "\u0000"; 
    
     private String contentTicket = 
                                 INITIALIZE + 
                                 "\n"+
                                 "--------------- PEDIDO --------------\n"+ 
                                 FONT_SIZE_BIG + "  PEDIDO:  {{empresa}}\n" + FONT_SIZE_NORMAL +  
                                 FONT_SIZE_BIG + "  MESA:    {{mesa}}\n" + FONT_SIZE_NORMAL +   
                                 "FECHA:   {{ciudadFecha}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "--------------------------------------\n"+
                                 "CANT.         PRODUCTO \n"+
                                 "--------------------------------------\n"+
                                 "{{items}}"+
                                 "--------------------------------------\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n";
     
      private String contentTicketValores = 
                                 INITIALIZE +
                                 "\n"+
                                 "--------------PRE FACTURA-------------\n"+
                                 FONT_SIZE_BIG + "PEDIDO:  {{empresa}}\n" + FONT_SIZE_NORMAL +
                                 FONT_SIZE_BIG + "MESA:    {{mesa}}\n" + FONT_SIZE_NORMAL +
                                 "FECHA:   {{ciudadFecha}}\n"+
                                 "CÈDULA/RUC: {{cedula}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "DIRECCIÒN: {{direccion}}\n"+
                                 "TELÈFONO: {{telefono}}\n"+
                                 "CORREO: {{correo}}\n"+
                                 "------------------------------------\n"+
                                 "CANT. PRODUCTO           V.TOTAL\n"+
                                 "------------------------------------\n"+
                                 "{{items}}"+
                                 "------------------------------------\n"+
                                 "   IMPORTE DEL IVA: {{iva}}\n"+
                                 FONT_SIZE_BIG +"   SUMA TOTAL:      {{total}}\n"+ FONT_SIZE_NORMAL +
                                 "\n"+
                                 "DOCUMENTO NO TRIBUTABLE\n"+
                                 "SOLICITE SU FACTURA\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n"+
                                 "\n";
   
    public TicketPedido(String empresa, String ciudadFecha, String mesa, String cliente, String items) {
        this.contentTicket = this.contentTicket.replace("{{empresa}}", empresa);
        this.contentTicket = this.contentTicket.replace("{{ciudadFecha}}", ciudadFecha);
        this.contentTicket = this.contentTicket.replace("{{mesa}}", mesa);
        this.contentTicket = this.contentTicket.replace("{{cliente}}", cliente);
        this.contentTicket = this.contentTicket.replace("{{items}}", items);
    }
      
    public TicketPedido(String empresa, String ciudadFecha, String mesa, String cliente, String items, String iva, String total
                       , String cedula, String direccion, String telefono, String correo) {
        this.contentTicketValores = this.contentTicketValores.replace("{{empresa}}", empresa);
        this.contentTicketValores = this.contentTicketValores.replace("{{ciudadFecha}}", ciudadFecha);
        this.contentTicketValores = this.contentTicketValores.replace("{{mesa}}", mesa);
        this.contentTicketValores = this.contentTicketValores.replace("{{cliente}}", cliente);
        this.contentTicketValores = this.contentTicketValores.replace("{{items}}", items);
        this.contentTicketValores = this.contentTicketValores.replace("{{iva}}", iva);
        this.contentTicketValores = this.contentTicketValores.replace("{{total}}", total);
        
        this.contentTicketValores = this.contentTicketValores.replace("{{cedula}}", cedula);
        this.contentTicketValores = this.contentTicketValores.replace("{{direccion}}", direccion);
        this.contentTicketValores = this.contentTicketValores.replace("{{telefono}}", telefono);
        this.contentTicketValores = this.contentTicketValores.replace("{{correo}}", correo);
    }
    
    public Boolean print(String impresora, Integer tipoTiket) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        byte[] bytes = null;
        
        try {
            switch (tipoTiket) {
                case 1:
                    bytes = this.contentTicketValores.getBytes("ISO-8859-1");
                    break;
                case 2:
                    bytes = this.contentTicket.getBytes("ISO-8859-1");
                    break;
                default:
                    return Boolean.FALSE;
            }
        } catch (UnsupportedEncodingException ex) {
            bytes = (tipoTiket == 1) ? this.contentTicketValores.getBytes() : this.contentTicket.getBytes();
        }

        Doc doc = new SimpleDoc(bytes, flavor, null);
        DocPrintJob job = null;
        DocPrintJob jobCorte = null; // Restauramos la variable para el segundo trabajo

        if (services.length > 0) {
            for (PrintService service : services) {
                if (service.getName().equals(impresora)) {
                    job = service.createPrintJob();
                    jobCorte = service.createPrintJob(); // Inicializamos el segundo trabajo
                    break;
                }
            }
        }

        if (job != null) {
            try {
                // PRIMER TRABAJO: Imprimir el texto
                job.print(doc, null);
                
                // SEGUNDO TRABAJO: Ejecutar el corte
                if (jobCorte != null) {
                    byte[] bytesCorte = {27, 109, 1}; // Comando ESC m 1
                    DocFlavor flavorCorte = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                    Doc docCorte = new SimpleDoc(bytesCorte, flavorCorte, null);
                    jobCorte.print(docCorte, null); // Usamos jobCorte específicamente
                }
                
                return Boolean.TRUE;
            } catch (PrintException ex) {
                System.out.println("Error en la impresión: " + ex.getMessage());
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }
}
