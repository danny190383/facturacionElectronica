package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "opciones_menu")
@XmlRootElement
public class OpcionesMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OpcionesMenuPK opcionesMenuPK;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "rol", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Rol rol;
    @JoinColumn(name = "menu", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Menu menu;

    public OpcionesMenu() {
    }

    public OpcionesMenu(OpcionesMenuPK opcionesMenuPK) {
        this.opcionesMenuPK = opcionesMenuPK;
    }

    public OpcionesMenu(int rol, int menu) {
        this.opcionesMenuPK = new OpcionesMenuPK(rol, menu);
    }

    public OpcionesMenuPK getOpcionesMenuPK() {
        return opcionesMenuPK;
    }

    public void setOpcionesMenuPK(OpcionesMenuPK opcionesMenuPK) {
        this.opcionesMenuPK = opcionesMenuPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}
