package com.jvc.factunet.session;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "login")
@SessionScoped
public class LoginPedidos implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public Cuenta cuenta;
    private Integer tipo;
    private String urlLogo;
    private String urlFotoCliente;
    private String nombreLogin;
    
    public LoginPedidos() {
        this.tipo = 0;
    }

    public void logout() {
        HttpSession session = SessionPedidosBean.getSession();
        this.cuenta = null;
        this.tipo = 0;
        this.urlFotoCliente = "";
        this.urlLogo = "";
        FacesUtils.redireccionar("/faces/index.xhtml");
    }
    
    public void cargarLogo()
    {
        FileOutputStream fosl = null;
        try {
            String pathLogol = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathLL = Paths.get(pathLogol);
            if (!Files.exists(pathLL)) {
                Files.createDirectory(pathLL);
            }
            String nombreFotoL;
            if (this.cuenta.getEmpresa().getLogoImagen() != null && this.cuenta.getEmpresa().getLogoImagen().length > 0) {
                nombreFotoL = this.cuenta.getEmpresa().getNombreAbreviado();
                fosl = new FileOutputStream(pathLogol + File.separator + nombreFotoL);
                fosl.write(this.cuenta.getEmpresa().getLogoImagen());
                this.urlLogo = "/temp/" + nombreFotoL;
            } else {
                this.urlLogo = "/resources/imagenes/logo.jpg";
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fosl.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void cargarFotoCliente()
    {
        FileOutputStream fosC = null;
        try {
            String pathLogoC = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathLC = Paths.get(pathLogoC);
            if (!Files.exists(pathLC)) {
                Files.createDirectory(pathLC);
            }
            String nombreFotoC;
            if (this.cuenta.getCliente().getPersona().getFoto() != null && this.cuenta.getCliente().getPersona().getFoto().length > 0) {
                nombreFotoC = this.cuenta.getCliente().getPersona().getNombres();
                fosC = new FileOutputStream(pathLogoC + File.separator + nombreFotoC);
                fosC.write(this.cuenta.getCliente().getPersona().getFoto());
                this.urlFotoCliente = "/temp/" + nombreFotoC;
            } else {
                this.urlFotoCliente = "/resources/imagenes/logo.jpg";
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fosC.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void regresarHome(){
        FacesUtils.redireccionar("/faces/index.xhtml");
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlFotoCliente() {
        return urlFotoCliente;
    }

    public void setUrlFotoCliente(String urlFotoCliente) {
        this.urlFotoCliente = urlFotoCliente;
    }

    public String getNombreLogin() {
        return nombreLogin;
    }

    public void setNombreLogin(String nombreLogin) {
        this.nombreLogin = nombreLogin;
    }
}
