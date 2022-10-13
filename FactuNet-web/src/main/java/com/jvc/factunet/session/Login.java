package com.jvc.factunet.session;

import com.jvc.factunet.entidades.Controles;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.OpcionesMenu;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletRequest;
import com.jvc.factunet.servicios.OpcionesMenuServicio;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @EJB
    private OpcionesMenuServicio opcionesMenuServicio;
    
    private Empleado empleado;
    private MenuModel menuModel;
    private MenuModel menuModelBoton;
    private String urlLogo;
    private String urlFotoLogin;
    private BigDecimal ivaEmpresa;
    private String pathEmpresa;
    
    public Login() {
    }
    
    public void verIVA()
    {
        for(Controles obj : empleado.getEmpresa().getControlesList())
        {
            if("IVA".equals(obj.getNombre()))
            {
                this.ivaEmpresa =  obj.getValor();
                break;
            }
        }
    }
    
    public void verPath()
    {
        for(Controles obj : empleado.getEmpresa().getControlesList())
        {
            if("PATH".equals(obj.getNombre()))
            {
                this.pathEmpresa =  obj.getDetalle();
                break;
            }
        }
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
            if (this.empleado.getEmpresa().getLogoImagen() != null && this.empleado.getEmpresa().getLogoImagen().length > 0) {
                nombreFotoL = "logo.jpg";
                fosl = new FileOutputStream(pathLogol + File.separator + nombreFotoL);
                fosl.write(this.empleado.getEmpresa().getLogoImagen());
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
    
    public void cargarFotoLogin()
    {
        FileOutputStream fos = null;
        try {
            String pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathL = Paths.get(pathLogo);
            if (!Files.exists(pathL)) {
                Files.createDirectory(pathL);
            }
            String nombreFoto;
            if (this.empleado.getPersona().getFoto() != null && this.empleado.getPersona().getFoto().length > 0) {
                nombreFoto = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + nombreFoto);
                fos.write(this.empleado.getPersona().getFoto());
                this.urlFotoLogin = "/temp/" + nombreFoto;
            } else {
                if (this.empleado.getPersona().getSexo().getNombre().equals("Hombre") || this.empleado.getPersona().getSexo().getNombre().equals("Masculino")) {
                    nombreFoto = "foto_hombre.jpg";
                } else {
                    nombreFoto = "foto_mujer.jpg";
                }
                this.urlFotoLogin = "/resources/imagenes/" + nombreFoto;
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void crearMenu(Integer rol) {
        this.menuModel = new DefaultMenuModel();
        try {
            List<OpcionesMenu> listaMenu = this.opcionesMenuServicio.listar(rol);
            for (OpcionesMenu opcion : listaMenu) 
            {
                if("1".equals(opcion.getMenu().getTipo()))
                {
                    DefaultSubMenu submenu = new DefaultSubMenu("Dynamic Submenu");
                    if(opcion.getMenu().getNivel() == 1)
                    {
                        submenu.setLabel(opcion.getMenu().getNombre());
                        submenu.setExpanded(true);
                        this.populateMenu(opcion, submenu,listaMenu);
                        this.menuModel.addElement(submenu);
                    }
                }
            }
        } catch (Exception e) {
            
        }
    }
     
    public void populateMenu(OpcionesMenu opcionPadre, DefaultSubMenu menuPadre, List<OpcionesMenu> lista) {
        for (OpcionesMenu opcion : lista) 
        {
            if("1".equals(opcion.getMenu().getTipo()))
            {   
                if(opcion.getMenu().getPadre() != null && Objects.equals(opcion.getMenu().getPadre().getCodigo(), opcionPadre.getMenu().getCodigo()))
                {
                    DefaultMenuItem itemHijo = new DefaultMenuItem();
                    itemHijo.setValue(opcion.getMenu().getNombre());
                    if(opcion.getMenu().getUrl().contains("?"))
                    {
                        itemHijo.setUrl(getServletRequest().getContextPath() + opcion.getMenu().getUrl()+"&color="+opcion.getMenu().getColor());
                    }
                    else
                    {
                        itemHijo.setUrl(getServletRequest().getContextPath() + opcion.getMenu().getUrl()+"?color="+opcion.getMenu().getColor());
                    }
                    itemHijo.setProcess("@this"); 
                    itemHijo.setUpdate(":frmCabecera, :frmCabecera:grMensajes");
                    menuPadre.getElements().add(itemHijo);
                }
            }
        }
    }
    
    public void crearMenuBoton(Integer rol) {
        this.menuModelBoton = new DefaultMenuModel();
        try {
            List<OpcionesMenu> listaMenu = this.opcionesMenuServicio.listar(rol);

            for (OpcionesMenu opcion : listaMenu) {
                if(opcion.getMenu().getIcon() != null &&
                   !"".equals(opcion.getMenu().getIcon())){
                    DefaultSubMenu submenu = new DefaultSubMenu("Dynamic Submenu");
                    submenu.setLabel("");
                    submenu.setIcon(opcion.getMenu().getIcon()); 
                    submenu.setStyle("width: 0;font-size: 18px;color:black");
                    this.populateMenuBoton(opcion, submenu,listaMenu);
                    if (submenu.getElementsCount() > 0) {
                        this.menuModelBoton.addElement(submenu);
                    }
                }
            }

        } catch (Exception e) {
            
        }
    }
    
    public void populateMenuBoton(OpcionesMenu opcionPadre, DefaultSubMenu menuPadre, List<OpcionesMenu> lista) {
        for (OpcionesMenu opcion : lista) 
        {
            if("1".equals(opcion.getMenu().getTipo()))
            {   
                if(opcion.getMenu().getPadre() != null && Objects.equals(opcion.getMenu().getPadre().getCodigo(), opcionPadre.getMenu().getCodigo()))
                {
                    if(opcion.getMenu().getIcon() != null && !"".equals(opcion.getMenu().getIcon())){
                    
                        DefaultMenuItem itemHijo = new DefaultMenuItem();
                        if(opcion.getMenu().getUrl().contains("?"))
                        {
                            itemHijo.setUrl(getServletRequest().getContextPath() + opcion.getMenu().getUrl()+"&color="+opcion.getMenu().getColor());
                        }
                        else
                        {
                            itemHijo.setUrl(getServletRequest().getContextPath() + opcion.getMenu().getUrl()+"?color="+opcion.getMenu().getColor());
                        }
                        itemHijo.setStyle("width: 50%;font-size: 18px;color:black");
                        itemHijo.setIcon(opcion.getMenu().getIcon()); 
                        menuPadre.getElements().add(itemHijo);
                }
                }
            }
        }
    }

    public void logout() {
        HttpSession session = SessionBean.getSession();
        session.invalidate();
        FacesUtils.redireccionar("/faces/login.xhtml");
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getUrlFotoLogin() {
        return urlFotoLogin;
    }

    public void setUrlFotoLogin(String urlFotoLogin) {
        this.urlFotoLogin = urlFotoLogin;
    }

    public BigDecimal getIvaEmpresa() {
        return ivaEmpresa;
    }

    public void setIvaEmpresa(BigDecimal ivaEmpresa) {
        this.ivaEmpresa = ivaEmpresa;
    }

    public String getPathEmpresa() {
        return pathEmpresa;
    }

    public void setPathEmpresa(String pathEmpresa) {
        this.pathEmpresa = pathEmpresa;
    }
    
    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    
    public MenuModel getMenuModelBoton() {
        return menuModelBoton;
    }

    public void setMenuModelBoton(MenuModel menuModelBoton) {
        this.menuModelBoton = menuModelBoton;
    }
}
