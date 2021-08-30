package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.GarantiaCabecera;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.IngresoServicio;
import com.jvc.factunet.session.Login;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class BuscarIngresosBean extends ImprimirReportesBean {
    private static final Logger LOG = Logger.getLogger(BuscarIngresosBean.class.getName());
    
    @EJB
    private IngresoServicio ingresoServicio;

    private List<GarantiaCabecera> listaIngresos;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    
    public BuscarIngresosBean() {
        this.listaIngresos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.estado = "0";
        this.listaIngresos.addAll(this.ingresoServicio.listarIngresosFiltro(empresa, null, null, null, "0", null));
    }
    
    public void seleccionar(GarantiaCabecera event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void filtrar()
    {
        this.listaIngresos.clear();
        this.listaIngresos.addAll(this.ingresoServicio.listarIngresosFiltro(empresa, numero, fecha, nombre, estado, ruc));
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

    public List<GarantiaCabecera> getListaIngresos() {
        return listaIngresos;
    }

    public void setListaIngresos(List<GarantiaCabecera> listaIngresos) {
        this.listaIngresos = listaIngresos;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
