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
import javax.persistence.Table;

@Entity
@Table(name = "empresa_catalogo_parametros")
public class EmpresaCatalogoParametro implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne
    private Empresa empresa;
    @JoinColumn(name = "catalogo_parametros", referencedColumnName = "id")
    @ManyToOne
    private CatalogoParametrosEmpresa catalogoParametrosEmpresa;

    public EmpresaCatalogoParametro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public CatalogoParametrosEmpresa getCatalogoParametrosEmpresa() {
        return catalogoParametrosEmpresa;
    }

    public void setCatalogoParametrosEmpresa(CatalogoParametrosEmpresa catalogoParametrosEmpresa) {
        this.catalogoParametrosEmpresa = catalogoParametrosEmpresa;
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
        if (!(object instanceof EmpresaCatalogoParametro)) {
            return false;
        }
        EmpresaCatalogoParametro other = (EmpresaCatalogoParametro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.EmpresaCatalogoParametro[ id=" + id + " ]";
    }
}
