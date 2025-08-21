package com.jvc.factunet.print;

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
    private String contentTicket = 
                                 "\n"+
                                 "PEDIDO:  {{empresa}}\n"+
                                 "MESA:    {{mesa}}\n"+
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
                                 "\n"+
                                 "PEDIDO:  {{empresa}}\n"+
                                 "MESA:    {{mesa}}\n"+
                                 "FECHA:   {{ciudadFecha}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "------------------------------------\n"+
                                 "CANT. PRODUCTO           V.TOTAL\n"+
                                 "------------------------------------\n"+
                                 "{{items}}"+
                                 "------------------------------------\n"+
                                 "   IMPORTE DEL IVA: {{iva}}\n"+
                                 "   SUMA TOTAL:      {{total}}\n"+
                                 "\n"+
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
      
    public TicketPedido(String empresa, String ciudadFecha, String mesa, String cliente, String items, String iva, String total) {
        this.contentTicketValores = this.contentTicketValores.replace("{{empresa}}", empresa);
        this.contentTicketValores = this.contentTicketValores.replace("{{ciudadFecha}}", ciudadFecha);
        this.contentTicketValores = this.contentTicketValores.replace("{{mesa}}", mesa);
        this.contentTicketValores = this.contentTicketValores.replace("{{cliente}}", cliente);
        this.contentTicketValores = this.contentTicketValores.replace("{{items}}", items);
        this.contentTicketValores = this.contentTicketValores.replace("{{iva}}", iva);
        this.contentTicketValores = this.contentTicketValores.replace("{{total}}", total);
    }
    
    public Boolean print(String impresora, String tipoTiket) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        byte[] bytes = null;
        switch (tipoTiket) {
            case "1":
                bytes = this.contentTicket.getBytes();
                break;
            case "2":
                bytes = this.contentTicketValores.getBytes();
                break;
            default:
                break;
        }
        Doc doc = new SimpleDoc(bytes,flavor,null);
        DocPrintJob job = null;
        DocPrintJob jobCorte = null;
        if (services.length > 0) {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(impresora)) {
                    job = services[i].createPrintJob();
                    jobCorte = services[i].createPrintJob();
                    System.out.println(i + ": " + services[i].getName());
                    break;
                }
            }
        }
        if(job != null)
        {
            try {
                job.print(doc, null);
                byte[] bytesCorte = {27, 109, 1};
                DocFlavor flavorCorte = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc docCorte = new SimpleDoc(bytesCorte, flavorCorte, null);
                jobCorte.print(docCorte, null);
                return Boolean.TRUE;
            } catch (PrintException ex) {
                System.out.println("Error de driver o conexion");
                return Boolean.FALSE;
            }
        }
        else
        {
            System.out.println("Impresora no encontrada");
            return Boolean.FALSE;
        }
    }
}
