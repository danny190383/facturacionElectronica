package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Rol;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.RolServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

@ManagedBean
@ViewScoped
public class RolUsuarioBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(RolUsuarioBean.class.getName());
    
    @EJB
    private RolServicio rolServicio;
    @EJB
    private CuentaServicio cuentaServicio;
    
    private List<Rol> listaRol;
    private Rol rol;
    private DualListModel<Cuenta> listaCuentas;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public RolUsuarioBean() {
        this.listaRol = new ArrayList<>();
        this.rol = new Rol();
    }
    
    @PostConstruct
    private void init()
    {
        this.listaRol.addAll(this.rolServicio.listar());
        this.rol = this.listaRol.get(0);
        this.cargarCuentas();
    }
    
    public void cargarCuentas()
    {
        List<Cuenta> cuentaSource = this.cuentaServicio.listarSinRol(this.empresa.getCodigo());
        List<Cuenta> cuentaTarget = this.cuentaServicio.listar(this.rol.getCodigo(), this.empresa.getCodigo());
         
        this.listaCuentas = new DualListModel<>(cuentaSource, cuentaTarget);
    }
    
    public void onSelect()
    {
        this.rol.getCodigo();
        this.cargarCuentas();
    }
    
    public void guardar()
    {
        List<Cuenta> listaSource = this.listaCuentas.getSource();
        List<Cuenta> listaTarget = this.listaCuentas.getTarget();
        
        for(Cuenta cuenta: listaSource)
        {
            cuenta.setRol(null);
        }
        
        for(Cuenta cuenta: listaTarget)
        {
            cuenta.setRol(this.rol);
        }
        List<Cuenta> insertar = new ArrayList();
        insertar.addAll(listaSource);
        insertar.addAll(listaTarget);
        try {
             this.cuentaServicio.guardar(insertar);
             FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
       
    }
    
    public void nuevo() {
        this.rol = new Rol();
    }
    
    public void guardarRol() {
        try {
            this.rol.setNombre(this.rol.getNombre().trim().toUpperCase());
            if (this.rol.getCodigo() != null) {
                this.rolServicio.actualizar(this.rol);
            } else {
                this.rolServicio.insertar(this.rol);
                this.listaRol.add(rol);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog005').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }

    public void eliminarRol() {
        try {
            this.rolServicio.eliminar(this.rol);
            this.listaRol.remove(this.rol);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }

    public List<Rol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<Rol> listaRol) {
        this.listaRol = listaRol;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public DualListModel<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(DualListModel<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }
    
    
}
