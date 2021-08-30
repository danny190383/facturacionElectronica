package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "transportista")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("5")
public class Transportista extends TipoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    
    public Transportista() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
