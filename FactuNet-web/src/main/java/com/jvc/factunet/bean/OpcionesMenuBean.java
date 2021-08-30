package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Menu;
import com.jvc.factunet.entidades.OpcionesMenu;
import com.jvc.factunet.entidades.Rol;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.MenuServicio;
import com.jvc.factunet.servicios.OpcionesMenuServicio;
import com.jvc.factunet.servicios.RolServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class OpcionesMenuBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(OpcionesMenuBean.class.getName());

    @EJB
    private RolServicio rolServicio;
    @EJB
    private MenuServicio menuServicio;
    @EJB
    private OpcionesMenuServicio opcionesMenuServicio;
    
    private List<Rol> listaRol;
    private Rol rol;
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private List<OpcionesMenu> listaSelc;
    
    public OpcionesMenuBean() {
        this.listaRol = new ArrayList<>();
        this.listaSelc = new ArrayList<>();
        this.rol = new Rol();
        this.selectedNodes = new TreeNode[20];
    }
    
    @PostConstruct
    private void init()
    {
        this.listaRol.addAll(this.rolServicio.listar());
        this.rol = this.listaRol.get(0);
        this.cargarArbol();
        this.onSelect();
    }
    
    public Boolean isSelected(Menu opcion)
    {
        for(OpcionesMenu op : this.listaSelc)
        {
            if(opcion.getCodigo() == op.getOpcionesMenuPK().getMenu())
            {
                return true;
            }
        }
        return false;
    }
    
    public void cargarArbol() {
        this.root = new DefaultTreeNode("Menú", null);
        List<Menu> principales = this.menuServicio.listarPorNivel(1);
        for (Menu opcion : principales) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, root);
            itemHijo.setSelected(this.isSelected(opcion));
            itemHijo.setExpanded(true);
            llenarHijos(opcion, itemHijo);
        }
    }

    private void llenarHijos(Menu opc, TreeNode menuPadre) {
        List<Menu> aux = this.menuServicio.listarPorPadre(opc.getCodigo());
        for (Menu opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            itemHijo.setSelected(this.isSelected(opcion));
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public void insertSelectedMultiple() {
        if(this.rol.getCodigo() != null)
        {
            if(this.selectedNodes != null && this.selectedNodes.length > 0) {
                try {
                    List<Menu> listInsert = this.llenarListaInsertar(this.selectedNodes);
                    this.opcionesMenuServicio.insertar(listInsert,this.rol.getCodigo());
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "No se puede guardar.", e);
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                }
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public List<Menu>  llenarListaInsertar(TreeNode[] nodes) {
        List<Menu> insertar = new ArrayList<>();
        for (TreeNode opcion : nodes) {
            if(!insertar.contains((Menu)opcion.getData()))
            {
                insertar.add((Menu)opcion.getData());
                llenarPadresInsertar(opcion,insertar);
            }
        }
        return insertar;
    }

    private void llenarPadresInsertar(TreeNode opc,  List<Menu> insertar) {
        if(!opc.getParent().getRowKey().equals("root"))
        {
            if(!insertar.contains((Menu)opc.getParent().getData()))
            {
                insertar.add((Menu)opc.getParent().getData());
                this.llenarPadresInsertar(opc.getParent(), insertar);
            }
        }
    }
     
    public void onSelect()
    {
        this.listaSelc.clear();
        this.listaSelc = this.opcionesMenuServicio.listar(this.rol.getCodigo());
        this.cargarArbol();
    }

    public List<Rol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<Rol> listaRol) {
        this.listaRol = listaRol;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }
    
    
}
