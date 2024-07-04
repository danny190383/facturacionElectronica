package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_empresa")
    @SequenceGenerator(name = "secuencia_empresa", sequenceName = "secuencia_empresa", allocationSize = 1)
    private Integer codigo;
    @Column(name = "decimales")
    private Integer decimales;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(min = 1, max = 200)
    @Column(name = "razon_social ")
    private String razonSocial;
    @Column(name = "nombre_abreviado ")
    private String nombreAbreviado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 13)
    @Column(name = "ruc")
    private String ruc;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "email_clave")
    private String emailClave;
    @Size(max = 15)
    @Column(name = "telefono1")
    private String telefono1;
    @Size(max = 15)
    @Column(name = "telefono2")
    private String telefono2;
    @Size(max = 50)
    @Column(name = "tema")
    private String tema;
    @Lob
    @Column(name = "logo_imagen")
    private byte[] logoImagen;
    @Size(max = 1)
    @Column(name = "metodo_costo")
    private String metodoCosto;
    @Size(max = 1)
    @Column(name = "generar_nombre_producto")
    private String generarNombreProducto;
    @Size(max = 1)
    @Column(name = "tipo_empresa")
    private String tipoEmpresa;
    @Size(max = 1)
    @Column(name = "cliente_completo")
    private String clienteCompleto;
    @Column(name = "moneda")
    private String moneda;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Basic(optional = false)
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @JoinColumn(name = "ciudad", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ciudad ciudad;
    @JoinColumn(name = "id_impuesto_tarifa", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImpuestoTarifa impuestoTarifa;
    @JoinColumn(name = "id_tipo_empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoEmpresa tipoEmpresaWeb;
    @Column(name = "coordenada_lat")
    private BigDecimal coordenadaLatitud;
    @Column(name = "coordenada_long")
    private BigDecimal coordenadaLongitud;
    @JoinColumns({
        @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia")
        , @JoinColumn(name = "id_canton", referencedColumnName = "id_canton")
        , @JoinColumn(name = "id_parroquia", referencedColumnName = "id")})
    @ManyToOne
    private Parroquia parroquia;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PuntoVenta> puntoVentaList;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Controles> controlesList;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TarjetaEmpresa> tarjetaEmpresaList;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seccion> seccionList;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bodega> bodegaList;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmpresaCatalogoParametro> empresaCatalogoParametroList;
    @Column(name = "obligado_contabilidad ")
    private Boolean obligadoContabilidad ;
    @Column(name = "usa_tablet")
    private Boolean usaTablet;
    @Column(name = "usa_balanza")
    private Boolean usaBalanza;
    @Column(name = "numero_caracteres_balanza")
    private Integer numeroCaracteresBalanza;
    
    public Empresa() {
        this.obligadoContabilidad = Boolean.FALSE;
        this.tipoEmpresa = "1";
        this.usaTablet = Boolean.FALSE;
    }

    public Empresa(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getUsaBalanza() {
        return usaBalanza;
    }

    public void setUsaBalanza(Boolean usaBalanza) {
        this.usaBalanza = usaBalanza;
    }

    public Integer getNumeroCaracteresBalanza() {
        return numeroCaracteresBalanza;
    }

    public void setNumeroCaracteresBalanza(Integer numeroCaracteresBalanza) {
        this.numeroCaracteresBalanza = numeroCaracteresBalanza;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public byte[] getLogoImagen() {
        return logoImagen;
    }

    public void setLogoImagen(byte[] logoImagen) {
        this.logoImagen = logoImagen;
    }

    public List<PuntoVenta> getPuntoVentaList() {
        return puntoVentaList;
    }

    public void setPuntoVentaList(List<PuntoVenta> puntoVentaList) {
        this.puntoVentaList = puntoVentaList;
    }

    public List<Controles> getControlesList() {
        return controlesList;
    }

    public void setControlesList(List<Controles> controlesList) {
        this.controlesList = controlesList;
    }

    public String getMetodoCosto() {
        return metodoCosto;
    }

    public void setMetodoCosto(String metodoCosto) {
        this.metodoCosto = metodoCosto;
    }

    public String getGenerarNombreProducto() {
        return generarNombreProducto;
    }

    public void setGenerarNombreProducto(String generarNombreProducto) {
        this.generarNombreProducto = generarNombreProducto;
    }

    public List<TarjetaEmpresa> getTarjetaEmpresaList() {
        return tarjetaEmpresaList;
    }

    public void setTarjetaEmpresaList(List<TarjetaEmpresa> tarjetaEmpresaList) {
        this.tarjetaEmpresaList = tarjetaEmpresaList;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public List<Seccion> getSeccionList() {
        return seccionList;
    }

    public void setSeccionList(List<Seccion> seccionList) {
        this.seccionList = seccionList;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getEmailClave() {
        return emailClave;
    }

    public void setEmailClave(String emailClave) {
        this.emailClave = emailClave;
    }

    public String getClienteCompleto() {
        return clienteCompleto;
    }

    public void setClienteCompleto(String clienteCompleto) {
        this.clienteCompleto = clienteCompleto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreAbreviado() {
        return nombreAbreviado;
    }

    public void setNombreAbreviado(String nombreAbreviado) {
        this.nombreAbreviado = nombreAbreviado;
    }

    public Integer getDecimales() {
        return decimales;
    }

    public void setDecimales(Integer decimales) {
        this.decimales = decimales;
    }
    
    public BigDecimal getCoordenadaLatitud() {
        return coordenadaLatitud;
    }

    public void setCoordenadaLatitud(BigDecimal coordenadaLatitud) {
        this.coordenadaLatitud = coordenadaLatitud;
    }

    public BigDecimal getCoordenadaLongitud() {
        return coordenadaLongitud;
    }

    public void setCoordenadaLongitud(BigDecimal coordenadaLongitud) {
        this.coordenadaLongitud = coordenadaLongitud;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public TipoEmpresa getTipoEmpresaWeb() {
        return tipoEmpresaWeb;
    }

    public void setTipoEmpresaWeb(TipoEmpresa tipoEmpresaWeb) {
        this.tipoEmpresaWeb = tipoEmpresaWeb;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Boolean getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(Boolean obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<Bodega> getBodegaList() {
        return bodegaList;
    }

    public void setBodegaList(List<Bodega> bodegaList) {
        this.bodegaList = bodegaList;
    }

    public Boolean getUsaTablet() {
        return usaTablet;
    }

    public void setUsaTablet(Boolean usaTablet) {
        this.usaTablet = usaTablet;
    }

    public ImpuestoTarifa getImpuestoTarifa() {
        return impuestoTarifa;
    }

    public void setImpuestoTarifa(ImpuestoTarifa impuestoTarifa) {
        this.impuestoTarifa = impuestoTarifa;
    }

    public List<EmpresaCatalogoParametro> getEmpresaCatalogoParametroList() {
        return empresaCatalogoParametroList;
    }

    public void setEmpresaCatalogoParametroList(List<EmpresaCatalogoParametro> empresaCatalogoParametroList) {
        this.empresaCatalogoParametroList = empresaCatalogoParametroList;
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
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Empresa[ codigo=" + codigo + " ]";
    }
}
