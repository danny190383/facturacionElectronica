package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mesa")
@PrimaryKeyJoinColumn(name = "codigo" , referencedColumnName = "codigo")
@DiscriminatorValue("21")
public class Mesa extends Mantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "sillas")
    private Integer sillas;
    @Size(max = 800)
    @Column(name = "detalle")
    private String detalle;
    @JoinColumn(name = "seccion", referencedColumnName = "codigo")
    @ManyToOne
    private Seccion seccion;
    
    @Transient
    private Boolean estadoMesa;
    @Transient
    private List<PedidoVenta> listaPedidosVenta;
    @Transient
    private Cliente cliente;
    @Transient
    private Mascota mascotas;

    public Mesa() {
        this.listaPedidosVenta = new ArrayList<>();
        //estadoMesa
        //1 libre
        //2 por facturar
    }

    public Mesa(Integer codigo) {
        super.setCodigo(codigo);
    }

    public Integer getSillas() {
        return sillas;
    }

    public void setSillas(Integer sillas) {
        this.sillas = sillas;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public Boolean getEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(Boolean estadoMesa) {
        this.estadoMesa = estadoMesa;
    }

    public List<PedidoVenta> getListaPedidosVenta() {
        return listaPedidosVenta;
    }

    public void setListaPedidosVenta(List<PedidoVenta> listaPedidosVenta) {
        this.listaPedidosVenta = listaPedidosVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mascota getMascotas() {
        return mascotas;
    }

    public void setMascotas(Mascota mascotas) {
        this.mascotas = mascotas;
    }
}
