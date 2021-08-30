package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "parroquia")
public class Parroquia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParroquiaPK parroquiaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "descripcion")
    private String descripcion;
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "codigo_msp")
    private String codigoMsp;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumns({
        @JoinColumn(name = "id_canton", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Canton canton;
    @Column(name = "coordenada_lat")
    private BigDecimal coordenadaLatitud;
    @Column(name = "coordenada_long")
    private BigDecimal coordenadaLongitud;
    

    public Parroquia() {
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Parroquia(ParroquiaPK parroquiaPK) {
        this.parroquiaPK = parroquiaPK;
    }

    public Parroquia(ParroquiaPK parroquiaPK, String descripcion) {
        this.parroquiaPK = parroquiaPK;
        this.descripcion = descripcion;
    }

    public Parroquia(Integer idProvincia, Integer idCanton, Integer id) {
        this.parroquiaPK = new ParroquiaPK(idProvincia, idCanton, id);
    }

    public ParroquiaPK getParroquiaPK() {
        return parroquiaPK;
    }

    public void setParroquiaPK(ParroquiaPK parroquiaPK) {
        this.parroquiaPK = parroquiaPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoMsp() {
        return codigoMsp;
    }

    public void setCodigoMsp(String codigoMsp) {
        this.codigoMsp = codigoMsp;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public BigDecimal getCoordenadaLatitud() {
        return coordenadaLatitud;
    }

    public void setCoordenadaLatitud(BigDecimal coordenadaLatitud) {
        this.coordenadaLatitud = coordenadaLatitud;
    }

    public BigDecimal getCoordenadaLongitud() {
        return coordenadaLongitud;
    }

    public void setCoordenadaLongitud(BigDecimal coordenadaLongitud) {
        this.coordenadaLongitud = coordenadaLongitud;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parroquiaPK != null ? parroquiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parroquia)) {
            return false;
        }
        Parroquia other = (Parroquia) object;
        if ((this.parroquiaPK == null && other.parroquiaPK != null) || (this.parroquiaPK != null && !this.parroquiaPK.equals(other.parroquiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Parroquia[ parroquiaPK=" + parroquiaPK + " ]";
    }
    
    
}
