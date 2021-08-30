package com.jvc.factunet.busquedas;

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
public class BuscarCreditosBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarCreditosBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    private List<FacturaPago> listaFacturaPagos;
    private Integer cliente;
    
    public BuscarCreditosBean() {
        this.listaFacturaPagos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.cliente = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente");
        this.listaFacturaPagos.addAll(this.documentosServicios.listarCreditosCliente(this.empresa, this.cliente));
    }
    
    public void filtrar()
    {
        this.listaFacturaPagos.clear();
        this.listaFacturaPagos.addAll(this.documentosServicios.listarCreditosClienteFiltro(empresa, numero, fecha, nombre, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
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
            super.getParametros().put("empresa", this.empresa);
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
    
    public List<FacturaPago> getListaFacturaPagos() {
        return listaFacturaPagos;
    }

    public void setListaFacturaPagos(List<FacturaPago> listaFacturaPagos) {
        this.listaFacturaPagos = listaFacturaPagos;
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

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }
}
