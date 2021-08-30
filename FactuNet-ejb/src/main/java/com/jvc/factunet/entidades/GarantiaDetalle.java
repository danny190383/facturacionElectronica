package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "garantia_detalle")
public class GarantiaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_garantia")
    @SequenceGenerator(name = "secuencia_garantia", sequenceName = "secuencia_garantia", allocationSize = 1)
    private Integer codigo;
    @Size(max = 800)
    @Column(name = "producto_recepcion")
    private String productoRecepcion;
    @Size(max = 1)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Size(max = 100)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    @Size(max = 800)
    @Column(name = "problema_reportado")
    private String problemaReportado;
    @Size(max = 800)
    @Column(name = "diagnostico_local")
    private String diagnosticoLocal;
    @Size(max = 800)
    @Column(name = "diagnostico_final")
    private String diagnosticoFinal;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_estimada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstimada;
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Column(name = "fecha_reingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReingreso;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo")
    private BigDecimal costo;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "drivers")
    private Boolean drivers;
    @Size(max = 400)
    @Column(name = "deja_adicionales")
    private String dejaAdicionales;
    @Size(max = 50)
    @Column(name = "clave")
    private String clave;
    @JoinColumn(name = "producto_servicio", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoServicio;
    @JoinColumn(name = "garantia_cabecera", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GarantiaCabecera garantiaCabecera;
    @JoinColumn(name = "factura_compra", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Factura facturaCompra;
    @JoinColumn(name = "factura_venta", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Factura facturaVenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garantiaDetalle", fetch = FetchType.LAZY)
    private List<ServicioDetalle> servicioDetalleList;

    public GarantiaDetalle() {
        
        //tipo
        //1 servicio
        //2 mantenimiento
        //estado
        //1 ingresado
        //2 enviado
        //3 reingreso
        //4 entregado
    }

    public GarantiaDetalle(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getProblemaReportado() {
        return problemaReportado;
    }

    public void setProblemaReportado(String problemaReportado) {
        this.problemaReportado = problemaReportado;
    }

    public String getDiagnosticoLocal() {
        return diagnosticoLocal;
    }

    public void setDiagnosticoLocal(String diagnosticoLocal) {
        this.diagnosticoLocal = diagnosticoLocal;
    }

    public String getDiagnosticoFinal() {
        return diagnosticoFinal;
    }

    public void setDiagnosticoFinal(String diagnosticoFinal) {
        this.diagnosticoFinal = diagnosticoFinal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Producto getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(Producto productoServicio) {
        this.productoServicio = productoServicio;
    }

    public GarantiaCabecera getGarantiaCabecera() {
        return garantiaCabecera;
    }

    public void setGarantiaCabecera(GarantiaCabecera garantiaCabecera) {
        this.garantiaCabecera = garantiaCabecera;
    }

    public Factura getFacturaCompra() {
        return facturaCompra;
    }

    public void setFacturaCompra(Factura facturaCompra) {
        this.facturaCompra = facturaCompra;
    }

    public Factura getFacturaVenta() {
        return facturaVenta;
    }

    public void setFacturaVenta(Factura facturaVenta) {
        this.facturaVenta = facturaVenta;
    }

    public List<ServicioDetalle> getServicioDetalleList() {
        return servicioDetalleList;
    }

    public void setServicioDetalleList(List<ServicioDetalle> servicioDetalleList) {
        this.servicioDetalleList = servicioDetalleList;
    }

    public Date getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(Date fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getProductoRecepcion() {
        return productoRecepcion;
    }

    public void setProductoRecepcion(String productoRecepcion) {
        this.productoRecepcion = productoRecepcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getDrivers() {
        return drivers;
    }

    public void setDrivers(Boolean drivers) {
        this.drivers = drivers;
    }

    public String getDejaAdicionales() {
        return dejaAdicionales;
    }

    public void setDejaAdicionales(String dejaAdicionales) {
        this.dejaAdicionales = dejaAdicionales;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
