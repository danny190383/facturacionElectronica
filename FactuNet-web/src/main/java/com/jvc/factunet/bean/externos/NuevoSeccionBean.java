package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.entidades.Seccion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoSeccionBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoSeccionBean.class.getName());

    private Seccion seccion;
    private Mesa mesa;
    private Boolean editar;
    
    public NuevoSeccionBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.seccion = (Seccion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("seccion");
        if(this.seccion == null)
        {
            this.inicializar();
        }
        this.inicializarMesa();
    }
    
    public void inicializar()
    {
        this.seccion = new Seccion();
        this.seccion.setColumnas(1);
        this.seccion.setMesaList(new ArrayList<Mesa>());
    }
    
    public void inicializarMesa()
    {
        this.mesa = new Mesa();
        this.mesa.setNombre(StringUtils.EMPTY);
        this.mesa.setSillas(1);
        this.editar = Boolean.FALSE; 
    }
    
    public void eliminarMesa(Mesa parametro) {
        try {
            this.seccion.getMesaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarMesa(Mesa parametro) {
        this.mesa = parametro;
        this.editar = Boolean.TRUE;
    }
    
    public void agregarMesa()
    {
        if(!this.editar)
        {
            this.mesa.setSeccion(this.seccion);
            this.seccion.getMesaList().add(this.mesa);
        }
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        PrimeFaces.current().executeScript("PF('dlgMesa').hide();");    
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.seccion);
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
