package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.PedidoCompra;
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
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class BuscarPedidosCompra extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarPedidosCompra.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private List<PedidoCompra> listaPedidos;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String estadoF;
    private String ruc;
    
    public BuscarPedidosCompra() {
        this.listaPedidos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.estado = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("estado");
        this.listaPedidos.addAll(this.documentosServicios.listarPedidosCompra(this.empresa, this.estado));
        if(this.estado.equals("2"))
        {
            this.estadoF = "2";
        }
        else
        {
            this.estadoF = "0";
        }
    }
    
    public void seleccionar(PedidoCompra event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void filtrar()
    {
        this.listaPedidos.clear();
        this.listaPedidos.addAll(this.documentosServicios.listarPedidoComprasFiltro(empresa, numero, fecha, nombre, estadoF, ruc));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Pedido de compra");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<PedidoCompra> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<PedidoCompra> listaPedidos) {
        this.listaPedidos = listaPedidos;
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

    public String getEstadoF() {
        return estadoF;
    }

    public void setEstadoF(String estadoF) {
        this.estadoF = estadoF;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
