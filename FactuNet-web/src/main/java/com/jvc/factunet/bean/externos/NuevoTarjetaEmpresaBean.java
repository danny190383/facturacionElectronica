package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Banco;
import com.jvc.factunet.entidades.ComisionTarjeta;
import com.jvc.factunet.entidades.FactorPlazoTarjeta;
import com.jvc.factunet.entidades.FormaPago;
import com.jvc.factunet.entidades.RetencionTarjeta;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import com.jvc.factunet.entidades.TipoRetencion;
import com.jvc.factunet.entidades.TipoTarjeta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BancoServicio;
import com.jvc.factunet.servicios.FormaPagoServicio;
import com.jvc.factunet.servicios.TipoRetencionServicio;
import com.jvc.factunet.servicios.TipoTarjetaServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoTarjetaEmpresaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoTarjetaEmpresaBean.class.getName());

    @EJB
    private FormaPagoServicio formaPagoServicio;
    @EJB
    private BancoServicio bancoServicio;
    @EJB
    private TipoTarjetaServicio tipoTarjetaServicio;
    @EJB
    private TipoRetencionServicio tipoRetencionServicio;
    
    private TarjetaEmpresa tarjetaEmpresa;
    private ComisionTarjeta comisionTarjeta;
    private RetencionTarjeta retencionTarjeta;
    private FactorPlazoTarjeta factorPlazoTarjeta;
    private List<FormaPago> listaFormaPagoTarjeta;
    private List<TipoTarjeta> listaTipoTarjeta;
    private List<Banco> listaBancos;
    private List<TipoRetencion> listaTipoRetencion;
    private boolean estado;
    private Integer formaTarjetaSlc;
    private Integer tipoTarjetaSlc;
    private Integer bancoSlc;
    private Integer retencionSlc;
    private Boolean editar;
    
    public NuevoTarjetaEmpresaBean() {
        this.listaTipoRetencion = new ArrayList<>();
        this.listaFormaPagoTarjeta = new ArrayList<>();
        this.listaBancos = new ArrayList<>();
        this.listaTipoTarjeta = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.tarjetaEmpresa = (TarjetaEmpresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tarjeta");
        this.listaTipoRetencion.addAll(this.tipoRetencionServicio.listar());
        this.listaBancos.addAll(this.bancoServicio.listar());
        this.listaTipoTarjeta.addAll(this.tipoTarjetaServicio.listar());
        this.listaFormaPagoTarjeta.addAll(this.formaPagoServicio.listarTipo(3));
        if(this.tarjetaEmpresa == null)
        {
            this.inicializarTarjeta();
        }
        else
        {
            this.bancoSlc = this.tarjetaEmpresa.getBanco().getCodigo();
            this.tipoTarjetaSlc = this.tarjetaEmpresa.getTipoTarjeta().getCodigo();
            this.estado = "1".equals(this.tarjetaEmpresa.getEstado()) ? (this.estado = Boolean.TRUE) : (this.estado = Boolean.FALSE);
        }
        this.inicializarComision();
        this.inicializarRetencion();
        this.inicializarFactor();
    }
    
    public void inicializarTarjeta()
    {
        this.tarjetaEmpresa = new TarjetaEmpresa();
        this.tarjetaEmpresa.setFecha(new Date());
        this.tarjetaEmpresa.setDetalle(StringUtils.EMPTY);
        this.tarjetaEmpresa.setComisionTarjetaList(new ArrayList<>());
        this.tarjetaEmpresa.setRetencionTarjetaList(new ArrayList<>());
        this.tarjetaEmpresa.setFactorPlazoTarjetaList(new ArrayList<>());
        this.bancoSlc = this.listaBancos.get(0).getCodigo();
        this.tipoTarjetaSlc = this.listaTipoTarjeta.get(0).getCodigo();
        this.estado = Boolean.TRUE;
    }
    
    public void inicializarComision()
    {
        this.comisionTarjeta = new ComisionTarjeta();
        this.comisionTarjeta.setTarjetaEmpresa(this.tarjetaEmpresa);
        this.comisionTarjeta.setObservacion(StringUtils.EMPTY);
        this.comisionTarjeta.setNombre(StringUtils.EMPTY);
        this.comisionTarjeta.setValor(BigDecimal.ZERO);
        this.comisionTarjeta.setInteres(1);
        this.formaTarjetaSlc = this.listaFormaPagoTarjeta.get(0).getCodigo();
        this.editar = Boolean.FALSE;
    }
    
    public void inicializarRetencion()
    {
        this.retencionTarjeta = new RetencionTarjeta();
        this.retencionTarjeta.setTarjetaEmpresa(this.tarjetaEmpresa);
        this.retencionSlc = this.listaTipoRetencion.get(0).getCodigo();
        this.retencionTarjeta.setObservacion(StringUtils.EMPTY);
        this.editar = Boolean.FALSE;
    }
    
    public void inicializarFactor()
    {
        this.factorPlazoTarjeta = new FactorPlazoTarjeta();
        this.factorPlazoTarjeta.setTarjetaEmpresa(this.tarjetaEmpresa);
        this.factorPlazoTarjeta.setObservacion(StringUtils.EMPTY);
        this.factorPlazoTarjeta.setFactor(BigDecimal.ZERO);
        this.factorPlazoTarjeta.setPlazo(0);
        this.editar = Boolean.FALSE; 
    }
    
    public void agregarComision()
    {
        if((this.comisionTarjeta.getValor() != null) && (this.comisionTarjeta.getValor().floatValue() > 0))
        {
            this.comisionTarjeta.setFormaPago(this.setearFormaPagoTarjeta());
            if((this.comisionTarjeta.getFormaPago().getCodigo() != 157) && (this.comisionTarjeta.getMeses() == null) )
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("mesesmayorcero"));
            }
            else
            {
                if(this.comisionTarjeta.getFormaPago().getCodigo() == 157)
                {
                    this.comisionTarjeta.setNombre(this.comisionTarjeta.getFormaPago().getNombre());
                }
                else
                {
                    this.comisionTarjeta.setNombre(this.comisionTarjeta.getFormaPago().getNombre() + " " + this.comisionTarjeta.getMeses() + " MESES " +  (this.comisionTarjeta.getInteres().equals(1) ? "CON INTERES" : "SIN INTERES"));
                }
                if(!this.editar)
                {
                    this.tarjetaEmpresa.getComisionTarjetaList().add(this.comisionTarjeta);
                }
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                PrimeFaces.current().executeScript("PF('dlgComision').hide();");
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("comisionmayorcero"));
        }
    }
    
    public void eliminarComision(ComisionTarjeta parametro) {
        try {
            this.tarjetaEmpresa.getComisionTarjetaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarComision(ComisionTarjeta parametro) {
        this.comisionTarjeta = parametro;
        this.formaTarjetaSlc = parametro.getFormaPago().getCodigo();
        this.editar = Boolean.TRUE;
    }
    
    public void agregarRetencion()
    {
        if(!this.editar)
        {
            if(!this.tieneRetencion(this.retencionSlc))
            { 
                this.retencionTarjeta.setTipoRetencion(this.setearRetencion());
                this.tarjetaEmpresa.getRetencionTarjetaList().add(retencionTarjeta);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("retencionyaagregada"));
            }
        }
        else
        {
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        }
        PrimeFaces.current().executeScript("PF('dlgRetencion').hide();");    
    }
    
    public boolean tieneRetencion(Integer tipoRetencion)
    {
        for(RetencionTarjeta obj : this.tarjetaEmpresa.getRetencionTarjetaList())
        {
            if(Objects.equals(tipoRetencion, obj.getTipoRetencion().getCodigo()))
            {
                return true;
            }
        }
        return false;
    }
    
    public void eliminarRetencion(RetencionTarjeta parametro) {
        try {
            this.tarjetaEmpresa.getRetencionTarjetaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarRetencion(RetencionTarjeta parametro) {
        this.retencionTarjeta = parametro;
        this.retencionSlc = parametro.getTipoRetencion().getCodigo();
        this.editar = Boolean.TRUE;
    }
    
    public void agregarFactor()
    {
        if(!this.editar)
        {
            this.tarjetaEmpresa.getFactorPlazoTarjetaList().add(this.factorPlazoTarjeta);
        }
        FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        PrimeFaces.current().executeScript("PF('dlgFactor').hide();");    
    }
    
    public void eliminarFactor(FactorPlazoTarjeta parametro) {
        try {
            this.tarjetaEmpresa.getFactorPlazoTarjetaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarFactor(FactorPlazoTarjeta parametro) {
        this.factorPlazoTarjeta = parametro;
        this.editar = Boolean.TRUE;
    }
    
    public Banco setearBanco()
    {
        for(Banco obj : this.listaBancos)
        {
            if(Objects.equals(this.bancoSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public FormaPago setearFormaPagoTarjeta()
    {
        for(FormaPago obj : this.listaFormaPagoTarjeta)
        {
            if(Objects.equals(this.formaTarjetaSlc, obj.getCodigo()))
            {
                return obj; 
            }
        }
        return null;
    }
    
    public TipoTarjeta setearTipoTarjeta()
    {
        for(TipoTarjeta obj : this.listaTipoTarjeta)
        {
            if(Objects.equals(this.tipoTarjetaSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public TipoRetencion setearRetencion()
    {
        for(TipoRetencion obj : this.listaTipoRetencion)
        {
            if(Objects.equals(this.retencionSlc, obj.getCodigo()))
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
        if(this.tarjetaEmpresa.getComisionTarjetaList() == null || this.tarjetaEmpresa.getComisionTarjetaList().isEmpty()){
            FacesUtils.addErrorMessage("Debe registrar al menos un recargo");
            return;
        }
        if((this.tarjetaEmpresa.getDiasCobro() != null) && (this.tarjetaEmpresa.getDiasCobro().floatValue() > 0))
        {
            this.tarjetaEmpresa.setEstado(this.estado ? "1" : "2");
            this.tarjetaEmpresa.setBanco(this.setearBanco());
            this.tarjetaEmpresa.setTipoTarjeta(this.setearTipoTarjeta());
            PrimeFaces.current().dialog().closeDynamic(this.tarjetaEmpresa);
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("diascobromayor"));
        }
    }

    public List<FormaPago> getListaFormaPagoTarjeta() {
        return listaFormaPagoTarjeta;
    }

    public void setListaFormaPagoTarjeta(List<FormaPago> listaFormaPagoTarjeta) {
        this.listaFormaPagoTarjeta = listaFormaPagoTarjeta;
    }

    public List<TipoTarjeta> getListaTipoTarjeta() {
        return listaTipoTarjeta;
    }

    public void setListaTipoTarjeta(List<TipoTarjeta> listaTipoTarjeta) {
        this.listaTipoTarjeta = listaTipoTarjeta;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<TipoRetencion> getListaTipoRetencion() {
        return listaTipoRetencion;
    }

    public void setListaTipoRetencion(List<TipoRetencion> listaTipoRetencion) {
        this.listaTipoRetencion = listaTipoRetencion;
    }

    public TarjetaEmpresa getTarjetaEmpresa() {
        return tarjetaEmpresa;
    }

    public void setTarjetaEmpresa(TarjetaEmpresa tarjetaEmpresa) {
        this.tarjetaEmpresa = tarjetaEmpresa;
    }

    public ComisionTarjeta getComisionTarjeta() {
        return comisionTarjeta;
    }

    public void setComisionTarjeta(ComisionTarjeta comisionTarjeta) {
        this.comisionTarjeta = comisionTarjeta;
    }

    public RetencionTarjeta getRetencionTarjeta() {
        return retencionTarjeta;
    }

    public void setRetencionTarjeta(RetencionTarjeta retencionTarjeta) {
        this.retencionTarjeta = retencionTarjeta;
    }

    public FactorPlazoTarjeta getFactorPlazoTarjeta() {
        return factorPlazoTarjeta;
    }

    public void setFactorPlazoTarjeta(FactorPlazoTarjeta factorPlazoTarjeta) {
        this.factorPlazoTarjeta = factorPlazoTarjeta;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getFormaTarjetaSlc() {
        return formaTarjetaSlc;
    }

    public void setFormaTarjetaSlc(Integer formaTarjetaSlc) {
        this.formaTarjetaSlc = formaTarjetaSlc;
    }

    public Integer getTipoTarjetaSlc() {
        return tipoTarjetaSlc;
    }

    public void setTipoTarjetaSlc(Integer tipoTarjetaSlc) {
        this.tipoTarjetaSlc = tipoTarjetaSlc;
    }

    public Integer getBancoSlc() {
        return bancoSlc;
    }

    public void setBancoSlc(Integer bancoSlc) {
        this.bancoSlc = bancoSlc;
    }

    public Integer getRetencionSlc() {
        return retencionSlc;
    }

    public void setRetencionSlc(Integer retencionSlc) {
        this.retencionSlc = retencionSlc;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }
}
