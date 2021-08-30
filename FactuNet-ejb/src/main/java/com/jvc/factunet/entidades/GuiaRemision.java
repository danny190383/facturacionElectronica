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
@Table(name = "guia_remision")
public class GuiaRemision implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_guia")
    @SequenceGenerator(name = "secuencia_guia", sequenceName = "secuencia_guia", allocationSize = 1)
    private Integer codigo;
    @Size(max = 20)
    @Column(name = "numero")
    private String numero;
    @Column(name = "secuencia")
    private Integer secuencia;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 200)
    @Column(name = "ruta")
    private String ruta;
    @Size(max = 300)
    @Column(name = "direccion_origen")
    private String direccionOrigen;
    @Size(max = 20)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_recepcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecepcion;
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "cartones")
    private Integer cartones;
    @JoinColumn(name = "motivo", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Motivo motivo;
    @JoinColumn(name = "factura", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "transportista", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Transportista transportista;
    @JoinColumn(name = "destinatario", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente destinatario;
    @Size(max = 200)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    //sri facturacion electronica
    @Column(name = "path_xml_firmado")
    private String pathXmlFirmado;
    //Recepcion
    @Column(name = "estado_sri")
    private String estadoSri;
    @Column(name = "identificador_error_sri")
    private String identificadorErrorSri;
    @Column(name = "descripcion_error_sri")
    private String descripcionErrorSri;
    @Column(name = "descripcion_adicional_error_sri")
    private String descripcionAdicionalErrorSri;
    @Column(name = "tipo_mensaje_sri")
    private String tipoMensajeSri;
    @Column(name = "fecha_envio_sri")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvioSri;
    @JoinColumn(name = "empleado_envio_sri", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleadoEnvioSri;
    //Autorizacion
    @Column(name = "estado_autorizacion_sri")
    private String estadoAutorizacionSri;
    @Column(name = "ambiente_autorizacion_sri")
    private String ambienteAutorizacionSri;
    @Column(name = "identificador_error_autorizacion_sri")
    private String identificadorErrorAutorizacionSri;
    @Column(name = "descripcion_error_autorizacion_sri")
    private String descripcionErrorAutorizacionSri;
    @Column(name = "descripcion_adicional_error_autorizacion_sri")
    private String descripcionAdicionalErrorAutorizacionSri;
    @Column(name = "tipo_mensaje_autorizacion_sri")
    private String tipoMensajeAutorizacionSri;
    @Column(name = "fecha_autorizacion_sri")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacionSri;

    public GuiaRemision() {
    }

    public GuiaRemision(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public Integer getCartones() {
        return cartones;
    }

    public void setCartones(Integer cartones) {
        this.cartones = cartones;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Cliente destinatario) {
        this.destinatario = destinatario;
    }

    public String getPathXmlFirmado() {
        return pathXmlFirmado;
    }

    public void setPathXmlFirmado(String pathXmlFirmado) {
        this.pathXmlFirmado = pathXmlFirmado;
    }

    public String getEstadoSri() {
        return estadoSri;
    }

    public void setEstadoSri(String estadoSri) {
        this.estadoSri = estadoSri;
    }

    public String getIdentificadorErrorSri() {
        return identificadorErrorSri;
    }

    public void setIdentificadorErrorSri(String identificadorErrorSri) {
        this.identificadorErrorSri = identificadorErrorSri;
    }

    public String getDescripcionErrorSri() {
        return descripcionErrorSri;
    }

    public void setDescripcionErrorSri(String descripcionErrorSri) {
        this.descripcionErrorSri = descripcionErrorSri;
    }

    public String getDescripcionAdicionalErrorSri() {
        return descripcionAdicionalErrorSri;
    }

    public void setDescripcionAdicionalErrorSri(String descripcionAdicionalErrorSri) {
        this.descripcionAdicionalErrorSri = descripcionAdicionalErrorSri;
    }

    public String getTipoMensajeSri() {
        return tipoMensajeSri;
    }

    public void setTipoMensajeSri(String tipoMensajeSri) {
        this.tipoMensajeSri = tipoMensajeSri;
    }

    public Date getFechaEnvioSri() {
        return fechaEnvioSri;
    }

    public void setFechaEnvioSri(Date fechaEnvioSri) {
        this.fechaEnvioSri = fechaEnvioSri;
    }

    public Empleado getEmpleadoEnvioSri() {
        return empleadoEnvioSri;
    }

    public void setEmpleadoEnvioSri(Empleado empleadoEnvioSri) {
        this.empleadoEnvioSri = empleadoEnvioSri;
    }

    public String getEstadoAutorizacionSri() {
        return estadoAutorizacionSri;
    }

    public void setEstadoAutorizacionSri(String estadoAutorizacionSri) {
        this.estadoAutorizacionSri = estadoAutorizacionSri;
    }

    public String getAmbienteAutorizacionSri() {
        return ambienteAutorizacionSri;
    }

    public void setAmbienteAutorizacionSri(String ambienteAutorizacionSri) {
        this.ambienteAutorizacionSri = ambienteAutorizacionSri;
    }

    public String getIdentificadorErrorAutorizacionSri() {
        return identificadorErrorAutorizacionSri;
    }

    public void setIdentificadorErrorAutorizacionSri(String identificadorErrorAutorizacionSri) {
        this.identificadorErrorAutorizacionSri = identificadorErrorAutorizacionSri;
    }

    public String getDescripcionErrorAutorizacionSri() {
        return descripcionErrorAutorizacionSri;
    }

    public void setDescripcionErrorAutorizacionSri(String descripcionErrorAutorizacionSri) {
        this.descripcionErrorAutorizacionSri = descripcionErrorAutorizacionSri;
    }

    public String getDescripcionAdicionalErrorAutorizacionSri() {
        return descripcionAdicionalErrorAutorizacionSri;
    }

    public void setDescripcionAdicionalErrorAutorizacionSri(String descripcionAdicionalErrorAutorizacionSri) {
        this.descripcionAdicionalErrorAutorizacionSri = descripcionAdicionalErrorAutorizacionSri;
    }

    public String getTipoMensajeAutorizacionSri() {
        return tipoMensajeAutorizacionSri;
    }

    public void setTipoMensajeAutorizacionSri(String tipoMensajeAutorizacionSri) {
        this.tipoMensajeAutorizacionSri = tipoMensajeAutorizacionSri;
    }

    public Date getFechaAutorizacionSri() {
        return fechaAutorizacionSri;
    }

    public void setFechaAutorizacionSri(Date fechaAutorizacionSri) {
        this.fechaAutorizacionSri = fechaAutorizacionSri;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
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
        if (!(object instanceof GuiaRemision)) {
            return false;
        }
        GuiaRemision other = (GuiaRemision) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.GuiaRemision[ codigo=" + codigo + " ]";
    }
}
