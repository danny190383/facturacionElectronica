package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "producto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminador", discriminatorType = DiscriminatorType.INTEGER)
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_producto")
    @SequenceGenerator(name = "secuencia_producto", sequenceName = "secuencia_producto", allocationSize = 1)
    private Integer codigo;
    @Size(max = 100)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "precio_ultima_compra")
    private BigDecimal precioUltimaCompra;
    @Column(name = "utilidad")
    private BigDecimal utilidad;
    @Column(name = "descuento_venta")
    private BigDecimal descuentoVenta;
    @Column(name = "pvp")
    private BigDecimal pvp;
    @JoinColumn(name = "grupo", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoProducto grupo;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoImpuestoTarifa> productoImpuestoTarifaList;
    
    @Transient
    private FacturaDetalleSeries serie;
    @Transient
    private Bodega bodega;
    @Transient
    private FacturaDetalle lote;
    @Transient
    private BigDecimal cantidad;
    @Transient
    private String observacion;

    public Producto() {
    }

    public Producto(Integer codigo) {
        this.codigo = codigo;
    }

    public Producto(Integer codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getIvaB() {
        for(ProductoImpuestoTarifa tarifa : this.productoImpuestoTarifaList){
            if((tarifa.getImpuestoTarifa().getImpuesto().getId() == 1) && (tarifa.getImpuestoTarifa().getPorcentaje().floatValue() > 0)){   
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public GrupoProducto getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoProducto grupo) {
        this.grupo = grupo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FacturaDetalleSeries getSerie() {
        return serie;
    }

    public void setSerie(FacturaDetalleSeries serie) {
        this.serie = serie;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }
    
    public BigDecimal getPrecioUltimaCompra() {
        return precioUltimaCompra;
    }

    public void setPrecioUltimaCompra(BigDecimal precioUltimaCompra) {
        this.precioUltimaCompra = precioUltimaCompra;
    }

    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    public List<ProductoImpuestoTarifa> getProductoImpuestoTarifaList() {
        return productoImpuestoTarifaList;
    }

    public void setProductoImpuestoTarifaList(List<ProductoImpuestoTarifa> productoImpuestoTarifaList) {
        this.productoImpuestoTarifaList = productoImpuestoTarifaList;
    }

    public BigDecimal getDescuentoVenta() {
        return descuentoVenta;
    }

    public void setDescuentoVenta(BigDecimal descuentoVenta) {
        this.descuentoVenta = descuentoVenta;
    }
    
    public FacturaDetalle getLote() {
        return lote;
    }

    public void setLote(FacturaDetalle lote) {
        this.lote = lote;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Producto[ codigo=" + codigo + " ]";
    }
}
