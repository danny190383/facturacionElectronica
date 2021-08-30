package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "estado_civil")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("7")
public class EstadoCivil extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    public EstadoCivil() {
    }
    
    public EstadoCivil(Integer codigo) {
        super.setCodigo(codigo);
    }
}
