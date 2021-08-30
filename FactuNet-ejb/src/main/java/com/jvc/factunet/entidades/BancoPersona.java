package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "banco_persona")
public class BancoPersona implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_banco_persona")
    @SequenceGenerator(name = "secuencia_banco_persona", sequenceName = "secuencia_banco_persona", allocationSize = 1)
    private Integer codigo;
    @Size(max = 100)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Size(max = 500)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "tipo_cuenta", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoCuenta tipoCuenta;
    @JoinColumn(name = "banco", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Banco banco;
    @JoinColumn(name = "persona", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoPersona tipoPersona;

    public BancoPersona() {
    }

    public BancoPersona(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
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
        if (!(object instanceof BancoPersona)) {
            return false;
        }
        BancoPersona other = (BancoPersona) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.BancoPersona[ codigo=" + codigo + " ]";
    }
}
