package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class PendientesCompra extends Factura implements Serializable{

    public PendientesCompra() {
    }
    
}
