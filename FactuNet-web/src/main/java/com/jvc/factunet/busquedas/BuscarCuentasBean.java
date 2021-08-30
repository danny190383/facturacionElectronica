package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class BuscarCuentasBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarCuentasBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    
    private List<CuentaFactura> cuentaFacturaList;
    private List<CuentaFactura> cuentaFacturaListSelect;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private Integer numero;
    private Date fecha;
    private Date fechaV;
    private String nombre;
    private String ruc;
    private String tipo;

    public BuscarCuentasBean() {
        this.cuentaFacturaList = new ArrayList<>();
        this.cuentaFacturaListSelect = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.tipo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipo");
        if("1".equals(this.tipo))
        {
            this.cuentaFacturaList.addAll(this.documentosServicios.listarCuentasProveedor(this.empresa.getCodigo()));
        }
        else
        {
            this.cuentaFacturaList.addAll(this.documentosServicios.listarCuentasCliente(this.empresa.getCodigo()));
        }
    }
    
    public void filtrar()
    {
        this.cuentaFacturaList.clear();
        if("1".equals(this.tipo))
        {
            this.cuentaFacturaList.addAll(this.documentosServicios.listarCuentasProveedorFiltro(empresa.getCodigo(), numero, fecha, nombre, ruc, fechaV));
        }
        else
        {
            this.cuentaFacturaList.addAll(this.documentosServicios.listarCuentasClienteFiltro(empresa.getCodigo(), numero, fecha, nombre, ruc, fechaV));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            if("1".equals(this.tipo))
            {
                super.getParametros().put("nombreReporte", "Factura de Compra");
            }
            else
            {
                super.getParametros().put("nombreReporte", "Factura de Venta");
            }
            super.getParametros().put("nombreReporte", "Factura de Venta");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporteCuentas(String tipoReporte, FacturaPago documento) {
        try {
            super.getParametros().put("cuenta", documento.getCodigo());
            super.getParametros().put("empresa", this.empresa.getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            if(documento.getFactura() instanceof FacturaCompra){
                super.getParametros().put("nombreReporte", "Reporte Abonos en Cuentas por Pagar");
            }
            else
            {
                super.getParametros().put("nombreReporte", "Reporte Abonos en Cuentas por Cobrar");
            }
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_CUENTA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void envioNotificacionCuenta() {
        String asunto = "Recordatorio cuenta vencida ";
        for (CuentaFactura nota : cuentaFacturaListSelect) {
            String usuario = this.empresa.getEmail().trim();
            String password = this.empresa.getEmailClave().trim();
            String destinatario = nota.getFacturaPago().getFactura().getCliente().getPersona().getEmail();
            String mensaje = this.empresa.getNombre() + "\n" + "LE RECORDAMOS QUE MANTIENE CON NOSOTROS UNA CUENTA VENCIDA POR" + 
                             "\n" + nota.getSaldo() + "\n" + "LE SOLICITAMOS QUE SE ACERQUE A NUESTRO LOCAL GRACIAS" ;
//            EmailSenderThread emailSenderThread = new EmailSenderThread(usuario, password, destinatario, asunto, mensaje, null);
//            emailSenderThread.start();
        }
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("correoEnviado"));
    }

    public List<CuentaFactura> getCuentaFacturaList() {
        return cuentaFacturaList;
    }

    public void setCuentaFacturaList(List<CuentaFactura> cuentaFacturaList) {
        this.cuentaFacturaList = cuentaFacturaList;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Date getFechaV() {
        return fechaV;
    }

    public void setFechaV(Date fechaV) {
        this.fechaV = fechaV;
    }

    public List<CuentaFactura> getCuentaFacturaListSelect() {
        return cuentaFacturaListSelect;
    }

    public void setCuentaFacturaListSelect(List<CuentaFactura> cuentaFacturaListSelect) {
        this.cuentaFacturaListSelect = cuentaFacturaListSelect;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
