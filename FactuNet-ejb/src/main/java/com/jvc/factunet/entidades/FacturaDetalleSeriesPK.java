package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class FacturaDetalleSeriesPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "factura_detalle")
    private int facturaDetalle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "codigo_barras")
    private String codigoBarras;

    public FacturaDetalleSeriesPK() {
    }

    public FacturaDetalleSeriesPK(int facturaDetalle, String codigoBarras) {
        this.facturaDetalle = facturaDetalle;
        this.codigoBarras = codigoBarras;
    }

    public int getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(int facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}
