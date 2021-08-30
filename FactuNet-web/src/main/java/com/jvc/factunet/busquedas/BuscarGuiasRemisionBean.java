package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.GuiaRemision;
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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "buscarGuiasRemisionBean")
@ViewScoped
public class BuscarGuiasRemisionBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarGuiasRemisionBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<GuiaRemision> listaGuiaRemision;
    private List<GuiaRemision> listaGuiaRemisionSlc;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    
    public BuscarGuiasRemisionBean() {
        this.listaGuiaRemision = new ArrayList<>();
        this.listaGuiaRemisionSlc = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaGuiaRemision.addAll(this.documentosServicios.listarGuiasRemision(this.empresa, "1"));
    }
    
    public void seleccionar(GuiaRemision event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaGuiaRemisionSlc);
    }
    
    public void filtrar()
    {
        this.listaGuiaRemision.clear();
        this.listaGuiaRemision.addAll(this.documentosServicios.listarGuiaRemisionFiltro(empresa, numero, fecha, nombre, "1", ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        try {
            super.getParametros().put("factura", documento);
            if(tipoDocumento == 21){ super.getParametros().put("nombreReporte", "Factura de Venta");}
            if(tipoDocumento == 22){ super.getParametros().put("nombreReporte", "Reserva");}
            if(tipoDocumento == 100){ super.getParametros().put("nombreReporte", "Nota de Crédito");}
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer guia) {
        try {
            super.getParametros().put("guia", guia);
            super.getParametros().put("nombreReporte", "GUIA DE REMISION");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_GUIA_REMISION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BuscarGuiasRemisionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<GuiaRemision> getListaGuiaRemision() {
        return listaGuiaRemision.stream().filter(p -> (!(Objects.equals(p.getEstadoAutorizacionSri(), "AUTORIZADO")))).collect(Collectors.toList());
    }

    public void setListaGuiaRemision(List<GuiaRemision> listaGuiaRemision) {
        this.listaGuiaRemision = listaGuiaRemision;
    }

    public List<GuiaRemision> getListaGuiaRemisionSlc() {
        return listaGuiaRemisionSlc;
    }

    public void setListaGuiaRemisionSlc(List<GuiaRemision> listaGuiaRemisionSlc) {
        this.listaGuiaRemisionSlc = listaGuiaRemisionSlc;
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
