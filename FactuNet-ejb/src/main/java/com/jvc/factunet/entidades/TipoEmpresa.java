package com.jvc.factunet.entidades;

import java.io.Serializable;
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
@Table(name = "tipo_empresa")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("27")
public class TipoEmpresa extends Mantenimiento implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "tipo")
    private Integer tipo;
    @JoinColumn(name = "padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoEmpresa padre;
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    private List<TipoEmpresa> tipoEmpresaList;

    public TipoEmpresa() {
    }
    
    public TipoEmpresa(Integer codigo) {
        super.setCodigo(codigo);
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public TipoEmpresa getPadre() {
        return padre;
    }

    public void setPadre(TipoEmpresa padre) {
        this.padre = padre;
    }

    public List<TipoEmpresa> getTipoEmpresaList() {
        return tipoEmpresaList;
    }

    public void setTipoEmpresaList(List<TipoEmpresa> tipoEmpresaList) {
        this.tipoEmpresaList = tipoEmpresaList;
    }
    
    
}
