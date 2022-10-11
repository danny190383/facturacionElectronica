package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.PuntoRestriccion;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import com.jvc.factunet.entidades.TipoDocumento;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.TipoDocumentoServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;

@ManagedBean
@ViewScoped
public class PuntoVentaBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(PuntoVentaBean.class.getName());
    
    @EJB
    private CuentaServicio cuentaServicio;
    @EJB
    private TipoDocumentoServicio tipoDocumentoServicio;
    @EJB
    public BodegaServicio bodegaServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    
    private PuntoVenta puntoVenta;
    private DualListModel<Empleado> listaCuentas;
    private List<TipoDocumento> listaTipoDocumento; 
    private SecuenciaDocumento secuenciaDocumento;
    private Integer tipoDocumentoSlc;
    private Empresa empresa;
    private Integer bodegaSelc;
    private List<Bodega> listaBodegas;
    private PrintService[] services;
    private DualListModel<GrupoProducto> listaGrupos;
    
    public PuntoVentaBean() {
        this.listaTipoDocumento = new ArrayList<>();
        this.listaBodegas = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.services = PrintServiceLookup.lookupPrintServices(null, null);
        this.empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
        this.puntoVenta = (PuntoVenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("punto");
        this.listaTipoDocumento.addAll(this.tipoDocumentoServicio.listar());
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa.getCodigo()));
        if(this.puntoVenta == null)
        {
            this.iniciarPuntoVenta(); 
        }
        else
        {
            this.bodegaSelc = this.puntoVenta.getBodega().getCodigo();
        }
        this.cargarCuentas();
        this.cargarGrupos();
        this.nuevoSecuencia();
    }
    
    public void cargarGrupos()
    {
        List<GrupoProducto> grupoSource = this.grupoProductoServicio.listarSinHijos(this.empresa.getCodigo());
        List<GrupoProducto> grupoTarget = this.grupoProductoServicio.listarPorPuntoVenta(this.puntoVenta.getCodigo());
        for(GrupoProducto grupoT : grupoTarget){
            for(GrupoProducto grupoS : grupoSource){
                if(Objects.equals(grupoT.getCodigo(), grupoS.getCodigo())){
                    grupoSource.remove(grupoS);
                    break;
                }
            }
        }
        this.listaGrupos = new DualListModel<>(grupoSource, grupoTarget);
    }
    
    public void nuevoSecuencia()
    {
        this.secuenciaDocumento = new SecuenciaDocumento();
        this.secuenciaDocumento.setPuntoVenta(this.puntoVenta);
        this.secuenciaDocumento.setDesde(0);
        this.secuenciaDocumento.setHasta(0);
        this.secuenciaDocumento.setMaxItems(0);
        this.secuenciaDocumento.setAutoPrint("2");
        this.tipoDocumentoSlc = this.listaTipoDocumento.get(0).getCodigo();
    }
    
    public void cargarCuentas()
    {
        List<Empleado> cuentaSource = this.cuentaServicio.listarSinPuntoVenta(this.empresa.getCodigo());
        List<Empleado> cuentaTarget = this.cuentaServicio.listarPuntoVenta(this.puntoVenta.getCodigo(), this.empresa.getCodigo());
        this.listaCuentas = new DualListModel<>(cuentaSource, cuentaTarget);
    }
    
    public void iniciarPuntoVenta()
    {
        this.puntoVenta = new PuntoVenta();
        this.puntoVenta.setEmpresa(this.empresa);
        this.puntoVenta.setSecuenciaDocumentoList(new ArrayList<>());
        this.puntoVenta.setUsuarioPuntoVentaList(new ArrayList<>());
        this.puntoVenta.setPuntoRestriccionList(new ArrayList<>());
        this.puntoVenta.setTablet("2");
        this.bodegaSelc = this.listaBodegas.get(0).getCodigo();
    }
    
    public void verificarCuentas()
    {
        List<Empleado> listaSource = this.listaCuentas.getSource();
        List<Empleado> listaTarget = this.listaCuentas.getTarget();
        
        for(Empleado cuenta: listaSource)
        {
            cuenta.setPuntoVenta(null);
        }
        
        for(Empleado cuenta: listaTarget)
        {
            cuenta.setPuntoVenta(this.puntoVenta);
        }
        List<Empleado> insertar = new ArrayList();
        insertar.addAll(listaSource);
        insertar.addAll(listaTarget);
        this.puntoVenta.getUsuarioPuntoVentaList().addAll(insertar);
    }
    
    public void verificarGrupos()
    {
        List<GrupoProducto> listaTarget = this.listaGrupos.getTarget();
        
        List<PuntoRestriccion> puntoRestriccionTarget = new ArrayList<>();
        for(GrupoProducto grupo: listaTarget)
        {
            PuntoRestriccion restriccion = new PuntoRestriccion();
            restriccion.setGrupo(grupo);
            restriccion.setPuntoVenta(this.puntoVenta);
            puntoRestriccionTarget.add(restriccion);
        }
        this.puntoVenta.getPuntoRestriccionList().clear();
        this.puntoVenta.getPuntoRestriccionList().addAll(puntoRestriccionTarget);
    }
    
    public void agregarSecuencia()
    {
        Boolean ban = Boolean.FALSE;
        for(SecuenciaDocumento sec : this.puntoVenta.getSecuenciaDocumentoList())
        {
            if((this.tipoDocumentoSlc.equals(sec.getTipoDocumento().getCodigo())) && (sec.getEstado().equals("1")))
            {
                ban = Boolean.TRUE;
                break;
            }
        }
        if(!ban)
        {
            if(this.secuenciaDocumento.getHasta() > 0)
            {
                if(this.secuenciaDocumento.getDesde() < this.secuenciaDocumento.getHasta())
                {
                    this.secuenciaDocumento.setNumeroActual(this.secuenciaDocumento.getDesde() -1 );
                    this.secuenciaDocumento.setEstado("1");
                    this.secuenciaDocumento.setTipoDocumento(this.setearTipoDocumento());
                    this.puntoVenta.getSecuenciaDocumentoList().add(this.secuenciaDocumento);
                    this.nuevoSecuencia();
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("desdeMenor"));
                }
            }
            else
            {
                this.secuenciaDocumento.setNumeroActual(this.secuenciaDocumento.getDesde() -1 );
                this.secuenciaDocumento.setEstado("1");
                this.secuenciaDocumento.setHasta(null);
                this.secuenciaDocumento.setTipoDocumento(this.setearTipoDocumento());
                this.puntoVenta.getSecuenciaDocumentoList().add(this.secuenciaDocumento);
                this.nuevoSecuencia();
            }
            
        }
        else
        {
             FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("existeRango"));
        }
    }
    
    public TipoDocumento setearTipoDocumento()
    {
        for(TipoDocumento obj : this.listaTipoDocumento)
        {
            if(Objects.equals(this.tipoDocumentoSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        this.puntoVenta.setNombre(this.puntoVenta.getNombre().trim().toUpperCase());
        this.puntoVenta.setCodigoSri(this.puntoVenta.getCodigoSri().trim());
        this.puntoVenta.setBodega(this.verBodega());
        if(!(this.puntoVenta.getNombre().isEmpty() || this.puntoVenta.getCodigoSri().isEmpty()))
        {
            this.verificarCuentas();
            this.verificarGrupos();
            PrimeFaces.current().dialog().closeDynamic(this.puntoVenta);
        }
        else
        {
             FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("nombrecodigoObligatorios"));
        }
    }
    
    public Bodega verBodega(){
        for(Bodega bodega : this.listaBodegas){
            if(Objects.equals(this.bodegaSelc, bodega.getCodigo())){
                return bodega;
            }
        }
        return null;
    }
    
    public void eliminarSecuencia(SecuenciaDocumento parametro) {
        try {
            this.puntoVenta.getSecuenciaDocumentoList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void subirFirma(FileUploadEvent event) {
        this.puntoVenta.setFirmaElectronica(event.getFile().getContent()); 
    }

    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public DualListModel<Empleado> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(DualListModel<Empleado> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<TipoDocumento> getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public SecuenciaDocumento getSecuenciaDocumento() {
        return secuenciaDocumento;
    }

    public void setSecuenciaDocumento(SecuenciaDocumento secuenciaDocumento) {
        this.secuenciaDocumento = secuenciaDocumento;
    }

    public Integer getTipoDocumentoSlc() {
        return tipoDocumentoSlc;
    }

    public void setTipoDocumentoSlc(Integer tipoDocumentoSlc) {
        this.tipoDocumentoSlc = tipoDocumentoSlc;
    }

    public Integer getBodegaSelc() {
        return bodegaSelc;
    }

    public void setBodegaSelc(Integer bodegaSelc) {
        this.bodegaSelc = bodegaSelc;
    }

    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public PrintService[] getServices() {
        return services;
    }

    public void setServices(PrintService[] services) {
        this.services = services;
    }

    public DualListModel<GrupoProducto> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(DualListModel<GrupoProducto> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }
}
