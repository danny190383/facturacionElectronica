package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "retencion")
public class Retencion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RetencionPK retencionPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @JoinColumn(name = "documento", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DocumentoRetencion documentoRetencion;
    @JoinColumn(name = "tipo_retencion", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoRetencion tipoRetencion;

    public Retencion() {
    }

    public Retencion(RetencionPK retencionPK) {
        this.retencionPK = retencionPK;
    }

    public RetencionPK getRetencionPK() {
        return retencionPK;
    }

    public void setRetencionPK(RetencionPK retencionPK) {
        this.retencionPK = retencionPK;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public DocumentoRetencion getDocumentoRetencion() {
        return documentoRetencion;
    }

    public void setDocumentoRetencion(DocumentoRetencion documentoRetencion) {
        this.documentoRetencion = documentoRetencion;
    }

    public TipoRetencion getTipoRetencion() {
        return tipoRetencion;
    }

    public void setTipoRetencion(TipoRetencion tipoRetencion) {
        this.tipoRetencion = tipoRetencion;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }
}
