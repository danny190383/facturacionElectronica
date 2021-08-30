/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Menu;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.MenuServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author danny
 */
@ManagedBean
@ViewScoped
public class MenuBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(MenuBean.class.getName());
    
    @EJB
    private MenuServicio menuServicio;
    
    private TreeNode root;
    private TreeNode selectedNode;
    private Menu opcionMenu;
    private Menu padre;
    
    public MenuBean() {
        this.opcionMenu = new Menu();
        this.padre = new Menu();
    }
    
    @PostConstruct
    private void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<Menu> principales = this.menuServicio.listarPorNivel(1);
        for (Menu opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setExpanded(true);
            llenarHijos(opcion, itemHijo);
        }
    }

    private void llenarHijos(Menu opc, TreeNode menuPadre) {
        List<Menu> aux = this.menuServicio.listarPorPadre(opc.getCodigo());
        for (Menu opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        this.opcionMenu = (Menu) this.selectedNode.getData();
        if(this.opcionMenu.getPadre() == null)
        {
            this.padre.setNombre("Raiz");
        }
        else
        {
            this.padre = this.opcionMenu.getPadre();
        }
    }
    
    public void nuevoMenuPadre() {
        this.opcionMenu = new Menu();
        this.opcionMenu.setNivel(1);
        this.selectedNode = null;
        this.padre.setNombre("Raiz");
    }
    
    public void nuevoMenuHijo() {
        if(this.selectedNode != null)
        {
            this.opcionMenu = new Menu();
            this.padre = (Menu)this.selectedNode.getData();
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneNodo"));
        }
    }
    
    public void guardar() {
        if (this.opcionMenu.getCodigo() == null) {
            try {
                this.opcionMenu.setNombre(this.opcionMenu.getNombre().trim());
                this.opcionMenu.setUrl(this.opcionMenu.getUrl().trim());
                if(this.selectedNode != null)
                {
                    this.opcionMenu.setNivel((((Menu)this.selectedNode.getData()).getNivel())+1);
                    this.opcionMenu.setPadre((Menu)this.selectedNode.getData());
                }
                this.menuServicio.insertar(this.opcionMenu);
                if(this.selectedNode == null)
                {
                    TreeNode itemHijo = new DefaultTreeNode(opcionMenu, root);
                    this.selectedNode = itemHijo;
                }
                else
                {
                    TreeNode itemHijo = new DefaultTreeNode(opcionMenu, selectedNode);
                    this.selectedNode = itemHijo;
                }
                
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede guardar el menú.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        } else {
            try {
                this.menuServicio.actualizar(this.opcionMenu);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede actualiza el menú.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoActualizado"));
            }
        }
    }
    
    public void deleteNode() { 
        if(this.selectedNode == null)
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneNodo"));
        }
        else
        {
            List<Menu> listaEliminar = this.cargarListaEliminar();
            try {
                this.menuServicio.eliminar(listaEliminar);
                this.selectedNode.getChildren().clear();
                this.selectedNode.getParent().getChildren().remove(this.selectedNode);
                this.selectedNode.setParent(null);
                this.selectedNode = null;
                this.opcionMenu = new Menu();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede eliminar el menú.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoEliminado"));
            }
        }
    } 
    
    public List<Menu>  cargarListaEliminar() {
        List<TreeNode> principales = this.selectedNode.getChildren();
        List<Menu> eliminar = new ArrayList<>();
        eliminar.add((Menu)this.selectedNode.getData());
        for (TreeNode opcion : principales) {
            eliminar.add((Menu)opcion.getData());
            llenarHijosEliminar(opcion,eliminar);
        }
        return eliminar;
    }

    private void llenarHijosEliminar(TreeNode opc,  List<Menu> eliminar) {
        List<TreeNode> aux = opc.getChildren();
        for (TreeNode opcion : aux) {
            eliminar.add((Menu)opcion.getData());
            this.llenarHijosEliminar(opcion, eliminar);
        }
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

    public Menu getOpcionMenu() {
        return opcionMenu;
    }

    public void setOpcionMenu(Menu opcionMenu) {
        this.opcionMenu = opcionMenu;
    }

    public Menu getPadre() {
        return padre;
    }

    public void setPadre(Menu padre) {
        this.padre = padre;
    }
}
