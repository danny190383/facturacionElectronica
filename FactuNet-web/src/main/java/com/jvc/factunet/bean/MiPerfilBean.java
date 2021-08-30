package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Contactar;
import com.jvc.factunet.entidades.Contacto;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.session.Login;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class MiPerfilBean extends CatalogosPersonaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(MiPerfilBean .class.getName());

    @EJB
    private CuentaServicio cuentaServicio;
    
    private Cuenta cuenta;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public MiPerfilBean() {
        this.cuenta = ((Login) FacesUtils.getManagedBean("login")).getEmpleado().getCuenta();
    }
    
    @PostConstruct
    public void init()
    {
        FileOutputStream fos = null;
        try {
            this.cuenta = ((Login) FacesUtils.getManagedBean("login")).getEmpleado().getCuenta();
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path pathL = Paths.get(this.pathLogo);
            if (!Files.exists(pathL)) {
                Files.createDirectory(pathL);
            }
            if (this.cuenta.getEmpleado().getPersona().getFoto() != null && this.cuenta.getEmpleado().getPersona().getFoto().length > 0) {
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.cuenta.getEmpleado().getPersona().getFoto());
                this.urlLogo = "/temp/" + this.nombreLogo;
            } else {
                this.nombreLogo = "logo.png";
                this.pathLogo = getServletContext().getRealPath("/") + "resources" + File.separator + "imagenes";
                this.urlLogo = "/resources/imagenes/" + this.nombreLogo;
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void subirLogo(FileUploadEvent event) {
        OutputStream os = null;
        InputStream is = null;
        try {
            this.nombreLogo = (new Date()).getTime() + ".jpg";
            this.pathLogo = getServletContext().getRealPath("/temp");
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            is = event.getFile().getInputStream();
            os = Files.newOutputStream(path);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            this.urlLogo = "/temp/" + this.nombreLogo;
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir la imagen");
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void onCapture(CaptureEvent captureEvent) {
        this.nombreLogo = (new Date()).getTime() + ".jpg";
        byte[] data = captureEvent.getData();
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
        this.urlLogo = "/temp/" + this.nombreLogo;
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(this.pathLogo + File.separator + this.nombreLogo));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }
    
    public void verContactar(Contactar contactar) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contactar", contactar);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cuenta.getEmpleado());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/contactos/nuevoContactarDialog", options, null);
    }
    
    public void onContactarSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contactar contactar = (Contactar) event.getObject();
            if(this.cuenta.getEmpleado().getContactarPersonaList() == null)
            {
                this.cuenta.getEmpleado().setContactarPersonaList(new ArrayList<Contactar>());
            }
            if((!this.cuenta.getEmpleado().getContactarPersonaList().contains(contactar)) && (contactar != null))
            {
                this.cuenta.getEmpleado().getContactarPersonaList().add(contactar);
            }
        }
    }
    
    public void verContacto(Contacto contacto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contacto", contacto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cuenta.getEmpleado());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 650);
        options.put("height", 400);
        options.put("contentHeight", 400);
        options.put("contentWidth", 650);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/contactos/nuevoContactoDialog", options, null);
    }
    
    public void onContactoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contacto contacto = (Contacto) event.getObject();
            if(this.cuenta.getEmpleado().getContactoPersonaList() == null)
            {
                this.cuenta.getEmpleado().setContactoPersonaList(new ArrayList<Contacto>());
            }
            if((!this.cuenta.getEmpleado().getContactoPersonaList().contains(contacto)) && (contacto != null))
            {
                this.cuenta.getEmpleado().getContactoPersonaList().add(contacto);
            }
        }
    }
    
    public void verBusquedaPersonas() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 850);
        options.put("height", 400);
        options.put("contentWidth", 840);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPersonasDialog", options, null);
    }
    
    public void onPersonaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contacto contacto = new Contacto();
            contacto.setPersona((Persona) event.getObject());
            contacto.setPersonaPadre(this.cuenta.getEmpleado());
            contacto.setEmpresa(this.empresa); 
            contacto.setEstado(Boolean.TRUE); 
            this.cuenta.getEmpleado().getContactoPersonaList().add(contacto);
        }
    }
    
    public void verRetetClave() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cuenta", this.cuenta);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 350);
        options.put("height", 150);
        options.put("contentHeight", 150);
        options.put("contentWidth", 350);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/recursosHumanos/extras/cambiarContrasenaDialog", options, null);
    }
    
    public void onResetClaveSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.cuenta = (Cuenta) event.getObject();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("claveActualizada"));
        }
    }
    
    public void eliminarContacto(Contacto parametro) {
        try {
            this.cuenta.getEmpleado().getContactoPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarContactoWeb(Contactar parametro) {
        try {
            this.cuenta.getEmpleado().getContactarPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void guardar() {
        try {
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.cuenta.getEmpleado().getPersona().setFoto(foto);
            if (this.cuenta.getIdentificador() == null) {
                this.cuentaServicio.insertar(this.cuenta);
            } else {
                this.cuentaServicio.actualizar(this.cuenta);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
}
