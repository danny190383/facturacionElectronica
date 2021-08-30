package com.jvc.factunet.entidades;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tarjeta_empresa")
public class TarjetaEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_tarjeta_empresa")
    @SequenceGenerator(name = "secuencia_tarjeta_empresa", sequenceName = "secuencia_tarjeta_empresa", allocationSize = 1)
    private Integer codigo;
    @Size(max = 800)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "dias_cobro")
    private Integer diasCobro;
    @JoinColumn(name = "tipo_tarjeta", referencedColumnName = "codigo")
    @ManyToOne
    private TipoTarjeta tipoTarjeta;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne
    private Empresa empresa;
    @JoinColumn(name = "banco", referencedColumnName = "codigo")
    @ManyToOne
    private Banco banco;
    @OneToMany(mappedBy = "tarjetaEmpresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComisionTarjeta> comisionTarjetaList;
    @OneToMany(mappedBy = "tarjetaEmpresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetencionTarjeta> retencionTarjetaList;
    @OneToMany(mappedBy = "tarjetaEmpresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FactorPlazoTarjeta> factorPlazoTarjetaList;

    public TarjetaEmpresa() {
    }

    public TarjetaEmpresa(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public List<ComisionTarjeta> getComisionTarjetaList() {
        return comisionTarjetaList;
    }

    public void setComisionTarjetaList(List<ComisionTarjeta> comisionTarjetaList) {
        this.comisionTarjetaList = comisionTarjetaList;
    }

    public List<RetencionTarjeta> getRetencionTarjetaList() {
        return retencionTarjetaList;
    }

    public void setRetencionTarjetaList(List<RetencionTarjeta> retencionTarjetaList) {
        this.retencionTarjetaList = retencionTarjetaList;
    }

    public List<FactorPlazoTarjeta> getFactorPlazoTarjetaList() {
        return factorPlazoTarjetaList;
    }

    public void setFactorPlazoTarjetaList(List<FactorPlazoTarjeta> factorPlazoTarjetaList) {
        this.factorPlazoTarjetaList = factorPlazoTarjetaList;
    }

    public Integer getDiasCobro() {
        return diasCobro;
    }

    public void setDiasCobro(Integer diasCobro) {
        this.diasCobro = diasCobro;
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
        if (!(object instanceof TarjetaEmpresa)) {
            return false;
        }
        TarjetaEmpresa other = (TarjetaEmpresa) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.TarjetaEmpresa[ codigo=" + codigo + " ]";
    }
}
