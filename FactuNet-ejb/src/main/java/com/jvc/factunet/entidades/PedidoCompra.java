package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class PedidoCompra extends Factura implements Serializable{

    public PedidoCompra() {
        //estado
        //1 activo
        //2 cerrado
        //3 facturada
    }
    
}
