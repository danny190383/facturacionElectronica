package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.Marca;
import com.jvc.factunet.entidades.UnidadMedida;
import com.jvc.factunet.servicios.MarcaServicio;
import com.jvc.factunet.servicios.UnidadMedidaServicio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class CatalogosProductoPedidos {

    @EJB
    public UnidadMedidaServicio unidadMedidaServicio;
    @EJB
    public MarcaServicio marcaServicio;
    
    private List<UnidadMedida> listaUnidadMedida;
    private List<Marca> listaMarca;
    
    public CatalogosProductoPedidos() {
        this.listaUnidadMedida = new ArrayList<>();
        this.listaMarca = new ArrayList<>();
    }
    
    @PostConstruct
    public void initCatalogos()
    {
        this.listaUnidadMedida.addAll(this.unidadMedidaServicio.listar());
        this.listaMarca.addAll(this.marcaServicio.listar());
    }
    
    public void refreshMarca()
    {
        this.listaMarca.clear();
        this.listaMarca.addAll(this.marcaServicio.listar());
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

    public List<Marca> getListaMarca() {
        this.refreshMarca();
        return listaMarca;
    }

    public void setListaMarca(List<Marca> listaMarca) {
        this.listaMarca = listaMarca;
    }
}
