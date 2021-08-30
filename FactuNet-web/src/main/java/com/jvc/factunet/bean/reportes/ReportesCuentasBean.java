package com.jvc.factunet.bean.reportes;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.CuentaServicio;
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

@ManagedBean
@ViewScoped
public class ReportesCuentasBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(ReportesCuentasBean.class.getName());
    
    @EJB
    private CuentaServicio cuentaServicio;
    @EJB
    private ReportesServicio reportesServicio;

    private Date fechaInicio;
    private Date fechaFin;
    private List<Cuenta> listaCuenta;
    private String usuarioSelec;
    private Cliente cliente;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private Proveedor proveedor;
    
    public ReportesCuentasBean() {
        this.listaCuenta = new ArrayList<>();
        this.fechaFin = new Date();
        this.fechaInicio = new Date();
        this.cliente = new Cliente();
        this.proveedor = new Proveedor();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaCuenta.addAll(this.cuentaServicio.listarConPuntoVenta(this.empresa.getCodigo()));
        this.usuarioSelec = "-1";
        super.getListaReportes().addAll(this.reportesServicio.listarPadre("5"));
        super.setReporteSeleccionado("5.1");
    }
    
    public void iniciarCliente(){
        this.cliente = new Cliente();
    }
    
    public void iniciarProveedor(){
        this.proveedor = new Proveedor();
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            this.getParametros().put("empresa", this.empresa.getCodigo());
            this.getParametros().put("usuario", "-1".equals(this.usuarioSelec) ? "%%" : this.usuarioSelec);
            this.getParametros().put("ad_fini", Fecha.fechaInicio(this.fechaInicio));
            this.getParametros().put("ad_ffin", Fecha.utilDateToSQLTimestamp(Fecha.fechaFin(this.fechaFin)));
            this.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            switch (super.getReporteSeleccionado()) {
                case "5.1":
                    this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_CUENTAS_COBRAR,tipoReporte, null, this.getParametros());
                    break;
                case "5.2":
                    this.getParametros().put("proveedor", this.proveedor.getCodigo() == null ? "%%" : this.proveedor.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_CUENTAS_PAGAR,tipoReporte, null, this.getParametros());
                    break;
                case "5.3":
                    this.getParametros().put("cliente", this.cliente.getCodigo() == null ? "%%" : this.cliente.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_TOTAL_CUENTAS_COBRAR,tipoReporte, null, this.getParametros());
                    break;
                case "5.4":
                    this.getParametros().put("proveedor", this.proveedor.getCodigo() == null ? "%%" : this.proveedor.getCodigo());
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_TOTAL_CUENTAS_PAGAR,tipoReporte, null, this.getParametros());
                    break;
                default:
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("reporteNoEncontrado"));
                    break;
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
     public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 950);
        options.put("height", 500);
        options.put("contentWidth", 950);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
    }
     
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.cliente = (Cliente) event.getObject();
        }
    }
    
    public void verBusquedaProveedores() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 950);
        options.put("height", 500);
        options.put("contentWidth", 950);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProveedoresDialog", options, null);
    }
     
    public void onPRoveedorSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.proveedor = (Proveedor) event.getObject();
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

    public List<Cuenta> getListaCuenta() {
        return listaCuenta;
    }

    public void setListaCuenta(List<Cuenta> listaCuenta) {
        this.listaCuenta = listaCuenta;
    }

    public String getUsuarioSelec() {
        return usuarioSelec;
    }

    public void setUsuarioSelec(String usuarioSelec) {
        this.usuarioSelec = usuarioSelec;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    
}
