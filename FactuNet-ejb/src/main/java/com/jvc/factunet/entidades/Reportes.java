package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "reportes")
public class Reportes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 110)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "codigo_padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Reportes codigoPadre;

    public Reportes() {
    }

    public Reportes(String codigo) {
        this.codigo = codigo;
    }

    public Reportes(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOpcEstado() {
        return estado;
    }

    public void setOpcEstado(String estado) {
        this.estado = estado;
    }

    public Reportes getCodigoPadre() {
        return codigoPadre;
    }

    public void setCodigoPadre(Reportes codigoPadre) {
        this.codigoPadre = codigoPadre;
    }
}
