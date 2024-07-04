package com.jvc.factunet.session;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.EmpleadoServicio;
import com.jvc.factunet.theme.ThemeSwitcherBean;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class CrearSesion implements Serializable{
    
    @EJB
    private CuentaServicio cuentaServicio;
    @EJB
    private EmpleadoServicio empleadoServicio;
    
    private Login login;
    private String identificador;
    private String clave;
    private List<Empleado> listaEmpleado;

    public CrearSesion() {
        this.listaEmpleado = new ArrayList<>();
    }
    
    public String validateUsernamePassword() {
        this.login = (Login) FacesUtils.getManagedBean("login");
        Cuenta cuenta = this.cuentaServicio.validar(this.identificador, this.clave);
        if (cuenta != null) {
            this.listaEmpleado.clear();
            this.listaEmpleado.addAll(empleadoServicio.listarEmpleadoEmpresas(this.identificador));
        }
        if(this.listaEmpleado.size()>1){
            PrimeFaces.current().executeScript("PF('mdlFichaMedicaMilitar').show();");
            PrimeFaces.current().ajax().update("frmDialog:mdlFichaMedicaMilitar");
        }
        if(this.listaEmpleado == null || this.listaEmpleado.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "La cuenta ingresada está inactiva consulte con el administrador.",
                                        "Please enter correct username and Password"));
            return "login";
        }
        if (cuenta != null) {
            this.login.setEmpleado(this.listaEmpleado.get(0));
            if(this.login.getEmpleado().getEmpresa().getFechaHasta().before(Fecha.fechaInicio(new Date()))){
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "La fecha de contrato para el uso del sistema a caducado.",
                                        "Please enter correct username and Password"));
                return "login";
            }
            if(!this.login.getEmpleado().getEstado()){
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "La cuenta ingresada está inactiva, consulte con el administrador.",
                                        "Please enter correct username and Password"));
                return "login";
            }
            if(!this.login.getEmpleado().getEmpresa().getEstado()){
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "La empresa registrada está inactiva, consulte con el administrador.",
                                        "Please enter correct username and Password"));
                return "login";
            }
            if(this.login.getEmpleado().getCuenta().getRol() == null){
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "La cuenta ingresada no tiene un perfil asignado, consulte con el administrador.",
                                        "Please enter correct username and Password"));
                return "login";
            }
            HttpSession session = SessionBean.getSession();
            session.setAttribute("username", cuenta.getIdentificador());
            session.setAttribute("empleado", this.login.getEmpleado());
            this.login.crearMenu(this.login.getEmpleado().getCuenta().getRol().getCodigo());
            this.login.crearMenuBoton(this.login.getEmpleado().getCuenta().getRol().getCodigo());
            this.login.cargarLogo();
            this.login.cargarFotoLogin();
            this.login.verPath();
            ThemeSwitcherBean theme = (ThemeSwitcherBean) FacesUtils.getManagedBean("themeSwitcherBean");
            theme.setTheme(this.login.getEmpleado().getEmpresa().getTema());
            return "usuario";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "Incorrecto Usuario ó Password",
                                        "Please enter correct username and Password"));
            return "login";
        }
    }
    
//    public String cargarDatosAcceso(){
//        if (this.login.getCuenta() != null) {
//            if(!this.login.getCuenta().getEmpleado().getEstado()){
//                FacesContext.getCurrentInstance().addMessage(
//                        null,
//                        new FacesMessage(FacesMessage.SEVERITY_WARN,
//                                        "La cuenta ingresada está inactiva consulte con el administrador.",
//                                        "Please enter correct username and Password"));
//                return "login";
//            }
//            if(!this.login.getEmpresa().getEstado()){
//                FacesContext.getCurrentInstance().addMessage(
//                        null,
//                        new FacesMessage(FacesMessage.SEVERITY_WARN,
//                                        "La empresa registrada está inactiva consulte con el administrador.",
//                                        "Please enter correct username and Password"));
//                return "login";
//            }
//            HttpSession session = SessionBean.getSession();
//            session.setAttribute("username", this.login.getCuenta().getIdentificador());
//            session.setAttribute("cuenta", this.login.getCuenta());
//            this.login.crearMenu(this.login.getCuenta().getRol().getCodigo());
//            this.login.cargarLogo();
//            this.login.cargarFotoLogin();
//            this.login.verIVA();
//            this.login.verPath();
//            ThemeSwitcherBean theme = (ThemeSwitcherBean) FacesUtils.getManagedBean("themeSwitcherBean");
//            theme.setTheme(this.login.getCuenta().getEmpleado().getEmpresa().getTema());
//            return "usuario";
//        } else {
//            FacesContext.getCurrentInstance().addMessage(
//                        null,
//                        new FacesMessage(FacesMessage.SEVERITY_WARN,
//                                        "Incorrecto Usuario ó Password",
//                                        "Please enter correct username and Password"));
//            return "login";
//        }
//    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Empleado> getListaEmpleado() {
        return listaEmpleado;
    }

    public void setListaEmpleado(List<Empleado> listaEmpleado) {
        this.listaEmpleado = listaEmpleado;
    }
}
