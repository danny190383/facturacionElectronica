package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Ciudad;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Contactar;
import com.jvc.factunet.entidades.Contacto;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.EstadoCivil;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.RetencionPersona;
import com.jvc.factunet.entidades.Sexo;
import com.jvc.factunet.entidades.TipoCliente;
import com.jvc.factunet.entidades.TipoIdentificacion;
import com.jvc.factunet.icefacesUtil.CatalogosPersonaPedidosBean;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.icefacesUtil.JasperReporPedidostUtil;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.MascotaServicio;
import com.jvc.factunet.servicios.PersonaServicio;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class ClientePedidosBean extends CatalogosPersonaPedidosBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(ClientePedidosBean.class.getName());

    @EJB
    public ClienteServicio clienteServicio;
    @EJB
    private PersonaServicio personaServicio;
    @EJB
    private MascotaServicio mascotaServicio;
    @EJB
    private CuentaServicio cuentaServicio;
    
    private Cliente cliente;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private Integer tipoIdentificacionSlc;
    private TipoIdentificacion tipoIdentificacion;
    
    public ClientePedidosBean() {
        this.tipoIdentificacion = new TipoIdentificacion();
    }
    
    @PostConstruct
    public void init()
    {
        Path pathL = Paths.get(getServletContext().getRealPath("/") + File.separator + "temp");
        if (!Files.exists(pathL)) {
            try {
                Files.createDirectory(pathL);
            } catch (IOException ex) {
                Logger.getLogger(ClientePedidosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nuevoCliente();
        this.tipoIdentificacion = super.getListaTipoIdentificacion().get(0);
    }
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            if (this.cliente.getPersona().getFoto() != null && this.cliente.getPersona().getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.cliente.getPersona().getFoto());
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
    
    public void nuevoCliente() {
        try {
            this.cliente = new Cliente();
            this.cliente.setPersona(new Persona());
            this.cliente.getPersona().setSexo(super.getListaSexo().get(0));
            this.cliente.getPersona().setCiudad(super.getListaCiudad().get(0));
            this.cliente.setTipoCliente(super.getListaTipoCliente().get(0));
            this.cliente.getPersona().setEstadoCivil(super.getListaEstadoCivil().get(0));
            this.cliente.setCapacidadCredito(BigDecimal.ZERO);
            this.nombreLogo = "foto_hombre.jpg";
            this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
        } catch (Exception ex) {
            Logger.getLogger(ClientePedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void verContactar(Contactar contactar) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contactar", contactar);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cliente.getPersona());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("height", 300);
        options.put("contentHeight", 300);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/contactos/nuevoContactarDialog", options, null);
    }
    
    public void onContactarSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contactar contactar = (Contactar) event.getObject();
            if(this.cliente.getPersona().getContactarPersonaList() == null)
            {
                this.cliente.getPersona().setContactarPersonaList(new ArrayList<Contactar>());
            }
            if((!this.cliente.getPersona().getContactarPersonaList().contains(contactar)) && (contactar != null))
            {
                this.cliente.getPersona().getContactarPersonaList().add(contactar);
            }
        }
    }
    
    public void verContacto(Contacto contacto) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contacto", contacto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cliente.getPersona());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 650);
        options.put("height", 400);
        options.put("contentHeight", 400);
        options.put("contentWidth", 650);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/contactos/nuevoContactoDialog", options, null);
    }
    
    public void onContactoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contacto contacto = (Contacto) event.getObject();
            if(this.cliente.getPersona().getContactoPersonaList() == null)
            {
                this.cliente.getPersona().setContactoPersonaList(new ArrayList<Contacto>());
            }
            if((!this.cliente.getPersona().getContactoPersonaList().contains(contacto)) && (contacto != null))
            {
                this.cliente.getPersona().getContactoPersonaList().add(contacto);
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
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarPersonasDialog", options, null);
    }
    
    public void onPersonaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Contacto contacto = new Contacto();
            contacto.setPersona((Persona) event.getObject());
            contacto.setPersonaPadre(this.cliente.getPersona());
            if(this.cliente.getPersona().getContactoPersonaList() == null)
            {
                this.cliente.getPersona().setContactoPersonaList(new ArrayList<Contacto>());
            }
            this.cliente.getPersona().getContactoPersonaList().add(contacto);
        }
    }
    
    public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 950);
        options.put("height", 500);
        options.put("contentWidth", 950);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarClientesDialog", options, null);
    }
    
    public void verBusquedaNotaMedica() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("height", 500);
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarNotaMedicaDialog", options, null);
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.cliente = (Cliente) event.getObject();
            
            this.cargarFoto();
        }
    }
    
    public void verRetencion(RetencionPersona retencion) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoPantalla", 1);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("retencionPersona", retencion);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cliente.getPersona());
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
            RetencionPersona retenccion = (RetencionPersona) event.getObject();
            if(this.cliente.getPersona().getRetencionPersonaList() == null)
            {
                this.cliente.getPersona().setRetencionPersonaList(new ArrayList<RetencionPersona>());
            }
            if(retenccion != null)
            {
                if(!this.tieneRetencion(retenccion.getTipoRetencion().getCodigo()))
                { 
                    this.cliente.getPersona().getRetencionPersonaList().add(retenccion);
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("retencionyaagregada"));
                }
            }
        }
    }
    
    public void verMascota(Mascota mascota) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mascota", mascota);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cliente.getPersona());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 400);
        options.put("contentHeight", 400);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/ventas/extras/nuevaMascotaDialog", options, null);
    }
    
    public void onMascotaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Mascota mascota = (Mascota) event.getObject();
            if(this.cliente.getPersona().getMascotaPersonaList() == null)
            {
                this.cliente.getPersona().setMascotaPersonaList(new ArrayList<Mascota>());
            }
            if((!this.cliente.getPersona().getMascotaPersonaList().contains(mascota)) && (mascota != null))
            {
                this.cliente.getPersona().getMascotaPersonaList().add(mascota);
            }
            else
            {
                this.cliente.getPersona().getMascotaPersonaList().clear();
                this.cliente.getPersona().getMascotaPersonaList().addAll(this.mascotaServicio.listar(this.cliente.getPersona().getCodigo()));
            }
        }
    }
    
    public boolean tieneRetencion(Integer tipoRetencion)
    {
        for(RetencionPersona obj : this.cliente.getPersona().getRetencionPersonaList())
        {
            if(Objects.equals(tipoRetencion, obj.getTipoRetencion().getCodigo()))
            {
                return true;
            }
        }
        return false;
    }
    
    public void eliminarMascota(Mascota parametro) {
        try {
            this.cliente.getPersona().getMascotaPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarRetencion(RetencionPersona parametro) {
        try {
            this.cliente.getPersona().getRetencionPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public Boolean guardarCliente() throws IOException, Exception
    {
        Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
        byte[] foto = Files.readAllBytes(path);
        this.cliente.getPersona().setFoto(foto);
        this.cliente.getPersona().setNombres(this.cliente.getPersona().getNombres().trim().toUpperCase());
        this.cliente.getPersona().setApellidos(this.cliente.getPersona().getApellidos().trim().toUpperCase());
        if(this.cliente.getPersona().getCedula()!= null){
            if(this.cliente.getPersona().getCedula().trim().isEmpty())
            {
               this.cliente.getPersona().setCedula(null); 
            }
        }
        this.cliente = this.clienteServicio.actualizar(this.cliente);
        Cuenta cuenta = this.cuentaServicio.buscar(this.cliente.getPersona().getCedula());
        if(cuenta == null){
            this.crearCuenta(this.cliente);
        }
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        return Boolean.TRUE;
    }
    
    public Boolean guardar()
    {
        try {
            if(this.cliente.getPersona().getCodigo() != null)
            {
                return this.guardarCliente();
            }
            else
            {
                if(!this.verificarCedulaSistema())
                {
                    return this.guardarCliente();
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
    
    public void crearCuenta(Cliente cliente){
        try {
            this.cuentaServicio.crearCuenta(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ClientePedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean verificarCedulaSistema()
    {
        try {
            Cliente clienteTmp = this.clienteServicio.buscarCedula(this.cliente.getPersona().getCedula());
            if(clienteTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.cliente.getPersona().getCedula());
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
    
    public void eliminarContacto(Contacto parametro) {
        try {
            this.cliente.getPersona().getContactoPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void eliminarContactar(Contactar parametro) {
        try {
            this.cliente.getPersona().getContactarPersonaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void verificarPersona()
    {
        try {
            Cliente clienteTmp = this.clienteServicio.buscarCedula(this.cliente.getPersona().getCedula());
            if(clienteTmp == null)
            {
                Persona personaTmp = this.personaServicio.buscarCedula(this.cliente.getPersona().getCedula());
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
                    this.cliente.setPersona(personaTmp);
                    this.cargarFoto();
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("personaEncontrada"));
                }
            }
            else
            {
                this.cliente = clienteTmp;
                this.cargarFoto();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("clienteEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    public void cambiarValidacion()
    {
        for(TipoIdentificacion obj : super.getListaTipoIdentificacion())
        {
            if(Objects.equals(this.tipoIdentificacionSlc, obj.getCodigo()))
            {
                this.tipoIdentificacion = obj;
            }
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", "");
            super.getParametros().put("persona", this.cliente.getPersona().getCodigo());
            super.getParametros().put("tipo", 1);
            super.getParametros().put("nombreReporte", "Ficha Cliente");
            super.getParametros().put("SUBREPORT_DIR", JasperReporPedidostUtil.PATH);
            JasperReporPedidostUtil jasperBean = (JasperReporPedidostUtil) FacesUtils.getManagedBean(JasperReporPedidostUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReporPedidostUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public void subirArchivo(FileUploadEvent event) {
        InputStream in = null;
        try {
            List cellDataList = new ArrayList();
            in = event.getFile().getInputstream();
            XSSFWorkbook workBook = new XSSFWorkbook(in);
            XSSFSheet hssfSheet = workBook.getSheetAt(0);
            Iterator rowIterator = hssfSheet.rowIterator();
            while (rowIterator.hasNext()) {
                XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                Iterator iterator = hssfRow.cellIterator();
                List cellTempList = new ArrayList();
                while (iterator.hasNext()) {
                    XSSFCell hssfCell = (XSSFCell) iterator.next();
                    cellTempList.add(hssfCell);
                }
                cellDataList.add(cellTempList);
            }
            this.clienteServicio.insertarListaClientes(this.leer(cellDataList));
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir la imagen");
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }
    
    public String conversion(double valor)
    {
      Locale.setDefault(Locale.US);
      DecimalFormat num = new DecimalFormat("#");
      return num.format(valor);
    }
    
    private List<Cliente> leer(List cellDataList) {
        try {
            List<Cliente> clientes = new ArrayList<>();
            for (int i = 0; i < cellDataList.size(); i++) {
                try {
                    List cellTempList = (List) cellDataList.get(i);
                    Cliente clienteTmp = new Cliente();
                    Persona persona = new Persona();
                    String cedula;
                    persona.setNombres(((XSSFCell) cellTempList.get(0)).getStringCellValue().trim());
                    try {
                        cedula = ((XSSFCell) cellTempList.get(1)).getStringCellValue().trim();
                    } catch (Exception e) {
                        cedula = this.conversion(((XSSFCell) cellTempList.get(1)).getNumericCellValue());
                    }
                    if(!cedula.equals("S/N"))
                    {
                        if(cedula.contains("-")){
                            cedula = cedula.replace("-", "");
                        }
                        if(cedula.length() < 10){
                            while (cedula.length() < 10) {
                                cedula = "0".concat(cedula);
                            }
                        }
                        if((cedula.length() < 13) && (cedula.length() > 10)){
                            while (cedula.length() < 13) {
                                cedula = "0".concat(cedula);
                            }
                        }
                        if(cedula.length()>13)
                        {
                            LOG.log(Level.INFO, cedula);
                        }
                    }
                    persona.setCedula(cedula.equals("S/N") ? null : cedula);
                    persona.setCiudad(new Ciudad(29));
                    persona.setSexo(((XSSFCell) cellTempList.get(2)).getStringCellValue().trim().equals("M") ? new Sexo(28) : new Sexo(43));
                    persona.setEstadoCivil(((XSSFCell) cellTempList.get(3)).getStringCellValue().trim().equals("S") ? new EstadoCivil(30) : new EstadoCivil(46));
                    clienteTmp.setFechaCreacion(((XSSFCell) cellTempList.get(4)).getDateCellValue());
                    clienteTmp.setFechaUltimaCompra(((XSSFCell) cellTempList.get(5)).getDateCellValue());
                    persona.setDireccion(((XSSFCell) cellTempList.get(6)).getStringCellValue().trim());
                    Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
                    byte[] foto = Files.readAllBytes(path);
                    persona.setFoto(foto);
                    persona.setEstado("1");
                    clienteTmp.setTipoCliente(new TipoCliente(32));
                    clienteTmp.setPersona(persona);
                    clientes.add(clienteTmp);
                    LOG.log(Level.INFO, String.valueOf(i));
                } catch (Exception e) {
                    LOG.log(Level.INFO, "final");
                }
            }
            List<Cliente> clientesFinal = new ArrayList<>();
            Boolean ban;
            String cedula1;
            String cedula2;
            for(Cliente clienteA : clientes){
                ban = Boolean.TRUE;
                if(clienteA.getPersona().getCedula() != null){
                    for(Cliente clienteF : clientesFinal){
                        if(clienteF.getPersona().getCedula() != null){
                            cedula1 = clienteA.getPersona().getCedula().substring(0,9);
                            cedula2 = clienteF.getPersona().getCedula().substring(0,9);
                            if(cedula1.equals(cedula2)){
                                ban = Boolean.FALSE;
                                break;
                            }
                        }
                    }
                }
                if(ban){
                    clientesFinal.add(clienteA);
                    LOG.log(Level.INFO, clienteA.getPersona().getCedula());
                }
            }
            return clientesFinal;
        } catch (Exception ex) {
            Logger.getLogger(ClientePedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }  

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Integer getTipoIdentificacionSlc() {
        return tipoIdentificacionSlc;
    }

    public void setTipoIdentificacionSlc(Integer tipoIdentificacionSlc) {
        this.tipoIdentificacionSlc = tipoIdentificacionSlc;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }
}
