package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_factura_detalle")
    @SequenceGenerator(name = "secuencia_factura_detalle", sequenceName = "secuencia_factura_detalle", allocationSize = 1)
    private Integer codigo;
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "cantidad_facturado")
    private BigDecimal cantidadPorFacturar;
    @Column(name = "stock_fecha")
    private BigDecimal stockFecha;
    @Column(name = "stock_actual")
    private BigDecimal stockActual;
    @Column(name = "stock_origen_fecha")
    private BigDecimal stockOrigenFecha;
    @Column(name = "precio_venta_unitario")
    private BigDecimal precioVentaUnitario;
    @Column(name = "costo_fecha")
    private BigDecimal costoFecha;
    @Column(name = "precio_venta_unitario_descuento")
    private BigDecimal precioVentaUnitarioDescuento;
    @Column(name = "precio_venta_unitario_transporte")
    private BigDecimal precioVentaUnitarioTransporte;
    @Column(name = "subtotal_sin_descuento")
    private BigDecimal subtotalSinDescuento;
    @Column(name = "subtotal_con_descuento")
    private BigDecimal subtotalConDescuento;
    @JoinColumn(name = "impuesto_tarifa", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImpuestoTarifa impuestoTarifa;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "comision")
    private BigDecimal comision;
    @Column(name = "valor_descuento")
    private BigDecimal valorDescuento;
    @Column(name = "valor_comision")
    private BigDecimal valorComision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaducidad;
    @Size(max = 800)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "empleado", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
    @JoinColumn(name = "factura", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "bodega", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bodega bodega;
    @JoinColumn(name = "producto_servicio", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoServicio;
    @JoinColumn(name = "producto_servicio_destino", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto productoServicioDestino;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "facturaDetalle", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<FacturaDetalleSeries> facturaDetalleSeriesList;
    @JoinColumn(name = "paquete", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacturaDetalle paquete;
    @JoinColumn(name = "lote_venta", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacturaDetalle loteVenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paquete", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<FacturaDetalle> detallePaqueteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleFactura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<DetalleFacturaImpuestoTarifa> detalleFacturaImpuestoTarifaList;
    
    @Transient
    private BigDecimal stock;
    @Transient
    private BigDecimal utilidad;
    @Transient
    private BigDecimal descuentoVentas;
    @Transient
    private BigDecimal pvp;
    @Transient
    private BigDecimal precioUnitarioTmp;
    @Transient
    private BigDecimal pvpIva;
    @Transient 
    private Boolean isPaquete;
    @Transient
    private List<FacturaDetalleSeries> listaSeriesDelete;
    @Transient
    private BigDecimal valorProrrateo;
    @Transient 
    private Boolean estado;
    @Transient
    private BigDecimal subtotalPedido;

    public FacturaDetalle() {
        this.isPaquete = Boolean.FALSE;
        this.estado = Boolean.FALSE;
    }

    public BigDecimal getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(BigDecimal precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura1) {
        this.factura = factura1;
    }

    public List<FacturaDetalleSeries> getFacturaDetalleSeriesList() {
        return facturaDetalleSeriesList;
    }

    public void setFacturaDetalleSeriesList(List<FacturaDetalleSeries> facturaDetalleSeriesList) {
        this.facturaDetalleSeriesList = facturaDetalleSeriesList;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Producto getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(Producto productoServicio) {
        this.productoServicio = productoServicio;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public BigDecimal getSubtotalSinDescuento() {
        return subtotalSinDescuento;
    }

    public void setSubtotalSinDescuento(BigDecimal subtotalSinDescuento) {
        this.subtotalSinDescuento = subtotalSinDescuento;
    }

    public BigDecimal getSubtotalConDescuento() {
        return subtotalConDescuento;
    }

    public void setSubtotalConDescuento(BigDecimal subtotalConDescuento) {
        this.subtotalConDescuento = subtotalConDescuento;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }

    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    public BigDecimal getPvpIva() {
        return pvpIva;
    }

    public void setPvpIva(BigDecimal pvpIva) {
        this.pvpIva = pvpIva;
    }

    public BigDecimal getPrecioVentaUnitarioDescuento() {
        return precioVentaUnitarioDescuento;
    }

    public void setPrecioVentaUnitarioDescuento(BigDecimal precioVentaUnitarioDescuento) {
        this.precioVentaUnitarioDescuento = precioVentaUnitarioDescuento;
    }

    public BigDecimal getStockFecha() {
        return stockFecha;
    }

    public void setStockFecha(BigDecimal stockFecha) {
        this.stockFecha = stockFecha;
    }

    public BigDecimal getStockOrigenFecha() {
        return stockOrigenFecha;
    }

    public void setStockOrigenFecha(BigDecimal stockOrigenFecha) {
        this.stockOrigenFecha = stockOrigenFecha;
    }

    public Boolean getIsPaquete() {
        return isPaquete;
    }

    public void setIsPaquete(Boolean isPaquete) {
        this.isPaquete = isPaquete;
    }

    public List<FacturaDetalle> getDetallePaqueteList() {
        return detallePaqueteList;
    }

    public void setDetallePaqueteList(List<FacturaDetalle> detallePaqueteList) {
        this.detallePaqueteList = detallePaqueteList;
    }

    public FacturaDetalle getPaquete() {
        return paquete;
    }

    public void setPaquete(FacturaDetalle paquete) {
        this.paquete = paquete;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public BigDecimal getValorComision() {
        return valorComision;
    }

    public void setValorComision(BigDecimal valorComision) {
        this.valorComision = valorComision;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public BigDecimal getPrecioUnitarioTmp() {
        return precioUnitarioTmp;
    }

    public void setPrecioUnitarioTmp(BigDecimal precioUnitarioTmp) {
        this.precioUnitarioTmp = precioUnitarioTmp;
    }

    public BigDecimal getPrecioVentaUnitarioTransporte() {
        return precioVentaUnitarioTransporte;
    }

    public void setPrecioVentaUnitarioTransporte(BigDecimal precioVentaUnitarioTransporte) {
        this.precioVentaUnitarioTransporte = precioVentaUnitarioTransporte;
    }

    public List<FacturaDetalleSeries> getListaSeriesDelete() {
        return listaSeriesDelete;
    }

    public void setListaSeriesDelete(List<FacturaDetalleSeries> listaSeriesDelete) {
        this.listaSeriesDelete = listaSeriesDelete;
    }

    public Producto getProductoServicioDestino() {
        return productoServicioDestino;
    }

    public void setProductoServicioDestino(Producto productoServicioDestino) {
        this.productoServicioDestino = productoServicioDestino;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    
    public Integer getCantidadInt(){
        return cantidad.intValue();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCostoFecha() {
        return costoFecha;
    }

    public void setCostoFecha(BigDecimal costoFecha) {
        this.costoFecha = costoFecha;
    }

    public BigDecimal getValorProrrateo() {
        return valorProrrateo;
    }

    public void setValorProrrateo(BigDecimal valorProrrateo) {
        this.valorProrrateo = valorProrrateo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getCantidadPorFacturar() {
        return cantidadPorFacturar;
    }

    public void setCantidadPorFacturar(BigDecimal cantidadPorFacturar) {
        this.cantidadPorFacturar = cantidadPorFacturar;
    }

    public BigDecimal getSubtotalPedido() {
        subtotalPedido = productoServicio.getPvp().multiply(cantidad).setScale(2,RoundingMode.HALF_UP);
        return subtotalPedido ;
    }

    public void setSubtotalPedido(BigDecimal subtotalPedido) {
        this.subtotalPedido = subtotalPedido;
    }

    public List<DetalleFacturaImpuestoTarifa> getDetalleFacturaImpuestoTarifaList() {
        return detalleFacturaImpuestoTarifaList;
    }

    public void setDetalleFacturaImpuestoTarifaList(List<DetalleFacturaImpuestoTarifa> detalleFacturaImpuestoTarifaList) {
        this.detalleFacturaImpuestoTarifaList = detalleFacturaImpuestoTarifaList;
    }

    public ImpuestoTarifa getImpuestoTarifa() {
        return impuestoTarifa;
    }

    public void setImpuestoTarifa(ImpuestoTarifa impuestoTarifa) {
        this.impuestoTarifa = impuestoTarifa;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }
    
    public Integer getStockActualInt() {
        return stockActual.intValue();
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public FacturaDetalle getLoteVenta() {
        return loteVenta;
    }

    public void setLoteVenta(FacturaDetalle loteVenta) {
        this.loteVenta = loteVenta;
    }

    public BigDecimal getDescuentoVentas() {
        return descuentoVentas;
    }

    public void setDescuentoVentas(BigDecimal descuentoVentas) {
        this.descuentoVentas = descuentoVentas;
    }
    
    public Boolean getIvaB() {
        if((this.impuestoTarifa.getImpuesto().getId() == 1) && (this.impuestoTarifa.getPorcentaje().floatValue() > 0)){   
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
        if (!(object instanceof FacturaDetalle)) {
            return false;
        }
        FacturaDetalle other = (FacturaDetalle) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.FacturaDetalle[ codigo=" + codigo + " ]";
    }
}
