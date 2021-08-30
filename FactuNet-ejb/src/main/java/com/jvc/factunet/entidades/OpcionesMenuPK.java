package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class OpcionesMenuPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "menu")
    private int menu;

    public OpcionesMenuPK() {
    }

    public OpcionesMenuPK(int rol, int menu) {
        this.rol = rol;
        this.menu = menu;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

}
