package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "canton")
public class Canton implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CantonPK cantonPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(min = 1, max = 40)
    @Column(name = "codigo_msp")
    private String codigoMsp;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "id_provincia", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Provincia provincia;
    @OneToMany(mappedBy = "canton", fetch = FetchType.LAZY)
    private List<Parroquia> listParroquia;
    @Column(name = "coordenada_lat")
    private BigDecimal coordenadaLatitud;
    @Column(name = "coordenada_long")
    private BigDecimal coordenadaLongitud;

    public Canton() {
    }

    public Canton(CantonPK cantonPK) {
        this.cantonPK = cantonPK;
    }

    public Canton(CantonPK cantonPK, String descripcion) {
        this.cantonPK = cantonPK;
        this.descripcion = descripcion;
    }

    public Canton(Integer idProvincia, Integer idCanton) {
        this.cantonPK = new CantonPK(idProvincia, idCanton);
    }

    public CantonPK getCantonPK() {
        return cantonPK;
    }

    public void setCantonPK(CantonPK cantonPK) {
        this.cantonPK = cantonPK;
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

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Parroquia> getListParroquia() {
        return listParroquia;
    }

    public void setListParroquia(List<Parroquia> listParroquia) {
        this.listParroquia = listParroquia;
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
        hash += (cantonPK != null ? cantonPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Canton)) {
            return false;
        }
        Canton other = (Canton) object;
        if ((this.cantonPK == null && other.cantonPK != null) || (this.cantonPK != null && !this.cantonPK.equals(other.cantonPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Canton[ cantonPK=" + cantonPK + " ]";
    }

}
