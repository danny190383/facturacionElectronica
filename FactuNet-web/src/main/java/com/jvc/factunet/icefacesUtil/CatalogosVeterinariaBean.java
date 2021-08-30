package com.jvc.factunet.icefacesUtil;

import com.jvc.factunet.entidades.EspecieMascota;
import com.jvc.factunet.entidades.RazaMascota;
import com.jvc.factunet.entidades.SexoMascota;
import com.jvc.factunet.servicios.EspecieMascotaServicio;
import com.jvc.factunet.servicios.RazaMascotaServicio;
import com.jvc.factunet.servicios.SexoMascotaServicio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class CatalogosVeterinariaBean {
    
    @EJB
    public EspecieMascotaServicio especieMascotaServicio;
    @EJB
    public SexoMascotaServicio sexoMascotaServicio;
    @EJB
    public RazaMascotaServicio razaMascotaServicio;
    
    private List<EspecieMascota> listaEspecies;
    private List<RazaMascota> listaRaza;
    private List<SexoMascota> listaSexo;

    public CatalogosVeterinariaBean() {
        this.listaEspecies = new ArrayList<>();
        this.listaRaza = new ArrayList<>();
        this.listaSexo = new ArrayList<>();
    }
    
    @PostConstruct
    public void initCatalogos()
    {
        this.listaEspecies.addAll(this.especieMascotaServicio.listar());
        this.listaRaza.addAll(this.razaMascotaServicio.listar());
        this.listaSexo.addAll(this.sexoMascotaServicio.listar());
    }
    
    public void refreshEspecie()
    {
        this.listaEspecies.clear();
        this.listaEspecies.addAll(this.especieMascotaServicio.listar());
    }
    
    public void refreshSexo()
    {
        this.listaSexo.clear();
        this.listaSexo.addAll(this.sexoMascotaServicio.listar());
    }
    
    public void refreshRaza()
    {
        this.listaRaza.clear();
        this.listaRaza.addAll(this.razaMascotaServicio.listar());
    }

    public List<EspecieMascota> getListaEspecies() {
        this.refreshEspecie();
        return listaEspecies;
    }

    public void setListaEspecies(List<EspecieMascota> listaEspecies) {
        this.listaEspecies = listaEspecies;
    }

    public List<RazaMascota> getListaRaza() {
        this.refreshRaza();
        return listaRaza;
    }

    public void setListaRaza(List<RazaMascota> listaRaza) {
        this.listaRaza = listaRaza;
    }

    public List<SexoMascota> getListaSexo() {
        this.refreshSexo();
        return listaSexo;
    }

    public void setListaSexo(List<SexoMascota> listaSexo) {
        this.listaSexo = listaSexo;
    }
    
    
}
