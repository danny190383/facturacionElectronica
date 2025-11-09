package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "servicio_persona")
public class ServicioPersona implements Serializable{
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    @Size(max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "requiere_lectura ")
    private Boolean requiereLectura ;
    @Column(name = "cobro_automatico ")
    private Boolean cobroAutomatico ;
    @JoinColumn(name = "persona", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Persona persona;
    @JoinColumn(name = "servicio", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto servicio;
    
    @Transient
    private List<CobrosServicio> cobroServicioList;

    public ServicioPersona() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Producto getServicio() {
        return servicio;
    }

    public void setServicio(Producto servicio) {
        this.servicio = servicio;
    }

    public List<CobrosServicio> getCobroServicioList() {
        return cobroServicioList;
    }

    public void setCobroServicioList(List<CobrosServicio> cobroServicioList) {
        this.cobroServicioList = cobroServicioList;
    }

    public Boolean getRequiereLectura() {
        return requiereLectura;
    }

    public void setRequiereLectura(Boolean requiereLectura) {
        this.requiereLectura = requiereLectura;
    }

    public Boolean getCobroAutomatico() {
        return cobroAutomatico;
    }

    public void setCobroAutomatico(Boolean cobroAutomatico) {
        this.cobroAutomatico = cobroAutomatico;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServicioPersona)) {
            return false;
        }
        ServicioPersona other = (ServicioPersona) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.ServicioPersona[ codigo=" + codigo + " ]";
    }
    
}
