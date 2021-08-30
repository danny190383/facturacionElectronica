package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tipo_persona")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.INTEGER)
public class TipoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_tipo_persona")
    @SequenceGenerator(name = "secuencia_tipo_persona", sequenceName = "secuencia_tipo_persona", allocationSize = 1)
    private Integer codigo;
    @JoinColumn(name = "persona", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Persona persona;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "personaPadre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contacto> contactoPersonaList;
    @OneToMany(mappedBy = "tipoPersona", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contactar> contactarPersonaList;
    @OneToMany(mappedBy = "tipoPersona", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BancoPersona> bancoPersonaList;

    public TipoPersona() {
        // 1 Contacto
        // 2 Cliente
        // 3 Empleado
        // 4 Proveedor
        // 5 Transportista
    }

    public TipoPersona(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    public List<Contacto> getContactoPersonaList() {
        return contactoPersonaList;
    }

    public void setContactoPersonaList(List<Contacto> contactoPersonaList) {
        this.contactoPersonaList = contactoPersonaList;
    }
    
    public List<Contactar> getContactarPersonaList() {
        return contactarPersonaList;
    }

    public void setContactarPersonaList(List<Contactar> contactarPersonaList) {
        this.contactarPersonaList = contactarPersonaList;
    }
    
    public List<BancoPersona> getBancoPersonaList() {
        return bancoPersonaList;
    }

    public void setBancoPersonaList(List<BancoPersona> bancoPersonaList) {
        this.bancoPersonaList = bancoPersonaList;
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
        if (!(object instanceof TipoPersona)) {
            return false;
        }
        TipoPersona other = (TipoPersona) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.TipoPersona[ codigo=" + codigo + " ]";
    }
}
