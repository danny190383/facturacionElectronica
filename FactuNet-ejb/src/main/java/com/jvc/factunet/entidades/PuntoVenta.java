package com.jvc.factunet.entidades;

import java.io.Serializable;
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
@Table(name = "punto_venta")
public class PuntoVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_punto")
    @SequenceGenerator(name = "secuencia_punto", sequenceName = "secuencia_punto", allocationSize = 1)
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "codigo_sri")
    private String codigoSri;
    @Column(name = "impresora")
    private String impresora;
    @Size(max = 1)
    @Column(name = "tipo_impresora")
    private String tipoImpresora;
    @Size(max = 1)
    @Column(name = "rise")
    private String rise;
    @Size(max = 1)
    @Column(name = "tablet")
    private String tablet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puntoVenta", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SecuenciaDocumento> secuenciaDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puntoVenta", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Empleado> usuarioPuntoVentaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puntoVenta", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PuntoRestriccion> puntoRestriccionList;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    @JoinColumn(name = "bodega", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.EAGER)
    private Bodega bodega;
    

    public PuntoVenta() {
    }

    public PuntoVenta(Integer codigo) {
        this.codigo = codigo;
    }

    public PuntoVenta(Integer codigo, String nombre, String ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCodigoSri() {
        return codigoSri;
    }

    public void setCodigoSri(String codigoSri) {
        this.codigoSri = codigoSri;
    }

    public List<SecuenciaDocumento> getSecuenciaDocumentoList() {
        return secuenciaDocumentoList;
    }

    public void setSecuenciaDocumentoList(List<SecuenciaDocumento> secuenciaDocumentoList) {
        this.secuenciaDocumentoList = secuenciaDocumentoList;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Empleado> getUsuarioPuntoVentaList() {
        return usuarioPuntoVentaList;
    }

    public void setUsuarioPuntoVentaList(List<Empleado> usuarioPuntoVentaList) {
        this.usuarioPuntoVentaList = usuarioPuntoVentaList;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

    public List<PuntoRestriccion> getPuntoRestriccionList() {
        return puntoRestriccionList;
    }

    public void setPuntoRestriccionList(List<PuntoRestriccion> puntoRestriccionList) {
        this.puntoRestriccionList = puntoRestriccionList;
    }

    public String getTablet() {
        return tablet;
    }

    public void setTablet(String tablet) {
        this.tablet = tablet;
    }

    public String getTipoImpresora() {
        return tipoImpresora;
    }

    public void setTipoImpresora(String tipoImpresora) {
        this.tipoImpresora = tipoImpresora;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
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
        if (!(object instanceof PuntoVenta)) {
            return false;
        }
        PuntoVenta other = (PuntoVenta) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.PuntoVenta[ codigo=" + codigo + " ]";
    }
}
