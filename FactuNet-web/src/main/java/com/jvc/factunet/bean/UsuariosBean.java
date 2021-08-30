package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentaServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "usuariosBean")
@ViewScoped
public class UsuariosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(UsuariosBean.class.getName());
    
    @EJB
    private CuentaServicio cuentaServicio;
    
    private List<Cuenta> listaCuentas;

    public UsuariosBean() {
        this.listaCuentas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init(){
        this.listaCuentas.addAll(this.cuentaServicio.listar());
    }
    
    public void retetClave(Cuenta cuenta){
        try {
            cuenta.setClave(this.cuentaServicio.encriptaPassword(cuenta.getEmpleado().getPersona().getCedula()));
            this.cuentaServicio.actualizar(cuenta);
            FacesUtils.addInfoMessage("Clave reseteada exitosamente.");
        } catch (Exception ex) {
           FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorGeneralGA"));
           LOG.log(Level.SEVERE, "Error al resetear clave.", ex);
        }
    }

    public List<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }
}
