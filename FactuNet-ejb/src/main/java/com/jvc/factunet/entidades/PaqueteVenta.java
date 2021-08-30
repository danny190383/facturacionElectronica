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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "paquete_venta")
public class PaqueteVenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_paquete")
    @SequenceGenerator(name = "secuencia_paquete", sequenceName = "secuencia_paquete", allocationSize = 1)
    private Integer codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "pvp")
    private BigDecimal pvp;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "comision")
    private BigDecimal comision;
    @Column(name = "total")
    private BigDecimal total;
    @JoinColumn(name = "producto", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;
    @JoinColumn(name = "producto_padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductoPaquete productoPadre;
    
    @Transient
    private BigDecimal stock;
    @Transient
    private Bodega bodega;
    @Transient
    private Boolean isBodega;
    @Transient
    private Integer bodegaSlc;

    public PaqueteVenta() {
        this.isBodega = Boolean.FALSE;
    }

    public PaqueteVenta(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ProductoPaquete getProductoPadre() {
        return productoPadre;
    }

    public void setProductoPadre(ProductoPaquete productoPadre) {
        this.productoPadre = productoPadre;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public Boolean getIsBodega() {
        return isBodega;
    }

    public void setIsBodega(Boolean isBodega) {
        this.isBodega = isBodega;
    }

    public Integer getBodegaSlc() {
        return bodegaSlc;
    }

    public void setBodegaSlc(Integer bodegaSlc) {
        this.bodegaSlc = bodegaSlc;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }
    
    
}
