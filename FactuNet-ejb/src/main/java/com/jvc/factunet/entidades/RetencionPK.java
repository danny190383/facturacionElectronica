package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class RetencionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_retencion")
    private Integer tipoRetencion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "documento")
    private Integer documento;

    public RetencionPK() {
    }

    public RetencionPK(Integer tipoRetencion, Integer documento) {
        this.tipoRetencion = tipoRetencion;
        this.documento = documento;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public Integer getTipoRetencion() {
        return tipoRetencion;
    }

    public void setTipoRetencion(Integer tipoRetencion) {
        this.tipoRetencion = tipoRetencion;
    }
}
