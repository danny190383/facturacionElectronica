package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "motivo")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("22")
public class Motivo extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;

    public Motivo() {
    }

    public Motivo(Integer codigo) {
        super.setCodigo(codigo);
    }
}
