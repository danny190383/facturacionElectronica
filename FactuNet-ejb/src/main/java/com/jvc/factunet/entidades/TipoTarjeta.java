package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_tarjeta")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("19")
public class TipoTarjeta extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;

    public TipoTarjeta() {
    }

    public TipoTarjeta(Integer codigo) {
        super.setCodigo(codigo);
    }
}
