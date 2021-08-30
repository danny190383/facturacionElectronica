package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.NotaDebito;
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

@Named(value = "buscarNotasDebitoBean")
@ViewScoped
public class BuscarNotasDebitoBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarNotasCreditoBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private List<NotaDebito> listaNotaDebito;
    private List<NotaDebito> listaNotaDebitoSlc;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    private Integer tipoSeleccion;
    
    public BuscarNotasDebitoBean() {
        this.listaNotaDebito = new ArrayList<>();
        this.listaNotaDebitoSlc = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.tipoSeleccion = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoBusqueda");
        this.listaNotaDebito.addAll(this.documentosServicios.listarNotasDebito(this.empresa));
    }
    
    public void seleccionar(NotaDebito event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.listaNotaDebitoSlc);
    }
    
    public void filtrar()
    {
        this.listaNotaDebito.clear();
        this.listaNotaDebito.addAll(this.documentosServicios.listarNotasDebitoFiltro(empresa, numero, fecha, nombre, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento, Integer tipoDocumento) {
        try {
            super.getParametros().put("factura", documento);
            if(tipoDocumento == 21){ super.getParametros().put("nombreReporte", "Factura de Venta");}
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
    
    public List<NotaDebito> getListaNotaDebito() {
        if(this.tipoSeleccion == 2){
            return listaNotaDebito.stream().filter(p -> (!(Objects.equals(p.getEstadoAutorizacionSri(), "AUTORIZADO")))).collect(Collectors.toList());
        }
        return listaNotaDebito;
    }

    public void setListaNotaDebito(List<NotaDebito> listaNotaDebito) {
        this.listaNotaDebito = listaNotaDebito;
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

    public List<NotaDebito> getListaNotaDebitoSlc() {
        return listaNotaDebitoSlc;
    }

    public void setListaNotaDebitoSlc(List<NotaDebito> listaNotaDebitoSlc) {
        this.listaNotaDebitoSlc = listaNotaDebitoSlc;
    }

    public Integer getTipoSeleccion() {
        return tipoSeleccion;
    }

    public void setTipoSeleccion(Integer tipoSeleccion) {
        this.tipoSeleccion = tipoSeleccion;
    }
}
