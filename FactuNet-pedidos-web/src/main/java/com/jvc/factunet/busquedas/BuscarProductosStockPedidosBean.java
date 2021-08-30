package com.jvc.factunet.busquedas;

import com.jvc.factunet.bean.ProductoStockPedidosBean;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.ProductoPaqueteServicio;
import com.jvc.factunet.servicios.ProductoServiciosServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class BuscarProductosStockPedidosBean extends ProductoStockPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarProductosStockPedidosBean.class.getName());
    
    @EJB
    private ProductoServiciosServicio productoServiciosServicio;
    @EJB
    private ProductoPaqueteServicio productoPaqueteServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    @EJB
    private BodegaServicio bodegaServicio;
    
    private Producto productoSelc;
    private ProductoStock productoStockSelc;
    private LazyDataModel<ProductoServicio> lazyModelServicios;
    private LazyDataModel<ProductoPaquete> lazyModelPaquetes;
    private String opcion;
    private List<GrupoProducto> listaPadres;
    private List<Producto> listaProductosTodosSelc;

    public BuscarProductosStockPedidosBean() {
        this.listaPadres = new ArrayList<>();
        this.listaProductosTodosSelc = new ArrayList<>();
    }
    
    @PostConstruct
    public void initPS(){
        try {
            this.listaPadres = this.grupoProductoServicio.listarPorNivel(1,super.getEmpresa().getCodigo());
            this.opcion = "S";
            this.setVisible(true);
            super.getListaBodegas().clear();
            super.getListaBodegas().addAll(this.bodegaServicio.listar(this.getEmpresa().getCodigo()));
            super.setBodegaSelc(super.getListaBodegas().get(0).getCodigo());
            if(super.isVisible())
            {
                this.onNodeSelect(this.listaPadres.get(0));
            }
            else
            {
                this.llenarTabla();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorCargarDatos"));
        }
    }
    
    public void llenarTablaServicios()
    {
        try {
            this.lazyModelServicios = new LazyDataModel<ProductoServicio>()
            {
                @Override
                public List<ProductoServicio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if (filters != null && filters.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY; 
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("grupo.nombre"))
                            {grupo=(String)filters.get(filterProperty);}
                        }
                        if(isVisible())
                        {
                            if((getGrupoProductoSelc().getGrupoProductoList() == null) ||  (getGrupoProductoSelc().getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoServicio> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoServiciosServicio.listarBuscar(nombre, getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                    lazyModelServicios.setRowCount(productoServiciosServicio.contar(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoServiciosServicio.listarBuscarBarras(codigoBarras,getEmpresa().getCodigo(), pageSize, first);
                                    lazyModelServicios.setRowCount(productoServiciosServicio.contarBarras(codigoBarras,getEmpresa().getCodigo()).intValue());
                                }
                                return result;
                            }
                            else
                            {
                                List<ProductoServicio> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoServiciosServicio.listarBuscarPadre(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                    lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoServiciosServicio.listarBuscarBarras(codigoBarras,getEmpresa().getCodigo(), pageSize, first);
                                    lazyModelServicios.setRowCount(productoServiciosServicio.contarBarras(codigoBarras,getEmpresa().getCodigo()).intValue());
                                }
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarBuscar(nombre, getEmpresa().getCodigo(), grupo, pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contar(nombre,getEmpresa().getCodigo(),grupo).intValue());
                            return result;
                        }
                    }
                    else
                    {
                        if(isVisible())
                        {
                            if((getGrupoProductoSelc().getGrupoProductoList() == null) ||  (getGrupoProductoSelc().getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoServicio> result = productoServiciosServicio.listar(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contar(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                return result;
                            }
                            else
                            {
                                List<ProductoServicio> result = productoServiciosServicio.listarPadre(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarBuscar(StringUtils.EMPTY, getEmpresa().getCodigo(),StringUtils.EMPTY, pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contar(StringUtils.EMPTY,getEmpresa().getCodigo(),StringUtils.EMPTY).intValue());
                            return result;
                        }
                    }
                }
                
                @Override
                public ProductoServicio getRowData(String rowKey) {
                    List<ProductoServicio> lista = (List<ProductoServicio>) getWrappedData();
                    for(ProductoServicio car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoServicio car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void llenarTablaPaquetes()
    {
        try {
            this.lazyModelPaquetes = new LazyDataModel<ProductoPaquete>()
            {
                @Override
                public List<ProductoPaquete> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if (filters != null && filters.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY; 
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("grupo.nombre"))
                            {grupo=(String)filters.get(filterProperty);}
                        }
                        if(isVisible())
                        {
                            if((getGrupoProductoSelc().getGrupoProductoList() == null) ||  (getGrupoProductoSelc().getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoPaquete> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoPaqueteServicio.listarBuscar(nombre, getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                    lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoPaqueteServicio.listarBuscarBarras(codigoBarras,getEmpresa().getCodigo(), pageSize, first);
                                    lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarBarras(codigoBarras,getEmpresa().getCodigo()).intValue());
                                }
                                return result;
                            }
                            else
                            {
                                List<ProductoPaquete> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoPaqueteServicio.listarBuscarPadre(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                    lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(nombre,getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoPaqueteServicio.listarBuscarBarras(codigoBarras,getEmpresa().getCodigo(), pageSize, first);
                                    lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarBarras(codigoBarras,getEmpresa().getCodigo()).intValue());
                                }
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarBuscar(nombre, getEmpresa().getCodigo(),grupo, pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(nombre,getEmpresa().getCodigo(),grupo).intValue());
                            return result;
                        }
                    }
                    else
                    {
                        if(isVisible())
                        {
                            if((getGrupoProductoSelc().getGrupoProductoList() == null) ||  (getGrupoProductoSelc().getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoPaquete> result = productoPaqueteServicio.listar(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                return result;
                            }
                            else
                            {
                                List<ProductoPaquete> result = productoPaqueteServicio.listarPadre(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(getEmpresa().getCodigo(),getGrupoProductoSelc().getCodigo()).intValue());
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarBuscar(StringUtils.EMPTY, getEmpresa().getCodigo(),StringUtils.EMPTY, pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(StringUtils.EMPTY,getEmpresa().getCodigo(),StringUtils.EMPTY).intValue());
                            return result;
                        }
                    }
                }
                
                @Override
                public ProductoPaquete getRowData(String rowKey) {
                    List<ProductoPaquete> lista = (List<ProductoPaquete>) getWrappedData();
                    for(ProductoPaquete car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoPaquete car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        if(event.getObject() instanceof ProductoStock){
            ProductoBodega producto = new ProductoBodega();
            producto = ((ProductoStock)event.getObject()).getProductoBodega();
            producto.setStock(((ProductoStock)event.getObject()).getStock());
            producto.setBodega(((ProductoStock)event.getObject()).getBodega());
            this.listaProductosTodosSelc.add(producto);
        }
        else
        {
            this.listaProductosTodosSelc.add((Producto)event.getObject());
        }
    }
    
    public void eliminar(Producto parametro) {
        try {
            this.listaProductosTodosSelc.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaProductosTodosSelc);
    }
    
    public void onTabChange(TabChangeEvent event) {
        
        for(GrupoProducto grupo : this.listaPadres){
            if(grupo.getNombre().equals(event.getTab().getTitle())){
                onNodeSelect(grupo);
                break;
            }
        }
    }
    
    public void onNodeSelect(GrupoProducto event) {
        super.setGrupoProductoSelc(event);
        if(super.getGrupoProductoSelc().getTipo() == 1)
        {
            this.llenarTabla();
            this.opcion = "P";
        }
        if(super.getGrupoProductoSelc().getTipo() == 2)
        {
            this.llenarTablaServicios();
            this.opcion = "S";
        }
        if(super.getGrupoProductoSelc().getTipo() == 3)
        {
            this.llenarTablaPaquetes();
            this.opcion = "PA";
        }
    }
    
    public void escogerTipo()
    {
        if(this.opcion.equals("P"))
        {
            this.llenarTabla();
        }
        if(this.opcion.equals("S"))
        {
            this.llenarTablaServicios();
        }
        if(this.opcion.equals("PA"))
        {
            this.llenarTablaPaquetes();
        }
    }
    
    public LazyDataModel<ProductoServicio> getLazyModelServicios() {
        return lazyModelServicios;
    }

    public void setLazyModelServicios(LazyDataModel<ProductoServicio> lazyModelServicios) {
        this.lazyModelServicios = lazyModelServicios;
    }

    public LazyDataModel<ProductoPaquete> getLazyModelPaquetes() {
        return lazyModelPaquetes;
    }

    public void setLazyModelPaquetes(LazyDataModel<ProductoPaquete> lazyModelPaquetes) {
        this.lazyModelPaquetes = lazyModelPaquetes;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public List<GrupoProducto> getListaPadres() {
        return listaPadres;
    }

    public void setListaPadres(List<GrupoProducto> listaPadres) {
        this.listaPadres = listaPadres;
    }

    public List<Producto> getListaProductosTodosSelc() {
        return listaProductosTodosSelc;
    }

    public void setListaProductosTodosSelc(List<Producto> listaProductosTodosSelc) {
        this.listaProductosTodosSelc = listaProductosTodosSelc;
    }
}
