package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "provincia")
public class Provincia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
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
    @OneToMany(mappedBy = "provincia", fetch = FetchType.LAZY)
    private List<Canton> listCanton;
    @Column(name = "coordenada_lat")
    private BigDecimal coordenadaLatitud;
    @Column(name = "coordenada_long")
    private BigDecimal coordenadaLongitud;
    

    public Provincia() {
    }

    public Provincia(Integer id) {
        this.id = id;
    }

    public Provincia(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Canton> getListCanton() {
        return listCanton;
    }

    public List<Canton> getListCantonOrdenado() {
        if (listCanton==null)
            return new ArrayList<>();
        return listCanton.stream().sorted((c1,c2)->c1.getDescripcion().compareTo(c2.getDescripcion())).collect(Collectors.toList());
    }

    public void setListCanton(List<Canton> listCanton) {
        this.listCanton = listCanton;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Provincia[ id=" + id + " ]";
    }
    
    
}
