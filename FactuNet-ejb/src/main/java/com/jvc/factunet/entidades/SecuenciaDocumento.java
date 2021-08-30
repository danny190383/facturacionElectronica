package com.jvc.factunet.entidades;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "secuencia_documento")
public class SecuenciaDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_documentos")
    @SequenceGenerator(name = "secuencia_documentos", sequenceName = "secuencia_documentos", allocationSize = 1)
    private Integer codigo;
    @Column(name = "desde")
    private Integer desde;
    @Column(name = "hasta")
    private Integer hasta;
    @Basic(optional = false)
    @Column(name = "numero_actual")
    private Integer numeroActual;
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "auto_print")
    private String autoPrint;
    @Column(name = "max_items")
    private Integer maxItems;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "punto_venta", referencedColumnName = "codigo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PuntoVenta puntoVenta;
    
    @Transient
    private Boolean autoPrintB;

    public SecuenciaDocumento() {
    }

    public SecuenciaDocumento(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getDesde() {
        return desde;
    }

    public void setDesde(Integer desde) {
        this.desde = desde;
    }

    public Integer getHasta() {
        return hasta;
    }

    public void setHasta(Integer hasta) {
        this.hasta = hasta;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public Integer getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(Integer numeroActual) {
        this.numeroActual = numeroActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAutoPrint() {
        return autoPrint;
    }

    public void setAutoPrint(String autoPrint) {
        this.autoPrint = autoPrint;
    }
    
    public Boolean getAutoPrintB() {
        if(this.autoPrint.equals("1")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setAutoPrintB(Boolean autoPrintB) {
        this.autoPrintB = autoPrintB;
        if(this.autoPrintB){
            this.setAutoPrint("1");
        }
        else
        {
            this.setAutoPrint("2");
        }
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
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
        if (!(object instanceof SecuenciaDocumento)) {
            return false;
        }
        SecuenciaDocumento other = (SecuenciaDocumento) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jvc.factunet.entidades.SecuenciaDocumento[ codigo=" + codigo + " ]";
    }
}
