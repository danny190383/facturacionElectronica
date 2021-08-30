package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class BuscarFacturasVenta extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarFacturasVenta.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<FacturaVenta> listaFacturas;
    private List<FacturaVenta> listaFacturasSlc;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    private Integer tipoSeleccion;
    private Integer tipoDocumento;
    
    public BuscarFacturasVenta() {
        this.listaFacturas = new ArrayList<>();
        this.listaFacturasSlc = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.estado = "0";
        this.listaFacturas.addAll(this.documentosServicios.listarFacturasVenta(this.empresa, this.estado));
        this.tipoSeleccion = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoBusqueda");
        this.tipoDocumento = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoDocumento");
    }
    
    public void seleccionar(FacturaVenta event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaFacturasSlc);
    }
    
    public void filtrar()
    {
        this.listaFacturas.clear();
        this.listaFacturas.addAll(this.documentosServicios.listarFacturasVentaFiltro(empresa, numero, fecha, nombre, estado, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        try {
            super.getParametros().put("factura", documento);
            if(tipoDocumento == 21 ||  tipoDocumento == 23){ super.getParametros().put("nombreReporte", "Factura de Venta");}
            if(tipoDocumento == 22){ super.getParametros().put("nombreReporte", "Reserva");}
            if(tipoDocumento == 100){ super.getParametros().put("nombreReporte", "Nota de Crédito");}
            if(tipoDocumento == 200){ super.getParametros().put("nombreReporte", "Nota de Débito");}
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<FacturaVenta> getListaFacturas() {
        if(this.tipoDocumento == 21 && this.tipoSeleccion == 1){
            return listaFacturas.stream().filter(p -> (((Objects.equals(p.getTipoDocumento(), this.tipoDocumento)) || (Objects.equals(p.getTipoDocumento(), 23))))).collect(Collectors.toList());
        }
        if(this.tipoDocumento == 21){
            return listaFacturas.stream().filter(p -> (((Objects.equals(p.getTipoDocumento(), this.tipoDocumento)) || (Objects.equals(p.getTipoDocumento(), 23))) && !(Objects.equals(p.getEstadoAutorizacionSri(), "AUTORIZADO")))).collect(Collectors.toList());
        }
        return listaFacturas;
    }

    public void setListaFacturas(List<FacturaVenta> listaFacturas) {
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

    public Integer getTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(Integer tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }

    public List<FacturaVenta> getListaFacturasSlc() {
        return listaFacturasSlc;
    }

    public void setListaFacturasSlc(List<FacturaVenta> listaFacturasSlc) {
        this.listaFacturasSlc = listaFacturasSlc;
    }
}
