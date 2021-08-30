package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("2")
public class Cliente extends TipoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Size(max = 500)
    @Column(name = "detalle")
    private String detalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capacidad_credito")
    private BigDecimal capacidadCredito;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fecha_ultima_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaCompra;
    @JoinColumn(name = "tipo_cliente", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoCliente tipoCliente;
    
    @Transient
    private Mascota mascota;
    @Transient
    private BigDecimal totalDeuda;
    @Transient
    private Empresa empresaPedidoSlc;

    public Cliente() {
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public Empresa getEmpresaPedidoSlc() {
        return empresaPedidoSlc;
    }

    public void setEmpresaPedidoSlc(Empresa empresaPedidoSlc) {
        this.empresaPedidoSlc = empresaPedidoSlc;
    }
}
