package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "factura")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminador", discriminatorType = DiscriminatorType.INTEGER)
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_factura")
    @SequenceGenerator(name = "secuencia_factura", sequenceName = "secuencia_factura", allocationSize = 1)
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private Integer numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 800)
    @Column(name = "despachar_a")
    private String despacharA;
    @Size(max = 800)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Size(max = 800)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "comision")
    private BigDecimal comision;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "total_retencion")
    private BigDecimal totalRetencion;
    @Column(name = "total_pagar")
    private BigDecimal totalPagar;
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Size(max = 200)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    @Column(name = "irbpnr")
    private BigDecimal irbpnr;
    @Column(name = "ice")
    private BigDecimal ice;
    @Column(name = "iva")
    private BigDecimal iva;
    @Column(name = "tarifa_iva")
    private BigDecimal tarifaIva;
    @JoinColumn(name = "cliente", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    @JoinColumn(name = "empleado", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
    @JoinColumn(name = "proveedor", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor proveedor;
    @JoinColumn(name = "contacto_proveedor", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Contacto contactoProveedor;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa; 
    @JoinColumn(name = "bodega", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bodega bodega;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<FacturaDetalle> facturaDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<GuiaRemision> guiaRemisionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<DocumentoRetencion> documentoRetencion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<FacturaPago> facturaPagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cabeceraFactura", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<CabeceraFacturaImpuestoTarifa> cabeceraFacturaImpuestoTarifaList;
    @OneToMany(mappedBy = "documentoRelacionado", fetch = FetchType.LAZY)
    private List<NotaCredito> notaCreditoList;
    @OneToMany(mappedBy = "documentoRelacionado", fetch = FetchType.LAZY)
    private List<Factura> documentoRelacionadoList;
    @JoinColumn(name = "id_factura", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Factura documentoRelacionado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_Nota")
    private Integer tipoNota;
    @Lob
    @Column(name = "xml")
    private byte[] xml;
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
    @JoinColumn(name = "punto_venta", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private PuntoVenta puntoVenta;
    
    @Transient
    private SecuenciaDocumento secuenciaDocumento;

    public Factura() {
        // discriminador
        //1 Pendientes de Compra
        //2 Pedido de Compra
        //3 Pedido de Venta
        //4 Factura de compra
        //5 Factura de Venta
        //6 Transferencia
        //7 Proforma
    }

    public Factura(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public String getDespacharA() {
        return despacharA;
    }

    public void setDespacharA(String despacharA) {
        this.despacharA = despacharA;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<FacturaDetalle> getFacturaDetalleList() {
        Collections.sort(facturaDetalleList, new Comparator<FacturaDetalle>() {
            @Override
            public int compare(FacturaDetalle o1, FacturaDetalle o2) {
                if(o1.getCodigo() != null)
                {
                    return o1.getCodigo() < (o2.getCodigo()) ? -1 : 1;
                }
                else
                {
                    return 1;
                }
            }
        });
        return facturaDetalleList;
    }

    public void setFacturaDetalleList(List<FacturaDetalle> facturaDetalleList) {
        this.facturaDetalleList = facturaDetalleList;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<GuiaRemision> getGuiaRemisionList() {
        return guiaRemisionList;
    }

    public void setGuiaRemisionList(List<GuiaRemision> guiaRemisionList) {
        this.guiaRemisionList = guiaRemisionList;
    }

    public List<DocumentoRetencion> getDocumentoRetencion() {
        return documentoRetencion;
    }

    public void setDocumentoRetencion(List<DocumentoRetencion> documentoRetencion) {
        this.documentoRetencion = documentoRetencion;
    }

    public List<FacturaPago> getFacturaPagoList() {
        return facturaPagoList;
    }

    public void setFacturaPagoList(List<FacturaPago> facturaPagoList) {
        this.facturaPagoList = facturaPagoList;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public Contacto getContactoProveedor() {
        return contactoProveedor;
    }

    public void setContactoProveedor(Contacto contactoProveedor) {
        this.contactoProveedor = contactoProveedor;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }
    
    public SecuenciaDocumento getSecuenciaDocumento() {
        return secuenciaDocumento;
    }

    public void setSecuenciaDocumento(SecuenciaDocumento secuenciaDocumento) {
        this.secuenciaDocumento = secuenciaDocumento;
    }

    public BigDecimal getTotalRetencion() {
        return totalRetencion;
    }

    public void setTotalRetencion(BigDecimal totalRetencion) {
        this.totalRetencion = totalRetencion;
    }

    public BigDecimal getTarifaIva() {
        return tarifaIva;
    }

    public void setTarifaIva(BigDecimal tarifaIva) {
        this.tarifaIva = tarifaIva;
    }

    public List<CabeceraFacturaImpuestoTarifa> getCabeceraFacturaImpuestoTarifaList() {
        return cabeceraFacturaImpuestoTarifaList;
    }

    public void setCabeceraFacturaImpuestoTarifaList(List<CabeceraFacturaImpuestoTarifa> cabeceraFacturaImpuestoTarifaList) {
        this.cabeceraFacturaImpuestoTarifaList = cabeceraFacturaImpuestoTarifaList;
    }

    public BigDecimal getIce() {
        return ice;
    }

    public void setIce(BigDecimal ice) {
        this.ice = ice;
    }

    public BigDecimal getIrbpnr() {
        return irbpnr;
    }

    public void setIrbpnr(BigDecimal irbpnr) {
        this.irbpnr = irbpnr;
    }

    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
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

    public String getPathXmlFirmado() {
        return pathXmlFirmado;
    }

    public void setPathXmlFirmado(String pathXmlFirmado) {
        this.pathXmlFirmado = pathXmlFirmado;
    }

    public Factura getDocumentoRelacionado() {
        return documentoRelacionado;
    }

    public void setDocumentoRelacionado(Factura documentoRelacionado) {
        this.documentoRelacionado = documentoRelacionado;
    }
    
    public List<NotaCredito> getNotaCreditoList() {
        return notaCreditoList;
    }

    public void setNotaCreditoList(List<NotaCredito> notaCreditoList) {
        this.notaCreditoList = notaCreditoList;
    }

    public List<Factura> getDocumentoRelacionadoList() {
        return documentoRelacionadoList;
    }

    public void setDocumentoRelacionadoList(List<Factura> documentoRelacionadoList) {
        this.documentoRelacionadoList = documentoRelacionadoList;
    }
    
    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }
    
    public Integer getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(Integer tipoNota) {
        this.tipoNota = tipoNota;
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
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.Factura[ codigo=" + codigo + " ]";
    }
}
