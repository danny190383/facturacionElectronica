package com.jvc.factunet.entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "reporte_impresora")
public class ReporteImpresora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "reporte", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ReporteTablet reporte;

    @JoinColumn(name = "impresora", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Impresora impresora;
    
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;

    @Column(name = "restriccion")
    private Boolean restriccion;

    public ReporteImpresora() {
    }

    public ReporteImpresora(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ReporteTablet getReporte() {
        return reporte;
    }

    public Impresora getImpresora() {
        return impresora;
    }

    public Boolean getRestriccion() {
        return restriccion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReporte(ReporteTablet reporte) {
        this.reporte = reporte;
    }

    public void setImpresora(Impresora impresora) {
        this.impresora = impresora;
    }

    public void setRestriccion(Boolean restriccion) {
        this.restriccion = restriccion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
}