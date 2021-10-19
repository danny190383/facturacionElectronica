package com.jvc.factunet.busquedas;

import com.jvc.factunet.bean.ProductoStockBean;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.ProductoPaqueteServicio;
import com.jvc.factunet.servicios.ProductoServiciosServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarProductosStockBean extends ProductoStockBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarProductosStockBean.class.getName());
    
    @EJB
    private ProductoServiciosServicio productoServiciosServicio;
    @EJB
    private ProductoPaqueteServicio productoPaqueteServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    
    private Producto productoSelc;
    private ProductoStock productoStockSelc;
    private LazyDataModel<ProductoServicio> lazyModelServicios;
    private LazyDataModel<ProductoPaquete> lazyModelPaquetes;
    private String opcion;
    private List<GrupoProducto> listaPadres;
    private List<Producto> listaProductosTodosSelc;
    private List<FacturaDetalle> lotes;

    public BuscarProductosStockBean() {
        this.listaPadres = new ArrayList<>();
        this.listaProductosTodosSelc = new ArrayList<>();
        this.lotes = new ArrayList<>();
    }
    
    @Override
    public void init(){
        try {
            this.listaPadres = super.grupoProductoServicio.listarPorNivel(1,this.empresa.getCodigo());
            this.opcion = "P";
            this.visible = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getCuenta().getVerGrupoBusqueda().equals("1");
            super.getListaBodegas().clear();
            super.getListaBodegas().addAll(super.bodegaServicio.listar(this.empresa.getCodigo()));
            super.setBodegaSelc(super.getListaBodegas().get(0).getCodigo());
            if(this.visible)
            {
                this.onNodeSelect(this.listaPadres.get(0));
            }
            else
            {
                this.llenarTabla();
            }
            super.setListaGruposBuscar(this.llenarGruposBuscar());
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
                public List<ProductoServicio> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY; 
                        for (FilterMeta meta : filterMeta.values()) {
                            if(meta.getFilterField().equals("nombre"))
                            {nombre=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("codigoBarras"))
                            {codigoBarras=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("grupo.nombre"))
                            {grupo=(String)meta.getFilterValue();}
                        }
                        if(visible)
                        {
                            List<ProductoServicio> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoServiciosServicio.listarBuscarPadre(nombre,empresa.getCodigo(), getListaGruposBuscar(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(nombre,empresa.getCodigo(), getListaGruposBuscar()).intValue());
                            }
                            else
                            {
                                result = productoServiciosServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                            }
                            return result;
                        }
                        else
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarBuscar(nombre, empresa.getCodigo(), grupo, pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contar(nombre,empresa.getCodigo(),grupo).intValue());
                            return result;
                        }
                    }
                    else
                    {
                        if(visible)
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarPadre(empresa.getCodigo(), getListaGruposBuscar(), pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(empresa.getCodigo(),getListaGruposBuscar()).intValue());
                            return result;
                        }
                        else
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarBuscar(StringUtils.EMPTY, empresa.getCodigo(),StringUtils.EMPTY, pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contar(StringUtils.EMPTY,empresa.getCodigo(),StringUtils.EMPTY).intValue());
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
                public List<ProductoPaquete> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY; 
                        for (FilterMeta meta : filterMeta.values()) {
                            if(meta.getFilterField().equals("nombre"))
                            {nombre=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("codigoBarras"))
                            {codigoBarras=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("grupo.nombre"))
                            {grupo=(String)meta.getFilterValue();}
                        }
                        if(visible)
                        {
                            List<ProductoPaquete> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoPaqueteServicio.listarBuscarPadre(nombre,empresa.getCodigo(), getListaGruposBuscar(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(nombre,empresa.getCodigo(), getListaGruposBuscar()).intValue());
                            }
                            else
                            {
                                result = productoPaqueteServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                            }
                            return result;
                        }
                        else
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarBuscar(nombre, empresa.getCodigo(),grupo, pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(nombre,empresa.getCodigo(),grupo).intValue());
                            return result;
                        }
                    }
                    else
                    {
                        if(visible)
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarPadre(empresa.getCodigo(),getListaGruposBuscar(), pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(empresa.getCodigo(),getListaGruposBuscar()).intValue());
                            return result;
                        }
                        else
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarBuscar(StringUtils.EMPTY, empresa.getCodigo(),StringUtils.EMPTY, pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(StringUtils.EMPTY,empresa.getCodigo(),StringUtils.EMPTY).intValue());
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
            this.productoSelc = ((ProductoStock) event.getObject()).getProductoBodega();
            ProductoBodega producto = ((ProductoStock)event.getObject()).getProductoBodega();
            producto.setStock(((ProductoStock)event.getObject()).getStock());
            producto.setBodega(((ProductoStock)event.getObject()).getBodega());
            this.lotes.clear();
            this.lotes.addAll(documentosServicios.buscarLotesCompraMayorCero(producto.getCodigo() ,producto.getBodega().getCodigo()));
            this.lotes.addAll(documentosServicios.buscarLotesCompraMayorCeroDestino(producto.getCodigo() ,producto.getBodega().getCodigo()));
            if(!lotes.isEmpty() && lotes.size()>0){
                if(lotes.size()>1){
                    PrimeFaces.current().executeScript("PF('mdlLoteProducto').show();");
                    PrimeFaces.current().executeScript("PF('mdlLoteProducto').update();");
                }
                else
                {
                    producto.setLote(this.lotes.get(0)); 
                    this.listaProductosTodosSelc.add(producto);
                }
            }
            else
            {
                this.listaProductosTodosSelc.add(producto);
            }
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
        this.grupoProductoSelc = event;
       super.setListaGruposBuscar(this.llenarGruposBuscar());
        if(this.grupoProductoSelc.getTipo() == 1)
        {
            this.llenarTabla();
            this.opcion = "P";
        }
        if(this.grupoProductoSelc.getTipo() == 2)
        {
            this.llenarTablaServicios();
            this.opcion = "S";
        }
        if(this.grupoProductoSelc.getTipo() == 3)
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
    
    public void seleccionarLote(FacturaDetalle lote){
        ProductoBodega producto;
        if(lote.getFactura().getNumero() == -100){
            producto = (ProductoBodega)lote.getProductoServicioDestino();
        }
        else
        {
            producto = (ProductoBodega)lote.getProductoServicio();   
        }
        producto.setLote(lote); 
        this.listaProductosTodosSelc.add(producto);
        PrimeFaces.current().executeScript("PF('mdlLoteProducto').hide();");
    } 
    
    public Integer getLoteTotal() {
        return this.lotes.stream().mapToInt(FacturaDetalle::getStockActualInt).sum();
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
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public List<FacturaDetalle> getLotes() {
        return lotes;
    }

    public void setLotes(List<FacturaDetalle> lotes) {
        this.lotes = lotes;
    }

    public Producto getProductoSelc() {
        return productoSelc;
    }

    public void setProductoSelc(Producto productoSelc) {
        this.productoSelc = productoSelc;
    }
}
