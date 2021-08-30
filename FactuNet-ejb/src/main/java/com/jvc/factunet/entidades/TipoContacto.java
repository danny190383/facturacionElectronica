package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tipo_contacto")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("13")
public class TipoContacto extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @JoinColumn(name = "validacion", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoValidacion tipoValidacion;

    public TipoContacto() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public TipoValidacion getTipoValidacion() {
        return tipoValidacion;
    }

    public void setTipoValidacion(TipoValidacion tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
    }
}
