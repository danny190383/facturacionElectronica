package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.ServicioPersona;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoServicioPersonaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoServicioPersonaBean.class.getName());
    
    private ServicioPersona servicioPersona; 

    public NuevoServicioPersonaBean() {
        this.servicioPersona = new ServicioPersona();
    }
    
    @PostConstruct
    public void init(){
        this.servicioPersona = (ServicioPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicioPersona");
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() throws Exception {
        PrimeFaces.current().dialog().closeDynamic(this.servicioPersona);
    }

    public ServicioPersona getServicioPersona() {
        return servicioPersona;
    }

    public void setServicioPersona(ServicioPersona servicioPersona) {
        this.servicioPersona = servicioPersona;
    }
    
    
}
