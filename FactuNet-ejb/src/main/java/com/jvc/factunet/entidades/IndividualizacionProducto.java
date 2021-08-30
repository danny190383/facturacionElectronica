package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("7")
public class IndividualizacionProducto extends Factura implements Serializable{
    
    public IndividualizacionProducto() {
        //estado
        //1 ejecutado
        //2 anulado
    }
}
