package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.TipoEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.TipoEmpresaServicio;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named(value = "tipoEmpresaBean")
@ViewScoped
public class TipoEmpresaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(TipoEmpresaBean.class.getName());
    
    @EJB
    private TipoEmpresaServicio tipoEmpresaServicio;

    private TipoEmpresa tipoEmpresa;
    private TipoEmpresa tipoEmpresaSelc;
    private TreeNode root;
    private TreeNode selectedNode;
    
    public TipoEmpresaBean() {
        this.tipoEmpresa = new TipoEmpresa();
        this.tipoEmpresaSelc = new TipoEmpresa();
    }
    
    @PostConstruct
    public void init(){
        this.cargarArbol();
    }
    
    public void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<TipoEmpresa> principales = this.tipoEmpresaServicio.listarPorNivel(1);
        Boolean ban = true;
        for (TipoEmpresa opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setExpanded(true);
            if(ban)
            {
                itemHijo.setSelected(true);
                this.tipoEmpresaSelc = (TipoEmpresa)itemHijo.getData();
                ban = Boolean.FALSE;
            }
            llenarHijos(opcion, itemHijo);
        }
    }

    private void llenarHijos(TipoEmpresa opc, TreeNode menuPadre) {
        List<TipoEmpresa> aux = opc.getTipoEmpresaList();
        for (TipoEmpresa opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public void nuevoGrupoPadre() {
        this.tipoEmpresa =  new TipoEmpresa();
        this.tipoEmpresa.setNombre(StringUtils.EMPTY);
        this.tipoEmpresa.setNivel(1);
        PrimeFaces.current().executeScript("PF('dialogWiget').show();");
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        this.tipoEmpresaSelc = (TipoEmpresa) this.selectedNode.getData();
    }
    
    public void nuevoGrupo()
    {
        this.tipoEmpresa =  new TipoEmpresa();
        this.tipoEmpresa.setNombre(StringUtils.EMPTY);
        this.tipoEmpresa.setPadre(this.tipoEmpresaSelc);
        this.tipoEmpresa.setNivel(this.tipoEmpresaSelc.getNivel() + 1);
        this.tipoEmpresa.setTipo(this.tipoEmpresaSelc.getTipo());
        PrimeFaces.current().executeScript("PF('dialogWiget').show();"); 
    }
    
    public void editarGrupo()
    {
        this.tipoEmpresa = this.tipoEmpresaSelc;
        PrimeFaces.current().executeScript("PF('dialogWiget').show();");
    }
    
    public void eliminarGrupo()
    {
        try {
            this.tipoEmpresaServicio.eliminar(this.tipoEmpresaSelc);
            this.selectedNode.getChildren().clear();
            this.selectedNode.getParent().getChildren().remove(this.selectedNode);
            this.selectedNode.setParent(null);
            this.tipoEmpresaSelc = new TipoEmpresa();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        } 
    }
    
    public void guardarGrupo()
    {
        try {
            this.tipoEmpresa.setNombre(this.tipoEmpresa.getNombre().trim().toUpperCase());
            if(this.tipoEmpresa.getCodigo() == null)
            {
                this.tipoEmpresaServicio.insertar(this.tipoEmpresa);
                this.cargarArbol();
            }
            else
            {
                this.tipoEmpresaServicio.actualizar(this.tipoEmpresa);
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

    public TipoEmpresa getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public TipoEmpresa getTipoEmpresaSelc() {
        return tipoEmpresaSelc;
    }

    public void setTipoEmpresaSelc(TipoEmpresa tipoEmpresaSelc) {
        this.tipoEmpresaSelc = tipoEmpresaSelc;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    
}
