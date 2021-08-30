package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Contactar;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.TipoContacto;
import com.jvc.factunet.entidades.TipoPersona;
import com.jvc.factunet.servicios.TipoContactoServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class ContactarBean implements Serializable{
    
    @EJB
    private TipoContactoServicio tipoContactoServicio;

    private Contactar contactar;
    private TipoPersona persona;
    private List<TipoContacto> listaTipoContacto;
    private Integer tipoSelect;
    
    public ContactarBean() {
        this.listaTipoContacto = new ArrayList<>();
        this.persona = new TipoPersona();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaTipoContacto.addAll(this.tipoContactoServicio.listar());
        this.contactar = (Contactar) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contactar");
        this.persona = (TipoPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("persona");
        if(this.contactar == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.contactar = new Contactar();
        this.contactar.setTipoPersona(this.persona);
        this.tipoSelect = this.listaTipoContacto.get(0).getCodigo();
        this.contactar.setTipoContacto(this.listaTipoContacto.get(0));
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.contactar);
    }
    
    public void cambiarValidacion()
    {
        for(TipoContacto obj : this.listaTipoContacto)
        {
            if(Objects.equals(this.tipoSelect, obj.getCodigo()))
            {
                this.contactar.setTipoContacto(obj);
            }
        }
    }

    public Contactar getContactar() {
        return contactar;
    }

    public void setContactar(Contactar contactar) {
        this.contactar = contactar;
    }

    public List<TipoContacto> getListaTipoContacto() {
        return listaTipoContacto;
    }

    public void setListaTipoContacto(List<TipoContacto> listaTipoContacto) {
        this.listaTipoContacto = listaTipoContacto;
    }

    public Integer getTipoSelect() {
        return tipoSelect;
    }

    public void setTipoSelect(Integer tipoSelect) {
        this.tipoSelect = tipoSelect;
    }
}
