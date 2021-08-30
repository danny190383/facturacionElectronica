package com.jvc.factunet.bean;

import com.jvc.factunet.bean.application.ParametrosApplicationPedidos;
import com.jvc.factunet.entidades.Canton;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Parroquia;
import com.jvc.factunet.entidades.Provincia;
import com.jvc.factunet.entidades.TipoEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.servicios.CantonServicio;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.EmpresaServicio;
import com.jvc.factunet.servicios.ParroquiaServicio;
import com.jvc.factunet.servicios.ProvinciaServicio;
import com.jvc.factunet.servicios.TipoEmpresaServicio;
import com.jvc.factunet.session.LoginPedidos;
import com.jvc.factunet.session.SessionPedidosBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

@Named(value = "loginBean")
@ViewScoped
public class LoginPedidosBean implements Serializable{
    
    @EJB
    private ProvinciaServicio servicioProvincias;
    @EJB
    private CantonServicio servicioCantones;
    @EJB
    private ParroquiaServicio parroquiaDAO;
    @EJB
    private TipoEmpresaServicio tipoEmpresaServicio;
    @EJB
    private CuentaServicio cuentaServicio;
    @EJB
    private EmpresaServicio empresaServicio;
    @Inject
    private ParametrosApplicationPedidos parametrosApplication;
    
    private String username;
    private String password;
    private TipoEmpresa tipoEmpresa;
    private MapModel simpleModel;
    private Marker marker;
    private Integer zoom;
    private BigDecimal coordenadaLatitud;
    private BigDecimal coordenadaLongitud;
    
    private Provincia provincia;
    private Canton canton;
    private Parroquia parroquia;
    
    private List<Provincia> listaProvincias;
    private List<Canton> listaCantones;
    private List<Parroquia> listaParroquias;
    private List<Empresa> listaHospitales;
    private Empresa empresaSlc;
    
    private LoginPedidos login;
    private MenuModel menuModel;
    
    public LoginPedidosBean() {
        this.listaHospitales = new ArrayList<>();
        this.listaProvincias = new ArrayList<>();
        this.listaCantones = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
        this.tipoEmpresa = new TipoEmpresa();
    }
    
    @PostConstruct
    public void init() {
        this.listaProvincias = servicioProvincias.listarProvincias();
        this.crearMarker(5);
        this.crearMenu(); 
        this.login = (LoginPedidos) FacesUtils.getManagedBean("login");
    }
    
