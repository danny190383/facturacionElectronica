package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.servicios.DocumentosServicios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class KardexServiciosBean implements Serializable{
    @EJB
    private DocumentosServicios documentosServicios;
    
    private List<Object> listaKardex;
    private Producto producto;
    
    public KardexServiciosBean() {
        this.listaKardex = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.producto = (Producto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        this.verKardex();
    }
    
    public void verKardex()
    {
        try {
            this.listaKardex.clear();
            this.listaKardex.addAll(this.documentosServicios.kardexServicios(this.producto.getCodigo()));
        } catch (Exception e) {
        }
    }

    public List<Object> getListaKardex() {
        return listaKardex;
    }

    public void setListaKardex(List<Object> listaKardex) {
        this.listaKardex = listaKardex;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
