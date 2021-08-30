package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("10")
public class NotaDebito extends Factura implements Serializable{
    
    @Transient
    private String tipoDocumentoRelacionado;

    public NotaDebito() {
    }

    public String getTipoDocumentoRelacionado() {
        if(this.getDocumentoRelacionado() instanceof FacturaVenta){
            return "Factura de Venta";
        }
        if(this.getDocumentoRelacionado() instanceof NotaCredito){
            return "Nota de Crédito";
        }
        return "";
    }
}
