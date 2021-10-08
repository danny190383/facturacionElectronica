package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@DiscriminatorValue("1")
public class ProductoBodega extends Producto implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Size(max = 1)
    @Column(name = "presentacion")
    private String presentacion;
    @Column(name = "cantidad_empaque")
    private Integer cantidadEmpaque;
    @Size(max = 500)
    @Column(name = "referencia")
    private String referencia;
    @Size(max = 200)
    @Column(name = "enlace_web")
    private String enlaceWeb;
    @Size(max = 10)
    @Column(name = "garantia")
    private String garantia;
    @Size(max = 200)
    @Column(name = "modelo")
    private String modelo;
    @JoinColumn(name = "marca", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Marca marca;
    @JoinColumn(name = "padre_paca", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductoBodega padrePaca;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padrePaca", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductoBodega> pacaProductoList;
    @JoinColumn(name = "unidad_medida", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadMedida unidadMedida;
    @Column(name = "precio_promedio")
    private BigDecimal precioPromedio;
    @Column(name = "fecha_ultima_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaCompra;
    @Column(name = "fecha_ultima_venta")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimaVenta;
    @OneToMany(mappedBy = "productoBodega", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoStock> productoStockList;
    
    @Transient
    private BigDecimal stock; 
    
    public ProductoBodega() {
    }

    public ProductoBodega(Integer codigo) {
        super(codigo);
    }

    public ProductoBodega(Integer codigo, String nombre) {
        super(codigo, nombre);
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Integer getCantidadEmpaque() {
        return cantidadEmpaque;
    }

    public void setCantidadEmpaque(Integer cantidadEmpaque) {
        this.cantidadEmpaque = cantidadEmpaque;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEnlaceWeb() {
        return enlaceWeb;
    }

    public void setEnlaceWeb(String enlaceWeb) {
        this.enlaceWeb = enlaceWeb;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public List<ProductoStock> getProductoStockList() {
        return productoStockList;
    }

    public void setProductoStockList(List<ProductoStock> productoStockList) {
        this.productoStockList = productoStockList;
    }

    public BigDecimal getPrecioPromedio() {
        return precioPromedio;
    }

    public void setPrecioPromedio(BigDecimal precioPromedio) {
        this.precioPromedio = precioPromedio;
    }

    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public Date getFechaUltimaVenta() {
        return fechaUltimaVenta;
    }

    public void setFechaUltimaVenta(Date fechaUltimaVenta) {
        this.fechaUltimaVenta = fechaUltimaVenta;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public ProductoBodega getPadrePaca() {
        return padrePaca;
    }

    public void setPadrePaca(ProductoBodega padrePaca) {
        this.padrePaca = padrePaca;
    }

    public List<ProductoBodega> getPacaProductoList() {
        return pacaProductoList;
    }

    public void setPacaProductoList(List<ProductoBodega> pacaProductoList) {
        this.pacaProductoList = pacaProductoList;
    }
}

