package com.jvc.factunet.bean;

import com.jvc.factunet.bean.application.ParametrosApplicationPedidos;
import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Canton;
import com.jvc.factunet.entidades.Ciudad;
import com.jvc.factunet.entidades.Controles;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.entidades.Parroquia;
import com.jvc.factunet.entidades.Provincia;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.Seccion;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import com.jvc.factunet.entidades.TipoEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.CantonServicio;
import com.jvc.factunet.servicios.CiudadServicio;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.EmpresaServicio;
import com.jvc.factunet.servicios.ParroquiaServicio;
import com.jvc.factunet.servicios.ProvinciaServicio;
import com.jvc.factunet.servicios.TipoEmpresaServicio;
import com.jvc.factunet.session.LoginPedidos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@ViewScoped
public class EmpresaPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(EmpresaPedidosBean .class.getName());

    @EJB
    private EmpresaServicio empresaServicio;
    @EJB
    private CiudadServicio ciudadServicio;
    @EJB
    private CuentaServicio cuentaServicio;
    @EJB
    private BodegaServicio bodegaServicio;
    @EJB
    private CantonServicio servicioCantones;
    @EJB
    private ParroquiaServicio parroquiaDAO;
    @EJB
    private ProvinciaServicio servicioProvincias;
    @EJB
    private TipoEmpresaServicio tipoEmpresaServicio;
    @Inject
    private ParametrosApplicationPedidos parametrosApplication;
    
    private Empresa empresa;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private List<Ciudad> listaCiudad;
    private List<TipoEmpresa> listaTipoEmpresa;
    
    private MapModel simpleModel;
    private Marker marker;
    private Integer zoom;
    
    private Provincia provincia;
    private Canton canton;
    
    private List<Provincia> listaProvincias;
    private List<Canton> listaCantones;
    private List<Parroquia> listaParroquias;
    
    public EmpresaPedidosBean() {
        this.listaCiudad = new ArrayList<>();
        this.listaProvincias = new ArrayList<>();
        this.listaCantones = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
        this.listaTipoEmpresa = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaTipoEmpresa.addAll(this.tipoEmpresaServicio.listarPorNivel(2));
        this.listaCiudad.addAll(this.ciudadServicio.listar());
        FileOutputStream fos = null;
        try {
            if(((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta() != null)
                this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
             
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathL = Paths.get(this.pathLogo);
            if (!Files.exists(pathL)) {
                Files.createDirectory(pathL);
            }
            this.listaProvincias = servicioProvincias.listarProvincias();
            if(this.empresa != null)
            {
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.empresa.getLogoImagen());
                this.urlLogo = "/temp/" + this.nombreLogo;
                provincia = this.empresa.getParroquia().getCanton().getProvincia();
                canton = this.empresa.getParroquia().getCanton();
                this.llenarCantones();
                this.llenarParroquias();
                this.crearMarker(4);
            } else {
                this.empresa = new Empresa();
                this.nombreLogo = "logo.png";
                this.pathLogo = getServletContext().getRealPath("/") + "resources" + File.separator + "imagenes";
                this.urlLogo = "/resources/imagenes/" + this.nombreLogo;
                this.crearMarker(5);
                this.provincia = null;
                this.canton = null;
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void crearCuenta(Empresa cliente){
        try {
            this.cuentaServicio.crearCuenta(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ClientePedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardar() {
        try {
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.empresa.setLogoImagen(foto);
            this.empresa.setDecimales(2); 
            this.empresa.setCodigoSri("001"); 
            this.empresa.setMetodoCosto("2"); 
            this.empresa.setGenerarNombreProducto("1");
            this.empresa.setClienteCompleto("1"); 
            if (this.empresa.getCodigo() == null) {
                this.crearSeccionMesaPedidos();
                this.empresaServicio.insertar(this.empresa);
            } else {
                this.empresaServicio.actualizar(this.empresa);
            }
            Cuenta cuenta = this.cuentaServicio.buscar(this.empresa.getRuc());
            if(cuenta == null){
                this.crearCuenta(this.empresa);
            }
            Bodega bodega = this.bodegaServicio.bodegaPrincipal(this.empresa.getCodigo());
            if(bodega == null){
                this.crearBodega(this.empresa); 
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void crearSeccionMesaPedidos(){
        Seccion seccion = new Seccion();
        seccion.setEmpresa(this.empresa);
        seccion.setColumnas(1);
        seccion.setDetalle(this.empresa.getNombreAbreviado() + " " + "Pedidos");
        seccion.setNombre(this.empresa.getNombreAbreviado() + " " + "Pedidos"); 
        
        Mesa mesa = new Mesa();
        mesa.setSeccion(seccion);
        mesa.setSillas(1);
        mesa.setNombre(this.empresa.getNombreAbreviado() + " " + "Pedidos"); 
        mesa.setDetalle(this.empresa.getNombreAbreviado() + " " + "Pedidos"); 
        
        seccion.setMesaList(new ArrayList<>());
        seccion.getMesaList().add(mesa);
        
        this.empresa.setSeccionList(new ArrayList<>());
        this.empresa.getSeccionList().add(seccion);
    }
    
    public void crearBodega(Empresa empresa){
        try {
            Bodega bodega = new Bodega();
            bodega.setNombre("Principal");
            bodega.setUbicacion(empresa.getDireccion());
            bodega.setCiudad(empresa.getCiudad());
            bodega.setEmpresa(empresa);
            bodega.setNivel(1);
            bodega.setSiglas("B1");
            this.bodegaServicio.insertar(bodega);
        } catch (Exception ex) {
            Logger.getLogger(EmpresaPedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirLogo(FileUploadEvent event) {
        OutputStream os = null;
        InputStream is = null;
        try {
            this.nombreLogo = (new Date()).getTime() + ".jpg";
            this.pathLogo = getServletContext().getRealPath("/temp");
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            is = event.getFile().getInputstream();
            os = Files.newOutputStream(path);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            this.urlLogo = "/temp/" + this.nombreLogo;
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir la imagen");
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void verPuntoVenta(PuntoVenta punto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("punto", punto);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/seguridades/opcionesEmpresa/nuevoPuntoVentaDialog", options, null);
    }
    
    public void onPuntoVentaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            PuntoVenta punto = (PuntoVenta) event.getObject();
            this.empresa.getPuntoVentaList().add(punto);
        }
    }
    
    public void eliminarPunto(PuntoVenta parametro) {
        try {
            this.empresa.getPuntoVentaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarControl(Controles parametro) {
        try {
            this.empresa.getControlesList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarTarjeta(TarjetaEmpresa parametro) {
        try {
            this.empresa.getTarjetaEmpresaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarSeccion(Seccion parametro) {
        try {
            this.empresa.getSeccionList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void verControles(Controles control) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("control", control);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("height", 350);
        options.put("width", 500);
        options.put("contentHeight", 350);
        options.put("contentWidth", 500);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/seguridades/opcionesEmpresa/nuevoControlDialog", options, null);
    }
    
    public void onControlSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Controles control = (Controles) event.getObject();
            if(this.empresa.getControlesList() == null)
            {
                this.empresa.setControlesList(new ArrayList<Controles>());
            }
            if((!this.empresa.getControlesList().contains(control)) && (control != null))
            {
                control.setEmpresa(this.empresa);
                this.empresa.getControlesList().add(control);
            }
        }
    }
    
    public void verTarjeta(TarjetaEmpresa tarjeta) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tarjeta", tarjeta);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("height", 450);
        options.put("width", 900);
        options.put("contentHeight", 450);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/seguridades/opcionesEmpresa/nuevoTarjetaDialog", options, null);
    }
    
    public void onTarjetaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            TarjetaEmpresa tarjeta = (TarjetaEmpresa) event.getObject();
            if(this.empresa.getTarjetaEmpresaList() == null)
            {
                this.empresa.setTarjetaEmpresaList(new ArrayList<TarjetaEmpresa>());
            }
            if((!this.empresa.getTarjetaEmpresaList().contains(tarjeta)) && (tarjeta != null))
            {
                tarjeta.setEmpresa(this.empresa);
                this.empresa.getTarjetaEmpresaList().add(tarjeta);
            }
        }
    }
    
    public void verSeccion(Seccion seccion) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("seccion", seccion);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("height", 450);
        options.put("width", 900);
        options.put("contentHeight", 450);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/seguridades/opcionesEmpresa/nuevoSeccionDialog", options, null);
    }
    
    public void onSeccionSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Seccion seccion= (Seccion) event.getObject();
            if(this.empresa.getSeccionList() == null)
            {
                this.empresa.setSeccionList(new ArrayList<Seccion>());
            }
            if((!this.empresa.getSeccionList().contains(seccion)) && (seccion != null))
            {
                seccion.setEmpresa(this.empresa);
                this.empresa.getSeccionList().add(seccion);
            }
        }
    }
    
    public void refreshCiudad()
    {
        this.listaCiudad.clear();
        this.listaCiudad.addAll(this.ciudadServicio.listar());
    }
    
    public void actualizarProvincia(AjaxBehaviorEvent event) {
        this.llenarCantones();
        this.empresa.setParroquia(null);
        canton = null;
        listaParroquias.clear();
        crearMarker(1);
    }

    public void actualizarCanton(AjaxBehaviorEvent event) {
        this.llenarParroquias();
        this.empresa.setParroquia(null);
        crearMarker(2);
    }

    public void actualizarParroquia(AjaxBehaviorEvent event) {
        crearMarker(3);
    }
    
    private void llenarCantones() {
        this.listaCantones.clear();
        this.listaCantones = servicioCantones.listarCantonesPorProvincia(provincia.getId());
    }

    private void llenarParroquias() {
        this.listaParroquias.clear();
        this.listaParroquias = parroquiaDAO.listarParroquiasPorCanton(provincia.getId(), canton.getCantonPK().getId());
    }
    
    private void crearMarker(int tipoSeleccion) {

        if (tipoSeleccion == 1 || tipoSeleccion == 2 || tipoSeleccion == 3) {
            if (this.empresa.getParroquia() != null && this.empresa.getParroquia().getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(this.empresa.getParroquia().getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(this.empresa.getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(canton.getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                this.empresa.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                this.empresa.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else if (tipoSeleccion == 4) {
            if (this.empresa.getCoordenadaLatitud() != null) {
                zoom = parametrosApplication.getZoomDireccion();
            } else if (this.empresa.getParroquia() != null && this.empresa.getParroquia().getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(this.empresa.getParroquia().getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(this.empresa.getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(canton.getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                this.empresa.setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                this.empresa.setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                this.empresa.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                this.empresa.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else {
            this.empresa.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
            this.empresa.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
            zoom = parametrosApplication.getZoomPais();
        }
        simpleModel = new DefaultMapModel();
        marker = new Marker(new LatLng(this.empresa.getCoordenadaLatitud().doubleValue(), this.empresa.getCoordenadaLongitud().doubleValue()));
        marker.setDraggable(true);
        simpleModel.addOverlay(marker);
    }
    
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        this.empresa.setCoordenadaLatitud(new BigDecimal(marker.getLatlng().getLat()));
        this.empresa.setCoordenadaLongitud(new BigDecimal(marker.getLatlng().getLng()));
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public List<Ciudad> getListaCiudad() {
        return listaCiudad;
    }

    public void setListaCiudad(List<Ciudad> listaCiudad) {
        this.listaCiudad = listaCiudad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List<Canton> getListaCantones() {
        return listaCantones;
    }

    public void setListaCantones(List<Canton> listaCantones) {
        this.listaCantones = listaCantones;
    }

    public List<Parroquia> getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List<Parroquia> listaParroquias) {
        this.listaParroquias = listaParroquias;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void setSimpleModel(MapModel simpleModel) {
        this.simpleModel = simpleModel;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public List<TipoEmpresa> getListaTipoEmpresa() {
        return listaTipoEmpresa;
    }

    public void setListaTipoEmpresa(List<TipoEmpresa> listaTipoEmpresa) {
        this.listaTipoEmpresa = listaTipoEmpresa;
    }
}
