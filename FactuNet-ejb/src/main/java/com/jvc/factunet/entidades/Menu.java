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
import javax.validation.constraints.Size;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "codigo")
    @GeneratedValue(generator = "codigo_menu", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "codigo_menu", allocationSize = 1,
    initialValue = 1, sequenceName = "secuencia_menu")
    private Integer codigo;
    @Column(name = "nivel")
    private Integer nivel;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 200)
    @Column(name = "url")
    private String url;
    @Size(max = 200)
    @Column(name = "color")
    private String color;
    @Size(max = 1)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "icon")
    private String icon;
    @JoinColumn(name = "padre", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu padre;

    public Menu() {
    }

    public Menu(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Menu getPadre() {
        return padre;
    }

    public void setPadre(Menu padre) {
        this.padre = padre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
