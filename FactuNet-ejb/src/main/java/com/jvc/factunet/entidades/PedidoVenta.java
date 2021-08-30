package com.jvc.factunet.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("3")
public class PedidoVenta extends Factura implements Serializable{
    
    @JoinColumn(name = "mesa", referencedColumnName = "codigo")
    @ManyToOne
    private Mesa mesa;
    @JoinColumn(name = "mascota", referencedColumnName = "codigo")
    @ManyToOne
    private Mascota mascota;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<MascotaNotaMedica> listaMascotaNotasMedicas;
    
    @Transient
    private Boolean facturar;
    
    public PedidoVenta() {
        this.facturar = Boolean.FALSE;
        //estado
        //1 por facturar
        //2 facturado
        //3 registrado
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public List<MascotaNotaMedica> getListaMascotaNotasMedicas() {
        return listaMascotaNotasMedicas;
    }

    public void setListaMascotaNotasMedicas(List<MascotaNotaMedica> listaMascotaNotasMedicas) {
        this.listaMascotaNotasMedicas = listaMascotaNotasMedicas;
    }

    public Boolean getFacturar() {
        return facturar;
    }

    public void setFacturar(Boolean facturar) {
        this.facturar = facturar;
    }
}
