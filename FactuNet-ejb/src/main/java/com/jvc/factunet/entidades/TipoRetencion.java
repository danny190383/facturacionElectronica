package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tipo_retencion")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("14")
public class TipoRetencion extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo_impuesto")
    private String codigoImpuesto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "calculado_con")
    private String calculadoCon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "concepto")
    private String concepto;
    @Column(name = "tipo")
    private String tipo;

    public TipoRetencion() {
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCalculadoCon() {
        return calculadoCon;
    }

    public void setCalculadoCon(String calculadoCon) {
        this.calculadoCon = calculadoCon;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCodigoImpuesto() {
        return codigoImpuesto;
    }

    public void setCodigoImpuesto(String codigoImpuesto) {
        this.codigoImpuesto = codigoImpuesto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
