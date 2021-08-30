package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "sexo")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("5")
public class Sexo extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    public Sexo() {
    }

    public Sexo(Integer codigo) {
        super.setCodigo(codigo);
    }
}
