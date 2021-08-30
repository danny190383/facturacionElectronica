package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tipo_cliente")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("9")
public class TipoCliente extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "interes_mora")
    private BigDecimal interesMora;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;

    public TipoCliente() {
    }
    
    public TipoCliente(Integer codigo) {
        super.setCodigo(codigo);
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getInteresMora() {
        return interesMora;
    }

    public void setInteresMora(BigDecimal interesMora) {
        this.interesMora = interesMora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
 
}
