package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "cargo")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("8")
public class Cargo extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    public Cargo() {
    }
    
    public Cargo(Integer codigo) {
        super.setCodigo(codigo);
    }
}
