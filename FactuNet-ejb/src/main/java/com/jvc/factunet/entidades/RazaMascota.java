package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "raza_mascota")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("23")
public class RazaMascota extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    public RazaMascota() {
    }

    public RazaMascota(Integer codigo) {
        super.setCodigo(codigo);
    }
}
