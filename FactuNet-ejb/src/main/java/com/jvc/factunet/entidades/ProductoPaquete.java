package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("3")
public class ProductoPaquete extends Producto implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "productoPadre", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PaqueteVenta> paqueteVentaList;
    
    @Transient
    private BigDecimal costo;
    
    public ProductoPaquete() {
    }

    public ProductoPaquete(Integer codigo) {
        super(codigo);
    }

    public ProductoPaquete(Integer codigo, String nombre) {
        super(codigo, nombre);
    }

    public List<PaqueteVenta> getPaqueteVentaList() {
        return paqueteVentaList;
    }

    public void setPaqueteVentaList(List<PaqueteVenta> paqueteVentaList) {
        this.paqueteVentaList = paqueteVentaList;
    }

    public BigDecimal getCosto() {
        BigDecimal costoPaquete = BigDecimal.ZERO;
        for(PaqueteVenta proPaquete : this.getPaqueteVentaList())
        {
            costoPaquete = costoPaquete.add(proPaquete.getProducto().getPrecioUltimaCompra().multiply(proPaquete.getCantidad()));  
        }
        return costoPaquete.setScale(2, BigDecimal.ROUND_HALF_UP); 
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
}
