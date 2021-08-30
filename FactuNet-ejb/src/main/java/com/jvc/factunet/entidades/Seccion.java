package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "seccion")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("20")
public class Seccion extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "columnas")
    private Integer columnas;
    @Size(max = 800)
    @Column(name = "detalle")
    private String detalle;
    @OneToMany(mappedBy = "seccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mesa> mesaList;
    
    public Seccion() {
    }

    public Seccion(Integer codigo) {
        super.setCodigo(codigo);
    }

    public Integer getColumnas() {
        return columnas;
    }

    public void setColumnas(Integer columnas) {
        this.columnas = columnas;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<Mesa> getMesaList() {
        return mesaList;
    }

    public void setMesaList(List<Mesa> mesaList) {
        this.mesaList = mesaList;
    }
}
