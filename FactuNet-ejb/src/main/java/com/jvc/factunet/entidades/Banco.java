package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "banco")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("16")
public class Banco extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;

    public Banco() {
    }
    
    public Banco(Integer codigo) {
        super.setCodigo(codigo);
    }
}
