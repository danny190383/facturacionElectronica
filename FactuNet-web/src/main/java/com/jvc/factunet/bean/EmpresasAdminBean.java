package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.EmpresaServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "empresasAdminBean")
@ViewScoped
public class EmpresasAdminBean implements Serializable{

    @EJB
    private EmpresaServicio empresaServicio;
    
    private List<Empresa> listaEmpresas;
    private Empresa empresa;
    
    public EmpresasAdminBean() {
        this.listaEmpresas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        this.listaEmpresas.addAll(this.empresaServicio.listar());
    }
    
    public void seleccionar(Empresa empresa){
        if(empresa!= null){
            FacesUtils.redireccionar("/faces/seguridades/empresaAdminDialog.xhtml?empresa=" + empresa.getCodigo());
        }
        else
        {
            FacesUtils.redireccionar("/faces/seguridades/empresaAdminDialog.xhtml?empresa=-1");
        }
    }
    
    public void guardar(){
        
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
