package com.jvc.factunet.entidades;

import java.io.Serializable;
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
@Table(name = "garantia_cabecera")
public class GarantiaCabecera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_garantia_secuencia")
    @SequenceGenerator(name = "secuencia_garantia_secuencia", sequenceName = "secuencia_garantia_secuencia", allocationSize = 1)
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 800)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "empresa", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa empresa;
    @JoinColumn(name = "empleado", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
    @JoinColumn(name = "cliente", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garantiaCabecera", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<GarantiaDetalle> garantiaDetalleList;

    public GarantiaCabecera() {
        //estado
        //1 ingresado
        //2 retirado
    }

    public GarantiaCabecera(Integer codigo) {
        this.codigo = codigo;
    }

    public GarantiaCabecera(Integer codigo, Date fechaIngreso) {
        this.codigo = codigo;
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<GarantiaDetalle> getGarantiaDetalleList() {
        return garantiaDetalleList;
    }

    public void setGarantiaDetalleList(List<GarantiaDetalle> garantiaDetalleList) {
        this.garantiaDetalleList = garantiaDetalleList;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
