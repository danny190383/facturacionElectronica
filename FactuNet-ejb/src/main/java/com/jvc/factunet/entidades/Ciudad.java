package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ciudad")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("6")
public class Ciudad extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    public Ciudad() {
    }
    
    public Ciudad(Integer codigo) {
        super.setCodigo(codigo);
    }
}
