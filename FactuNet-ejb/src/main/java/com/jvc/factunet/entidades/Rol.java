package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "rol")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("15")
public class Rol extends Mantenimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<OpcionesMenu> opcionesMenuList;

    public Rol() {
    }

    public List<OpcionesMenu> getOpcionesMenuList() {
        return opcionesMenuList;
    }

    public void setOpcionesMenuList(List<OpcionesMenu> opcionesMenuList) {
        this.opcionesMenuList = opcionesMenuList;
    }
}