    public void validateUsernamePassword() {
        this.login.cuenta = this.cuentaServicio.validar(this.username, this.password);
        if (this.login.cuenta != null) {
            HttpSession session = SessionPedidosBean.getSession();
            session.setAttribute("username", this.login.cuenta.getIdentificador());
            session.setAttribute("cuenta", this.login.cuenta);
            if(this.login.cuenta.getEmpresa() != null)
            {
                this.login.setTipo(1);
                this.login.cargarLogo();
                this.login.setNombreLogin(this.login.getCuenta().getEmpresa().getNombre()); 
            }
            if(this.login.cuenta.getCliente() != null)
            {
                this.login.setTipo(2);
                this.login.cargarFotoCliente();
                this.login.setNombreLogin(this.login.getCuenta().getCliente().getPersona().getNombres() + " " + this.login.getCuenta().getCliente().getPersona().getApellidos()); 
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "Incorrect Username and Passowrd",
                                        "Please enter correct username and Password"));
        }
    }
    
    public void actualizarProvincia(AjaxBehaviorEvent event) {
        this.llenarCantones();
        canton = null;
        parroquia = null;
        crearMarker(1);
    }

    public void actualizarCanton(AjaxBehaviorEvent event) {
        this.llenarParroquias();
        parroquia = null;
        crearMarker(2);
    }

    public void actualizarParroquia(AjaxBehaviorEvent event) {
        crearMarker(3);
    }
    
    private void crearMarker(int tipoSeleccion) {

        if (tipoSeleccion == 1 || tipoSeleccion == 2 || tipoSeleccion == 3) {
            if (this.getParroquia() != null && this.getParroquia().getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(this.getParroquia().getCoordenadaLatitud());
                this.setCoordenadaLongitud(this.getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(canton.getCoordenadaLatitud());
                this.setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                this.setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                this.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                this.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else if (tipoSeleccion == 4) {
            if (this.getCoordenadaLatitud() != null) {
                zoom = parametrosApplication.getZoomDireccion();
            } else if (this.getParroquia() != null && this.getParroquia().getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(this.getParroquia().getCoordenadaLatitud());
                this.setCoordenadaLongitud(this.getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(canton.getCoordenadaLatitud());
                this.setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                this.setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                this.setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                this.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                this.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else {
            this.setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
            this.setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
            zoom = parametrosApplication.getZoomPais();
        }
        simpleModel = new DefaultMapModel();
        marker = new Marker(new LatLng(this.getCoordenadaLatitud().doubleValue(), this.getCoordenadaLongitud().doubleValue()));
        marker.setDraggable(true);
        simpleModel.addOverlay(marker);
        this.cargarEmpresasGmap();
    }
    
    private void llenarCantones() {
        this.listaCantones.clear();
        if(provincia == null){
         this.canton = null;
         this.listaCantones.clear();
         this.listaParroquias.clear();
        }else{
            this.listaCantones = servicioCantones.listarCantonesPorProvincia(provincia.getId());
        }
    }

    private void llenarParroquias() {
        this.listaParroquias.clear();
        if(canton == null){
            this.parroquia = null;
            this.listaParroquias.clear();
        }else{
            this.listaParroquias = parroquiaDAO.listarParroquiasPorCanton(provincia.getId(), canton.getCantonPK().getId());
        }
    }
    
    public void verEmpresaGmap() {
        if(this.empresaSlc != null){
            zoom = parametrosApplication.getZoomDireccion();
            simpleModel = new DefaultMapModel();
            marker = new Marker(new LatLng(empresaSlc.getCoordenadaLatitud().doubleValue(), empresaSlc.getCoordenadaLongitud().doubleValue()));
            marker.setDraggable(false);
            marker.setTitle(empresaSlc.getNombre()); 
            marker.setData(empresaSlc.getLogoImagen()); 
            marker.setIcon("http://maps.google.com/mapfiles/kml/paddle/red-stars.png"); 
            simpleModel.addOverlay(marker);
            this.coordenadaLatitud = this.empresaSlc.getCoordenadaLatitud();
            this.coordenadaLongitud = this.empresaSlc.getCoordenadaLongitud();
            this.provincia = this.empresaSlc.getParroquia().getCanton().getProvincia();
            this.canton = this.empresaSlc.getParroquia().getCanton();
            this.parroquia = this.empresaSlc.getParroquia();
            this.llenarCantones();
            this.llenarParroquias();
            this.tipoEmpresa = this.empresaSlc.getTipoEmpresaWeb();
        }
        else
        {
            this.crearMarker(5); 
        }
    }
    
    public void crearMenu() {
        this.menuModel = new DefaultMenuModel();
        try {
            List<TipoEmpresa> listaMenu = this.tipoEmpresaServicio.listarPorNivel(1);
            for (TipoEmpresa opcion : listaMenu) 
            {
                DefaultSubMenu submenu = new DefaultSubMenu("Dynamic Submenu");
                submenu.setLabel(opcion.getNombre());
                submenu.setExpanded(true);
                this.populateMenu(opcion, submenu, opcion.getTipoEmpresaList());
                this.menuModel.addElement(submenu);
            }
        } catch (Exception e) {
            
        }
    }
    
    public void verEmpresas(ActionEvent event){
        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
        this.tipoEmpresa = this.tipoEmpresaServicio.buscarNombre(menuItem.getValue().toString());
        this.cargarEmpresasGmap();
        FacesUtils.addInfoMessage(menuItem.getValue().toString());
    }
    
    public void populateMenu(TipoEmpresa opcionPadre, DefaultSubMenu menuPadre, List<TipoEmpresa> lista) {
        for (TipoEmpresa opcion : lista) 
        {
            DefaultMenuItem itemHijo = new DefaultMenuItem();
            itemHijo.setValue(opcion.getNombre());
            itemHijo.setStyle("width:150px");
            itemHijo.setCommand("#{loginBean.verEmpresas}");
            itemHijo.setPartialSubmit(true);
            itemHijo.setProcess("@this"); 
            itemHijo.setUpdate("frmCabecera, grMensajes");
            menuPadre.getElements().add(itemHijo);
        }
    }
    
    public void verNuevoCliente() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/creacion/nuevoClienteDialog", options, null);
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Cliente cliente = (Cliente) event.getObject();  
        }
    }
    
    public void verNuevoEmpresa() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/creacion/nuevoEmpresaDialog", options, null);
    }
    
    public void cargarEmpresasGmap() {
        simpleModel = new DefaultMapModel();
        this.listaHospitales.clear();
        if(provincia == null && canton == null && parroquia == null)
            this.listaHospitales = this.empresaServicio.listar(this.tipoEmpresa.getNombre());
        if(provincia != null && canton == null && parroquia == null)
            this.listaHospitales = this.empresaServicio.listarPorProvincia(provincia.getId(),this.tipoEmpresa.getNombre());
        if(provincia != null && canton != null && parroquia == null)
            this.listaHospitales = this.empresaServicio.listarPorCanton(canton.getCantonPK(),this.tipoEmpresa.getNombre());
        if(provincia != null && canton != null && parroquia != null)
            this.listaHospitales = this.empresaServicio.listarPorParroquia(parroquia.getParroquiaPK(),this.tipoEmpresa.getNombre());
        
        for(Empresa hospital : this.listaHospitales){
            if (hospital.getCoordenadaLatitud() != null) {
                marker = new Marker(new LatLng(hospital.getCoordenadaLatitud().doubleValue(), hospital.getCoordenadaLongitud().doubleValue()));
                marker.setDraggable(false);
                marker.setTitle(hospital.getNombre()); 
                marker.setData(hospital.getLogoImagen()); 
                marker.setIcon("http://maps.google.com/mapfiles/kml/paddle/red-stars.png"); 
                simpleModel.addOverlay(marker);
            }
        }
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        this.cargarFotoMarker();
    }
    
    public void cargarFotoMarker()
    {
        FileOutputStream fosC = null;
        try {
            String pathLogoC = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathLC = Paths.get(pathLogoC);
            if (!Files.exists(pathLC)) {
                Files.createDirectory(pathLC);
            }
            String nombreFotoC;
            byte[] imagen = (byte[]) marker.getData();
            nombreFotoC = marker.getTitle();
            fosC = new FileOutputStream(pathLogoC + File.separator + nombreFotoC);
            fosC.write(imagen);
            marker.setCursor("/temp/" + nombreFotoC);
            
        } catch (Exception e) {
            } finally {
            try {
                fosC.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void onEmpresaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Empresa empresa = (Empresa) event.getObject();  
        }
    }
    
    public void cargarInventario() {
        FacesUtils.redireccionar("/faces/inventario/productosAdmin.xhtml");
    }
    
    public void cargarPedidos() {
        if(this.login.getCuenta() == null){
            FacesUtils.addInfoMessage("Ingrese al sistema primero por favor");
        }else{
            if(this.login.getCuenta().getCliente() != null){
                this.login.getCuenta().getCliente().setEmpresaPedidoSlc(this.empresaSlc); 
            }
            FacesUtils.redireccionar("/faces/transacciones/pedidoVentaAdmin.xhtml");
        }
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
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarPedidosVentaDialog", options, null);
    }

    public BigDecimal getCoordenadaLatitud() {
        return coordenadaLatitud;
    }

    public void setCoordenadaLatitud(BigDecimal coordenadaLatitud) {
        this.coordenadaLatitud = coordenadaLatitud;
    }

    public BigDecimal getCoordenadaLongitud() {
        return coordenadaLongitud;
    }

    public void setCoordenadaLongitud(BigDecimal coordenadaLongitud) {
        this.coordenadaLongitud = coordenadaLongitud;
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

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
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

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void setSimpleModel(MapModel simpleModel) {
        this.simpleModel = simpleModel;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public TipoEmpresa getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public List<Empresa> getListaHospitales() {
        return listaHospitales;
    }

    public void setListaHospitales(List<Empresa> listaHospitales) {
        this.listaHospitales = listaHospitales;
    }

    public Empresa getEmpresaSlc() {
        return empresaSlc;
    }

    public void setEmpresaSlc(Empresa empresaSlc) {
        this.empresaSlc = empresaSlc;
    }
}
