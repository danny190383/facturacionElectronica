package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "grupo_producto")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("1")
public class GrupoProducto extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "tipo")
    private Integer tipo;
    @JoinColumn(name = "padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoProducto padre;
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    private List<GrupoProducto> grupoProductoList;

    public GrupoProducto() {
    }

    public GrupoProducto(Integer codigo) {
        super.setCodigo(codigo);
    }

    public GrupoProducto getPadre() {
        return padre;
    }

    public void setPadre(GrupoProducto padre) {
        this.padre = padre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public List<GrupoProducto> getGrupoProductoList() {
        if(grupoProductoList!= null){
            Collections.sort(grupoProductoList, new Comparator<GrupoProducto>() {
                @Override
                public int compare(GrupoProducto o1, GrupoProducto o2) {
                     return o1.getNombre().compareTo(o2.getNombre());
                }
            });
        }
        return grupoProductoList;
    }

    public void setGrupoProductoList(List<GrupoProducto> grupoProductoList) {
        this.grupoProductoList = grupoProductoList;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
