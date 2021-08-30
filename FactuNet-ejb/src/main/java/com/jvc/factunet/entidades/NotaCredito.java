package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("9")
public class NotaCredito extends Factura implements Serializable{
    
    public NotaCredito() {
    }
}
