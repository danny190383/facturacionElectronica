package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_cuenta")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("17")
public class TipoCuenta extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;

    public TipoCuenta() {
    }
    
    public TipoCuenta(Integer codigo) {
        super.setCodigo(codigo);
    }
}
