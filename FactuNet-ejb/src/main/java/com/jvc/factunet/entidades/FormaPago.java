package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "forma_pago")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("18")
public class FormaPago extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "descripcion")
    private String descripcion;
    
    public FormaPago() {
    }

    public FormaPago(Integer codigo) {
        super.setCodigo(codigo);
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
