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
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class ProductoStockBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ProductoStockBean.class.getName());

    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    public BodegaServicio bodegaServicio;
    @EJB
    public GrupoProductoServicio grupoProductoServicio;
    @EJB 
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    
    private LazyDataModel<ProductoStock> lazyModelStock;
    private List<Bodega> listaBodegas;
    private List<FacturaDetalle> listaLotesCompra;
    private ProductoStock productoStock;
    private Integer bodegaSelc;
    public GrupoProducto grupoProductoSelc;
    private TreeNode root;
    public TreeNode selectedNode;
    public Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    public boolean visible;
    private List<Integer> listaGruposBuscar;
    
    public ProductoStockBean() {
        this.listaBodegas = new ArrayList<>();
        this.productoStock = new ProductoStock();
        this.grupoProductoSelc = new GrupoProducto();
        this.listaLotesCompra = new ArrayList<>();
        this.listaGruposBuscar = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        try {
            this.visible = true;
            this.listaBodegas.clear();
            this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
            this.bodegaSelc = this.listaBodegas.get(0).getCodigo();
            this.cargarArbol();
            this.listaGruposBuscar = this.llenarGruposBuscar();
            if(this.root.getChildren().isEmpty()){
                return;
            }
            this.llenarTabla();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorCargarDatos"));
        }
    }
    
    public List<Integer> llenarGruposBuscar() {
        List<Integer> principales = new ArrayList<>();
        principales.add(this.grupoProductoSelc.getCodigo());
        if(this.grupoProductoSelc.getGrupoProductoList() != null){
            for (GrupoProducto opcion : this.grupoProductoSelc.getGrupoProductoList()) {
                principales.add(opcion.getCodigo());
                llenarGruposHijosBuscar(opcion, principales);
            }
        }
        return principales;
    }

    private void llenarGruposHijosBuscar(GrupoProducto opc, List<Integer> principales) {
        List<GrupoProducto> aux = opc.getGrupoProductoList();
        for (GrupoProducto opcion : aux) {
            principales.add(opcion.getCodigo());
            this.llenarGruposHijosBuscar(opcion, principales);
        }
    }
    
    public void llenarTabla()
    {
        try {
            this.lazyModelStock = new LazyDataModel<ProductoStock>()
            {
                @Override
                public List<ProductoStock> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String modelo = StringUtils.EMPTY;
                        String marca = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {             
                            if(meta.getFilterField().equals("productoBodega.nombre"))
                            {nombre=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("productoBodega.codigoBarras"))
                            {codigoBarras=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("productoBodega.modelo"))
                            {modelo=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("productoBodega.marca.nombre"))
                            {marca=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("productoBodega.grupo.nombre"))
                            {grupo=(String)meta.getFilterValue();}
                        }
                        if(visible)
                        {
                            List<ProductoStock> result;
                            if(codigoBarras == null || codigoBarras.trim().isEmpty())
                            {
                                result = productoStockServicio.listarBuscarPadre(nombre,modelo,marca,bodegaSelc,listaGruposBuscar, pageSize, first);
                                lazyModelStock.setRowCount(productoStockServicio.contarPadre(nombre,modelo,marca,bodegaSelc,listaGruposBuscar).intValue());
                            }
                            else
                            {
                                result = productoStockServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                lazyModelStock.setRowCount(productoStockServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                            }
                            return result;
                            
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
                            List<ProductoStock> result = productoStockServicio.listarPadre(bodegaSelc,listaGruposBuscar, pageSize, first);
                            lazyModelStock.setRowCount(productoStockServicio.contarPadre(bodegaSelc,listaGruposBuscar).intValue());
                            return result;
                            
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
        List<GrupoProducto> principales = this.grupoProductoServicio.listarPorNivelBodega(1,this.empresa.getCodigo());
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
        this.listaGruposBuscar = this.llenarGruposBuscar();
        this.llenarTabla();
    }
    
    public void verKardex(Producto producto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vista", "2");
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/kardexProductosDialog", options, null);
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

    public List<Integer> getListaGruposBuscar() {
        return listaGruposBuscar;
    }

    public void setListaGruposBuscar(List<Integer> listaGruposBuscar) {
        this.listaGruposBuscar = listaGruposBuscar;
    }
}
