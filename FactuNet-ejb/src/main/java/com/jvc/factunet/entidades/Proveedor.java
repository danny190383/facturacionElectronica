package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "proveedor")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("4")
public class Proveedor extends TipoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Size(max = 500)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "capacidad_credito")
    private BigDecimal capacidadCredito;
    @Column(name = "interes_mora")
    private BigDecimal interesMora;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "fecha_ultima_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaCompra;
    @Column(name = "tiempo_max_credito")
    private Integer tiempoMaxCredito;

    public Proveedor() {
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getCapacidadCredito() {
        return capacidadCredito;
    }

    public void setCapacidadCredito(BigDecimal capacidadCredito) {
        this.capacidadCredito = capacidadCredito;
    }

    public BigDecimal getInteresMora() {
        return interesMora;
    }

    public void setInteresMora(BigDecimal interesMora) {
        this.interesMora = interesMora;
    }

    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public Integer getTiempoMaxCredito() {
        return tiempoMaxCredito;
    }

    public void setTiempoMaxCredito(Integer tiempoMaxCredito) {
        this.tiempoMaxCredito = tiempoMaxCredito;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
}
