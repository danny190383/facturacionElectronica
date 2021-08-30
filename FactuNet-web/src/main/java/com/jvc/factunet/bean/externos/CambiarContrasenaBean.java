package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.servicios.CuentaServicio;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class CambiarContrasenaBean implements Serializable{
    
    @EJB
    private CuentaServicio cuentaServicio;

    private Cuenta cuenta;
    
    public CambiarContrasenaBean() {
        this.cuenta = new Cuenta();
    }
    
    @PostConstruct
    public void init()
    {
        this.cuenta = (Cuenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuenta");
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        try {
            this.cuenta.setClave(this.cuentaServicio.encriptaPassword(this.cuenta.getClave().trim()));
            this.cuentaServicio.actualizar(this.cuenta);
            PrimeFaces.current().dialog().closeDynamic(this.cuenta);
        } catch (Exception ex) {
            Logger.getLogger(CambiarContrasenaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
