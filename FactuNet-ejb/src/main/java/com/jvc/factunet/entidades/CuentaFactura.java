package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cuenta_factura")
public class CuentaFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_cuenta")
    @SequenceGenerator(name = "secuencia_cuenta", sequenceName = "secuencia_cuenta", allocationSize = 1)
    private Integer codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Size(max = 800)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "numero")
    private String numero;
    @JoinColumn(name = "banco", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Banco banco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuentaFactura", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AbonoCuenta> abonoCuentaList;
    @JoinColumn(name = "factura_pago", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FacturaPago facturaPago;
    @JoinColumn(name = "forma_pago", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FormaPago formaPago;
    @JoinColumn(name = "comision", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ComisionTarjeta comisionTarjeta;
    
    @Transient
    private long dias;
    @Transient
    private BigDecimal interes;
    @Transient
    private BigDecimal valorInteres;

    public CuentaFactura() {
        this.abonoCuentaList = new ArrayList<>();
        this.interes = BigDecimal.ZERO;
        this.valorInteres = BigDecimal.ZERO;
    }

    public CuentaFactura(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public List<AbonoCuenta> getAbonoCuentaList() {
        return abonoCuentaList;
    }

    public void setAbonoCuentaList(List<AbonoCuenta> abonoCuentaList) {
        this.abonoCuentaList = abonoCuentaList;
    }

    public FacturaPago getFacturaPago() {
        return facturaPago;
    }

    public void setFacturaPago(FacturaPago facturaPago) {
        this.facturaPago = facturaPago;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ComisionTarjeta getComisionTarjeta() {
        return comisionTarjeta;
    }

    public void setComisionTarjeta(ComisionTarjeta comisionTarjeta) {
        this.comisionTarjeta = comisionTarjeta;
    }

    public long getDias() {
        return ((this.getFechaVencimiento().getTime() - (new Date()).getTime())/86400000)  ;
    }

    public void setDias(long dias) {
        this.dias = dias;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(BigDecimal valorInteres) {
        this.valorInteres = valorInteres;
    }
}
