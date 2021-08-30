package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacto")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("1")
public class Contacto extends TipoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 200)
    @Column(name = "detalle")
    private String detalle;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "persona_padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoPersona personaPadre;

    public Contacto() {
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoPersona getPersonaPadre() {
        return personaPadre;
    }

    public void setPersonaPadre(TipoPersona personaPadre) {
        this.personaPadre = personaPadre;
    }
}
