package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "identificador")
    private String identificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "camara")
    private String camara;
    @Size(min = 1, max = 1)
    @Column(name = "ver_grupo_busqueda")
    private String verGrupoBusqueda;
    @JoinColumn(name = "rol", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.EAGER)
    private Rol rol;
    @JoinColumn(name = "empleado", referencedColumnName = "codigo")
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Empleado empleado;
    @JoinColumn(name = "cliente", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Cliente cliente;
    
    public Cuenta() {
    }

    public Cuenta(String identificador) {
        this.identificador = identificador;
    }

    public Cuenta(String identificador, String clave) {
        this.identificador = identificador;
        this.clave = clave;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public String getVerGrupoBusqueda() {
        return verGrupoBusqueda;
    }

    public void setVerGrupoBusqueda(String verGrupoBusqueda) {
        this.verGrupoBusqueda = verGrupoBusqueda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificador != null ? identificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Cuenta[ identificador=" + identificador + " ]";
    }
}

