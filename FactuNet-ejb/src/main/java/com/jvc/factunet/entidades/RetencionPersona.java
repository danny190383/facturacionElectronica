package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "retencion_persona")
public class RetencionPersona implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_retencion_persona")
    @SequenceGenerator(name = "secuencia_retencion_persona", sequenceName = "secuencia_retencion_persona", allocationSize = 1)
    private Integer codigo;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "tipo_retencion", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoRetencion tipoRetencion;
    @JoinColumn(name = "persona", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Persona persona;

    public RetencionPersona() {
    }

    public RetencionPersona(Integer codigo) {
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public TipoRetencion getTipoRetencion() {
        return tipoRetencion;
    }

    public void setTipoRetencion(TipoRetencion tipoRetencion) {
        this.tipoRetencion = tipoRetencion;
    }

}
