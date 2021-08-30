package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "retencion_tarjeta")
public class RetencionTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_retencion_tarjeta")
    @SequenceGenerator(name = "secuencia_retencion_tarjeta", sequenceName = "secuencia_retencion_tarjeta", allocationSize = 1)
    private Integer codigo;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "tipo_retencion", referencedColumnName = "codigo")
    @ManyToOne
    private TipoRetencion tipoRetencion;
    @JoinColumn(name = "tarjeta_empresa", referencedColumnName = "codigo")
    @ManyToOne
    private TarjetaEmpresa tarjetaEmpresa;

    public RetencionTarjeta() {
    }

    public RetencionTarjeta(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public TipoRetencion getTipoRetencion() {
        return tipoRetencion;
    }

    public void setTipoRetencion(TipoRetencion tipoRetencion) {
        this.tipoRetencion = tipoRetencion;
    }

    public TarjetaEmpresa getTarjetaEmpresa() {
        return tarjetaEmpresa;
    }

    public void setTarjetaEmpresa(TarjetaEmpresa tarjetaEmpresa) {
        this.tarjetaEmpresa = tarjetaEmpresa;
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
        if (!(object instanceof RetencionTarjeta)) {
            return false;
        }
        RetencionTarjeta other = (RetencionTarjeta) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.RetencionTarjeta[ codigo=" + codigo + " ]";
    }
}
