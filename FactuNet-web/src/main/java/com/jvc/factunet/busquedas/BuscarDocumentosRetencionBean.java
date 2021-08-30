package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.DocumentoRetencion;
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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "buscarDocumentosRetencionBean")
@ViewScoped
public class BuscarDocumentosRetencionBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarDocumentosRetencionBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<DocumentoRetencion> listaDocumentoRetencion;
    private List<DocumentoRetencion> listaDocumentoRetencionSlc;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    
    public BuscarDocumentosRetencionBean() {
        this.listaDocumentoRetencion = new ArrayList<>();
        this.listaDocumentoRetencionSlc = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaDocumentoRetencion.addAll(this.documentosServicios.listarDocumentosRetencion(this.empresa));
    }
    
    public void seleccionar(DocumentoRetencion event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaDocumentoRetencionSlc);
    }
    
    public void filtrar()
    {
        this.listaDocumentoRetencion.clear();
        this.listaDocumentoRetencion.addAll(this.documentosServicios.listarDocumentoRetencionFiltro(empresa, numero, fecha, nombre, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        try {
            super.getParametros().put("factura", documento);
            if(tipoDocumento == 21){ super.getParametros().put("nombreReporte", "Factura de Compra");}
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
            super.getParametros().put("factura", guia);
            super.getParametros().put("nombreReporte", "Retencion");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_RETENCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BuscarGuiasRemisionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<DocumentoRetencion> getListaDocumentoRetencion() {
        return listaDocumentoRetencion;
    }

    public void setListaDocumentoRetencion(List<DocumentoRetencion> listaDocumentoRetencion) {
        this.listaDocumentoRetencion = listaDocumentoRetencion;
    }

    public List<DocumentoRetencion> getListaDocumentoRetencionSlc() {
        return listaDocumentoRetencionSlc;
    }

    public void setListaDocumentoRetencionSlc(List<DocumentoRetencion> listaDocumentoRetencionSlc) {
        this.listaDocumentoRetencionSlc = listaDocumentoRetencionSlc;
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
