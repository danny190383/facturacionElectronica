package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("2")
public class ProductoServicio extends Producto implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "productoServicio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetencionServicio> retencionServicioList;
    
    public ProductoServicio() {
    }

    public ProductoServicio(Integer codigo) {
        super(codigo);
    }

    public ProductoServicio(Integer codigo, String nombre) {
        super(codigo, nombre);
    }

    public List<RetencionServicio> getRetencionServicioList() {
        return retencionServicioList;
    }

    public void setRetencionServicioList(List<RetencionServicio> retencionServicioList) {
        this.retencionServicioList = retencionServicioList;
    }
}
