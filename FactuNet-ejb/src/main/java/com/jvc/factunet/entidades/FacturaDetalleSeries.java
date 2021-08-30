package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "factura_detalle_series")
public class FacturaDetalleSeries implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FacturaDetalleSeriesPK facturaDetalleSeriesPK;
    @JoinColumn(name = "factura_detalle", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FacturaDetalle facturaDetalle;
    @JoinColumn(name = "bodega_actual", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bodega bodegaActual;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;

    public FacturaDetalleSeries() {
    }

    public FacturaDetalleSeries(FacturaDetalleSeriesPK facturaDetalleSeriesPK) {
        this.facturaDetalleSeriesPK = facturaDetalleSeriesPK;
    }

    public FacturaDetalleSeries(int facturaDetalle, String codigoBarras) {
        this.facturaDetalleSeriesPK = new FacturaDetalleSeriesPK(facturaDetalle, codigoBarras);
    }

    public FacturaDetalleSeriesPK getFacturaDetalleSeriesPK() {
        return facturaDetalleSeriesPK;
    }

    public void setFacturaDetalleSeriesPK(FacturaDetalleSeriesPK facturaDetalleSeriesPK) {
        this.facturaDetalleSeriesPK = facturaDetalleSeriesPK;
    }

    public FacturaDetalle getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(FacturaDetalle facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }

    public Bodega getBodegaActual() {
        return bodegaActual;
    }

    public void setBodegaActual(Bodega bodegaActual) {
        this.bodegaActual = bodegaActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
