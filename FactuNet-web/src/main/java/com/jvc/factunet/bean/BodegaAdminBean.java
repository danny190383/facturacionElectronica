package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.session.Login;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class BodegaAdminBean extends CatalogosPersonaBean{
    
    private static final Logger LOG = Logger.getLogger(BodegaAdminBean.class.getName());
    
    @EJB
    private BodegaServicio bodegaServicio;
    
    private TreeNode root;
    private TreeNode selectedNode;
    private Bodega bodegaSelc;
    private Bodega bodega;
    private List<ProductoStock> listaProductosSelct;
    private ProductoStock productoStockTmp;
    private List<Bodega> listaBodegas;
    private Integer bodegaTranf;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public BodegaAdminBean() {
        this.listaProductosSelct = new ArrayList<>();
        this.listaBodegas = new ArrayList<>();
        this.productoStockTmp = new ProductoStock();
    }
    
    @PostConstruct
    public void init()
    {
        this.cargarArbol();
        this.crearBodega();
    }
    
    public void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<Bodega> principales = this.bodegaServicio.listarPorNivel(1);
        Boolean ban = true;
        for (Bodega opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setExpanded(true);
            if(ban)
            {
                itemHijo.setSelected(true);
                this.bodegaSelc = (Bodega)itemHijo.getData();
                ban = Boolean.FALSE;
            }
            llenarHijos(opcion, itemHijo);
        }
    }

    private void llenarHijos(Bodega opc, TreeNode menuPadre) {
        List<Bodega> aux = opc.getBodegaList();
        for (Bodega opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        this.bodegaSelc = (Bodega) this.selectedNode.getData();
    }
    
    public void crearBodega()
    {
        this.bodega =  new Bodega();
        this.bodega.setCiudad(this.getListaCiudad().get(0));
        this.bodega.setDescripcion(StringUtils.EMPTY);
        this.bodega.setEmpresa(this.empresa);
        this.bodega.setNivel(this.bodegaSelc.getNivel() + 1);
        this.bodega.setNombre(StringUtils.EMPTY);
        this.bodega.setPadre(this.bodegaSelc);
        this.bodega.setSiglas(StringUtils.EMPTY);
        this.bodega.setUbicacion(StringUtils.EMPTY); 
    }
    
    public void nuevoBodega()
    {
        Boolean ban = Boolean.FALSE;
        if(this.bodegaSelc.getNivel() == 1)
        {
            ban = Boolean.TRUE;
        }
        if(ban)
        {
            this.crearBodega();
            PrimeFaces.current().executeScript("PF('dialogWiget').show();"); 
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("bodegaDependiente"));
        }
    }
    
    public void eliminarBodega()
    {
//        if((this.bodegaSelc.getNivel() == 2) && (this.bodegaSelc.getProductoStockList().isEmpty()))
//        {
        if((this.bodegaSelc.getNivel() == 2))
        {
            try {
                this.bodegaServicio.eliminar(this.bodegaSelc);
                this.selectedNode.getChildren().clear();
                this.selectedNode.getParent().getChildren().remove(this.selectedNode);
                this.selectedNode.setParent(null);
                this.bodegaSelc = new Bodega();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se puede eliminar.", e);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
            } 
        }
    }
    
    public void editarBodega()
    {
        this.bodega = this.bodegaSelc;
        PrimeFaces.current().executeScript("PF('dialogWiget').show();");
    }
    
    public void guardarBodega()
    {
        try {
            this.bodega.setNombre(this.bodega.getNombre().trim().toUpperCase());
            if(this.bodega.getCodigo() == null)
            {
                this.bodegaServicio.insertar(this.bodega);
                this.cargarArbol();
            }
            else
            {
                this.bodegaServicio.actualizar(this.bodega);
            } 
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogWiget').hide();");
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verBodegas()
    {
        this.listaBodegas.clear();
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
        this.bodegaTranf = this.listaBodegas.get(0).getCodigo();
        PrimeFaces.current().executeScript("PF('dialogTransferencia').show();");
    }
    
    public void transferirProductos()
    {
        if(!Objects.equals(this.bodegaSelc.getCodigo(), this.bodegaTranf))
        {
            try {
                this.bodegaServicio.trasnferirProductos(this.listaProductosSelct,this.bodegaSelc, this.buscarBodega(), ((Login)FacesUtils.getManagedBean("login")).getEmpleado());
                this.listaProductosSelct.clear();
                this.cargarArbol();
                PrimeFaces.current().executeScript("PF('dialogTransferencia').hide();");
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
    }
    
    public Bodega buscarBodega()
    {
        for(Bodega bodegaP : this.listaBodegas)
        {
            if(Objects.equals(bodegaP.getCodigo(), this.bodegaTranf))
            {
                return bodegaP;
            }
        }
        return null;
    }
    
    public void verKardex(Producto producto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("producto", producto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vista", "1");
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
    
    public void verRegistroSeries(ProductoStock producto) {
        this.productoStockTmp = producto;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("series", producto.getListaSeries());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cantidad", producto.getCantidad());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 450);
        options.put("contentHeight", 450);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/codigosProductosDialog", options, null);
    }
    
    public void onRegistroSeriesSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaDetalleSeries> lista = (List) event.getObject();
            this.productoStockTmp.setListaSeries(lista);
        }
    }
    
    public void verBusquedaTransferencias() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarTransferenciasDialog", options, null);
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

    public Bodega getBodegaSelc() {
        return bodegaSelc;
    }

    public void setBodegaSelc(Bodega bodegaSelc) {
        this.bodegaSelc = bodegaSelc;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public List<ProductoStock> getListaProductosSelct() {
        return listaProductosSelct;
    }

    public void setListaProductosSelct(List<ProductoStock> listaProductosSelct) {
        this.listaProductosSelct = listaProductosSelct;
    }

    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public Integer getBodegaTranf() {
        return bodegaTranf;
    }

    public void setBodegaTranf(Integer bodegaTranf) {
        this.bodegaTranf = bodegaTranf;
    }
    
}
