package com.jvc.factunet.entidades;

import com.jvc.medisys.bussines.utilitario.Edad;
import com.jvc.medisys.bussines.utilitario.NumeroFormato;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
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
@Table(name = "mascota")
public class Mascota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_mascota")
    @SequenceGenerator(name = "secuencia_mascota", sequenceName = "secuencia_mascota", allocationSize = 1)
    private Integer codigo;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "chip")
    private String chip;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Size(max = 50)
    @Column(name = "color")
    private String color;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @Column(name = "reproductor")
    private Boolean reproductor;
    @Column(name = "vivo")
    private Boolean vivo;
    @JoinColumn(name = "sexo", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private SexoMascota sexo;
    @JoinColumn(name = "raza", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private RazaMascota raza;
    @JoinColumn(name = "persona", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Persona persona;
    @JoinColumn(name = "especie", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private EspecieMascota especie;
    @OneToMany(mappedBy = "mascota", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MascotaNotaMedica> mascotaNotaMedicaList;

    public Mascota() {
    }

    public Boolean getVivo() {
        return vivo;
    }

    public void setVivo(Boolean vivo) {
        this.vivo = vivo;
    }

    public Mascota(Integer codigo) {
        this.codigo = codigo;
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

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public SexoMascota getSexo() {
        return sexo;
    }

    public void setSexo(SexoMascota sexo) {
        this.sexo = sexo;
    }

    public RazaMascota getRaza() {
        return raza;
    }

    public void setRaza(RazaMascota raza) {
        this.raza = raza;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public EspecieMascota getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieMascota especie) {
        this.especie = especie;
    }

    public Boolean getReproductor() {
        return reproductor;
    }

    public void setReproductor(Boolean reproductor) {
        this.reproductor = reproductor;
    }

    public List<MascotaNotaMedica> getMascotaNotaMedicaList() {
        Collections.sort(mascotaNotaMedicaList, new Comparator<MascotaNotaMedica>() {
            @Override
            public int compare(MascotaNotaMedica o1, MascotaNotaMedica o2) {
                return o1.getFecha().after(o2.getFecha()) ? -1 : 1;
            }
        });
        return mascotaNotaMedicaList;
    }

    public void setMascotaNotaMedicaList(List<MascotaNotaMedica> mascotaNotaMedicaList) {
        this.mascotaNotaMedicaList = mascotaNotaMedicaList;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    public BigDecimal getEdad(){
        return NumeroFormato.formatoDecimal(2, new Edad().
                        edadEnMeses(fechaNacimiento, null));
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
        if (!(object instanceof Mascota)) {
            return false;
        }
        Mascota other = (Mascota) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Mascota[ codigo=" + codigo + " ]";
    }
}

