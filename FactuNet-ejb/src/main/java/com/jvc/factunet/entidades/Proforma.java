package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("8")
public class Proforma extends Factura implements Serializable{

    public Proforma() {
    }
    
}
