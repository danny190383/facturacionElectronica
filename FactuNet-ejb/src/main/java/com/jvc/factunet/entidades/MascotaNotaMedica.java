package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mascota_nota_medica")
public class MascotaNotaMedica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_mascota_nota")
    @SequenceGenerator(name = "secuencia_mascota_nota", sequenceName = "secuencia_mascota_nota", allocationSize = 1)
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "fecha_proxima")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaProxima;
    @Column(name = "tipo")
    private Integer tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "temperatura")
    private BigDecimal temperatura;
    @Size(max = 1500)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 1500)
    @Column(name = "diagnostico")
    private String diagnostico;
    @Size(max = 1500)
    @Column(name = "tratamiento")
    private String tratamiento;
    @JoinColumn(name = "mascota", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Mascota mascota;
    @JoinColumn(name = "pedido", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private PedidoVenta pedido;

    public MascotaNotaMedica() {
    }

    public MascotaNotaMedica(Integer codigo) {
        this.codigo = codigo;
    }

    public MascotaNotaMedica(Integer codigo, Date fecha, Date fechaProxima) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.fechaProxima = fechaProxima;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaProxima() {
        return fechaProxima;
    }

    public void setFechaProxima(Date fechaProxima) {
        this.fechaProxima = fechaProxima;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public PedidoVenta getPedido() {
        return pedido;
    }

    public void setPedido(PedidoVenta pedido) {
        this.pedido = pedido;
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
        if (!(object instanceof MascotaNotaMedica)) {
            return false;
        }
        MascotaNotaMedica other = (MascotaNotaMedica) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.MascotaNotaMedica[ codigo=" + codigo + " ]";
    }
}
