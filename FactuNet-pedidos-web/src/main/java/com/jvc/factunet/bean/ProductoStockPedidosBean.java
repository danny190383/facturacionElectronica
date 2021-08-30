package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.servicios.ProductoStockServicio;
import com.jvc.factunet.session.LoginPedidos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class ProductoStockPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ProductoStockPedidosBean.class.getName());

    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    private BodegaServicio bodegaServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    @EJB 
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    
    private LazyDataModel<ProductoStock> lazyModelStock;
    private List<Bodega> listaBodegas;
    private List<FacturaDetalle> listaLotesCompra;
    private ProductoStock productoStock;
    private Integer bodegaSelc;
    private GrupoProducto grupoProductoSelc;
    private TreeNode root;
    private TreeNode selectedNode;
    private Empresa empresa ;
    private boolean visible;
    
    public ProductoStockPedidosBean() {
        this.listaBodegas = new ArrayList<>();
        this.productoStock = new ProductoStock();
        this.grupoProductoSelc = new GrupoProducto();
        this.listaLotesCompra = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        try {
            this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
            this.visible = true;
            this.listaBodegas.clear();
            this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
            this.bodegaSelc = this.listaBodegas.get(0).getCodigo();
            this.llenarTabla();
            this.cargarArbol();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorCargarDatos"));
        }
    }
    
    public void llenarTabla()
    {
        try {
            this.lazyModelStock = new LazyDataModel<ProductoStock>()
            {
                @Override
                public List<ProductoStock> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if (filters != null && filters.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String modelo = StringUtils.EMPTY;
                        String marca = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY;
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("productoBodega.nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("productoBodega.codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("productoBodega.modelo"))
                            {modelo=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("productoBodega.marca.nombre"))
                            {marca=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("productoBodega.grupo.nombre"))
                            {grupo=(String)filters.get(filterProperty);}
                        }
                        if(visible)
                        {
                            if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoStock> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoStockServicio.listarBuscar(nombre,modelo,marca,bodegaSelc,grupoProductoSelc.getCodigo(), pageSize, first);
                                    lazyModelStock.setRowCount(productoStockServicio.contar(nombre,modelo,marca,bodegaSelc,grupoProductoSelc.getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoStockServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                    lazyModelStock.setRowCount(productoStockServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                                }
                                return result;
                            }
                            else
                            {
                                List<ProductoStock> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoStockServicio.listarBuscarPadre(nombre,modelo,marca,bodegaSelc,grupoProductoSelc.getCodigo(), pageSize, first);
                                    lazyModelStock.setRowCount(productoStockServicio.contarPadre(nombre,modelo,marca,bodegaSelc,grupoProductoSelc.getCodigo()).intValue());
                                }
                                else
                                {
                                    result = productoStockServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                    lazyModelStock.setRowCount(productoStockServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                                }
                                return result;
                            }
                        }    
                        else{
                            List<ProductoStock> result = productoStockServicio.listarBuscar(nombre,modelo,marca,bodegaSelc,grupo, pageSize, first);
                            lazyModelStock.setRowCount(productoStockServicio.contar(nombre,modelo,marca,bodegaSelc,grupo).intValue());
                            return result;
                        }
                    }
                    else
                    {
                        if (visible)
                        {
                             if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoStock> result = productoStockServicio.listar(bodegaSelc,grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelStock.setRowCount(productoStockServicio.contar(bodegaSelc,grupoProductoSelc.getCodigo()).intValue());
                                return result;
                            }
                            else
                            {
                                List<ProductoStock> result = productoStockServicio.listarPadre(bodegaSelc,grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelStock.setRowCount(productoStockServicio.contarPadre(bodegaSelc,grupoProductoSelc.getCodigo()).intValue());
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoStock> result = productoStockServicio.listar(bodegaSelc,StringUtils.EMPTY, pageSize, first);
                            lazyModelStock.setRowCount(productoStockServicio.contar(bodegaSelc,StringUtils.EMPTY).intValue());
                            return result;
                        }
                    }
                }
                
                @Override
                public ProductoStock getRowData(String rowKey) {
                    List<ProductoStock> lista = (List<ProductoStock>) getWrappedData();
                    for(ProductoStock car : lista) {
                        if(car.getProductoBodega().getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoStock car) {
                    return car.getProductoBodega().getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<GrupoProducto> principales = this.grupoProductoServicio.listarPorNivelBodega(1);
        Boolean ban = true;
        for (GrupoProducto opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setExpanded(true);
            if(ban)
            {
                itemHijo.setSelected(true);
                this.grupoProductoSelc = (GrupoProducto)itemHijo.getData();
                ban = Boolean.FALSE;
            }
            llenarHijos(opcion, itemHijo);
        }
    }
    
    private void llenarHijos(GrupoProducto opc, TreeNode menuPadre) {
        List<GrupoProducto> aux = opc.getGrupoProductoList();
        for (GrupoProducto opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        this.grupoProductoSelc = (GrupoProducto) this.selectedNode.getData();
        this.llenarTabla();
    }
    
    public void verKardex(Producto producto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vista", "2");
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 800);
        options.put("height", 500);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/transacciones/extras/kardexProductosDialog", options, null);
    }
    
    public void verIndividual(ProductoStock producto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 550);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 550);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/cambiarIndividualDialog", options, null);
    }
    
    public void onCellEditUtilidad(ProductoStock event) {
        try {
            if(event.getProductoBodega().getPrecioUltimaCompra().floatValue() == 0){
                event.getProductoBodega().setUtilidad(BigDecimal.ZERO);
            }
            event.getProductoBodega().setPvp(event.getProductoBodega().getPrecioUltimaCompra().add(event.getProductoBodega().getPrecioUltimaCompra().multiply(event.getProductoBodega().getUtilidad().divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));
            event.setProductoBodega(this.productoBodegaServicio.actualizar(event.getProductoBodega()));
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void onCellEditPVP(ProductoStock event) {
        try {
            if(event.getProductoBodega().getPrecioUltimaCompra().floatValue() == 0){
                event.getProductoBodega().setPrecioUltimaCompra(event.getProductoBodega().getPvp());
            }
            event.getProductoBodega().setUtilidad(((event.getProductoBodega().getPvp().subtract(event.getProductoBodega().getPrecioUltimaCompra())).divide(event.getProductoBodega().getPrecioUltimaCompra(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
            event.setProductoBodega(this.productoBodegaServicio.actualizar(event.getProductoBodega()));
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void buscarDatosLote(ProductoStock productoStock)
    {
        this.listaLotesCompra.clear();
        this.listaLotesCompra.addAll(this.documentosServicios.buscarLotesCompra(productoStock.getProductoBodega().getCodigo() ,productoStock.getBodega().getCodigo()));
    }
    
    public void cerrarGrupos(){
        this.visible = false;
    }

    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public ProductoStock getProductoStock() {
        return productoStock;
    }

    public void setProductoStock(ProductoStock productoStock) {
        this.productoStock = productoStock;
    }

    public Integer getBodegaSelc() {
        return bodegaSelc;
    }

    public void setBodegaSelc(Integer bodegaSelc) {
        this.bodegaSelc = bodegaSelc;
    }

    public LazyDataModel<ProductoStock> getLazyModelStock() {
        return lazyModelStock;
    }

    public void setLazyModelStock(LazyDataModel<ProductoStock> lazyModelStock) {
        this.lazyModelStock = lazyModelStock;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public GrupoProducto getGrupoProductoSelc() {
        return grupoProductoSelc;
    }

    public void setGrupoProductoSelc(GrupoProducto grupoProductoSelc) {
        this.grupoProductoSelc = grupoProductoSelc;
    }

    public List<FacturaDetalle> getListaLotesCompra() {
        return listaLotesCompra;
    }

    public void setListaLotesCompra(List<FacturaDetalle> listaLotesCompra) {
        this.listaLotesCompra = listaLotesCompra;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
