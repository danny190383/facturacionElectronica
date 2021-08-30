package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("4")
public class FacturaCompra extends Factura implements Serializable{

    public FacturaCompra() {
        //estado
        //1 nueva
        //2 desde pedido de compra
        //3 procecada o guardada
        //4 anulada
    }
    
    @Transient
    private Integer pedidoCompra;

    public Integer getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(Integer pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }
}
