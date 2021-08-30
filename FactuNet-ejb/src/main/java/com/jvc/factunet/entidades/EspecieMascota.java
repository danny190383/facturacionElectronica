package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "especie_mascota")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("26")
public class EspecieMascota extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public EspecieMascota() {
    }

    public EspecieMascota(Integer codigo) {
        super.setCodigo(codigo);
    }
}
