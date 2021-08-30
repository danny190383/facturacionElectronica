package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Contactar;
import com.jvc.factunet.entidades.Contacto;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.EstadoCivil;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.Sexo;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.EmpleadoServicio;
import com.jvc.factunet.servicios.EmpresaServicio;
import com.jvc.factunet.servicios.PersonaServicio;
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
import java.util.List;
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
public class EmpleadoBean extends CatalogosPersonaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(EmpleadoBean.class.getName());

    @EJB
    private EmpleadoServicio empleadoServicio;
    @EJB
    private PersonaServicio personaServicio;
    @EJB
    private EmpresaServicio empresaServicio;
    
    private List<Empresa> listaEmpresas;
    private Empleado empleado;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public EmpleadoBean() {
        this.listaEmpresas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaEmpresas.addAll(this.empresaServicio.listar());
        Path pathL = Paths.get(getServletContext().getRealPath("/") + File.separator + "temp");
        if (!Files.exists(pathL)) {
            try {
                Files.createDirectory(pathL);
            } catch (IOException ex) {
                Logger.getLogger(ProveedorBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nuevoEmpleado();
    }
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            if (this.empleado.getPersona().getFoto() != null && this.empleado.getPersona().getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.empleado.getPersona().getFoto());
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
    
    public void nuevoEmpleado() {
        this.empleado = new Empleado();
        this.empleado.setPersona(new Persona());
        this.empleado.getPersona().setSexo(super.getListaSexo().get(0));
        this.empleado.getPersona().setCiudad(super.getListaCiudad().get(0));
        this.empleado.setCargo(super.getListaCargo().get(0));
        this.empleado.getPersona().setEstadoCivil(super.getListaEstadoCivil().get(0));
        this.empleado.getPersona().setTipoIdentificacion(super.getListaTipoIdentificacion().get(0)); 
        this.empleado.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.nombreLogo = "foto_hombre.jpg";
        this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
    }
    
    public void verContactar(Contactar contactar) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contactar", contactar);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.empleado);
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
            if(this.empleado.getContactarPersonaList() == null)
            {
                this.empleado.setContactarPersonaList(new ArrayList<Contactar>());
            }
            if((!this.empleado.getContactarPersonaList().contains(contactar)) && (contactar != null))
            {
                this.empleado.getContactarPersonaList().add(contactar);
            }
        }
    }
    
    public void verContacto(Contacto contacto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contacto", contacto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.empleado);
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
            if(this.empleado.getContactoPersonaList() == null)
            {
                this.empleado.setContactoPersonaList(new ArrayList<Contacto>());
            }
            if((!this.empleado.getContactoPersonaList().contains(contacto)) && (contacto != null))
            {
                this.empleado.getContactoPersonaList().add(contacto);
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
            contacto.setPersonaPadre(this.empleado);
            contacto.setEmpresa(this.empresa); 
            contacto.setEstado(Boolean.TRUE); 
            this.empleado.getContactoPersonaList().add(contacto);
        }
    }
    
    public void verBusquedaEmpleados() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarEmpleadosDialog", options, null);
    }
    
    public void onEmpleadoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.empleado = (Empleado) event.getObject();
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
            this.empleado.getContactoPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarContactoWeb(Contactar parametro) {
        try {
            this.empleado.getContactarPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void guardarEmpleado() throws IOException, Exception
    {
        Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
        byte[] foto = Files.readAllBytes(path);
        this.empleado.getPersona().setFoto(foto);
        this.empleado.getPersona().setNombres(this.empleado.getPersona().getNombres().trim().toUpperCase());
        this.empleado.getPersona().setApellidos(this.empleado.getPersona().getApellidos().trim().toUpperCase());
        if(this.empleado.getCodigo() == null){
            this.empleado.setEmpresa(this.empresa); 
        }
        this.empleado = this.empleadoServicio.actualizar(this.empleado);
        if(this.empleado.getCuenta() == null)
        {
            this.empleadoServicio.crearCuenta(this.empleado);
        }
    }
    
    public void guardar()
    {
        try {
            if(this.empleado.getPersona().getCodigo() != null)
            {
                this.guardarEmpleado();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            }
            else
            {
                if(!this.verificarCedulaSistema())
                {
                    this.guardarEmpleado();
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                }
                else
                {
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaExistente"));
                }
            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public Boolean verificarCedulaSistema()
    {
        try {
            Empleado empleadoTmp = this.empleadoServicio.buscarCedula(this.empleado.getPersona().getCedula(), this.empresa.getCodigo());
            if(empleadoTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.empleado.getPersona().getCedula());
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
            Empleado empleadoTmp = this.empleadoServicio.buscarCedula(this.empleado.getPersona().getCedula(), this.empresa.getCodigo());
            if(empleadoTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.empleado.getPersona().getCedula());
                if(personaTmp == null)
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
                }
                else
                {
                    if(personaTmp.getSexo() == null)
                    {
                        personaTmp.setSexo(new Sexo(super.getListaSexo().get(0).getCodigo()));
                    }
                    if(personaTmp.getEstadoCivil() == null)
                    {
                        personaTmp.setEstadoCivil(new EstadoCivil(super.getListaEstadoCivil().get(0).getCodigo()));
                    }
                    this.empleado.setPersona(personaTmp);
                    this.cargarFoto();
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaEncontrada"));
                }
            }
            else
            {
                this.empleado = empleadoTmp;
                this.cargarFoto();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("empleadoEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", this.empresa);
            super.getParametros().put("persona", this.empleado.getPersona().getCodigo());
            super.getParametros().put("tipo", 4);
            super.getParametros().put("nombreReporte", "Ficha Transportista");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }
}
