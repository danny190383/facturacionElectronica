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
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_persona")
    @SequenceGenerator(name = "secuencia_persona", sequenceName = "secuencia_persona", allocationSize = 1)
    private Integer codigo;
    @Size(max = 13)
    @Column(name = "cedula")
    private String cedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 50)
    @Column(name = "apellidos")
    private String apellidos;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @Size(max = 100)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    @Size(max = 10)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Size(max = 100)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    @Size(max = 10)
    @Column(name = "numero")
    private String numero;
    @Column(name = "obligado_contabilidad ")
    private Boolean obligadoContabilidad ;
    @JoinColumn(name = "ciudad", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ciudad ciudad;
    @JoinColumn(name = "sexo", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sexo sexo;
    @JoinColumn(name = "estado_civil", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoCivil estadoCivil;
    @JoinColumn(name = "tipo_identificacion", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoIdentificacion tipoIdentificacion;
    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetencionPersona> retencionPersonaList;
    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotaPersonaList;
    @JoinColumns({
        @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia")
        ,
        @JoinColumn(name = "id_canton", referencedColumnName = "id_canton")
        ,
        @JoinColumn(name = "id_parroquia", referencedColumnName = "id")})
    @ManyToOne(optional = false)
    private Parroquia parroquia;
    @Column(name = "coordenada_lat")
    private BigDecimal coordenadaLatitud;
    @Column(name = "coordenada_long")
    private BigDecimal coordenadaLongitud;
    
    public Persona() {
        this.obligadoContabilidad = Boolean.FALSE;
    }

    public Persona(Integer codigo) {
        this.codigo = codigo;
    }

    public Persona(Integer codigo, String nombres) {
        this.codigo = codigo;
        this.nombres = nombres;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<RetencionPersona> getRetencionPersonaList() {
        return retencionPersonaList;
    }

    public void setRetencionPersonaList(List<RetencionPersona> retencionPersonaList) {
        this.retencionPersonaList = retencionPersonaList;
    }

    public List<Mascota> getMascotaPersonaList() {
        return mascotaPersonaList;
    }

    public void setMascotaPersonaList(List<Mascota> mascotaPersonaList) {
        this.mascotaPersonaList = mascotaPersonaList;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
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

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Boolean getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(Boolean obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }
    
    public String getNombreApellido(){
        String concat = null;
        if(this.getApellidos() == null || this.getApellidos().isEmpty()){
            return this.getNombres();
        }
        if(this.getNombres() != null){
            String[] nombresSeparado = this.getNombres().split(" ");
            concat = nombresSeparado[0];
        }
        if(this.getApellidos() != null){
            String[] apellidosSeparado = this.getApellidos().split(" ");
            concat = concat + " " + apellidosSeparado[0];
        }
        return concat;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Persona[ codigo=" + codigo + " ]";
    }
}
