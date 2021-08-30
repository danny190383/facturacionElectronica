package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.PuntoRestriccion;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.Seccion;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesPedidosBean;
import com.jvc.factunet.icefacesUtil.JasperReporPedidostUtil;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.LoginPedidos;
import java.io.IOException;
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
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@Named(value = "pedidoVentasPedidosBean")
@ViewScoped
public class PedidoVentasPedidosBean extends ImprimirReportesPedidosBean{

    private static final Logger LOG = Logger.getLogger(PedidoVentasPedidosBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    private Empresa empresa;
    private final List<PedidoVenta> listaPedidos;
    private Mesa mesaSelec;
    private PedidoVenta pedidoVentaSelc;
    private Cliente cliente;
    private List<Empresa> listaEmpresasTmp;
    
    public PedidoVentasPedidosBean() {
        this.listaPedidos = new ArrayList<>();
        this.listaEmpresasTmp = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
        this.cliente = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getCliente();
        this.listaPedidos.clear();
        if(this.empresa != null){
            this.listaPedidos.addAll(this.documentosServicios.listarPedidosVenta(this.empresa.getCodigo(), "1"));
            this.listaEmpresasTmp.add(this.empresa);
        }
        if(this.cliente != null){
            this.listaPedidos.addAll(this.documentosServicios.listarPedidosVentaCliente(this.cliente.getCodigo(), "1"));
            this.listaEmpresasTmp.addAll(this.documentosServicios.listarPedidosVentaEmpresasCliente(this.cliente.getCodigo(), "1"));
            if(this.cliente.getEmpresaPedidoSlc() != null){
                if(!this.listaEmpresasTmp.contains(this.cliente.getEmpresaPedidoSlc())){
                   this.listaEmpresasTmp.add(this.cliente.getEmpresaPedidoSlc());
                }
            }
            if(this.listaEmpresasTmp.isEmpty()){
                this.regresarHome();
            }
        }
        
        for(Empresa empresaTmp : this.listaEmpresasTmp){
            this.ordenarSeccion(empresaTmp);
            this.ordenarMesas(empresaTmp);
            for(Seccion seccion : empresaTmp.getSeccionList())
            {
                for(Mesa mesaTmp : seccion.getMesaList())
                {
                    mesaTmp.getListaPedidosVenta().clear();
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
    }
    
    public PedidoVenta crearPedidoVenta(Mesa mesa)
    {
        PedidoVenta pedidoVenta = new PedidoVenta();
//        pedidoVenta.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getCuenta().getEmpleado());
        pedidoVenta.setEstado("1");
        pedidoVenta.setFecha(new Date());
        pedidoVenta.setFacturaDetalleList(new ArrayList<>());
        pedidoVenta.setMesa(mesa);
        pedidoVenta.setNumero(100);
        return pedidoVenta;
    }
    
    public void ordenarMesas(Empresa empresaTmp)
    {
        for(Seccion seccion : empresaTmp.getSeccionList())
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
    
    public void ordenarSeccion(Empresa empresaTmp)
    {
        for(int j=0 ; j<empresaTmp.getSeccionList().size() ; j++)
        {
            for(int i=0 ; i<empresaTmp.getSeccionList().size()-1 ; i++)
            {
                if(empresaTmp.getSeccionList().get(i).getOrden() > empresaTmp.getSeccionList().get(i+1).getOrden())
                {
                    Seccion mesaTmp = empresaTmp.getSeccionList().get(i);
                    empresaTmp.getSeccionList().set(i, empresaTmp.getSeccionList().get(i+1));
                    empresaTmp.getSeccionList().set(i +1, mesaTmp);
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
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarProductosStockDialog", options, null);
    }
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            for(Producto producto : listaProductos)
            {
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setProductoServicio(producto);
                detalle.setCantidad(BigDecimal.ONE);
                detalle.setCantidadPorFacturar(detalle.getCantidad());
                detalle.setSubtotalSinDescuento(BigDecimal.ONE);
                detalle.setPvp(producto.getPvp());
                detalle.setFecha(new Date());
//                detalle.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getCuenta().getEmpleado());
                detalle.setFactura(this.pedidoVentaSelc);
                this.documentosServicios.insertarDetalle(detalle);
                this.pedidoVentaSelc.getFacturaDetalleList().add(detalle);
            }
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
            this.documentosServicios.eliminar(detalle);
            pedido.getFacturaDetalleList().remove(detalle);
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
                if(this.empresa != null){
                    pedido.setEmpresa(this.empresa); 
                }else
                {
                    pedido.setEmpresa(this.cliente.getEmpresaPedidoSlc()); 
                }
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
            Logger.getLogger(PedidoVentasPedidosBean.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void onCellEdit(FacturaDetalle event) {
        try {
            event.setCantidadPorFacturar(event.getCantidad());
            this.documentosServicios.actualizar(event);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verBusquedaClientes(Mesa mesa) {
        this.mesaSelec = mesa;
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("contentWidth", 1000);
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarClientesDialog", options, null);
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
            this.mesaSelec.setCliente(this.cliente);
            this.guardarMesa(this.mesaSelec);
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("clienteNE"));
        }
    }
    
    public void verNuevoCliente(Mesa mesa) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", null);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/creacion/nuevoClienteDialog", options, null);
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
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarNotaMedicaDialog", options, null);
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
        PrimeFaces.current().dialog().openDynamic("/faces/transacciones/extras/cambiarSeccionMesaDialog", options, null);
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
                    try {
                        super.getParametros().put("empresa", pedido.getEmpresa().getNombre());
                        super.getParametros().put("mesa", pedido.getMesa().getNombre());
                        super.getParametros().put("fecha", pedido.getFecha());
                        JasperReporPedidostUtil jasperBean = (JasperReporPedidostUtil) FacesUtils.getManagedBean(JasperReporPedidostUtil.NOMBRE_BEAN);
                        jasperBean.jasperReportPrintBean(JasperReporPedidostUtil.PATH_REPORTE_PEDIDO_BEAN,super.getParametros(),listaPrint,punto.getImpresora());
                    } catch (IOException ex) {
                        Logger.getLogger(PedidoVentasPedidosBean.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void regresarHome(){
        FacesUtils.redireccionar("/faces/index.xhtml");
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Empresa> getListaEmpresasTmp() {
        return listaEmpresasTmp;
    }

    public void setListaEmpresasTmp(List<Empresa> listaEmpresasTmp) {
        this.listaEmpresasTmp = listaEmpresasTmp;
    }
}
