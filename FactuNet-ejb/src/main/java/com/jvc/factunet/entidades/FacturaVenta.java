package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("5")
public class FacturaVenta extends Factura implements Serializable{
    
    @Column(name = "tipo_documento")
    private Integer tipoDocumento;
    
    @Transient
    private Integer proforma;
    @Transient
    private String tipoDocuemento;
    @Transient
    private Map<Integer,String> pedidosVenta = new HashMap<>(); 
    @Transient
    private Integer ingreso;
    
    public FacturaVenta() {
        //estado
        //1 nueva
        //2 ingresada
        //3 anulada
    }

    public Integer getProforma() {
        return proforma;
    }

    public void setProforma(Integer proforma) {
        this.proforma = proforma;
    }

    public String getTipoDocuemento() {
        return tipoDocuemento;
    }

    public void setTipoDocuemento(String tipoDocuemento) {
        this.tipoDocuemento = tipoDocuemento;
    }

    public Integer getIngreso() {
        return ingreso;
    }

    public void setIngreso(Integer ingreso) {
        this.ingreso = ingreso;
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Map<Integer, String> getPedidosVenta() {
        return pedidosVenta;
    }

    public void setPedidosVenta(Map<Integer, String> pedidosVenta) {
        this.pedidosVenta = pedidosVenta;
    }
}
