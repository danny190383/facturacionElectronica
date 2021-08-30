package com.jvc.factunet.movil.print;

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
     private String contentTicket = "       {{empresa}}\n"+
                                 "\n"+
                                 "\n"+
                                 "MESA:    {{mesa}}\n"+
                                 "FECHA:   {{ciudadFecha}}\n"+
                                 "CLIENTE: {{cliente}}\n"+
                                 "-------------------------------------\n"+
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
    
    public TicketPedido(String empresa, String ciudadFecha, String mesa, String cliente, String items) {
      this.contentTicket = this.contentTicket.replace("{{empresa}}", empresa);
      this.contentTicket = this.contentTicket.replace("{{ciudadFecha}}", ciudadFecha);
      this.contentTicket = this.contentTicket.replace("{{mesa}}", mesa);
      this.contentTicket = this.contentTicket.replace("{{cliente}}", cliente);
      this.contentTicket = this.contentTicket.replace("{{items}}", items);
    }
    
    public Boolean print(String impresora) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        byte[] bytes;
        bytes = this.contentTicket.getBytes();
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
