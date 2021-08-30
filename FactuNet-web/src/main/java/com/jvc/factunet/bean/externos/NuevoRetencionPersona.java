package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.RetencionPersona;
import com.jvc.factunet.entidades.RetencionServicio;
import com.jvc.factunet.entidades.TipoRetencion;
import com.jvc.factunet.servicios.TipoRetencionServicio;
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
public class NuevoRetencionPersona implements Serializable{
    
    @EJB
    private TipoRetencionServicio tipoRetencionServicio;

    private RetencionPersona retencionPersona;
    private RetencionServicio retencionServicio;
    private Persona persona;
    private ProductoServicio servicio;
    private List<TipoRetencion> listaTipoRetencion;
    private Integer tipoSlc;
    private Boolean editar;
    private Integer tipoPantalla;
    
    public NuevoRetencionPersona() {
        this.listaTipoRetencion = new ArrayList<>();
        this.persona = new Persona();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaTipoRetencion.addAll(this.tipoRetencionServicio.listar());
        this.tipoPantalla = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoPantalla");
        if(this.tipoPantalla == 1){
            this.persona = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("persona");
            this.retencionPersona = (RetencionPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retencionPersona");
            if(this.retencionPersona == null)
            {
                this.inicializarRetencionPersona();
            }
            else
            {
                this.tipoSlc = this.retencionPersona.getTipoRetencion().getCodigo();
                this.editar = Boolean.TRUE;
            }
        }
        else
        {
            this.servicio = (ProductoServicio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicio");
            this.retencionServicio = (RetencionServicio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retencionServicio");
            if(this.retencionServicio == null)
            {
                this.inicializarRetencionServicio();
            }
            else
            {
                this.tipoSlc = this.retencionServicio.getTipoRetencion().getCodigo();
                this.editar = Boolean.TRUE;
            }
        }
        
    }
    
    private void inicializarRetencionPersona()
    {
       this.retencionPersona = new RetencionPersona();
       this.retencionPersona.setPersona(this.persona);
       this.tipoSlc = this.listaTipoRetencion.get(0).getCodigo();
       this.editar = Boolean.FALSE;
    }
    
    private void inicializarRetencionServicio()
    {
       this.retencionServicio = new RetencionServicio();
       this.retencionServicio.setProductoServicio(this.servicio);
       this.tipoSlc = this.listaTipoRetencion.get(0).getCodigo();
       this.editar = Boolean.FALSE;
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        this.setearTipoRetencion();
        if(this.tipoPantalla == 1)
        {
            PrimeFaces.current().dialog().closeDynamic(this.retencionPersona);
        }
        else
        {
            PrimeFaces.current().dialog().closeDynamic(this.retencionServicio);
        }
    }
    
    public void setearTipoRetencion()
    {
        for(TipoRetencion obj : this.listaTipoRetencion)
        {
            if(Objects.equals(this.tipoSlc, obj.getCodigo()))
            {
                if(this.tipoPantalla == 1)
                {
                    this.retencionPersona.setTipoRetencion(obj);
                }
                else
                {
                    this.retencionServicio.setTipoRetencion(obj);
                }
                return;
            }
        }
    }

    public RetencionPersona getRetencionPersona() {
        return retencionPersona;
    }

    public void setRetencionPersona(RetencionPersona retencionPersona) {
        this.retencionPersona = retencionPersona;
    }

    public List<TipoRetencion> getListaTipoRetencion() {
        return listaTipoRetencion;
    }

    public void setListaTipoRetencion(List<TipoRetencion> listaTipoRetencion) {
        this.listaTipoRetencion = listaTipoRetencion;
    }

    public Integer getTipoSlc() {
        return tipoSlc;
    }

    public void setTipoSlc(Integer TipoSlc) {
        this.tipoSlc = TipoSlc;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Integer getTipoPantalla() {
        return tipoPantalla;
    }

    public void setTipoPantalla(Integer tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public RetencionServicio getRetencionServicio() {
        return retencionServicio;
    }

    public void setRetencionServicio(RetencionServicio retencionServicio) {
        this.retencionServicio = retencionServicio;
    }
}
