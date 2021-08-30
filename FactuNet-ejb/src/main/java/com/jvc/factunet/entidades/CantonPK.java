package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class CantonPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_provincia")
    private Integer idProvincia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;

    public CantonPK() {
    }

    public CantonPK(Integer idProvincia, Integer id) {
        this.idProvincia = idProvincia;
        this.id = id;
    }

    public Integer getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Integer idProvincia) {
        this.idProvincia = idProvincia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idProvincia;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CantonPK)) {
            return false;
        }
        CantonPK other = (CantonPK) object;
        if (this.idProvincia != other.idProvincia) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.CantonPK[ idProvincia=" + idProvincia + ", id=" + id + " ]";
    }
    
    
}
