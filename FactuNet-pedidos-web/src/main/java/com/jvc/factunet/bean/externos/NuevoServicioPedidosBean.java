package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.ProductoServicio;
import com.jvc.factunet.entidades.RetencionServicio;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.session.LoginPedidos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
public class NuevoServicioPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoServicioPedidosBean.class.getName());

    private ProductoServicio productoServicio;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private Empresa empresa;
    
    private boolean autogenerar = false;
    
    public NuevoServicioPedidosBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
        this.productoServicio = (ProductoServicio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        if(this.productoServicio == null)
        {
            this.inicializar();
        }
        this.cargarFoto();
    }
    
    public void inicializar()
    {
        this.productoServicio = new ProductoServicio();
        this.productoServicio.setNombre((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nombregrupo"));
        this.productoServicio.setEmpresa(this.empresa);
        this.productoServicio.setPrecioUltimaCompra(BigDecimal.ZERO);
        this.productoServicio.setUtilidad(BigDecimal.ZERO);
        this.productoServicio.setPvp(BigDecimal.ZERO);
    }
    
    public void subirLogo(FileUploadEvent event) {
        OutputStream os = null;
        InputStream is = null;

        try {
            this.nombreLogo = (new Date()).getTime() + ".jpg";
            this.pathLogo = getServletContext().getRealPath("/temp");
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            is = event.getFile().getInputstream();
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
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            
            
            if (this.productoServicio.getFoto() != null && this.productoServicio.getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.productoServicio.getFoto());
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
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        this.productoServicio.setNombre(this.productoServicio.getNombre().trim().toUpperCase());
        if(this.autogenerar)
        {
            this.productoServicio.setCodigoBarras(this.productoServicio.getNombre());
        }
        Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
        try {
            byte[] foto = Files.readAllBytes(path);
            this.productoServicio.setFoto(foto);
        } catch (Exception e) {
            
        }
        PrimeFaces.current().dialog().closeDynamic(this.productoServicio);
    }
    
    public void editUtilidad(){
        this.productoServicio.setPvp(this.productoServicio.getPrecioUltimaCompra().add(this.productoServicio.getPrecioUltimaCompra().multiply(this.productoServicio.getUtilidad().divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public void editPVP(){
        this.productoServicio.setUtilidad(((this.productoServicio.getPvp().subtract(this.productoServicio.getPrecioUltimaCompra())).divide(this.productoServicio.getPrecioUltimaCompra(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
    }
    
    public void verRetencion(RetencionServicio retencion) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoPantalla", 2);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("retencionServicio", retencion);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicio", this.productoServicio);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/contactos/nuevoRetencionPersonaDialog", options, null);
    }
    
    public void onRetencionSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            RetencionServicio retenccion = (RetencionServicio) event.getObject();
            if(this.productoServicio.getRetencionServicioList() == null)
            {
                this.productoServicio.setRetencionServicioList(new ArrayList<RetencionServicio>());
            }
            if(retenccion != null)
            {
                if(!this.tieneRetencion(retenccion.getTipoRetencion().getCodigo()))
                { 
                    this.productoServicio.getRetencionServicioList().add(retenccion);
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("retencionyaagregada"));
                }
            }
        }
    }
    
    public boolean tieneRetencion(Integer tipoRetencion)
    {
        for(RetencionServicio obj : this.productoServicio.getRetencionServicioList())
        {
            if(Objects.equals(tipoRetencion, obj.getTipoRetencion().getCodigo()))
            {
                return true;
            }
        }
        return false;
    }
    
    public void eliminarRetencion(RetencionServicio parametro) {
        try {
            this.productoServicio.getRetencionServicioList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
    
    public boolean isAutogenerar() {
        return autogenerar;
    }

    public void setAutogenerar(boolean autogenerar) {
        this.autogenerar = autogenerar;
    }

    public ProductoServicio getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }
}
