package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "comision_tarjeta")
public class ComisionTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_comision_tarjeta")
    @SequenceGenerator(name = "secuencia_comision_tarjeta", sequenceName = "secuencia_comision_tarjeta", allocationSize = 1)
    private Integer codigo;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "interes")
    private Integer interes;
    @Column(name = "meses")
    private Integer meses;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "tarjeta_empresa", referencedColumnName = "codigo")
    @ManyToOne
    private TarjetaEmpresa tarjetaEmpresa;
    @JoinColumn(name = "forma_pago", referencedColumnName = "codigo")
    @ManyToOne
    private FormaPago formaPago;

    public ComisionTarjeta() {
    }

    public ComisionTarjeta(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public TarjetaEmpresa getTarjetaEmpresa() {
        return tarjetaEmpresa;
    }

    public void setTarjetaEmpresa(TarjetaEmpresa tarjetaEmpresa) {
        this.tarjetaEmpresa = tarjetaEmpresa;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getInteres() {
        return interes;
    }

    public void setInteres(Integer interes) {
        this.interes = interes;
    }

    public Integer getMeses() {
        return meses;
    }

    public void setMeses(Integer meses) {
        this.meses = meses;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionTarjeta)) {
            return false;
        }
        ComisionTarjeta other = (ComisionTarjeta) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.ComisionTarjeta[ codigo=" + codigo + " ]";
    }
}
