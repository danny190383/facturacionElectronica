package com.jvc.factunet.bean.reportes;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.ReportesServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named(value = "reportesInventarioBean")
@ViewScoped
public class ReportesInventarioBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(ReportesInventarioBean.class.getName());
    
    @EJB
    private BodegaServicio bodegaServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    @EJB
    private ReportesServicio reportesServicio;
    
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private List<Bodega> listaBodegas;
    private Integer bodegaSelc;
    private TreeNode root;
    private TreeNode selectedNode;
    private boolean todosGrupos;
    private Integer stockReporte;
    private List<Integer> listaGruposBuscar;
    
    public ReportesInventarioBean() {
        this.listaBodegas = new ArrayList<>();
        this.stockReporte = 4;
        this.listaGruposBuscar = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
        this.bodegaSelc = -1;
        super.getListaReportes().addAll(this.reportesServicio.listarPadre("2"));
        this.cargarArbol();
        this.todosGrupos = Boolean.TRUE;
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            this.getParametros().put("empresa", this.empresa.getCodigo());
            this.getParametros().put("bodega", this.bodegaSelc == -1 ? "%%" : this.bodegaSelc);
            if(!todosGrupos)
            {
                this.listaGruposBuscar = this.llenarGruposBuscar();
                this.getParametros().put("gruposBuscar", this.listaGruposBuscar);
                this.getParametros().put("grupo", "1");
            }
            else
            {
                this.getParametros().put("grupo", "2");
                this.getParametros().put("grupoPadre", "%%");
            }
            this.getParametros().put("nombreBodega", this.nombreBodega());
            this.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            switch (super.getReporteSeleccionado()) {
                case "2.1":
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK,tipoReporte, null, this.getParametros());
                    break;
                case "2.2":
                    this.getParametros().put("stock", this.stockReporte);
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_INVENTARIO_PRODUCTO_STOCK_TODOS,tipoReporte, null, this.getParametros());
                    break;
                case "2.3":
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_INVENTARIO_PRODUCTO_CADUCIDAD,tipoReporte, null, this.getParametros());
                    break;
                default:
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("reporteNoEncontrado"));
                    break;
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public List<Integer> llenarGruposBuscar() {
        List<Integer> principales = new ArrayList<>();
        principales.add(((GrupoProducto) this.selectedNode.getData()).getCodigo());
        for (GrupoProducto opcion : ((GrupoProducto) this.selectedNode.getData()).getGrupoProductoList()) {
            principales.add(opcion.getCodigo());
            llenarGruposHijosBuscar(opcion, principales);
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
                this.selectedNode = itemHijo;
                ban = Boolean.FALSE;
            }
            llenarHijos(opcion, itemHijo);
        }
    }
    
    public String nombreBodega()
    {
        for(Bodega bodega : this.listaBodegas)
        {
            if(Objects.equals(bodega.getCodigo(), this.bodegaSelc))
            {
                return bodega.getSiglas() + " " + bodega.getNombre();
            }
        }
        return " ";
    }
    
    private void llenarHijos(GrupoProducto opc, TreeNode menuPadre) {
        List<GrupoProducto> aux = opc.getGrupoProductoList();
        for (GrupoProducto opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }

    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public Integer getBodegaSelc() {
        return bodegaSelc;
    }

    public void setBodegaSelc(Integer bodegaSelc) {
        this.bodegaSelc = bodegaSelc;
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

    public boolean isTodosGrupos() {
        return todosGrupos;
    }

    public void setTodosGrupos(boolean todosGrupos) {
        this.todosGrupos = todosGrupos;
    }

    public Integer getStockReporte() {
        return stockReporte;
    }

    public void setStockReporte(Integer stockReporte) {
        this.stockReporte = stockReporte;
    }
}
