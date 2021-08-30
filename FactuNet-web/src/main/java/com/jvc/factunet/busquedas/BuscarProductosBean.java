package com.jvc.factunet.busquedas;

import com.jvc.factunet.bean.ProductoAdminBean;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@ViewScoped
public class BuscarProductosBean extends ProductoAdminBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarProductosBean.class.getName());

    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    
    private List<Producto> listaProductosTodosSelc;
    private List<GrupoProducto> listaPadres;

    public BuscarProductosBean() {
        this.listaPadres = new ArrayList<>();
        this.listaProductosTodosSelc = new ArrayList<>();
    }
    
    @PostConstruct
    public void initLugar()
    {
        this.listaPadres = super.grupoProductoServicio.listarPorNivelBodega(1,this.empresa.getCodigo());
        super.setLugar("2");
        this.visible = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getCuenta().getVerGrupoBusqueda().equals("1");
        if(this.visible)
        {
            this.onNodeSelect(this.listaPadres.get(0));
        }
    }
    
    public void seleccionar(ProductoBodega event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaProductosTodosSelc);
    }
    
    public void eliminar(ProductoBodega parametro) {
        try {
            this.productoBodegaServicio.eliminar(parametro);
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void onNodeSelect(GrupoProducto event) {
        this.grupoProductoSelc = event;
        super.setListaGruposBuscar(this.llenarGruposBuscar());
        if(this.grupoProductoSelc.getTipo() == 1)
        {
            super.llenarTabla();
        }
        if(this.grupoProductoSelc.getTipo() == 2)
        {
            this.llenarTablaServicios();
        }
        if(this.grupoProductoSelc.getTipo() == 3)
        {
            this.llenarTablaPaquetes();
        }
    }
    
    public void onTabChange(TabChangeEvent event) {
        
        for(GrupoProducto grupo : this.listaPadres){
            if(grupo.getNombre().equals(event.getTab().getTitle())){
                onNodeSelect(grupo);
                break;
            }
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        this.listaProductosTodosSelc.add((Producto)event.getObject());
    }
    
    public void eliminarSeleccion(Producto parametro) {
        try {
            this.listaProductosTodosSelc.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<Producto> getListaProductosTodosSelc() {
        return listaProductosTodosSelc;
    }

    public void setListaProductosTodosSelc(List<Producto> listaProductosTodosSelc) {
        this.listaProductosTodosSelc = listaProductosTodosSelc;
    }

    public List<GrupoProducto> getListaPadres() {
        return listaPadres;
    }

    public void setListaPadres(List<GrupoProducto> listaPadres) {
        this.listaPadres = listaPadres;
    }
}
