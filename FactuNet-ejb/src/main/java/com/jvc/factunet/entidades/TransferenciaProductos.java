package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("6")
public class TransferenciaProductos extends Factura implements Serializable{

    public TransferenciaProductos() {
        //estado
        //1 ejecutado
        //2 anulado
    }
}
