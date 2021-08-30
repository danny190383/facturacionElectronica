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
import javax.persistence.Table;

@Entity
@Table(name = "detalle_factura_impuesto_tarifa")
public class DetalleFacturaImpuestoTarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "detalle_factura", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacturaDetalle detalleFactura;
    @JoinColumn(name = "impuesto_tarifa", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImpuestoTarifa impuestoTarifa;
    @Column(name = "etiqueta")
    private String etiqueta;

    public DetalleFacturaImpuestoTarifa() {
    }

    public DetalleFacturaImpuestoTarifa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FacturaDetalle getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(FacturaDetalle detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public ImpuestoTarifa getImpuestoTarifa() {
        return impuestoTarifa;
    }

    public void setImpuestoTarifa(ImpuestoTarifa impuestoTarifa) {
        this.impuestoTarifa = impuestoTarifa;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleFacturaImpuestoTarifa)) {
            return false;
        }
        DetalleFacturaImpuestoTarifa other = (DetalleFacturaImpuestoTarifa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.DetalleFacturaImpuestoTarifa[ id=" + id + " ]";
    }
    
}
