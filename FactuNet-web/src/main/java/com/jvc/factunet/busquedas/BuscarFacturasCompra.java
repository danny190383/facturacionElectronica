package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.FacturaCompra;
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
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class BuscarFacturasCompra extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(BuscarFacturasCompra.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<FacturaCompra> listaFacturas;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    
    public BuscarFacturasCompra() {
        this.listaFacturas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.estado = "0";
        this.listaFacturas.addAll(this.documentosServicios.listarFacturasCompra(this.empresa, this.estado));
    }
    
    public void seleccionar(FacturaCompra event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void filtrar()
    {
        this.listaFacturas.clear();
        this.listaFacturas.addAll(this.documentosServicios.listarFacturaCompraFiltro(empresa, numero, fecha, nombre, estado, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Factura de compra");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<FacturaCompra> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<FacturaCompra> listaFacturas) {
        this.listaFacturas = listaFacturas;
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
