package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Controles;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoControlBean implements Serializable{

    private Controles control;
    
    public NuevoControlBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.control = (Controles) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("control");
        if(this.control == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.control = new Controles();
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.control);
    }

    public Controles getControl() {
        return control;
    }

    public void setControl(Controles control) {
        this.control = control;
    }
}
