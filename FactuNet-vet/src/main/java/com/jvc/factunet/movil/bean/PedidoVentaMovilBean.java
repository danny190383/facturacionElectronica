package com.jvc.factunet.movil.bean;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.MascotaNotaMedica;
import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.entidades.PuntoRestriccion;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.Seccion;
import com.jvc.factunet.movil.icefacesUtil.CatalogosPersonaMovilBean;
import com.jvc.factunet.movil.icefacesUtil.FacesUtilsMovil;
import static com.jvc.factunet.movil.icefacesUtil.FacesUtilsMovil.getServletContext;
import com.jvc.factunet.movil.icefacesUtil.JasperReportUtilMovil;
import com.jvc.factunet.movil.print.TicketPedido;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.EmpresaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.MascotaServicio;
import com.jvc.factunet.servicios.NotaMedicaVeterinariaServicio;
import com.jvc.factunet.servicios.PersonaServicio;
import com.jvc.factunet.servicios.ProductoPaqueteServicio;
import com.jvc.factunet.servicios.ProductoServiciosServicio;
import com.jvc.factunet.servicios.ProductoStockServicio;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class PedidoVentaMovilBean extends CatalogosPersonaMovilBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(PedidoVentaMovilBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private EmpresaServicio empresaServicio;
    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private PersonaServicio personaServicio;
    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    private MascotaServicio mascotaServicio;
    @EJB
    public GrupoProductoServicio grupoProductoServicio;
    @EJB
    private ProductoServiciosServicio productoServiciosServicio;
    @EJB
    private ProductoPaqueteServicio productoPaqueteServicio;
    @EJB
    private NotaMedicaVeterinariaServicio medicaVeterinariaServicio;
    
    private MascotaNotaMedica mascotaNotaMedica;
    private Empresa empresa;
    private List<PedidoVenta> listaPedidos;
    private Mesa mesaSelec;
    private PedidoVenta pedidoVentaSelc;
    private LazyDataModel<Persona> lazyModel;
    private LazyDataModel<Mascota> lazyModelMascotas;
    private List<Mascota> listaMascotasSelct;
    private Cliente cliente;
    private String nombreLogo;
    private String pathLogo;
    private Mascota mascotaObj;
    public GrupoProducto grupoProductoSelc;
    private String opcion;
    private List<GrupoProducto> listaPadres;
    private List<FacturaDetalle> listaProductosTodosSelc;
    private LazyDataModel<ProductoServicio> lazyModelServicios;
    private LazyDataModel<ProductoPaquete> lazyModelPaquetes;
    private List<MascotaNotaMedica> listaNotasMedicas;
    private Mascota mascotaSelc;
    
    public PedidoVentaMovilBean() {
        this.listaPedidos = new ArrayList<>();
        this.listaProductosStockSelc = new ArrayList<>();
        this.listaMascotasSelct = new ArrayList<>();
        this.mascotaObj = new Mascota();
        this.listaPadres = new ArrayList<>();
        this.listaProductosTodosSelc = new ArrayList<>();
        this.mascotaNotaMedica = new MascotaNotaMedica();
        this.listaNotasMedicas = new ArrayList<>();
        this.mascotaSelc = new Mascota();
    }
    
    @PostConstruct
    public void init()
    {
        this.opcion = "P";
        this.listaPadres = this.grupoProductoServicio.listarPorNivelEstado(1,1);
        this.grupoProductoSelc = this.listaPadres.get(0);
        this.empresa = this.empresaServicio.buscar(1);
        this.listaPedidos.clear();
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
        this.listarClientes();
        this.llenarTabla();
        this.llenarTablaServicios();
        this.llenarTablaPaquetes();
        this.initCliente();
        this.listarMascotas();
        this.nuevaMascota(null);
    }
    
    public void buscarNotasMedicas(Mascota mascota){
        this.listaNotasMedicas.clear();
        this.listaNotasMedicas.addAll(this.medicaVeterinariaServicio.listarNotasMedicasMascota(mascota.getCodigo()));
    }
    
    public void inicializarNM(PedidoVenta pedido){
        this.mascotaNotaMedica = new MascotaNotaMedica();
        this.mascotaNotaMedica.setFecha(new Date());
        this.mascotaNotaMedica.setPedido(pedido);
        this.mascotaNotaMedica.setMascota(this.mascotaSelc);
    }
    
    public void initCliente()
    {
        Path pathL = Paths.get(getServletContext().getRealPath("/") + File.separator + "temp");
        if (!Files.exists(pathL)) {
            try {
                Files.createDirectory(pathL);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "No se puede cargar los Datos.", ex);
            }
        }
        this.nuevoCliente();
    }
    
    public void nuevoCliente() {
        this.cliente = new Cliente();
        this.cliente.setPersona(new Persona());
        this.cliente.getPersona().setSexo(super.getListaSexo().get(0));
        this.cliente.getPersona().setCiudad(super.getListaCiudad().get(0));
        this.cliente.setTipoCliente(super.getListaTipoCliente().get(0));
        this.cliente.getPersona().setEstadoCivil(super.getListaEstadoCivil().get(0));
        this.cliente.setCapacidadCredito(BigDecimal.ZERO);
        this.cliente.getPersona().setEmpresa(new Empresa(1));
        this.nombreLogo = "foto_hombre.jpg";
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
    }
    
    public void guardarCliente()
    {
        try {
            if(this.cliente.getPersona().getCedula() != null && !this.cliente.getPersona().getCedula().trim().isEmpty()){
                if(!this.verificarCedulaSistema())
                {
                    this.guardarClienteDatos();
                    this.mesaSelec.setCliente(this.cliente);
                    this.guardarMesa(this.mesaSelec);
                }
                else
                {
                    FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("clienteEncontrado"));
                }
            }
            else
            {
                this.guardarClienteDatos();
                this.mesaSelec.setCliente(this.cliente);
                this.guardarMesa(this.mesaSelec);
            }
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", ex);
        }
    }
    
    public void consumidorFinal(Mesa mesa){
        this.mesaSelec = mesa;
        try {
            Cliente clienteTmp = this.clienteServicio.buscarConsumidorFinal(this.empresa.getCodigo());
            if(clienteTmp == null)
            {
                FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("clienteNE"));
            }
            else
            {
                this.mesaSelec.setCliente(clienteTmp);
                this.guardarMesa(this.mesaSelec);
            }
        } catch (Exception e) {
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("clienteNE"));
        }
    }
    
    public Boolean verificarCedulaSistema()
    {
        try {
            this.cliente.getPersona().setCedula(this.cliente.getPersona().getCedula().trim());
            Cliente clienteTmp = this.clienteServicio.buscarCedula(this.cliente.getPersona().getCedula());
            if(clienteTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.cliente.getPersona().getCedula());
                if(personaTmp == null)
                {
                    return Boolean.FALSE;
                }
                else
                {
                    this.cliente.setPersona(personaTmp);
                    return Boolean.FALSE;
                }
            }
            else
            {
                this.cliente = clienteTmp;
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
    
    public void guardarClienteDatos() throws IOException, Exception
    {
        Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
        byte[] foto = Files.readAllBytes(path);
        this.cliente.getPersona().setFoto(foto);
        this.cliente.getPersona().setNombres(this.cliente.getPersona().getNombres().trim().toUpperCase());
        this.cliente.getPersona().setApellidos(this.cliente.getPersona().getApellidos().trim().toUpperCase());
        if(this.cliente.getPersona().getCedula().trim().isEmpty())
        {
           this.cliente.getPersona().setCedula(null); 
        }
        this.cliente = this.clienteServicio.actualizar(this.cliente);
        this.mesaSelec.setCliente(this.cliente);
    }
    
    public void listarOpcion(){
        if(this.empresa.getTipoEmpresa().equals("2")){
            this.listarMascotas();
        }
        else
        {
            this.listarClientes();
        }
    }
    
    private String nombres = StringUtils.EMPTY;
    private String cedula = StringUtils.EMPTY;
    public void listarClientes(){
        try {
            this.lazyModel = new LazyDataModel<Persona>()
            {
                @Override
                public List<Persona> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    List<Persona> result = clienteServicio.listarBuscar(nombres,cedula,empresa.getCodigo(), pageSize, first);
                    lazyModel.setRowCount(clienteServicio.contar(nombres,cedula,empresa.getCodigo()).intValue());
                    return result;
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    private String mascota = StringUtils.EMPTY;
    private String numero = StringUtils.EMPTY;
    public void listarMascotas(){
        try {
            this.lazyModelMascotas = new LazyDataModel<Mascota>()
            {
                @Override
                public List<Mascota> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    List<Mascota> result = clienteServicio.listarBuscarMascota(numero,nombres,cedula,mascota,empresa.getCodigo(), pageSize, first);
                    lazyModelMascotas.setRowCount(clienteServicio.contarMascota(numero,nombres,cedula,mascota,empresa.getCodigo()).intValue());
                    return result;
                }
                
                @Override
                public Mascota getRowData(String rowKey) {
                    List<Mascota> lista = (List<Mascota>) getWrappedData();
                    for(Mascota car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(Mascota car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    private LazyDataModel<ProductoStock> lazyModelStock;
    private List<ProductoStock> listaProductosStockSelc;
    String nombreProducto = StringUtils.EMPTY;
    public void llenarTabla()
    {
        try {
            this.lazyModelStock = new LazyDataModel<ProductoStock>()
            {
                @Override
                public List<ProductoStock> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                    {
                        List<ProductoStock> result = productoStockServicio.listarBuscar(nombreProducto,StringUtils.EMPTY,StringUtils.EMPTY,1,grupoProductoSelc.getCodigo(), pageSize, first);
                        lazyModelStock.setRowCount(productoStockServicio.contar(nombreProducto,StringUtils.EMPTY,StringUtils.EMPTY,1,grupoProductoSelc.getCodigo()).intValue());
                        return result;
                    }
                    else
                    {
                         List<ProductoStock> result = productoStockServicio.listarBuscarPadre(nombreProducto,StringUtils.EMPTY,StringUtils.EMPTY,1,grupoProductoSelc.getCodigo(), pageSize, first);
                         lazyModelStock.setRowCount(productoStockServicio.contarPadre(nombreProducto,StringUtils.EMPTY,StringUtils.EMPTY,1,grupoProductoSelc.getCodigo()).intValue());
                         return result;
                    }
                }
                
                @Override
                public ProductoStock getRowData(String rowKey) {
                    List<ProductoStock> lista = (List<ProductoStock>) getWrappedData();
                    for(ProductoStock car : lista) {
                        if(car.getProductoBodega().getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoStock car) {
                    return car.getProductoBodega().getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void llenarTablaServicios()
    {
        try {
            this.lazyModelServicios = new LazyDataModel<ProductoServicio>()
            {
                @Override
                public List<ProductoServicio> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                    {
                        List<ProductoServicio> result = productoServiciosServicio.listarBuscar(nombreProducto, empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                        lazyModelServicios.setRowCount(productoServiciosServicio.contar(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                        return result;
                    }
                    else
                    {
                        List<ProductoServicio> result = productoServiciosServicio.listarBuscarPadre(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                        lazyModelServicios.setRowCount(productoServiciosServicio.contarPadre(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                        return result;
                    }
                }
                
                @Override
                public ProductoServicio getRowData(String rowKey) {
                    List<ProductoServicio> lista = (List<ProductoServicio>) getWrappedData();
                    for(ProductoServicio car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoServicio car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void llenarTablaPaquetes()
    {
        try {
            this.lazyModelPaquetes = new LazyDataModel<ProductoPaquete>()
            {
                @Override
                public List<ProductoPaquete> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if((grupoProductoSelc.getGrupoProductoList() == null) ||  (grupoProductoSelc.getGrupoProductoList().isEmpty()))
                    {
                        List<ProductoPaquete> result = productoPaqueteServicio.listarBuscar(nombreProducto, empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                        lazyModelPaquetes.setRowCount(productoPaqueteServicio.contar(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                        return result;
                    }
                    else
                    {
                        List<ProductoPaquete> result = productoPaqueteServicio.listarBuscarPadre(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo(), pageSize, first);
                        lazyModelPaquetes.setRowCount(productoPaqueteServicio.contarPadre(nombreProducto,empresa.getCodigo(),grupoProductoSelc.getCodigo()).intValue());
                        return result;
                    }
                       
                }
                
                @Override
                public ProductoPaquete getRowData(String rowKey) {
                    List<ProductoPaquete> lista = (List<ProductoPaquete>) getWrappedData();
                    for(ProductoPaquete car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(ProductoPaquete car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede cargar los Datos.", e);
        }
    }
    
    public void onRowSelect(Producto producto) {
//        producto.setCantidad(BigDecimal.ONE);
        Boolean ban = Boolean.TRUE;
        for(FacturaDetalle productoTmp : this.listaProductosTodosSelc){
            if(Objects.equals(producto.getCodigo(), productoTmp.getProductoServicio().getCodigo())){
                ban = Boolean.FALSE;
                break;
            }
        }
        if(ban){
            this.listaProductosTodosSelc.add(this.crearDetalleProducto(producto));
        }
        else
        {
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("productoAgregadoE"));
        }
    }
    
    public FacturaDetalle crearDetalleProducto(Producto producto){
        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setProductoServicio(producto);
        detalle.setCantidad(BigDecimal.ONE);
        detalle.setCantidadPorFacturar(detalle.getCantidad()); 
        detalle.setDescripcion(StringUtils.EMPTY);
        detalle.setSubtotalSinDescuento(BigDecimal.ONE);
        detalle.setPvp(producto.getPvp());
        detalle.setFecha(new Date());
        detalle.setFactura(this.pedidoVentaSelc);
        this.documentosServicios.insertarDetalle(detalle);
        this.pedidoVentaSelc.getFacturaDetalleList().add(detalle);
        return detalle;
    }
    
    public void imprimirReporte() throws IOException {
        if(!this.listaProductosTodosSelc.isEmpty()){
            if(this.generarReportePrint()){
                FacesUtilsMovil.addInfoMessage("Imprimiendo");
            }
            else
            {
                FacesUtilsMovil.addErrorMessage("Error de impresion");
            }
        }
    }
    
    public PedidoVenta crearPedidoVenta(Mesa mesa)
    {
        PedidoVenta pedidoVenta = new PedidoVenta();
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
    
    public void nuevaNotaMedica(PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
        this.mascotaSelc = pedido.getMascota();
        this.buscarNotasMedicas(pedido.getMascota());
        this.inicializarNM(pedido);
    }
    
    public void verBusquedaProductosStock(PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
        this.listaProductosStockSelc.clear();
        this.nombreProducto = StringUtils.EMPTY;
        this.listaProductosTodosSelc.clear();
        this.onNodeSelect(this.grupoProductoSelc);
    }
    
    public void verNotaMedica(PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
    }
    
    public void eliminarPedidoMesa(Mesa mesa, PedidoVenta pedido) {
        try {
            this.documentosServicios.eliminar(pedido);
            mesa.getListaPedidosVenta().remove(pedido);
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registronoEliminado"));
        }
    }
     
    public void eliminarDetalle(FacturaDetalle detalle, PedidoVenta pedido) {
        try {
            this.documentosServicios.eliminar(detalle);
            pedido.getFacturaDetalleList().remove(detalle);
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarProductoSelc(FacturaDetalle detalle) {
        try {
            this.documentosServicios.eliminar(detalle);
            this.listaProductosTodosSelc.remove(detalle);
            this.pedidoVentaSelc.getFacturaDetalleList().remove(detalle);
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registronoEliminado"));
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
                this.pedidoVentaSelc = this.documentosServicios.actualizar(pedido);
            }
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registroNoGuardado"));
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
//                    FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroGrabado"));
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                    FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registroNoGuardado"));
                }
        }
        else
        {
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("seleccioneCliente"));
        }
    }
    
    public void onCellEdit(FacturaDetalle event) {
        try {
            if((event.getCantidad() == null ) || (event.getCantidad().floatValue() < 0)){
                event.setCantidad(BigDecimal.ONE);
            }
            event.setCantidadPorFacturar(event.getCantidad());
            this.documentosServicios.actualizar(event);
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void verBusquedaClientes(Mesa mesa) {
        this.mesaSelec = mesa;
        this.nombres = StringUtils.EMPTY;
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            if((Cliente) event.getObject() != null)
            {
                this.mesaSelec.setCliente((Cliente) event.getObject());
                if(this.empresa.getTipoEmpresa().equals("2")){
                    this.mesaSelec.setMascotas(((Cliente) event.getObject()).getMascota());
                }
            }
        }
    }
    
    public void verNuevoCliente(Mesa mesa) {
        this.mesaSelec = mesa;
        this.initCliente();
    }
    
    public void verMascota(Mesa mesa) {
        if(mesa.getCliente() == null)
        {
            FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("seleccioneCliente"));
        }
        else
        {
            this.mesaSelec = mesa;
        }
    }
    
    public void seleccionarClienteMA(Mascota event) {
        this.mesaSelec.setCliente(this.clienteServicio.buscarCliente(event.getPersona().getCodigo()));
        this.mesaSelec.setMascotas(event);
        this.guardarMesa(this.mesaSelec);
    }
    
    public void seleccionarCliente(Persona event) {
        this.mesaSelec.setCliente(this.clienteServicio.buscarCliente(event.getCodigo()));
        this.guardarMesa(this.mesaSelec);
    }
    
    public void seleccionarClienteM(Mascota event) {
        this.mesaSelec.setCliente(this.clienteServicio.buscarCliente(event.getPersona().getCodigo()));
        this.nuevaMascota(event.getPersona());
    }
    
    public void nuevaMascota(Persona persona){
        this.mascotaObj = new Mascota();
        this.mascotaObj.setPersona(persona);
        this.mascotaObj.setFechaNacimiento(new Date());
        this.mascotaObj.setEspecie(super.getListaEspecies().get(0));
        this.mascotaObj.setRaza(super.getListaRaza().get(0));
        this.mascotaObj.setSexo(super.getListaSexoMascota().get(0));
        this.nombreLogo = "foto_hombre.jpg";
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
    }
    
    public void guardarMascota(){
        try {
            this.mascotaObj.setPersona(this.pedidoVentaSelc.getCliente().getPersona());
            this.mascotaObj.setNombre(this.mascotaObj.getNombre().trim().toUpperCase());
            this.mascotaServicio.insertar(this.mascotaObj);
            this.mesaSelec.setMascotas(this.mascotaObj);
            this.guardarMesa(this.mesaSelec);
        } catch (Exception ex) {
            Logger.getLogger(PedidoVentaMovilBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarMascotaNoAgregar(){
        try {
            this.mascotaObj.setPersona(this.pedidoVentaSelc.getCliente().getPersona());
            this.mascotaObj.setNombre(this.mascotaObj.getNombre().trim().toUpperCase());
            this.mascotaServicio.insertar(this.mascotaObj);
            this.pedidoVentaSelc.getCliente().getPersona().getMascotaPersonaList().add(this.mascotaObj);
        } catch (Exception ex) {
            Logger.getLogger(PedidoVentaMovilBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cambiarMesa(Mesa mesa,PedidoVenta pedido) {
        this.pedidoVentaSelc = pedido;
        this.mesaSelec = mesa;
    }
    
    public void guardarCambioMesa(Mesa mesa) {
        this.pedidoVentaSelc.setMesa(mesa);
        this.guardar(this.pedidoVentaSelc);
        mesa.getListaPedidosVenta().add(this.pedidoVentaSelc);
        this.init();
    }
    
    public void onTabChange(TabChangeEvent event) {
        for(GrupoProducto grupo : this.listaPadres){
            if(grupo.getNombre().equals(event.getTab().getTitle())){
                onNodeSelect(grupo);
                break;
            }
        }
    }
    
    public void filtrarTablas() {
        if(this.opcion.equals("P"))
        {
            this.llenarTabla();
        }
        if(this.opcion.equals("S"))
        {
            this.llenarTablaServicios();
        }
        if(this.opcion.equals("PA"))
        {
            this.llenarTablaPaquetes();
        }
    }
    
    public void onNodeSelect(GrupoProducto event) {
        this.grupoProductoSelc = event;
        if(this.grupoProductoSelc.getTipo() == 1)
        {
            this.llenarTabla();
            this.opcion = "P";
        }
        if(this.grupoProductoSelc.getTipo() == 2)
        {
            this.llenarTablaServicios();
            this.opcion = "S";
        }
        if(this.grupoProductoSelc.getTipo() == 3)
        {
            this.llenarTablaPaquetes();
            this.opcion = "PA";
        }
    }
    
    public void agregarNotaPedido() {
        try {
            this.mascotaNotaMedica.setFecha(new Date());
            this.medicaVeterinariaServicio.insertar(this.mascotaNotaMedica);
            this.mascotaSelc.getMascotaNotaMedicaList().add(this.mascotaNotaMedica);
            this.inicializarNM(this.pedidoVentaSelc);
//            FacesUtilsMovil.addInfoMessage(FacesUtilsMovil.getResourceBundle().getString("registroAgregado"));
        } catch (Exception e) {
           LOG.log(Level.SEVERE, "No se puede eliminar.", e);
           FacesUtilsMovil.addErrorMessage(FacesUtilsMovil.getResourceBundle().getString("errorCargarDatos"));
        }
    }
    
    public void sumar(FacturaDetalle event){
        event.setCantidad(event.getCantidad().add(BigDecimal.ONE));
        this.onCellEdit(event);
    }
    
    public void restar(FacturaDetalle event){
        if(event.getCantidad().floatValue() > 0){
            event.setCantidad(event.getCantidad().subtract(BigDecimal.ONE));
            this.onCellEdit(event);
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
    
    public Boolean generarReportePrint() throws IOException {
        Boolean print = Boolean.FALSE;
        if(this.pedidoVentaSelc.getCodigo() != null)
        {
            List<PuntoVenta> listaPuntos = new ArrayList<>();
            listaPuntos.addAll(this.verPuntoMovil());
            for(PuntoVenta punto : listaPuntos){
                List<FacturaDetalle> listaPrint = verificarRestriccion(this.listaProductosTodosSelc,punto);
                if(!listaPrint.isEmpty()){
                    if(punto.getTipoImpresora().equals("1")){
                        try {
                            super.parametros.put("empresa", this.pedidoVentaSelc.getEmpresa().getNombre());
                            super.parametros.put("mesa", this.pedidoVentaSelc.getMesa().getNombre());
                            super.parametros.put("fecha", this.pedidoVentaSelc.getFecha());
                            JasperReportUtilMovil jasperBean = (JasperReportUtilMovil) FacesUtilsMovil.getManagedBean(JasperReportUtilMovil.NOMBRE_BEAN);
                            if(jasperBean.jasperReportPrintBean(JasperReportUtilMovil.PATH_REPORTE_PEDIDO_BEAN,super.parametros,listaPrint,punto.getImpresora())){
                                print = Boolean.TRUE;
                            }
                            else{
                                print = Boolean.FALSE;
                            }
                        } catch (ClassNotFoundException ex) {
                            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
                            print = Boolean.FALSE;
                        }
                    }
                    else
                    {
                        if(this.imprimirTiket(listaPrint,punto)){
                            print = Boolean.TRUE;
                        }
                    }
                }
            }
            return print;
        }
        return print;
    }
    
    public Boolean imprimirTiket(List<FacturaDetalle> lista,PuntoVenta punto){
        String items = StringUtils.EMPTY;
        for(FacturaDetalle detalleTiket : lista){
            items = items + detalleTiket.getCantidad() + " " + detalleTiket.getProductoServicio().getNombre() + "\n" + detalleTiket.getDescripcion() + "\n";
        }
        TicketPedido ticket = new TicketPedido(this.empresa.getNombre(), 
                                               this.empresa.getCiudad().getNombre() + "," + Fecha.formatoDateTimeToStringF0(this.pedidoVentaSelc.getFecha()),
                                               this.pedidoVentaSelc.getMesa().getNombre(),
                                               this.pedidoVentaSelc.getCliente().getPersona().getNombres() + " " + this.pedidoVentaSelc.getCliente().getPersona().getApellidos(),
                                               items);
        if(ticket.print(punto.getImpresora())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
    
    public void onMascotaSelec(Mascota parametro){
        this.mascotaSelc = parametro;
        if(this.mascotaSelc.getMascotaNotaMedicaList() != null && !this.mascotaSelc.getMascotaNotaMedicaList().isEmpty()){
            if(this.mascotaSelc.getMascotaNotaMedicaList().size() > 10){
                List<MascotaNotaMedica> listaTmp = new ArrayList<>();
                listaTmp.addAll(this.mascotaSelc.getMascotaNotaMedicaList());
                this.mascotaSelc.getMascotaNotaMedicaList().clear();
                for(int i = 0;i<=10;i++){
                    this.mascotaSelc.getMascotaNotaMedicaList().add(listaTmp.get(i));
                }
            }
            this.inicializarNM(this.pedidoVentaSelc);
            this.buscarNotasMedicas(this.mascotaSelc);
        }
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public List<ProductoStock> getListaProductosStockSelc() {
        return listaProductosStockSelc;
    }

    public void setListaProductosStockSelc(List<ProductoStock> listaProductosStockSelc) {
        this.listaProductosStockSelc = listaProductosStockSelc;
    }
    
    public LazyDataModel<ProductoStock> getLazyModelStock() {
        return lazyModelStock;
    }

    public void setLazyModelStock(LazyDataModel<ProductoStock> lazyModelStock) {
        this.lazyModelStock = lazyModelStock;
    }
    
    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LazyDataModel<Mascota> getLazyModelMascotas() {
        return lazyModelMascotas;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public List<Mascota> getListaMascotasSelct() {
        return listaMascotasSelct;
    }

    public void setListaMascotasSelct(List<Mascota> listaMascotasSelct) {
        this.listaMascotasSelct = listaMascotasSelct;
    }

    public Mascota getMascotaObj() {
        return mascotaObj;
    }

    public void setMascotaObj(Mascota mascotaObj) {
        this.mascotaObj = mascotaObj;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public List<GrupoProducto> getListaPadres() {
        return listaPadres;
    }

    public void setListaPadres(List<GrupoProducto> listaPadres) {
        this.listaPadres = listaPadres;
    }

    public List<FacturaDetalle> getListaProductosTodosSelc() {
        return listaProductosTodosSelc;
    }

    public void setListaProductosTodosSelc(List<FacturaDetalle> listaProductosTodosSelc) {
        this.listaProductosTodosSelc = listaProductosTodosSelc;
    }
    
    public LazyDataModel<ProductoServicio> getLazyModelServicios() {
        return lazyModelServicios;
    }

    public LazyDataModel<ProductoPaquete> getLazyModelPaquetes() {
        return lazyModelPaquetes;
    }

    public MascotaNotaMedica getMascotaNotaMedica() {
        return mascotaNotaMedica;
    }

    public void setMascotaNotaMedica(MascotaNotaMedica mascotaNotaMedica) {
        this.mascotaNotaMedica = mascotaNotaMedica;
    }

    public PedidoVenta getPedidoVentaSelc() {
        return pedidoVentaSelc;
    }

    public void setPedidoVentaSelc(PedidoVenta pedidoVentaSelc) {
        this.pedidoVentaSelc = pedidoVentaSelc;
    }

    public List<MascotaNotaMedica> getListaNotasMedicas() {
        return listaNotasMedicas;
    }

    public void setListaNotasMedicas(List<MascotaNotaMedica> listaNotasMedicas) {
        this.listaNotasMedicas = listaNotasMedicas;
    }

    public Mascota getMascotaSelc() {
        return mascotaSelc;
    }

    public void setMascotaSelc(Mascota mascotaSelc) {
        this.mascotaSelc = mascotaSelc;
    }
}
