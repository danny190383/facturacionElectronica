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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "documento_retencion")
public class DocumentoRetencion implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_retencion")
    @SequenceGenerator(name = "secuencia_retencion", sequenceName = "secuencia_retencion", allocationSize = 1)
    private Integer codigo;
    @JoinColumn(name = "factura", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Factura factura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private Integer numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaCreacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    @Size(max = 1)
    @Column(name = "estadoSRI")
    private String estadoSRI;
    @Size(max = 1)
    @Column(name = "estadoDocumento")
    private String estadoDocumento ;
    @Column(name = "total_retencion")
    private BigDecimal totalRetencion;
    @OneToMany(mappedBy = "documentoRetencion",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Retencion> retencionList;
    //SRI
    @Size(max = 200)
    @Column(name = "codigo_barras")
    private String codigoBarras;
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

    public DocumentoRetencion() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstadoSRI() {
        return estadoSRI;
    }

    public void setEstadoSRI(String estadoSRI) {
        this.estadoSRI = estadoSRI;
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(String estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public List<Retencion> getRetencionList() {
        return retencionList;
    }

    public void setRetencionList(List<Retencion> retencionList) {
        this.retencionList = retencionList;
    }

    public BigDecimal getTotalRetencion() {
        return totalRetencion;
    }

    public void setTotalRetencion(BigDecimal totalRetencion) {
        this.totalRetencion = totalRetencion;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoRetencion)) {
            return false;
        }
        DocumentoRetencion other = (DocumentoRetencion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.DocumentoRetencion[ codigo=" + codigo + " ]";
    }
}
