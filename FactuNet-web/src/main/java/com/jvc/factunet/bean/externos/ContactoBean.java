package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Contacto;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.TipoIdentificacion;
import com.jvc.factunet.entidades.TipoPersona;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.PersonaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class ContactoBean extends CatalogosPersonaBean implements Serializable{
    
    @EJB
    private PersonaServicio personaServicio;

    private Contacto contacto;
    private TipoPersona persona;
    private Integer tipoIdentificacionSlc;
    private TipoIdentificacion tipoIdentificacion;
    
    public ContactoBean() {
        this.contacto = new Contacto();
        this.tipoIdentificacion = new TipoIdentificacion();
    }
    
    @PostConstruct
    public void init()
    {
        this.contacto = (Contacto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contacto");
        this.persona = (TipoPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("persona");
        if(this.contacto == null)
        {
            this.inicializar();
        }
        else
        {
            if(this.contacto.getPersona().getSexo() == null){
                this.contacto.getPersona().setSexo(super.getListaSexo().get(0));
            }
        }
        this.tipoIdentificacion = super.getListaTipoIdentificacion().get(0);
    }
    
    public void inicializar() {
        this.contacto = new Contacto();
        this.contacto.setPersona(new Persona());
        this.contacto.setPersonaPadre(this.persona);
        this.contacto.getPersona().setCiudad(super.getListaCiudad().get(0));
        this.contacto.getPersona().setSexo(super.getListaSexo().get(0));
        this.contacto.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(this.contacto.getPersona().getCedula() != null && !this.contacto.getPersona().getCedula().trim().isEmpty()){
            if(!this.verificarCedulaSistema())
            {
                if(this.contacto.getPersona().getCedula().trim().isEmpty())
                {
                    this.contacto.getPersona().setCedula(null);
                }
                if(this.contacto.getPersona().getApellidos() != null){
                    this.contacto.getPersona().setApellidos(this.contacto.getPersona().getApellidos().trim().toUpperCase());
                }
                this.contacto.getPersona().setNombres(this.contacto.getPersona().getNombres().trim().toUpperCase());
                PrimeFaces.current().dialog().closeDynamic(this.contacto);
            }
            else
            {
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaExistente"));
            }
        }
        else
        {
            this.contacto.getPersona().setCedula(null);
            if(this.contacto.getPersona().getApellidos() != null){
                this.contacto.getPersona().setApellidos(this.contacto.getPersona().getApellidos().trim().toUpperCase());
            }
            this.contacto.getPersona().setNombres(this.contacto.getPersona().getNombres().trim().toUpperCase());
            PrimeFaces.current().dialog().closeDynamic(this.contacto);
        }
    }
    
    public Boolean verificarCedulaSistema()
    {
        if(this.contacto.getCodigo() == null)
        {
            try {
                Persona personaTmp = this.personaServicio.buscarCedula(this.contacto.getPersona().getCedula());
                if(personaTmp == null)
                {
                    return Boolean.FALSE;
                }
                else
                {
                    return Boolean.TRUE;
                }
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        else
        {
            return Boolean.FALSE;
        }
    }
    
    public void cambiarValidacion()
    {
        for(TipoIdentificacion obj : super.getListaTipoIdentificacion())
        {
            if(Objects.equals(this.tipoIdentificacionSlc, obj.getCodigo()))
            {
                this.tipoIdentificacion = obj;
            }
        }
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Integer getTipoIdentificacionSlc() {
        return tipoIdentificacionSlc;
    }

    public void setTipoIdentificacionSlc(Integer tipoIdentificacionSlc) {
        this.tipoIdentificacionSlc = tipoIdentificacionSlc;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }
}
