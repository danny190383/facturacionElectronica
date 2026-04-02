package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Impresora;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoImpresoraBean implements Serializable{
    
    private Impresora impresora;
    private PrintService[] services;
    
    public NuevoImpresoraBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.impresora = (Impresora) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("impresora");
        this.services = PrintServiceLookup.lookupPrintServices(null, null);
        if(this.impresora == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.impresora = new Impresora();
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.impresora);
    }

    public Impresora getImpresora() {
        return impresora;
    }

    public void setImpresora(Impresora impresora) {
        this.impresora = impresora;
    }

    public PrintService[] getServices() {
        return services;
    }

    public void setServices(PrintService[] services) {
        this.services = services;
    }
    
}
