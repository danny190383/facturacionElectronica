package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "factura_pago")
public class FacturaPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_pago")
    @SequenceGenerator(name = "secuencia_pago", sequenceName = "secuencia_pago", allocationSize = 1)
    private Integer codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "factura", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "forma_pago", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FormaPago formaPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaPago", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<CuentaFactura> cuentaFacturaList;

    public FacturaPago() {
    }

    public FacturaPago(Integer codigo) {
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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<CuentaFactura> getCuentaFacturaList() {
        return cuentaFacturaList;
    }

    public void setCuentaFacturaList(List<CuentaFactura> cuentaFacturaList) {
        this.cuentaFacturaList = cuentaFacturaList;
    }
}
