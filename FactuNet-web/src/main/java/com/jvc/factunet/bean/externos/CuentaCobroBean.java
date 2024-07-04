package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Banco;
import com.jvc.factunet.entidades.ComisionTarjeta;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FormaPago;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BancoServicio;
import com.jvc.factunet.servicios.FormaPagoServicio;
import com.jvc.factunet.servicios.TarjetaEmpresaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
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
public class CuentaCobroBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(CuentaCobroBean.class.getName());
    
    @EJB
    private FormaPagoServicio formaPagoServicio;
    @EJB
    private BancoServicio bancoServicio;
    @EJB
    private TarjetaEmpresaServicio tarjetaEmpresaServicio;

    private BigDecimal totalFactura;
    private List<FacturaPago> listaFacturaPago;
    private CuentaFactura cuentaFactura;
    private FacturaPago retencion;
    private List<FormaPago> listaFormaPago;
    private List<FormaPago> listaFormaPagoCuenta;
    private List<Banco> listaBancos;
    private List<TarjetaEmpresa> listaTarjetaEmpresa;
    private List<ComisionTarjeta> listaComisionTarjeta;
    private Integer formaSlc;
    private Integer formaCuentaSlc;
    private Integer tarjetaEmpresaSlc;
    private Integer comisionTarjetaSlc;
    private Integer bancoSlc;
    private BigDecimal montoPago;
    private BigDecimal totalPagos;
    private Integer numeroCuentas;
    private Integer diasCredito;
    private Integer empresa;
    private Integer lugar;
    
    public CuentaCobroBean() {
        this.retencion = new FacturaPago();
        this.listaFormaPago = new ArrayList<>();
        this.listaFormaPagoCuenta = new ArrayList<>();
        this.listaBancos = new ArrayList<>();
        this.listaTarjetaEmpresa = new ArrayList<>();
        this.listaComisionTarjeta = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
        this.lugar = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lugar");
        this.totalFactura = (BigDecimal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pago");
        this.listaFacturaPago = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaPagos");
        this.retencion = null;
        for(FacturaPago obj : this.listaFacturaPago)
        {
            if(obj.getFormaPago().getCodigo() == 170)
            {
                this.retencion = obj;
                break;
            }
        }
        if(this.listaFacturaPago == null)
        {
             this.listaFacturaPago = new ArrayList<>();
        }
        this.numeroCuentas = 1;
        this.diasCredito = 0;
        this.listaBancos.addAll(this.bancoServicio.listar());
        this.listaFormaPago.addAll(this.formaPagoServicio.listarTipo(1));
        this.listaFormaPagoCuenta.addAll(this.formaPagoServicio.listarTipo(2));
        this.listaTarjetaEmpresa.addAll(this.tarjetaEmpresaServicio.listar(this.empresa));
        this.listaComisionTarjeta.addAll(this.listaTarjetaEmpresa.get(0).getComisionTarjetaList());
        this.formaSlc = this.listaFormaPago.get(0).getCodigo();
        this.formaCuentaSlc = this.listaFormaPagoCuenta.get(0).getCodigo();
        this.bancoSlc = this.listaBancos.get(0).getCodigo();
        this.tarjetaEmpresaSlc = this.listaTarjetaEmpresa.get(0).getCodigo();
        this.comisionTarjetaSlc = this.listaTarjetaEmpresa.get(0).getComisionTarjetaList().get(0).getCodigo();    
        this.verificarRetencion();
        this.inicializarCuenta();
    }
    
    public void inicializarCuenta()
    {
        this.cuentaFactura = new CuentaFactura();
        this.cuentaFactura.setFechaVencimiento(new Date());
        this.cuentaFactura.setDetalle(StringUtils.EMPTY); 
    }
    
    public void verificarRetencion()
    {
        if(this.lugar != 3)
        {
            for(FacturaPago obj : this.listaFacturaPago)
            {
                if(obj.getFormaPago().getCodigo() == 170)
                {
                    this.listaFacturaPago.remove(obj);
                    break;
                }
            }
            if(!((this.formaSlc == 149) && (this.formaCuentaSlc == 153)))
            {
                if(this.retencion != null)
                {
                    this.listaFacturaPago.add(this.retencion);
                }
            }
        }
        this.calcularTotal();
        this.calcularMonto();
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if((this.lugar == 1) || (this.lugar == 3))
        {
            PrimeFaces.current().dialog().closeDynamic(this.listaFacturaPago);
        }
        else
        {
            if (this.totalFactura.compareTo(this.totalPagos) != 0) {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("pagoIgual"));
            }
            else
            {
                PrimeFaces.current().dialog().closeDynamic(this.listaFacturaPago);
            }
        }
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
    
    public FormaPago setearFormaPago()
    {
        for(FormaPago obj : this.listaFormaPago)
        {
            if(Objects.equals(this.formaSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public FormaPago setearFormaPagoCuenta()
    {
        for(FormaPago obj : this.listaFormaPagoCuenta)
        {
            if(Objects.equals(this.formaCuentaSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public ComisionTarjeta setearComisionTarjeta()
    {
        for(ComisionTarjeta obj : this.listaComisionTarjeta)
        {
            if(Objects.equals(this.comisionTarjetaSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public TarjetaEmpresa setearTarjetaEmpresa()
    {
        for(TarjetaEmpresa obj : this.listaTarjetaEmpresa)
        {
            if(Objects.equals(this.tarjetaEmpresaSlc, obj.getCodigo()))
            {
                return obj;
            }
        }
        return null;
    }
    
    public Boolean agregarPago()
    {
        if(this.montoPago.doubleValue() > 0)
        {
            if (this.totalPagos.add(this.montoPago).compareTo(this.totalFactura) > 0){
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoError"));
                return Boolean.FALSE;
            }
            else
            {
                FacturaPago pago1 = new FacturaPago();
                if(this.retencion != null){
                    pago1.setFactura(this.retencion.getFactura()); 
                }
                pago1.setObservacion(this.cuentaFactura.getDetalle()); 
                pago1.setFormaPago(this.setearFormaPago());
                pago1.setValor(this.montoPago);
                if(pago1.getFormaPago().getCodigo() == 149)
                {
                    pago1.setCuentaFacturaList(new ArrayList<>());
                    pago1 = this.agregarPagoCuenta(pago1);
                }
                this.listaFacturaPago.add(pago1);
                this.calcularTotal();
                this.calcularMonto();
                return Boolean.TRUE;
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoMayorCero"));
            return Boolean.FALSE;
        }
    }
    
    public FacturaPago agregarPagoCuenta(FacturaPago pago)
    {
        if(this.formaCuentaSlc == 153)
        {
            this.numeroCuentas = 1;
            TarjetaEmpresa tarjeta = this.setearTarjetaEmpresa();
            this.diasCredito = tarjeta.getDiasCobro();
        }
        float diasCreditoCuenta = (this.diasCredito / this.numeroCuentas);
        int dias = (int) diasCreditoCuenta;
        if(this.numeroCuentas > 1)
        {
            BigDecimal bd3 = this.montoPago.divide(new BigDecimal(this.numeroCuentas), RoundingMode.HALF_UP);
            this.montoPago = bd3;
        }
        this.cuentaFactura.setFormaPago(this.setearFormaPago());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for(Integer i = 1; i<=this.numeroCuentas ; i++)
        {
            CuentaFactura cuenta = new CuentaFactura();
            cuenta.setNumero(this.cuentaFactura.getNumero());
            cuenta.setFormaPago(this.setearFormaPagoCuenta());
            cuenta.setBanco(this.setearBanco());
            cuenta.setValor(this.montoPago);
            cuenta.setSaldo(this.montoPago);
            cuenta.setFacturaPago(pago);
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+dias);
            Date fecha = calendar.getTime();
            cuenta.setFechaVencimiento(fecha);
            pago.getCuentaFacturaList().add(cuenta);
            calendar.setTime(calendar.getTime());
            if(this.formaCuentaSlc == 153)
            {
                cuenta.setComisionTarjeta(this.setearComisionTarjeta());
            }
        }
        this.numeroCuentas = 1;
        this.diasCredito = 0;
        this.inicializarCuenta();
        return pago;
    }
    
    public void calcularFechaVencimiento()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+this.diasCredito);
        this.cuentaFactura.setFechaVencimiento(calendar.getTime());
    }
    
    public void calcularTotal()
    {
        this.totalPagos = BigDecimal.ZERO;
        for(FacturaPago pago : this.listaFacturaPago)
        {
             this.totalPagos = this.totalPagos.add(pago.getValor());
        }
        this.totalPagos.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public void calcularMonto()
    {
        this.montoPago = this.totalFactura.subtract(this.totalPagos).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (this.montoPago.compareTo(BigDecimal.ZERO) == -1){
            this.totalPagos = this.totalPagos.add(this.montoPago).setScale(2, BigDecimal.ROUND_HALF_UP);
            this.montoPago = BigDecimal.ZERO;
        }
    }
    
    public void eliminarCuenta(FacturaPago parametro) {
        try {
            this.listaFacturaPago.remove(parametro);
            this.calcularTotal();
            this.calcularMonto();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void seleccionarFacturaPago(FacturaPago parametro)
    {
        this.montoPago = parametro.getValor();
        this.formaSlc = parametro.getFormaPago().getCodigo();
        this.listaFacturaPago.remove(parametro);
        this.calcularTotal();
    }
    
    public void seleccionarCuenta(CuentaFactura parametro)
    {
        this.cuentaFactura = parametro;
        this.montoPago = this.cuentaFactura.getValor();
        this.formaSlc = this.cuentaFactura.getFacturaPago().getFormaPago().getCodigo();
        this.formaCuentaSlc = this.cuentaFactura.getFormaPago().getCodigo();
        this.bancoSlc = this.cuentaFactura.getBanco().getCodigo();
        this.calcularTotal();
    }
    
    public void cargarComisiones()
    {
        for(TarjetaEmpresa obj : this.listaTarjetaEmpresa)
        {
            if(Objects.equals(this.tarjetaEmpresaSlc, obj.getCodigo()))
            {
                this.listaComisionTarjeta.clear();
                this.listaComisionTarjeta.addAll(obj.getComisionTarjetaList());
                break;
            }
        }
    }

    public List<FormaPago> getListaFormaPago() {
        return listaFormaPago;
    }

    public void setListaFormaPago(List<FormaPago> listaFormaPago) {
        this.listaFormaPago = listaFormaPago;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public Integer getFormaSlc() {
        return formaSlc;
    }

    public void setFormaSlc(Integer formaSlc) {
        this.formaSlc = formaSlc;
    }

    public Integer getBancoSlc() {
        return bancoSlc;
    }

    public void setBancoSlc(Integer bancoSlc) {
        this.bancoSlc = bancoSlc;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public CuentaFactura getCuentaFactura() {
        return cuentaFactura;
    }

    public void setCuentaFactura(CuentaFactura cuentaFactura) {
        this.cuentaFactura = cuentaFactura;
    }

    public Integer getNumeroCuentas() {
        return numeroCuentas;
    }

    public void setNumeroCuentas(Integer numeroCuentas) {
        this.numeroCuentas = numeroCuentas;
    }

    public Integer getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(Integer diasCredito) {
        this.diasCredito = diasCredito;
    }

    public List<FormaPago> getListaFormaPagoCuenta() {
        return listaFormaPagoCuenta;
    }

    public void setListaFormaPagoCuenta(List<FormaPago> listaFormaPagoCuenta) {
        this.listaFormaPagoCuenta = listaFormaPagoCuenta;
    }

    public Integer getFormaCuentaSlc() {
        return formaCuentaSlc;
    }

    public void setFormaCuentaSlc(Integer formaCuentaSlc) {
        this.formaCuentaSlc = formaCuentaSlc;
    }

    public List<FacturaPago> getListaFacturaPago() {
        return listaFacturaPago;
    }

    public void setListaFacturaPago(List<FacturaPago> listaFacturaPago) {
        this.listaFacturaPago = listaFacturaPago;
    }

    public BigDecimal getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(BigDecimal totalFactura) {
        this.totalFactura = totalFactura;
    }

    public List<TarjetaEmpresa> getListaTarjetaEmpresa() {
        return listaTarjetaEmpresa;
    }

    public void setListaTarjetaEmpresa(List<TarjetaEmpresa> listaTarjetaEmpresa) {
        this.listaTarjetaEmpresa = listaTarjetaEmpresa;
    }

    public Integer getTarjetaEmpresaSlc() {
        return tarjetaEmpresaSlc;
    }

    public void setTarjetaEmpresaSlc(Integer tarjetaEmpresaSlc) {
        this.tarjetaEmpresaSlc = tarjetaEmpresaSlc;
    }

    public Integer getComisionTarjetaSlc() {
        return comisionTarjetaSlc;
    }

    public void setComisionTarjetaSlc(Integer comisionTarjetaSlc) {
        this.comisionTarjetaSlc = comisionTarjetaSlc;
    }

    public List<ComisionTarjeta> getListaComisionTarjeta() {
        return listaComisionTarjeta;
    }

    public void setListaComisionTarjeta(List<ComisionTarjeta> listaComisionTarjeta) {
        this.listaComisionTarjeta = listaComisionTarjeta;
    }
}
