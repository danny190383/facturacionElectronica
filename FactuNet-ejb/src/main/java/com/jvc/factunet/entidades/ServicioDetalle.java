package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "servicio_detalle")
public class ServicioDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_garantia_detalle")
    @SequenceGenerator(name = "secuencia_garantia_detalle", sequenceName = "secuencia_garantia_detalle", allocationSize = 1)
    private Integer codigo;
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo")
    private BigDecimal costo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "producto_servicio", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoServicio;
    @JoinColumn(name = "garantia_detalle", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GarantiaDetalle garantiaDetalle;

    public ServicioDetalle() {
    }

    public ServicioDetalle(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Producto getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(Producto productoServicio) {
        this.productoServicio = productoServicio;
    }

    public GarantiaDetalle getGarantiaDetalle() {
        return garantiaDetalle;
    }

    public void setGarantiaDetalle(GarantiaDetalle garantiaDetalle) {
        this.garantiaDetalle = garantiaDetalle;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
