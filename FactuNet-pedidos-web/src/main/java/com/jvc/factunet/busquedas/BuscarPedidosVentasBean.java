package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesPedidosBean;
import com.jvc.factunet.icefacesUtil.JasperReporPedidostUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.LoginPedidos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

@Named(value = "buscarPedidosVentasBean")
@ViewScoped
public class BuscarPedidosVentasBean extends ImprimirReportesPedidosBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarPedidosVentasBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    
    private List<PedidoVenta> listaFacturas;
    private Empresa empresa;
    private Cliente cliente;
    private String estado;
    private Integer numero;
    private Date fecha;
    private String nombre;
    private String mesa;
    private String mascota;
    private String ruc;
    private boolean visible;
    
    public BuscarPedidosVentasBean() {
        this.listaFacturas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.cliente = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getCliente();
        this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
        this.estado = "1";
        if(this.cliente == null)
            this.listaFacturas.addAll(this.documentosServicios.listarPedidoVenta(this.empresa.getCodigo(), this.estado));
        else
            this.listaFacturas.addAll(this.documentosServicios.listarPedidoVentaCliente(this.cliente.getPersona().getCodigo(), "0"));
        this.visible = (boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("visible");
    }
    
    public List<FacturaDetalle> verificarSeleccion(PedidoVenta event){
        List<FacturaDetalle> listTmp = new ArrayList<>();
        for(FacturaDetalle detalle : event.getFacturaDetalleList()){
            if(detalle.getEstado()){
                listTmp.add(detalle);
            }
        }
        return listTmp;
    }
    
    public void seleccionar(PedidoVenta event) {
        List<FacturaDetalle> listaTemporal = this.verificarSeleccion(event);
        if(listaTemporal.size()>0)
        {
            event.getFacturaDetalleList().clear();
            event.getFacturaDetalleList().addAll(listaTemporal);
        }
        for(FacturaDetalle detalle : event.getFacturaDetalleList()){
            detalle.setCantidad(detalle.getCantidadPorFacturar());
        }
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public void filtrar()
    {
        this.listaFacturas.clear();
        if(this.cliente == null)
            this.listaFacturas.addAll(this.documentosServicios.listarPedidoVentaFiltro(empresa.getCodigo(), numero, fecha, nombre, estado, ruc, mascota, mesa));
        else
            this.listaFacturas.addAll(this.documentosServicios.listarPedidoVentaFiltroCliente(cliente.getPersona().getCodigo(), numero, fecha, nombre, estado, ruc, mascota, mesa));
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer documento) {
        try {
            super.getParametros().put("factura", documento);
            super.getParametros().put("nombreReporte", "Factura de Venta");
            super.getParametros().put("SUBREPORT_DIR", JasperReporPedidostUtil.PATH);
            JasperReporPedidostUtil jasperBean = (JasperReporPedidostUtil) FacesUtils.getManagedBean(JasperReporPedidostUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReporPedidostUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void verNotaMedica(PedidoVenta pedido) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pedido", pedido);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", pedido.getCliente());
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/transacciones/extras/nuevaNotaVeterinariaDialog", options, null);
    }

    public List<PedidoVenta> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<PedidoVenta> listaFacturas) {
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

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }
}
