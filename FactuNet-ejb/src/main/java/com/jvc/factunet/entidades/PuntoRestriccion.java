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

@Entity
@Table(name = "punto_restriccion")
public class PuntoRestriccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_restriccion")
    @SequenceGenerator(name = "secuencia_restriccion", sequenceName = "secuencia_restriccion", allocationSize = 1)
    private Integer codigo;
    @JoinColumn(name = "punto_venta", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PuntoVenta puntoVenta;
    @JoinColumn(name = "grupo", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GrupoProducto grupo;

    public PuntoRestriccion() {
    }

    public PuntoRestriccion(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public GrupoProducto getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoProducto grupo) {
        this.grupo = grupo;
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
        if (!(object instanceof PuntoRestriccion)) {
            return false;
        }
        PuntoRestriccion other = (PuntoRestriccion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.PuntoRestriccion[ codigo=" + codigo + " ]";
    }
}
