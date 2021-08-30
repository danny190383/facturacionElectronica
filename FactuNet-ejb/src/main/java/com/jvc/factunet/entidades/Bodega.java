package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "bodega")
public class Bodega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_bodega")
    @SequenceGenerator(name = "secuencia_bodega", sequenceName = "secuencia_bodega", allocationSize = 1)
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Size(min = 1, max = 2)
    @Column(name = "siglas")
    private String siglas;
    @Size(max = 200)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "nivel")
    private Integer nivel;
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    private List<Bodega> bodegaList;
    @JoinColumn(name = "padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bodega padre;
    @JoinColumn(name = "ciudad", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ciudad ciudad;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    @OneToMany(mappedBy = "bodega", fetch = FetchType.LAZY)
    private List<ProductoStock> productoStockList;
    
    public Bodega() {
    }

    public Bodega(Integer codigo) {
        this.codigo = codigo;
    }

    public List<ProductoStock> getProductoStockList() {
        return productoStockList;
    }

    public void setProductoStockList(List<ProductoStock> productoStockList) {
        this.productoStockList = productoStockList;
    }

    public Bodega(Integer codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Bodega> getBodegaList() {
        return bodegaList;
    }

    public void setBodegaList(List<Bodega> bodegaList) {
        this.bodegaList = bodegaList;
    }

    public Bodega getPadre() {
        return padre;
    }

    public void setPadre(Bodega padre) {
        this.padre = padre;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
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
        if (!(object instanceof Bodega)) {
            return false;
        }
        Bodega other = (Bodega) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Bodega[ codigo=" + codigo + " ]";
    }
}
