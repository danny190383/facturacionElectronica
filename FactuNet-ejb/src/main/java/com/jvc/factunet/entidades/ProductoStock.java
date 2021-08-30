package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "producto_stock")
public class ProductoStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stock")
    private BigDecimal stock;
    @Column(name = "stock_min")
    private BigDecimal stockMin;
    @Column(name = "stock_max")
    private BigDecimal stockMax;
    @JoinColumn(name = "bodega", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bodega bodega;
    @JoinColumn(name = "producto", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductoBodega productoBodega;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    
    @Transient
    private BigDecimal cantidad;
    @Transient
    private List<FacturaDetalleSeries> listaSeries;

    public ProductoStock() {
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getStockMin() {
        return stockMin;
    }

    public void setStockMin(BigDecimal stockMin) {
        this.stockMin = stockMin;
    }

    public BigDecimal getStockMax() {
        return stockMax;
    }

    public void setStockMax(BigDecimal stockMax) {
        this.stockMax = stockMax;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega1) {
        this.bodega = bodega1;
    }

    public ProductoBodega getProductoBodega() {
        return productoBodega;
    }

    public void setProductoBodega(ProductoBodega productoBodega) {
        this.productoBodega = productoBodega;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public List<FacturaDetalleSeries> getListaSeries() {
        return listaSeries;
    }

    public void setListaSeries(List<FacturaDetalleSeries> listaSeries) {
        this.listaSeries = listaSeries;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoStock)) {
            return false;
        }
        ProductoStock other = (ProductoStock) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.ProductoStock[ codigo=" + codigo + " ]";
    }
}
