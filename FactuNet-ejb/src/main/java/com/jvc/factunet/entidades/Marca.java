package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "marca")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("4")
public class Marca extends Mantenimiento implements Serializable{
    
    private static final long serialVersionUID = 1L;

    public Marca() {
    }
}
