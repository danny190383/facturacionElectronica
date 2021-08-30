package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ParroquiaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_provincia")
    private Integer idProvincia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_canton")
    private Integer idCanton;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;

    public ParroquiaPK() {
    }

    public ParroquiaPK(Integer idProvincia, Integer idCanton, Integer id) {
        this.idProvincia = idProvincia;
        this.idCanton = idCanton;
        this.id = id;
    }

    public Integer getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Integer idProvincia) {
        this.idProvincia = idProvincia;
    }

    public Integer getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(Integer idCanton) {
        this.idCanton = idCanton;
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
        hash += (int) idCanton;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParroquiaPK)) {
            return false;
        }
        ParroquiaPK other = (ParroquiaPK) object;
        if (this.idProvincia != other.idProvincia) {
            return false;
        }
        if (this.idCanton != other.idCanton) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.ParroquiaPK[ idProvincia=" + idProvincia + ", idCanton=" + idCanton + ", id=" + id + " ]";
    }
    
}
