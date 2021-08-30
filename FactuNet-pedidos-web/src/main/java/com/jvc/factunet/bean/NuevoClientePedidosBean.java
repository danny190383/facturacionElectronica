package com.jvc.factunet.bean;

import com.jvc.factunet.bean.application.ParametrosApplicationPedidos;
import com.jvc.factunet.entidades.Canton;
import com.jvc.factunet.entidades.Parroquia;
import com.jvc.factunet.entidades.Provincia;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CantonServicio;
import com.jvc.factunet.servicios.ParroquiaServicio;
import com.jvc.factunet.servicios.ProvinciaServicio;
import com.jvc.factunet.session.LoginPedidos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@ViewScoped
public class NuevoClientePedidosBean extends ClientePedidosBean implements Serializable{
    
    @EJB
    private CantonServicio servicioCantones;
    @EJB
    private ParroquiaServicio parroquiaDAO;
    @EJB
    private ProvinciaServicio servicioProvincias;
    @Inject
    private ParametrosApplicationPedidos parametrosApplication;
    
    private MapModel simpleModel;
    private Marker marker;
    private Integer zoom;
    
    private Provincia provincia;
    private Canton canton;
    
    private List<Provincia> listaProvincias;
    private List<Canton> listaCantones;
    private List<Parroquia> listaParroquias;
    
    public NuevoClientePedidosBean() {
        this.listaProvincias = new ArrayList<>();
        this.listaCantones = new ArrayList<>();
        this.listaParroquias = new ArrayList<>();
    }
    
    @PostConstruct
    public void initNuevo(){
        this.listaProvincias = servicioProvincias.listarProvincias();
        if(((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta() != null){
            if(((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getCliente() != null){
                super.setCliente(((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getCliente());
                provincia = super.getCliente().getPersona().getParroquia().getCanton().getProvincia();
                canton = super.getCliente().getPersona().getParroquia().getCanton();
                this.llenarCantones();
                this.llenarParroquias();
                this.crearMarker(4);
            }
            else
            {
                super.nuevoCliente();
                this.crearMarker(5);
                this.provincia = null;
                this.canton = null;
            }
        }
        else
        {
            super.nuevoCliente();
            this.crearMarker(5);
            this.provincia = null;
            this.canton = null;
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(super.guardar()){
            if(super.getCliente().getPersona().getMascotaPersonaList() != null && !super.getCliente().getPersona().getMascotaPersonaList().isEmpty()){
                super.getCliente().setMascota(super.getCliente().getPersona().getMascotaPersonaList().get(0));
            }
            PrimeFaces.current().dialog().closeDynamic(super.getCliente());
        }
    }
    
    public void actualizarProvincia(AjaxBehaviorEvent event) {
        this.llenarCantones();
        super.getCliente().getPersona().setParroquia(null);
        canton = null;
        listaParroquias.clear();
        crearMarker(1);
    }

    public void actualizarCanton(AjaxBehaviorEvent event) {
        this.llenarParroquias();
        super.getCliente().getPersona().setParroquia(null);
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
            if (super.getCliente().getPersona().getParroquia() != null && super.getCliente().getPersona().getParroquia().getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(super.getCliente().getPersona().getParroquia().getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(super.getCliente().getPersona().getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(canton.getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                super.getCliente().getPersona().setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                super.getCliente().getPersona().setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else if (tipoSeleccion == 4) {
            if (super.getCliente().getPersona().getCoordenadaLatitud() != null) {
                zoom = parametrosApplication.getZoomDireccion();
            } else if (super.getCliente().getPersona().getParroquia() != null && super.getCliente().getPersona().getParroquia().getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(super.getCliente().getPersona().getParroquia().getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(super.getCliente().getPersona().getParroquia().getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomParroquia();
            } else if (canton != null && canton.getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(canton.getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(canton.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomCanton();
            } else if (provincia != null && provincia.getCoordenadaLatitud() != null) {
                super.getCliente().getPersona().setCoordenadaLatitud(provincia.getCoordenadaLatitud());
                super.getCliente().getPersona().setCoordenadaLongitud(provincia.getCoordenadaLongitud());
                zoom = parametrosApplication.getZoomProvincia();
            } else {
                super.getCliente().getPersona().setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
                super.getCliente().getPersona().setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
                zoom = parametrosApplication.getZoomPais();
            }
        } else {
            super.getCliente().getPersona().setCoordenadaLatitud(parametrosApplication.getCoordenadaLatitudEcuador());
            super.getCliente().getPersona().setCoordenadaLongitud(parametrosApplication.getCoordenadaLongitudEcuador());
            zoom = parametrosApplication.getZoomPais();
        }
        simpleModel = new DefaultMapModel();
        marker = new Marker(new LatLng(super.getCliente().getPersona().getCoordenadaLatitud().doubleValue(), super.getCliente().getPersona().getCoordenadaLongitud().doubleValue()));
        marker.setDraggable(true);
        simpleModel.addOverlay(marker);
    }
    
    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        super.getCliente().getPersona().setCoordenadaLatitud(new BigDecimal(marker.getLatlng().getLat()));
        super.getCliente().getPersona().setCoordenadaLongitud(new BigDecimal(marker.getLatlng().getLng()));
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
}
