package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.servicios.ProductoPaqueteServicio;
import com.jvc.factunet.servicios.ProductoServiciosServicio;
import com.jvc.factunet.session.LoginPedidos;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class ProductoPedidosAdminBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(ProductoPedidosAdminBean.class.getName());

    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private ProductoServiciosServicio productoServiciosServicio;
    @EJB
    private ProductoPaqueteServicio productoPaqueteServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    
    private LazyDataModel<ProductoBodega> lazyModel;
    private LazyDataModel<ProductoServicio> lazyModelServicios;
    private LazyDataModel<ProductoPaquete> lazyModelPaquetes;
    private TreeNode root;
    private TreeNode selectedNode;
    private GrupoProducto grupoProducto;
    private ProductoBodega productoStock;
    private Empresa empresa;
    private GrupoProducto grupoProductoSelc;
    private List<Producto> listaProductosSelc;
    private List<GrupoProducto> listaGrupos;
    private GrupoProducto grupoSelect;
    public boolean visible;
    private String lugar;
    
    public ProductoPedidosAdminBean() {
        this.grupoProducto = new GrupoProducto();
        this.productoStock = new ProductoBodega();
        this.grupoProductoSelc = new GrupoProducto();
        this.listaProductosSelc = new ArrayList<>();
        this.listaGrupos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        try {
            this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
            if(this.empresa == null){
                this.regresarHome();
            }
            this.lugar = "1";
            this.visible = true;
            this.cargarArbol();
            this.llenarTablaServicios();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorCargarDatos"));
        } 
    }
    
    public void regresarHome(){
        FacesUtils.redireccionar("/faces/index.xhtml");
    }
    
    public void llenarReordenar(){
        if(this.listaProductosSelc.isEmpty())
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneProducto"));
        }
        else
        {
            this.listaGrupos.clear();
            this.listaGrupos.addAll(this.grupoProductoServicio.listarSinHijosTipo(this.listaProductosSelc.get(0).getGrupo().getTipo(), this.empresa.getCodigo()));
            PrimeFaces.current().executeScript("PF('dialogReordenarWiget').show();");
        }
    }
    
    public void llenarTabla()
    {
        try {
            this.lazyModel = new LazyDataModel<ProductoBodega>()
            {
                @Override
                public List<ProductoBodega> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if (filters != null && filters.size()>0) {
                        String nombre = StringUtils.EMPTY;
                        String codigoBarras = StringUtils.EMPTY;
                        String modelo = StringUtils.EMPTY;
                        String marca = StringUtils.EMPTY;
                        String grupo = StringUtils.EMPTY;
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("modelo"))
                            {modelo=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("marca.nombre"))
                            {marca=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("grupo.nombre"))
                            {grupo=(String)filters.get(filterProperty);}
                        }
                        if(visible)
                        {
                            if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoBodega> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoBodegaServicio.listarBuscar(nombre, modelo,marca,empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar, pageSize, first);
                                    lazyModel.setRowCount(productoBodegaServicio.contar(nombre,modelo,marca,empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar).intValue());
                                }
                                else
                                {
                                    result = productoBodegaServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), lugar, pageSize, first);
                                    lazyModel.setRowCount(productoBodegaServicio.contarBarras(codigoBarras,empresa.getCodigo(), lugar).intValue());
                                }
                                return result;
                            }
                            else
                            {
                                List<ProductoBodega> result;
                                if(codigoBarras.trim().isEmpty())
                                {
                                    result = productoBodegaServicio.listarBuscarPadre(nombre,modelo,marca,empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar, pageSize, first);
                                    lazyModel.setRowCount(productoBodegaServicio.contarPadre(nombre,modelo,marca,empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar).intValue());
                                }
                                else
                                {
                                    result = productoBodegaServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(),lugar, pageSize, first);
                                    lazyModel.setRowCount(productoBodegaServicio.contarBarras(codigoBarras,empresa.getCodigo(),lugar).intValue());
                                }
                                return result;
                            }
                        }
                        else
                        {
                            List<ProductoBodega> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoBodegaServicio.listarBuscar(nombre, modelo,marca,empresa.getCodigo(),grupo,lugar, pageSize, first);
                                lazyModel.setRowCount(productoBodegaServicio.contar(nombre,modelo,marca,empresa.getCodigo(),grupo,lugar).intValue());
                            }
                            else
                            {
                                result = productoBodegaServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), lugar, pageSize, first);
                                lazyModel.setRowCount(productoBodegaServicio.contarBarras(codigoBarras,empresa.getCodigo(), lugar).intValue());
                            }
                            return result;
                        }
                    }
                    else
                    {
                        if (visible)
                        {
                            if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                            {
                                List<ProductoBodega> result = productoBodegaServicio.listar(empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar, pageSize, first);
                                lazyModel.setRowCount(productoBodegaServicio.contar(empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar).intValue());
                                return result;
                            }
                            else
                            {
                                List<ProductoBodega> result = productoBodegaServicio.listarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar, pageSize, first);
                                lazyModel.setRowCount(productoBodegaServicio.contarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo(), lugar).intValue());
                                return result;
                            }
                        }  
                        else
                        {
                            List<ProductoBodega> result = productoBodegaServicio.listarBuscar(StringUtils.EMPTY, StringUtils.EMPTY,StringUtils.EMPTY,empresa.getCodigo(),StringUtils.EMPTY, lugar, pageSize, first);
                            lazyModel.setRowCount(productoBodegaServicio.contar(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,empresa.getCodigo(),StringUtils.EMPTY, lugar).intValue());
                            return result;
                        }
                    }
                }
                
                @Override
                public ProductoBodega getRowData(String rowKey) {
                    List<ProductoBodega> lista = (List<ProductoBodega>) getWrappedData();
                    for(ProductoBodega car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoBodega car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
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
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                        }
                        if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                        {
                            List<ProductoServicio> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoServiciosServicio.listarBuscar(nombre, empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contar(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
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
                            List<ProductoServicio> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoServiciosServicio.listarBuscarPadre(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                            }
                            else
                            {
                                result = productoServiciosServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                lazyModelServicios.setRowCount(productoServiciosServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                            }
                            return result;
                        }
                    }
                    else
                    {
                        if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listar(empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contar(empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                            return result;
                        }
                        else
                        {
                            List<ProductoServicio> result = productoServiciosServicio.listarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                            lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
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
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("nombre"))
                            {nombre=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("codigoBarras"))
                            {codigoBarras=(String)filters.get(filterProperty);}
                        }
                        if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                        {
                            List<ProductoPaquete> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoPaqueteServicio.listarBuscar(nombre, empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
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
                            List<ProductoPaquete> result;
                            if(codigoBarras.trim().isEmpty())
                            {
                                result = productoPaqueteServicio.listarBuscarPadre(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(nombre,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                            }
                            else
                            {
                                result = productoPaqueteServicio.listarBuscarBarras(codigoBarras,empresa.getCodigo(), pageSize, first);
                                lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarBarras(codigoBarras,empresa.getCodigo()).intValue());
                            }
                            return result;
                        }
                    }
                    else
                    {
                        if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listar(empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                            return result;
                        }
                        else
                        {
                            List<ProductoPaquete> result = productoPaqueteServicio.listarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                            lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
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
    
    public void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<GrupoProducto> principales = this.grupoProductoServicio.listarPorNivel(1,this.empresa.getCodigo());
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
        if(this.grupoProductoSelc.getTipo() == 1)
        {
            this.llenarTabla();
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
    
    public void nuevoGrupoPadre() {
        this.grupoProducto =  new GrupoProducto();
        this.grupoProducto.setNombre(StringUtils.EMPTY);
        this.grupoProducto.setNivel(1);
        this.grupoProducto.setEmpresa(this.empresa); 
        PrimeFaces.current().executeScript("PF('dialogWiget').show();");
    }
    
    public void nuevoGrupo()
    {
        Boolean ban = Boolean.FALSE;
        if((this.grupoProductoSelc.getGrupoProductoList() != null) && (this.grupoProductoSelc.getGrupoProductoList().size() > 0))
        {
            ban = Boolean.TRUE;
        }
        else
        {
            if(this.grupoProductoSelc.getTipo() == 1)
            {
                if(this.lazyModel.getRowCount() == 0)
                {
                    ban = Boolean.TRUE;
                }
            }
            if(this.grupoProductoSelc.getTipo() == 2)
            {
                if(this.lazyModelServicios.getRowCount() == 0)
                {
                    ban = Boolean.TRUE;
                }
            }
            if(this.grupoProductoSelc.getTipo() == 3)
            {
                if(this.lazyModelPaquetes.getRowCount() == 0)
                {
                    ban = Boolean.TRUE;
                }
            }
        }
        if(ban)
        {
            this.grupoProducto =  new GrupoProducto();
            this.grupoProducto.setNombre(StringUtils.EMPTY);
            this.grupoProducto.setPadre(this.grupoProductoSelc);
            this.grupoProducto.setNivel(this.grupoProductoSelc.getNivel() + 1);
            this.grupoProducto.setTipo(this.grupoProductoSelc.getTipo());
            this.grupoProducto.setEmpresa(this.empresa); 
            PrimeFaces.current().executeScript("PF('dialogWiget').show();"); 
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("grupoconProductos"));
        }
    }
    
    public void eliminarGrupo()
    {
        try {
            this.grupoProductoServicio.eliminar(this.grupoProductoSelc);
            this.selectedNode.getChildren().clear();
            this.selectedNode.getParent().getChildren().remove(this.selectedNode);
            this.selectedNode.setParent(null);
            this.grupoProductoSelc = new GrupoProducto();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        } 
    }
    
    public void editarGrupo()
    {
        this.grupoProducto = this.grupoProductoSelc;
        PrimeFaces.current().executeScript("PF('dialogWiget').show();");
    }
    
    public void guardarGrupo()
    {
        try {
            this.grupoProducto.setNombre(this.grupoProducto.getNombre().trim().toUpperCase());
            if(this.grupoProducto.getCodigo() == null)
            {
                this.grupoProductoServicio.insertar(this.grupoProducto);
                this.cargarArbol();
            }
            else
            {
                this.grupoProductoServicio.actualizar(this.grupoProducto);
            } 
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogWiget').hide();");
        }
        catch (EJBException e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("nombreGrupoDuplicado"));
        }
        catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verNuevoProductoPaca(ProductoBodega producto) {
        this.productoStock = producto;
        this.verNuevoProducto(null);
    }
    
    public void verNuevoProducto(ProductoBodega producto) {
        if(producto != null)
        {
            this.grupoProductoSelc = producto.getGrupo();
        }
        if((this.grupoProductoSelc.getGrupoProductoList() == null) ||  (this.grupoProductoSelc.getGrupoProductoList().isEmpty()))
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombregrupo", this.grupoProductoSelc.getNombre());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipo", 1);
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("contentWidth", 900);
            options.put("includeViewParams", true);
            PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/nuevoProductoDialog", options, null);
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("grupoNivel"));
        }
    }
    
    public void verImprimirBarras(String barras) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("barras", barras);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 500);
        options.put("contentHeight", 500);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/imprimirCodigoBarrasDialog", options, null);
        
    }
    
    public void onProductoSelect(SelectEvent event) {
        ProductoBodega productoBodega = (ProductoBodega) event.getObject();
        if((productoBodega != null))
        {
            try {
                if((productoBodega.getCodigoBarras() != null) && (productoBodega.getCodigoBarras().trim().isEmpty()))
                {
                    productoBodega.setCodigoBarras(null);
                }
                
                if(productoBodega.getCodigo() == null)
                {
                    productoBodega.setGrupo(this.grupoProductoSelc);
                    if(productoBodega.getPacaProductoList() != null){
                        productoBodega.getPacaProductoList().get(0).setGrupo(this.grupoProductoSelc);
                    }
                    if(productoStock.getCodigo() != null)
                    {
                        productoBodega.setPadrePaca(this.productoStock);
                    }
                    this.productoBodegaServicio.insertar(productoBodega);
                    if(productoStock.getCodigo() != null)
                    {
                        productoStock.getPacaProductoList().add(productoBodega);
                    }
                    else
                    {
                        this.llenarTabla();
                    }
                }
                else
                {
                    this.productoBodegaServicio.actualizar(productoBodega);
                }
                this.productoStock = new ProductoBodega();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
               LOG.log(Level.SEVERE, "No se puede guardar.", ex);
               FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public void verNuevoProductoServicio(ProductoServicio producto) {
        if(producto != null)
        {
            this.grupoProductoSelc = producto.getGrupo();
        }
        if((this.grupoProductoSelc.getGrupoProductoList() == null) ||  (this.grupoProductoSelc.getGrupoProductoList().isEmpty()))
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombregrupo", this.grupoProductoSelc.getNombre());
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("contentWidth", 900);
            options.put("includeViewParams", true);
            PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/nuevoServicioDialog", options, null);
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("grupoNivel"));
        }
    }
    
    public void onProductoServicioSelect(SelectEvent event) {
        ProductoServicio productoServicio = (ProductoServicio) event.getObject();
        if((productoServicio != null))
        {
            try {
                if((productoServicio.getCodigoBarras() != null) && (productoServicio.getCodigoBarras().trim().isEmpty()))
                {
                    productoServicio.setCodigoBarras(null);
                }
                
                if(productoServicio.getCodigo() == null)
                {
                    productoServicio.setGrupo(this.grupoProductoSelc);
                    this.productoServiciosServicio.insertar(productoServicio);
                }
                else
                {
                    this.productoServiciosServicio.actualizar(productoServicio);
                }
                this.llenarTablaServicios();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
               LOG.log(Level.SEVERE, "No se puede guardar.", ex);
               FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public void verNuevoProductoPaquete(ProductoPaquete producto) {
        if(producto != null)
        {
            this.grupoProductoSelc = producto.getGrupo();
        }
        if((this.grupoProductoSelc.getGrupoProductoList() == null) ||  (this.grupoProductoSelc.getGrupoProductoList().isEmpty()))
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombregrupo", this.grupoProductoSelc.getNombre());
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", true);
            options.put("modal", true);
            options.put("width", 900);
            options.put("height", 450);
            options.put("contentHeight", 450);
            options.put("contentWidth", 900);
            options.put("includeViewParams", true);
            PrimeFaces.current().dialog().openDynamic("/faces/inventario/extras/nuevoPaqueteDialog", options, null);
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("grupoNivel"));
        }
    }
    
    public void onProductoPaqueteSelect(SelectEvent event) {
        ProductoPaquete productoPaquete = (ProductoPaquete) event.getObject();
        if((productoPaquete != null))
        {
            try {
                if((productoPaquete.getCodigoBarras() != null) && (productoPaquete.getCodigoBarras().trim().isEmpty()))
                {
                    productoPaquete.setCodigoBarras(null);
                }
                
                if(productoPaquete.getCodigo() == null)
                {
                    productoPaquete.setGrupo(this.grupoProductoSelc);
                    this.productoPaqueteServicio.insertar(productoPaquete);
                }
                else
                {
                    this.productoPaqueteServicio.actualizar(productoPaquete);
                }
                this.llenarTablaPaquetes();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
               LOG.log(Level.SEVERE, "No se puede guardar.", ex);
               FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public void eliminarProducto(ProductoBodega parametro) {
        try {
            
            if(parametro.getPadrePaca() != null)
            {
                ProductoBodega padre = parametro.getPadrePaca();
                this.productoBodegaServicio.eliminar(parametro);
                padre.getPacaProductoList().remove(parametro);
            }
            else
            {
                this.productoBodegaServicio.eliminar(parametro);
                this.llenarTabla();
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarServicio(ProductoServicio parametro) {
        try {
            this.productoServiciosServicio.eliminar(parametro);
            this.llenarTablaServicios();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarPaquete(ProductoPaquete parametro) {
        try {
            this.productoPaqueteServicio.eliminar(parametro);
            this.llenarTablaPaquetes();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void moverProductosGrupo()
    {
        for(Producto obj : this.listaProductosSelc)
        {
            try {
                obj.setGrupo(this.grupoSelect);
                if(obj instanceof ProductoBodega){
                    ProductoBodega productoBodega = (ProductoBodega) obj;
                    for(ProductoBodega productoPaca : productoBodega.getPacaProductoList()){
                        productoPaca.setGrupo(this.grupoSelect); 
                    }
                }
                this.productoBodegaServicio.actualizar(obj);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
        this.listaProductosSelc.clear();
        this.llenarTabla();
        this.llenarTablaServicios();
        PrimeFaces.current().executeScript("PF('dialogReordenarWiget').hide();");
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
    }
    
    public void cerrarGrupos(){
        this.visible = false;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public GrupoProducto getGrupoProducto() {
        return grupoProducto;
    }

    public void setGrupoProducto(GrupoProducto grupoProducto) {
        this.grupoProducto = grupoProducto;
    }

    public ProductoBodega getProductoBodega() {
        return productoStock;
    }

    public void setProductoBodega(ProductoBodega productoStock) {
        this.productoStock = productoStock;
    }

    public LazyDataModel<ProductoBodega> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ProductoBodega> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public GrupoProducto getGrupoProductoSelc() {
        return grupoProductoSelc;
    }

    public void setGrupoProductoSelc(GrupoProducto grupoProductoSelc) {
        this.grupoProductoSelc = grupoProductoSelc;
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
    
    public List<Producto> getListaProductosSelc() {
        return listaProductosSelc;
    }

    public void setListaProductosSelc(List<Producto> listaProductosSelc) {
        this.listaProductosSelc = listaProductosSelc;
    }

    public List<GrupoProducto> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GrupoProducto> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public GrupoProducto getGrupoSelect() {
        return grupoSelect;
    }

    public void setGrupoSelect(GrupoProducto grupoSelect) {
        this.grupoSelect = grupoSelect;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
