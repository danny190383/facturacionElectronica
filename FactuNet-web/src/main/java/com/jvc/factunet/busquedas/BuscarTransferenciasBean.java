package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Proforma;
import com.jvc.factunet.entidades.TransferenciaProductos;
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
public class BuscarTransferenciasBean extends ImprimirReportesBean implements Serializable{
private static final Logger LOG = Logger.getLogger(BuscarProformasBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<TransferenciaProductos> listaTransferenciaProductos;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String ruc;
    
    public BuscarTransferenciasBean() {
        this.listaTransferenciaProductos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.estado = "0";
        this.listaTransferenciaProductos.addAll(this.documentosServicios.listarTransferenciaProductos(this.empresa, this.estado));
    }
    
    public void seleccionar(Proforma event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void filtrar()
    {
        this.listaTransferenciaProductos.clear();
        this.listaTransferenciaProductos.addAll(this.documentosServicios.listarTransferenciaProductosFiltro(empresa, numero, fecha, nombre, estado, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Transferencia");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TransferenciaProductos> getListaTransferenciaProductos() {
        return listaTransferenciaProductos;
    }

    public void setListaTransferenciaProductos(List<TransferenciaProductos> listaTransferenciaProductos) {
        this.listaTransferenciaProductos = listaTransferenciaProductos;
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
