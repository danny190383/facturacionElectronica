package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.NotaCredito;
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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "buscarNotasCreditoBean")
@ViewScoped
public class BuscarNotasCreditoBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarNotasCreditoBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private List<NotaCredito> listaNotaCredito;
    private List<NotaCredito> listaNotaCreditoSlc;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    private Integer tipoSeleccion;
    
    public BuscarNotasCreditoBean() {
        this.listaNotaCredito = new ArrayList<>();
        this.listaNotaCreditoSlc = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.tipoSeleccion = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoBusqueda");
        this.listaNotaCredito.addAll(this.documentosServicios.listarNotasCredito(this.empresa));
    }
    
    public void seleccionar(NotaCredito event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaNotaCreditoSlc);
    }
    
    public void filtrar()
    {
        this.listaNotaCredito.clear();
        this.listaNotaCredito.addAll(this.documentosServicios.listarNotasCreditoFiltro(empresa, numero, fecha, nombre, ruc));
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
    
    public List<NotaCredito> getListaNotaCredito() {
        if(this.tipoSeleccion == 2){
            return listaNotaCredito.stream().filter(p -> (!(Objects.equals(p.getEstadoAutorizacionSri(), "AUTORIZADO")))).collect(Collectors.toList());
        }
        return listaNotaCredito;
    }

    public void setListaNotaCredito(List<NotaCredito> listaNotaCredito) {
        this.listaNotaCredito = listaNotaCredito;
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

    public List<NotaCredito> getListaNotaCreditoSlc() {
        return listaNotaCreditoSlc;
    }

    public void setListaNotaCreditoSlc(List<NotaCredito> listaNotaCreditoSlc) {
        this.listaNotaCreditoSlc = listaNotaCreditoSlc;
    }

    public Integer getTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(Integer tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }
}
