package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.AbonoCuenta;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.ComisionTarjeta;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.DocumentoRetencion;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FormaPago;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.CuentasFacturaServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.FormaPagoServicio;
import com.jvc.factunet.servicios.TarjetaEmpresaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class CuentasAdminBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(CuentasAdminBean.class.getName());
    
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private TarjetaEmpresaServicio tarjetaEmpresaServicio;
    @EJB
    private FormaPagoServicio formaPagoServicio;

    private Persona persona;
    private Cliente cliente;
    private Proveedor proveedor;
    private List<CuentaFactura> listaCuentas;
    private List<CuentaFactura> listaCuentasSlc;
    private List<DocumentoRetencion> listaRetencion;
    private CuentaFactura cuentaSelc;
    private String tipo;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private BigDecimal totalListaSlc;
    private Integer formaPagoSlc;
    private TarjetaEmpresa tarjetaEmpresaSlc;
    private ComisionTarjeta comisionTarjetaSlc;
    private List<TarjetaEmpresa> listaTarjetaEmpresa;
    private List<FormaPago> listaFormaPago;
    
    public CuentasAdminBean() {
        this.cuentaSelc = new CuentaFactura();
        this.persona = new Persona();
        this.listaCuentas = new ArrayList<>();
        this.listaRetencion = new ArrayList<>();
        this.listaCuentasSlc = new ArrayList<>();
        this.listaTarjetaEmpresa = new ArrayList<>();
        this.listaFormaPago = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaFormaPago.addAll(this.formaPagoServicio.listar());
        this.listaTarjetaEmpresa.addAll(this.tarjetaEmpresaServicio.listar(this.empresa.getCodigo()));
        if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.tipo = request.getParameter("tipo");
        }
    }
    
    public void buscarCuentasCliente(Integer codigo)
    {
        this.listaCuentas.clear();
        this.listaCuentas.addAll(this.cuentasFacturaServicio.buscarPendientesCliente(codigo, this.empresa.getCodigo()));
    }
    
    public void buscarCuentasProveedor(Integer codigo)
    {
        this.listaCuentas.clear();
        this.listaCuentas.addAll(this.cuentasFacturaServicio.buscarPendientesProveedor(codigo, this.empresa.getCodigo()));
    }
    
    public void buscarRetencionCliente(Integer codigo)
    {
        this.listaRetencion.clear();
        this.listaRetencion.addAll(this.documentosServicios.listarFacturasRetencionCliente(codigo));
    }
    
    public void buscarRetencionProveedor(Integer codigo)
    {
        this.listaRetencion.clear();
        this.listaRetencion.addAll(this.documentosServicios.listarFacturasRetencionProveedor(codigo));
    }
    
    public void verBusquedaProveedores() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 850);
        options.put("height", 400);
        options.put("contentWidth", 840);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProveedoresDialog", options, null);
    }
    
    public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
    }
    
     public void verBusquedaCreditos() {
        if(this.persona.getCodigo() != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", this.cliente.getCodigo());
            Map<String,Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("modal", true);
            options.put("width", 1000);
            options.put("height", 500);
            options.put("contentWidth", 1000);
            options.put("contentHeight", 500);
            PrimeFaces.current().dialog().openDynamic("/busquedas/buscarCreditosDialog", options, null);
        }
        else
        {
             FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneCliente"));
        }
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.cliente = (Cliente) event.getObject();
            this.persona = ((Cliente) event.getObject()).getPersona();
            this.buscarCuentasCliente(((Cliente) event.getObject()).getCodigo());
            this.buscarRetencionCliente(((Cliente) event.getObject()).getCodigo());
        }
    }
    
    public void onProveedorSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.proveedor = (Proveedor) event.getObject();
            this.persona = ((Proveedor) event.getObject()).getPersona();
            this.buscarCuentasProveedor(((Proveedor) event.getObject()).getCodigo());
            this.buscarRetencionProveedor(((Proveedor) event.getObject()).getCodigo());
        }
    }
    
    public void verNuevaCuenta()
    {
        if(this.persona.getCodigo() != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", this.cliente);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proveedor", this.proveedor);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipo", this.tipo);
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", true);
            options.put("modal", true);
            options.put("width", 800);
            options.put("height", 500);
            options.put("contentWidth", 800);
            options.put("contentHeight", 500);
            options.put("includeViewParams", true);
            PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaCuentaDialog", options, null);
        }
        else
        {
            if(this.tipo != null && this.tipo.equals("1")){
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneProveedor"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneCliente"));
            }
        }
    }
    
    public void onCuentaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            try {
                Factura factura = (Factura) event.getObject();
                this.documentosServicios.insertar(factura);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            }
        }
    }
    
    public void verPago(CuentaFactura cuenta) {
        this.cuentaSelc = cuenta;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", new ArrayList<>());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.cuentaSelc.getSaldo().add(this.cuentaSelc.getValorInteres()));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 3);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 800);
        options.put("height", 500);
        options.put("contentWidth", 800);
        options.put("contentHeight", 500);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/cuentaCobroDialog", options, null);
    }
    
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            try {
                List<FacturaPago> listaPagosFin = new ArrayList<>();
                listaPagosFin.addAll((List) event.getObject());
                for(FacturaPago pago : listaPagosFin){
                    AbonoCuenta abono = new AbonoCuenta();
                    if(pago.getFormaPago().getCodigo() == 149){
                        for(CuentaFactura cuenta : pago.getCuentaFacturaList()){
                            if(cuenta.getFormaPago().getCodigo() != 154){
                                abono.setFormaPago(cuenta.getFormaPago());
                            }
                            else
                            {
                                FacesUtils.addErrorMessage("No puede agregar un pago con forma de pago documento");
                                return;
                            }
                        }
                    }
                    else
                    {
                        abono.setFormaPago(pago.getFormaPago());
                    }
                    abono.setCuentaFactura(this.cuentaSelc);
                    abono.setFecha(new Date());
                    abono.setValor(pago.getValor());
                    this.cuentaSelc.setSaldo(this.cuentaSelc.getSaldo().subtract(abono.getValor()));
                    this.cuentaSelc.getAbonoCuentaList().add(abono);
                    this.cuentasFacturaServicio.actualizar(this.cuentaSelc);
                }
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            }
        }
    }
    
    public void onCellEdit(CuentaFactura event) {
        if(event.getDias() < 0)
        {
            event.setValorInteres((((event.getSaldo().multiply(event.getInteres())).divide((new BigDecimal(100)), RoundingMode.HALF_UP)).divide((new BigDecimal(30)),RoundingMode.HALF_UP)).multiply((new BigDecimal(event.getDias()).multiply(new BigDecimal(-1)))));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Factura documento) {
        try {
            super.getParametros().put("factura", documento.getCodigo());
            if(documento instanceof FacturaCompra){
                super.getParametros().put("nombreReporte", "Factura de Compra");
            }
            else
            {
                super.getParametros().put("nombreReporte", "Factura de Venta");
            }
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporteRetencion(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Retencion");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_RETENCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporteCuentas(String tipoReporte, FacturaPago documento) {
        try {
            super.getParametros().put("cuenta", documento.getCodigo());
            super.getParametros().put("empresa", this.empresa.getCodigo());
            if(documento.getFactura() instanceof FacturaCompra){
                super.getParametros().put("nombreReporte", "Reporte Abonos en Cuentas por Pagar");
            }
            else
            {
                super.getParametros().put("nombreReporte", "Reporte Abonos en Cuentas por Cobrar");
            }
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_CUENTA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporteAbono(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("cuenta", documento);
            super.getParametros().put("empresa", this.empresa.getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_CUENTA_ABONO,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void verBusquedaCuentas() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipo", this.tipo);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarCuentasDialog", options, null);
    }
    
    public void calcularTotal(){
        this.totalListaSlc = BigDecimal.ZERO;
        for(CuentaFactura cuenta : this.listaCuentasSlc){
            this.totalListaSlc = this.totalListaSlc.add(cuenta.getSaldo());
        }
    }
    
    public void verPagoLista(){
        if(!this.listaCuentasSlc.isEmpty()){
            this.formaPagoSlc = null;
            this.calcularTotal();
            if(this.totalListaSlc.floatValue() > 0){
                PrimeFaces.current().executeScript("PF('mdlPagosConsolidados').show();");
                PrimeFaces.current().ajax().update(":frmCabecera:mdlPagosConsolidados");
            }
            else
            {
                FacesUtils.addErrorMessage("No existen valores para cobrar");
            }
        }
        else
        {
            FacesUtils.addErrorMessage("Debe seleccionar al menos una cuenta");
        }
    }
    
    public void guardarPagosList(){
        for(CuentaFactura cuentaSlc : this.listaCuentasSlc){
            if(cuentaSlc.getSaldo().floatValue()>0){
                try {
                    if(this.formaPagoSlc != 153){
                        AbonoCuenta abono = new AbonoCuenta();
                        abono.setCuentaFactura(cuentaSlc);
                        abono.setFecha(new Date());
                        abono.setFormaPago(this.verFormaPago(this.formaPagoSlc)); 
                        abono.setValor(cuentaSlc.getSaldo());
                        cuentaSlc.setSaldo(cuentaSlc.getSaldo().subtract(abono.getValor()));
                        cuentaSlc.getAbonoCuentaList().add(abono);
                        this.cuentasFacturaServicio.actualizar(cuentaSlc);
                    }
                    else
                    {
                        AbonoCuenta abono = new AbonoCuenta();
                        abono.setCuentaFactura(cuentaSlc);
                        abono.setFecha(new Date());
                        abono.setComisionTarjeta(this.comisionTarjetaSlc);
                        abono.setFormaPago(this.verFormaPago(this.formaPagoSlc)); 
                        abono.setValor(cuentaSlc.getSaldo());
                        cuentaSlc.setSaldo(cuentaSlc.getSaldo().subtract(abono.getValor()));
                        cuentaSlc.getAbonoCuentaList().add(abono);
                        this.cuentasFacturaServicio.actualizar(cuentaSlc);
                    }
                    FacesUtils.addInfoMessage("Cuenta: " + cuentaSlc.getCodigo() +" a sigo cancelada");
               } catch (Exception ex) {
                   FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                   LOG.log(Level.SEVERE, "No se puede guardar.", ex);
               }
            }
            PrimeFaces.current().executeScript("PF('mdlPagosConsolidados').hide();");
        }
    }
    
    public FormaPago verFormaPago(Integer formaSlc){
        for(FormaPago forma : this.listaFormaPago){
            if(Objects.equals(forma.getCodigo(), formaSlc)){
                return forma;
            }
        }
        return null;
    }

    public List<CuentaFactura> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<CuentaFactura> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public CuentaFactura getCuentaSelc() {
        return cuentaSelc;
    }

    public void setCuentaSelc(CuentaFactura cuentaSelc) {
        this.cuentaSelc = cuentaSelc;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<DocumentoRetencion> getListaRetencion() {
        return listaRetencion;
    }

    public void setListaRetencion(List<DocumentoRetencion> listaRetencion) {
        this.listaRetencion = listaRetencion;
    }

    public List<CuentaFactura> getListaCuentasSlc() {
        return listaCuentasSlc;
    }

    public void setListaCuentasSlc(List<CuentaFactura> listaCuentasSlc) {
        this.listaCuentasSlc = listaCuentasSlc;
    }

    public BigDecimal getTotalListaSlc() {
        return totalListaSlc;
    }

    public void setTotalListaSlc(BigDecimal totalListaSlc) {
        this.totalListaSlc = totalListaSlc;
    }

    public Integer getFormaPagoSlc() {
        return formaPagoSlc;
    }

    public void setFormaPagoSlc(Integer formaPagoSlc) {
        this.formaPagoSlc = formaPagoSlc;
    }

    public TarjetaEmpresa getTarjetaEmpresaSlc() {
        return tarjetaEmpresaSlc;
    }

    public void setTarjetaEmpresaSlc(TarjetaEmpresa tarjetaEmpresaSlc) {
        this.tarjetaEmpresaSlc = tarjetaEmpresaSlc;
    }

    public ComisionTarjeta getComisionTarjetaSlc() {
        return comisionTarjetaSlc;
    }

    public void setComisionTarjetaSlc(ComisionTarjeta comisionTarjetaSlc) {
        this.comisionTarjetaSlc = comisionTarjetaSlc;
    }

    public List<TarjetaEmpresa> getListaTarjetaEmpresa() {
        return listaTarjetaEmpresa;
    }

    public void setListaTarjetaEmpresa(List<TarjetaEmpresa> listaTarjetaEmpresa) {
        this.listaTarjetaEmpresa = listaTarjetaEmpresa;
    }
}
