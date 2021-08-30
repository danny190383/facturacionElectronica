package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.Cargo;
import com.jvc.factunet.entidades.Ciudad;
import com.jvc.factunet.entidades.EstadoCivil;
import com.jvc.factunet.entidades.Sexo;
import com.jvc.factunet.entidades.TipoCliente;
import com.jvc.factunet.entidades.TipoIdentificacion;
import com.jvc.factunet.servicios.CargoServicio;
import com.jvc.factunet.servicios.CiudadServicio;
import com.jvc.factunet.servicios.EstadoCivilServicio;
import com.jvc.factunet.servicios.SexoServicio;
import com.jvc.factunet.servicios.TipoClienteServicio;
import com.jvc.factunet.servicios.TipoIdentificacionServicio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

@ManagedBean
@ApplicationScoped
public class CatalogosPersonaBean extends ImprimirReportesBean {

    @EJB
    public CiudadServicio ciudadServicio;
    @EJB
    private EstadoCivilServicio estadoCivilServicio;
    @EJB
    private SexoServicio sexoServicio;
    @EJB
    private TipoClienteServicio tipoClienteServicio;
    @EJB
    private CargoServicio cargoServicio;
    @EJB
    private TipoIdentificacionServicio tipoIdentificacionServicio;
    
    private List<TipoCliente> listaTipoCliente;
    private List<Ciudad> listaCiudad;
    private List<EstadoCivil> listaEstadoCivil;
    private List<Sexo> listaSexo;
    private List<Cargo> listaCargo;
    private List<TipoIdentificacion> listaTipoIdentificacion;
    
    public CatalogosPersonaBean() {
        this.listaTipoCliente = new ArrayList<>();
        this.listaCiudad = new ArrayList<>();
        this.listaEstadoCivil = new ArrayList<>();
        this.listaSexo = new ArrayList<>();
        this.listaCargo = new ArrayList<>();
        this.listaTipoIdentificacion = new ArrayList<>();
    }
    
    @PostConstruct
    public void initCatalogos()
    {
        this.listaTipoCliente.addAll(this.tipoClienteServicio.listar());
        this.listaCiudad.addAll(this.ciudadServicio.listar());
        this.listaEstadoCivil.addAll(this.estadoCivilServicio.listar());
        this.listaSexo.addAll(this.sexoServicio.listar());
        this.listaCargo.addAll(this.cargoServicio.listar());
        this.listaTipoIdentificacion.addAll(this.tipoIdentificacionServicio.listar());
    }
    
    public void refreshTipoCliente()
    {
        this.listaTipoCliente.clear();
        this.listaTipoCliente.addAll(this.tipoClienteServicio.listar());
    }
    
    public void refreshCiudad()
    {
        this.listaCiudad.clear();
        this.listaCiudad.addAll(this.ciudadServicio.listar());
    }
    
    public void refreshEstadoCivil()
    {
        this.listaEstadoCivil.clear();
        this.listaEstadoCivil.addAll(this.estadoCivilServicio.listar());
    }
    
    public void refreshSexo()
    {
        this.listaSexo.clear();
        this.listaSexo.addAll(this.sexoServicio.listar());
    }
    
    public void refreshCargo()
    {
        this.listaCargo.clear();
        this.listaCargo.addAll(this.cargoServicio.listar());
    }

    public List<TipoCliente> getListaTipoCliente() {
        return listaTipoCliente;
    }

    public void setListaTipoCliente(List<TipoCliente> listaTipoCliente) {
        this.listaTipoCliente = listaTipoCliente;
    }

    public List<Ciudad> getListaCiudad() {
        this.refreshCiudad();
        return listaCiudad;
    }

    public void setListaCiudad(List<Ciudad> listaCiudad) {
        this.listaCiudad = listaCiudad;
    }

    public List<EstadoCivil> getListaEstadoCivil() {
        return listaEstadoCivil;
    }

    public void setListaEstadoCivil(List<EstadoCivil> listaEstadoCivil) {
        this.listaEstadoCivil = listaEstadoCivil;
    }

    public List<Sexo> getListaSexo() {
        return listaSexo;
    }

    public void setListaSexo(List<Sexo> listaSexo) {
        this.listaSexo = listaSexo;
    }

    public List<Cargo> getListaCargo() {
        return listaCargo;
    }

    public void setListaCargo(List<Cargo> listaCargo) {
        this.listaCargo = listaCargo;
    }

    public List<TipoIdentificacion> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<TipoIdentificacion> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }
}
