package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.UnidadMedida;
import com.jvc.factunet.servicios.UnidadMedidaServicio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class CatalogosProducto {

    @EJB
    public UnidadMedidaServicio unidadMedidaServicio;
   
    
    private List<UnidadMedida> listaUnidadMedida;
    
    public CatalogosProducto() {
        this.listaUnidadMedida = new ArrayList<>();
    }
    
    @PostConstruct
    public void initCatalogos()
    {
        this.listaUnidadMedida.addAll(this.unidadMedidaServicio.listar());
    }
    
    public void refreshUnidadMedida()
    {
        this.listaUnidadMedida.clear();
        this.listaUnidadMedida.addAll(this.unidadMedidaServicio.listar());
    }

    public List<UnidadMedida> getListaUnidadMedida() {
        this.refreshUnidadMedida();
        return this.listaUnidadMedida;
    }

    public void setListaUnidadMedida(List<UnidadMedida> listaUnidadMedida) {
        this.listaUnidadMedida = listaUnidadMedida;
    }
}
