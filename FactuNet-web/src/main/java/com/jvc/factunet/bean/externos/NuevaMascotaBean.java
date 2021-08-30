package com.jvc.factunet.bean.externos;

import com.jvc.factunet.bean.ClienteBean;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.icefacesUtil.CatalogosVeterinariaBean;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.servicios.MascotaServicio;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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

@ManagedBean
@ViewScoped
public class NuevaMascotaBean extends CatalogosVeterinariaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevaMascotaBean.class.getName());
    
    @EJB
    private MascotaServicio mascotaServicio;

    private Mascota mascota;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private Persona persona;
    
    public NuevaMascotaBean() {
        this.persona = new Persona();
    }
    
    @PostConstruct
    public void init()
    {
        Path pathL = Paths.get(getServletContext().getRealPath("/") + File.separator + "temp");
        if (!Files.exists(pathL)) {
            try {
                Files.createDirectory(pathL);
            } catch (IOException ex) {
                Logger.getLogger(ClienteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.mascota = (Mascota) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mascota");
        this.persona = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("persona");
        if(this.mascota == null)
        {
            this.nuevaMascota();
        }
        else
        {
            this.cargarFoto();
        }
    }
    
    public void nuevaMascota(){
        this.mascota = new Mascota();
        this.mascota.setFechaNacimiento(new Date());
        this.mascota.setEspecie(super.getListaEspecies().get(0));
        this.mascota.setRaza(super.getListaRaza().get(0));
        this.mascota.setSexo(super.getListaSexo().get(0));
        this.mascota.setPersona(this.persona);
        this.nombreLogo = "foto_hombre.jpg";
        this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
    }
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            if (this.mascota.getFoto() != null && this.mascota.getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.mascota.getFoto());
                this.urlLogo = "/temp/" + this.nombreLogo;
            } else {
                this.nombreLogo = "foto_hombre.jpg";
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
                this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
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
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() throws Exception {
        try {
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.mascota.setFoto(foto);
            this.mascota.setNombre(this.mascota.getNombre().trim().toUpperCase());
            if(this.mascota.getPersona().getCodigo() != null){
                if(this.mascota.getCodigo() != null){
                    this.mascotaServicio.actualizar(this.mascota);
                }
                else
                {
                    this.mascotaServicio.insertar(this.mascota);
                }
            }
            PrimeFaces.current().dialog().closeDynamic(this.mascota);
        } catch (IOException ex) {
            Logger.getLogger(NuevaMascotaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
}
