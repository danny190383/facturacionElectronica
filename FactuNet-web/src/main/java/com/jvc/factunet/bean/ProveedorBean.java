package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.BancoPersona;
import com.jvc.factunet.entidades.Contactar;
import com.jvc.factunet.entidades.Contacto;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Proveedor;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.RetencionPersona;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.PersonaServicio;
import com.jvc.factunet.servicios.ProveedorServicio;
import com.jvc.factunet.session.Login;
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
public class ProveedorBean extends CatalogosPersonaBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(ProveedorBean.class.getName());
    
    @EJB
    private PersonaServicio personaServicio;
    @EJB
    private ProveedorServicio proveedorServicio;
    
    private Proveedor proveedor;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public ProveedorBean() {
    }
    
    @PostConstruct
    public void init()
    {
        Path pathL = Paths.get(getServletContext().getRealPath("/") + File.separator + "temp");
        if (!Files.exists(pathL)) {
            try {
                Files.createDirectory(pathL);
            } catch (IOException ex) {
                Logger.getLogger(ProveedorBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nuevoProveedor();
    }
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            if (this.proveedor.getPersona().getFoto() != null && this.proveedor.getPersona().getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.proveedor.getPersona().getFoto());
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
    
    public void nuevoProveedor() {
        this.proveedor = new Proveedor();
        this.proveedor.setPersona(new Persona());
        this.proveedor.getPersona().setCiudad(super.getListaCiudad().get(0));
        this.proveedor.setEmpresa(this.empresa);
        this.proveedor.setEstado(Boolean.TRUE); 
        this.proveedor.setCapacidadCredito(BigDecimal.ZERO);
        this.proveedor.setInteresMora(BigDecimal.ZERO);
        this.proveedor.setTiempoMaxCredito(0);
        this.proveedor.setDescuento(BigDecimal.ZERO);
        this.proveedor.getPersona().setTipoIdentificacion(super.getListaTipoIdentificacion().get(0)); 
        this.nombreLogo = "foto_hombre.jpg";
        this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
    }
    
    public void verContactar(Contactar contactar) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contactar", contactar);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.proveedor);
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
            if(this.proveedor.getContactarPersonaList() == null)
            {
                this.proveedor.setContactarPersonaList(new ArrayList<>());
            }
            if((!this.proveedor.getContactarPersonaList().contains(contactar)) && (contactar != null))
            {
                this.proveedor.getContactarPersonaList().add(contactar);
            }
        }
    }
    
    public void verContacto(Contacto contacto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contacto", contacto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.proveedor);
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
            if(this.proveedor.getContactoPersonaList() == null)
            {
                this.proveedor.setContactoPersonaList(new ArrayList<>());
            }
            if((!this.proveedor.getContactoPersonaList().contains(contacto)) && (contacto != null))
            {
                this.proveedor.getContactoPersonaList().add(contacto);
            }
        }
    }
    
    public void verBancos(BancoPersona banco) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bancoPersona", banco);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.proveedor);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/contactos/nuevoBancoPersonaDialog", options, null);
    }
    
    public void onBancosSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            BancoPersona banco = (BancoPersona) event.getObject();
            if(this.proveedor.getBancoPersonaList() == null)
            {
                this.proveedor.setBancoPersonaList(new ArrayList<>());
            }
            this.proveedor.getBancoPersonaList().add(banco);
        }
    }
    
    public void verRetencion(RetencionPersona retencion) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoPantalla", 1);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("retencionPersona", retencion);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.proveedor.getPersona());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/contactos/nuevoRetencionPersonaDialog", options, null);
    }
    
    public void onRetencionSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            RetencionPersona retenccion = (RetencionPersona) event.getObject();
            if(this.proveedor.getPersona().getRetencionPersonaList() == null)
            {
                this.proveedor.getPersona().setRetencionPersonaList(new ArrayList<>());
            }
            if(retenccion != null)
            {
                if(!this.tieneRetencion(retenccion.getTipoRetencion().getCodigo()))
                { 
                    this.proveedor.getPersona().getRetencionPersonaList().add(retenccion);
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
        for(RetencionPersona obj : this.proveedor.getPersona().getRetencionPersonaList())
        {
            if(Objects.equals(tipoRetencion, obj.getTipoRetencion().getCodigo()))
            {
                return true;
            }
        }
        return false;
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
            contacto.setPersonaPadre(this.proveedor);
            contacto.setEmpresa(this.empresa); 
            contacto.setEstado(Boolean.TRUE); 
            if(this.proveedor.getContactoPersonaList() == null)
            {
                this.proveedor.setContactoPersonaList(new ArrayList<>());
            }
            this.proveedor.getContactoPersonaList().add(contacto);
        }
    }
    
    public void verBusquedaProveedores() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 850);
        options.put("height", 400);
        options.put("contentWidth", 840);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarProveedoresDialog", options, null);
    }
    
    public void onProveedorSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.proveedor = (Proveedor) event.getObject();
            this.cargarFoto();
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
    
    public void eliminarContacto(Contacto parametro) {
        try {
            this.proveedor.getContactoPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarContactoWeb(Contactar parametro) {
        try {
            this.proveedor.getContactarPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarBanco(BancoPersona parametro) {
        try {
            this.proveedor.getBancoPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarRetencion(RetencionPersona parametro) {
        try {
            this.proveedor.getPersona().getRetencionPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public Boolean guardarProveedor() throws IOException, Exception
    {
        if((this.proveedor.getContactoPersonaList() != null) && (!this.proveedor.getContactoPersonaList().isEmpty()))
        {
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.proveedor.getPersona().setFoto(foto);
            this.proveedor.getPersona().setNombres(this.proveedor.getPersona().getNombres().trim().toUpperCase());
            if(this.proveedor.getPersona().getApellidos() != null)
            {
                this.proveedor.getPersona().setApellidos(this.proveedor.getPersona().getApellidos().trim().toUpperCase());
            }
            this.proveedor = this.proveedorServicio.actualizar(this.proveedor);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            return Boolean.TRUE;
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registreContacto"));
            return Boolean.FALSE;
        }
    }
    
    public Boolean guardar()
    {
        if(this.proveedor.getContactoPersonaList() == null || this.proveedor.getContactoPersonaList().isEmpty()){
            FacesUtils.addInfoMessage("Debe ingresar al menos un contacto");
            return Boolean.FALSE;
        }
        try {
            if(this.proveedor.getPersona().getCodigo() != null)
            {
                return this.guardarProveedor();
            }
            else
            {
                if(!this.verificarCedulaSistema())
                {
                    return this.guardarProveedor();
                }
                else
                {
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaExistente"));
                    return Boolean.FALSE;
                }
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            return Boolean.FALSE;
        }
    }
    
    public Boolean verificarCedulaSistema()
    {
        try {
            Proveedor proveedorTmp = this.proveedorServicio.buscarCedula(this.proveedor.getPersona().getCedula(), this.empresa.getCodigo());
            if(proveedorTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.proveedor.getPersona().getCedula());
                if(personaTmp == null)
                {
                    return Boolean.FALSE;
                }
                else
                {
                    return Boolean.TRUE;
                }
            }
            else
            {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
    
    public void verificarPersona()
    {
        try {
            Proveedor proveedorTmp = this.proveedorServicio.buscarCedula(this.proveedor.getPersona().getCedula(), this.empresa.getCodigo());
            if(proveedorTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.proveedor.getPersona().getCedula());
                if(personaTmp == null)
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
                }
                else
                {
                    this.proveedor.setPersona(personaTmp);
                    this.cargarFoto();
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaEncontradaProveedor"));
                }
            }
            else
            {
                this.proveedor = proveedorTmp;
                this.cargarFoto();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("proveedorEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", this.empresa.getCodigo());
            super.getParametros().put("persona", this.proveedor.getCodigo());
            super.getParametros().put("tipo", 2);
            super.getParametros().put("nombreReporte", "Ficha Proveedor");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
}
