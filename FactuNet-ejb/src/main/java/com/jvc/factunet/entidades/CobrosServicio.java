package com.jvc.factunet.entidades;

import com.jvc.factunet.enumeracion.EnumMesAnio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cobros_servicio")
public class CobrosServicio implements Serializable{
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    @Size(max = 1000)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "estado")
    private String estado;
    @Enumerated(EnumType.STRING)
    @Column(name = "mes")
    private EnumMesAnio mes;
    @Column(name = "lectura ")
    private BigDecimal lectura ;
    @Column(name = "valor ")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "anio", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Anio anio;
    @JoinColumn(name = "servicio_persona", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServicioPersona servicioPersona;
    @JoinColumn(name = "persona_registra ", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Persona persona;
    @JoinColumn(name = "servicio", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Producto servicio;

    public CobrosServicio() {
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Producto getServicio() {
        return servicio;
    }

    public void setServicio(Producto servicio) {
        this.servicio = servicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getLectura() {
        return lectura;
    }

    public void setLectura(BigDecimal lectura) {
        this.lectura = lectura;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public ServicioPersona getServicioPersona() {
        return servicioPersona;
    }

    public void setServicioPersona(ServicioPersona servicioPersona) {
        this.servicioPersona = servicioPersona;
    }

    public EnumMesAnio getMes() {
        return mes;
    }

    public void setMes(EnumMesAnio mes) {
        this.mes = mes;
    }

    public Anio getAnio() {
        return anio;
    }

    public void setAnio(Anio anio) {
        this.anio = anio;
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
        if (!(object instanceof CobrosServicio)) {
            return false;
        }
        CobrosServicio other = (CobrosServicio) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.CobrosServicio[ codigo=" + codigo + " ]";
    }
}
