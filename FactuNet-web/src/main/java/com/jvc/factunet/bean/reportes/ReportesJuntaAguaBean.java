package com.jvc.factunet.bean.reportes;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.ReportesServicio;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class ReportesJuntaAguaBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(ReportesInventarioBean.class.getName());
    
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    @EJB
    private ReportesServicio reportesServicio;
    
    private Date fechaInicio;
    private Date fechaFin;
    private Producto producto;
    private boolean todosGrupos;
    private TreeNode root;
    private TreeNode selectedNode;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private List<Integer> listaGruposBuscar;
    
    public ReportesJuntaAguaBean() {
        this.fechaFin = new Date();
        this.fechaInicio = new Date();
        this.producto = new Producto();
        this.todosGrupos = Boolean.TRUE;
        this.listaGruposBuscar = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.cargarArbol();
        super.getListaReportes().addAll(this.reportesServicio.listarPadre("6"));
        super.setReporteSeleccionado(super.getListaReportes().get(0).getCodigo());
    }
    
    
    public void iniciarProducto(){
        this.producto = new Producto();
    }
    
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            this.getParametros().put("empresa", this.empresa.getCodigo());
            //this.getParametros().put("usuario", "-1".equals(this.usuarioSelec) ? "%%" : this.usuarioSelec);
            this.getParametros().put("ad_fini", Fecha.fechaInicio(this.fechaInicio));
            this.getParametros().put("ad_ffin", Fecha.utilDateToSQLTimestamp(Fecha.fechaFin(this.fechaFin)));
            this.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            switch (super.getReporteSeleccionado()) {
                case "6.1":
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
                    //this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_JUNTA_AGUA_CLIENTES,tipoReporte, null, this.getParametros());
                    break;
                case "3.2":
                    //this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_ARQUEO_CAJA_CUENTAS,tipoReporte, null, this.getParametros());
                    break;
                case "3.3":
                    //this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    this.getParametros().put("producto", this.producto.getCodigo() == null ? "%%" : this.producto.getCodigo());
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
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_ARQUEO_CAJA_PRODUCTOS,tipoReporte, null, this.getParametros());
                    break;
                case "3.4":
                    //this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_ARQUEO_CAJA_COMPRAS,tipoReporte, null, this.getParametros());
                    break;
                case "3.5":
                    this.getParametros().put("estado", "2");
                   // this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_ARQUEO_CAJA_RETENCIONES,tipoReporte, null, this.getParametros());
                    break;
                case "3.6":
                    this.getParametros().put("estado", "3");
                   // this.getParametros().put("proveedor", this.proveedor.getCodigo() == null ? "%%" : this.proveedor.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_ARQUEO_CAJA_RETENCIONES_COMPRAS,tipoReporte, null, this.getParametros());
                    break;
                case "3.7":
                   // this.getParametros().put("calculado", this.calculadoSelec.equals("-1") ? "%%" : this.calculadoSelec);
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_RETENCIONES_VENTAS,tipoReporte, null, this.getParametros());
                    break;
                case "3.8":
                   // this.getParametros().put("calculado", this.calculadoSelec.equals("-1") ? "%%" : this.calculadoSelec);
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_RETENCIONES_COMPRAS,tipoReporte, null, this.getParametros());
                    break;
                case "3.9":
                   // this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_NOTAS_CREDITO,tipoReporte, null, this.getParametros());
                    break;
                case "3.10":
                    //this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_NOTAS_DEBITO,tipoReporte, null, this.getParametros());
                    break;
                default:
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("reporteNoEncontrado"));
                    break;
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void verBusquedaProductosStock() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 600);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 600);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProductosStockDialog", options, null);
    }
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            if(!listaProductos.isEmpty()){
                this.producto = listaProductos.get(0);
            }
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
        List<GrupoProducto> principales = this.grupoProductoServicio.listarPorNivel(1,this.empresa.getCodigo());
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
    
    private void llenarHijos(GrupoProducto opc, TreeNode menuPadre) {
        List<GrupoProducto> aux = opc.getGrupoProductoList();
        for (GrupoProducto opcion : aux) {
            TreeNode itemHijo = new DefaultTreeNode(opcion, menuPadre);
            this.llenarHijos(opcion, itemHijo);
        }
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public boolean isTodosGrupos() {
        return todosGrupos;
    }

    public void setTodosGrupos(boolean todosGrupos) {
        this.todosGrupos = todosGrupos;
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
}

