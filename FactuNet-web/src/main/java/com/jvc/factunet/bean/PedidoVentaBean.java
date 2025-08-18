package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.CabeceraFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.entidades.PaqueteVenta;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoImpuestoTarifa;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.PuntoRestriccion;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.Seccion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.print.TicketPedido;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.EmpresaServicio;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name="pedidoVentaBean")
@ViewScoped
public class PedidoVentaBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(PedidoVentaBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private EmpresaServicio empresaServicio;
    @EJB
    public ClienteServicio clienteServicio;
    @ManagedProperty(value = "#{pedidoCompraBean}")
    private PedidoCompraBean pedidoCompraBean;
    
    public void setPedidoCompraBean(PedidoCompraBean pedidoCompraBean) {
        this.pedidoCompraBean = pedidoCompraBean;
    }

    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private List<PedidoVenta> listaPedidos;
    private Mesa mesaSelec;
    private PedidoVenta pedidoVentaSelc;
    
    public PedidoVentaBean() {
        this.listaPedidos = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaPedidos.clear();
        this.empresa = this.empresaServicio.buscar(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo());
        this.listaPedidos.addAll(this.documentosServicios.listarPedidosVenta(this.empresa.getCodigo(), "1"));
        this.ordenarSeccion();
        this.ordenarMesas();
        for(Seccion seccion : this.empresa.getSeccionList())
        {
            for(Mesa mesaTmp : seccion.getMesaList())
            {
                for(PedidoVenta pedido : this.listaPedidos)
                {
                    if(Objects.equals(pedido.getMesa().getCodigo(), mesaTmp.getCodigo()))
                    {
                        mesaTmp.setEstadoMesa(true);
                        mesaTmp.getListaPedidosVenta().add(pedido);
                    }
                }
            }
        }
    }
    
    public PedidoVenta crearPedidoVenta(Mesa mesa)
    {
        PedidoVenta pedidoVenta = new PedidoVenta();
        pedidoVenta.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        pedidoVenta.setEmpresa(this.empresa);
        pedidoVenta.setEstado("1");
        pedidoVenta.setFecha(new Date());
        pedidoVenta.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        pedidoVenta.setMesa(mesa);
        pedidoVenta.setNumero(100);
        return pedidoVenta;
    }
    
    public void ordenarMesas()
    {
        for(Seccion seccion : this.empresa.getSeccionList())
        {
            for(int j=0 ; j<seccion.getMesaList().size() ; j++)
            {
                for(int i=0 ; i<seccion.getMesaList().size()-1 ; i++)
                {
                    if(seccion.getMesaList().get(i).getOrden() > seccion.getMesaList().get(i+1).getOrden())
                    {
                        Mesa mesaTmp = seccion.getMesaList().get(i);
                        mesaTmp.setCliente(new Cliente());
                        mesaTmp.getCliente().setPersona(new Persona());
                        seccion.getMesaList().set(i, seccion.getMesaList().get(i+1));
                        seccion.getMesaList().set(i +1, mesaTmp);
                    }
                }
            }
        }
    }
    
    public void ordenarSeccion()
    {
        for(int j=0 ; j<this.empresa.getSeccionList().size() ; j++)
        {
            for(int i=0 ; i<this.empresa.getSeccionList().size()-1 ; i++)
            {
                if(this.empresa.getSeccionList().get(i).getOrden() > this.empresa.getSeccionList().get(i+1).getOrden())
                {
                    Seccion mesaTmp = this.empresa.getSeccionList().get(i);
                    this.empresa.getSeccionList().set(i, this.empresa.getSeccionList().get(i+1));
                    this.empresa.getSeccionList().set(i +1, mesaTmp);
                }
            }
        }
    }
    
    public void verBusquedaProductosStock(PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 600);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 600);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProductosStockDialog", options, null);
    }
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            for(Producto producto : listaProductos)
            {
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setProductoServicio(producto);
                detalle.setCantidad(producto.getCantidad());
                detalle.setDescripcion(producto.getObservacion()); 
                detalle.setCantidadPorFacturar(detalle.getCantidad());
                detalle.setFactura(this.pedidoVentaSelc);
                this.pedidoVentaSelc.getFacturaDetalleList().add(crearDetalle(detalle));
            }
            this.pedidoVentaSelc = (PedidoVenta)this.pedidoCompraBean.calcularTotalPago((Factura)this.pedidoVentaSelc);
            this.guardar(this.pedidoVentaSelc);
        }
    }
    
    public void onCellEdit(FacturaDetalle event) {
        try {
            event.setCantidadPorFacturar(event.getCantidad());
            event.setSubtotalSinDescuento((event.getPrecioVentaUnitario().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
            event.setSubtotalConDescuento((event.getPrecioVentaUnitarioDescuento().multiply(event.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
            event.setValorDescuento((event.getSubtotalSinDescuento().multiply(event.getDescuento().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
            event.setValorComision((event.getSubtotalConDescuento().multiply(event.getComision().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));  
            this.pedidoVentaSelc = (PedidoVenta)this.pedidoCompraBean.calcularTotalPago(event.getFactura());
            this.guardar(this.pedidoVentaSelc);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public FacturaDetalle crearDetalle(FacturaDetalle detalle)
    {
        detalle.setFecha(new Date());
        detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        detalle.setDescuento(detalle.getProductoServicio().getDescuentoVenta());
        detalle.setComision(BigDecimal.ZERO);
        detalle.setPrecioVentaUnitario(BigDecimal.ZERO);
        detalle.setStock(BigDecimal.ZERO);
        if(detalle.getProductoServicio() instanceof ProductoBodega)
        {
            ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
            detalle.setCostoFecha(productoBodega.getPrecioUltimaCompra());
            detalle.setPvp(productoBodega.getPvp());
            detalle.setPrecioVentaUnitario(productoBodega.getPvp().add((productoBodega.getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP)));  
            detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario().subtract((detalle.getPrecioVentaUnitario().multiply(detalle.getDescuento())).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP)));  
        }
        else
        {
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                detalle.setIsPaquete(Boolean.TRUE);
                for(PaqueteVenta proPa : ((ProductoPaquete)detalle.getProductoServicio()).getPaqueteVentaList())
                {
                    if(proPa.getProducto() instanceof ProductoBodega)
                    {
                        proPa.setIsBodega(Boolean.TRUE);
                        proPa.setBodegaSlc(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega().getCodigo());
                        proPa.setBodega(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getBodega());
                        proPa.setStock(((ProductoBodega)proPa.getProducto()).getProductoStockList().get(0).getStock());
                    }
                }
            }
            detalle.setCostoFecha(detalle.getProductoServicio().getPrecioUltimaCompra());
            detalle.setPvp(detalle.getProductoServicio().getPvp());
            detalle.setPrecioVentaUnitario(detalle.getProductoServicio().getPvp().add((detalle.getProductoServicio().getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));  
            detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario().subtract((detalle.getPrecioVentaUnitario().multiply(detalle.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));  
        }
        detalle.setPrecioUnitarioTmp(detalle.getPrecioVentaUnitario());
        detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
        detalle.setValorDescuento((((detalle.getPrecioVentaUnitarioDescuento().multiply(detalle.getDescuento())).divide(new BigDecimal(100))).multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));  
        detalle.setSubtotalConDescuento(detalle.getPrecioVentaUnitarioDescuento().multiply(detalle.getCantidad()));
        detalle.setValorComision((((detalle.getPvp().multiply(detalle.getComision())).divide(new BigDecimal(100))).multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));  
        for(ProductoImpuestoTarifa tarifaImpuesto : detalle.getProductoServicio().getProductoImpuestoTarifaList()){
            if(tarifaImpuesto.getImpuestoTarifa().getImpuesto().getId() == 1){
                detalle.setImpuestoTarifa(tarifaImpuesto.getImpuestoTarifa());
                break;
            }
        }
        return detalle;
    }
    
    public void verNotaMedica(PedidoVenta pedido) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pedido", pedido);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", pedido.getCliente());
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("contentWidth", 1100);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaNotaVeterinariaDialog", options, null);
    }
    
    public void eliminarPedidoMesa(Mesa mesa, PedidoVenta pedido) {
        try {
            this.documentosServicios.eliminar(pedido);
            mesa.getListaPedidosVenta().remove(pedido);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
     
     public void eliminarDetalle(FacturaDetalle detalle, PedidoVenta pedido) {
        try {
//            this.documentosServicios.eliminar(detalle);
            pedido.getFacturaDetalleList().remove(detalle);
            this.pedidoVentaSelc = (PedidoVenta)this.pedidoCompraBean.calcularTotalPago(pedido);
            this.guardar(this.pedidoVentaSelc);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
     
    public void guardar(PedidoVenta pedido)
    {
        try {
            if(pedido.getCodigo() == null)
            {
                this.documentosServicios.insertarDocumento(pedido);
            }
            else
            {
                this.documentosServicios.actualizar(pedido);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void guardarMesa(Mesa mesa)
    {
        if(mesa.getCliente() != null)
        {
            try {
                PedidoVenta pedido = this.crearPedidoVenta(mesa);
                pedido.setCliente(mesa.getCliente());
                pedido.setMascota(mesa.getMascotas());
                this.documentosServicios.insertarDocumento(pedido);
                mesa.getListaPedidosVenta().add(pedido);
                mesa.setCliente(new Cliente());
                mesa.getCliente().setPersona(new Persona());
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("seleccioneCliente"));
        }
    }
    
    public void facturarPedidos(Mesa mesa) {
        try {
            List<Integer> lista = new ArrayList<>();
            for(Object pedido : mesa.getListaPedidosVenta()){
                
                if(((PedidoVenta)pedido).getFacturar()){
                    lista.add(((PedidoVenta)pedido).getCodigo());
                }
            }
            if(!lista.isEmpty())
            {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPedidosF", lista);
                FacesContext.getCurrentInstance().getExternalContext().redirect("facturaVenta.xhtml?pedido=" + mesa.getCodigo());
            }
            else
            {
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("pedidoNoRegistrado"));
            }
        } catch (IOException ex) {
            Logger.getLogger(PedidoVentaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void registrarPedidos(Mesa mesa) {
        List<PedidoVenta> lista = new ArrayList<>();
        for(Object pedidoObj : mesa.getListaPedidosVenta()){
            PedidoVenta pedido = (PedidoVenta) pedidoObj;
            if(pedido.getFacturar()){
                pedido.setEstado("3");
                this.guardar(pedido);
                lista.add(pedido);
            }
        }
        for(PedidoVenta pedicoTmp : lista){
            mesa.getListaPedidosVenta().remove(pedicoTmp);
        }
    }
    
    public void verBusquedaClientes(Mesa mesa) {
        this.mesaSelec = mesa;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        if(this.empresa.getTipoEmpresa().equals("1")){
            options.put("width", 750);
            options.put("height", 500);
            options.put("contentWidth", 750);
            options.put("contentHeight", 500);
            PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
        }
        else
        {
            options.put("width", 1100);
            options.put("height", 550);
            options.put("contentWidth", 1100);
            options.put("contentHeight", 550);
            PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesMascotaDialog", options, null);
        }
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            try {
                if((Cliente) event.getObject() != null)
                {
                    this.mesaSelec.setCliente((Cliente) event.getObject());
                    if(this.empresa.getTipoEmpresa().equals("2")){
                        this.mesaSelec.setMascotas(((Cliente) event.getObject()).getMascota());
                    }
                    this.guardarMesa(this.mesaSelec);
                }
            } catch (Exception e) {
                if((List) event.getObject() != null)
                {
                    List<Cliente> listaClientesMascota = (List) event.getObject();
                    for(Cliente clienteTmp : listaClientesMascota){
                        this.mesaSelec.setCliente(clienteTmp);
                        if(this.empresa.getTipoEmpresa().equals("2")){
                            this.mesaSelec.setMascotas(clienteTmp.getMascota());
                        }
                        this.guardarMesa(this.mesaSelec);
                    }
                }
            }  
        }
    }
    
    public void consumidorFinal(Mesa mesa){
        this.mesaSelec = mesa;
        try {
            Cliente clienteTmp = this.clienteServicio.buscarConsumidorFinal(this.empresa.getCodigo());
            if(clienteTmp == null)
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("clienteNE"));
            }
            else
            {
                this.mesaSelec.setCliente(clienteTmp);
                this.guardarMesa(this.mesaSelec);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("clienteNE"));
        }
    }
    
    public void verNuevoCliente(Mesa mesa) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", null);
        this.mesaSelec = mesa;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 550);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 550);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoClienteDialog", options, null);
    }
    
    public void verBusquedaPedidos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("visible", false);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedidosVentaDialog", options, null);
    }
    
    public void verBusquedaNotaMedica() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarNotaMedicaDialog", options, null);
    }
    
    public void cambiarMesa(Mesa mesa,PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
        this.mesaSelec = mesa;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/cambiarSeccionMesaDialog", options, null);
    }
    
    public void guardarCambioMesa(SelectEvent event) {
        if(event.getObject() != null)
        {
            Mesa mesaTmp = (Mesa)event.getObject();
            this.pedidoVentaSelc.setMesa(mesaTmp);
            this.guardar(this.pedidoVentaSelc);
            this.init();
        }
    }
    
    public List<PuntoVenta> verPuntoMovil(){
        List<PuntoVenta> listaReturn = new ArrayList<>();
        for(PuntoVenta punto : this.empresa.getPuntoVentaList()){
            if(punto.getTablet().equals("1")){
                listaReturn.add(punto);
            }
        }
        return listaReturn;
    }
    
    public void generarReporteBean(PedidoVenta pedido) throws ClassNotFoundException{
        if(pedido.getFacturaDetalleList() != null)
        {
            List<PuntoVenta> listaPuntos = new ArrayList<>();
            listaPuntos.addAll(this.verPuntoMovil());
            for(PuntoVenta punto : listaPuntos){
                List<FacturaDetalle> listaPrint = verificarRestriccion(pedido.getFacturaDetalleList(),punto);
                if(!listaPrint.isEmpty()){
                    if(punto.getTipoImpresora().equals("1")){
                        try {
                            super.getParametros().put("empresa", pedido.getEmpresa().getNombre());
                            super.getParametros().put("mesa", pedido.getMesa().getNombre());
                            super.getParametros().put("fecha", pedido.getFecha());
                            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                            jasperBean.jasperReportPrintBean(JasperReportUtil.PATH_REPORTE_PEDIDO_BEAN,super.getParametros(),listaPrint,punto.getImpresora());
                        } catch (IOException ex) {
                            Logger.getLogger(PedidoVentaBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else
                    {
                        this.imprimirTiket(listaPrint,pedido,punto);
                    }
                }
            }
        }
        
    }
    
    public List<FacturaDetalle> verificarRestriccion(List<FacturaDetalle> listaDetalles, PuntoVenta punto){
        List<FacturaDetalle> listaDetallesReturn = new ArrayList<>();
        if(punto.getPuntoRestriccionList() != null){
            Boolean ban;
            for(FacturaDetalle detalle : listaDetalles)
            {
                ban = Boolean.TRUE;
                for(PuntoRestriccion restriccion : punto.getPuntoRestriccionList()){
                    if(Objects.equals(detalle.getProductoServicio().getGrupo().getCodigo(), restriccion.getGrupo().getCodigo())){
                        ban = Boolean.FALSE;
                        break;
                    }
                }
                if(ban){
                    listaDetallesReturn.add(detalle);
                }
            }
        }
        return listaDetallesReturn;
    }
    
    public Boolean imprimirTiket(List<FacturaDetalle> listaPint, PedidoVenta pedido,PuntoVenta punto){
        String items = StringUtils.EMPTY;
        for(FacturaDetalle detalleTiket : listaPint){
            items = items + detalleTiket.getCantidad() + " " + detalleTiket.getProductoServicio().getNombre() + "\n" + (detalleTiket.getDescripcion() == null ? " " : detalleTiket.getDescripcion()) + "\n";
        }
        TicketPedido ticket = new TicketPedido(this.empresa.getNombre(), 
                                               this.empresa.getCiudad().getNombre() + "," + Fecha.formatoDateTimeToStringF0(pedido.getFecha()),
                                               pedido.getMesa().getNombre(),
                                               pedido.getCliente().getPersona().getNombres() + " " + pedido.getCliente().getPersona().getApellidos(),
                                               items);
        if(ticket.print(punto.getImpresora())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
