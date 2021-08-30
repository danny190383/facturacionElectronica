package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.PendientesCompra;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.DocumentosServicios;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class BuscarPendientesCompra {
    
    private static final Logger LOG = Logger.getLogger(BuscarPendientesCompra.class.getName());

    @EJB
    private DocumentosServicios documentosServicios;
    
    private List<FacturaDetalle> facturaDetalle;
    private List<FacturaDetalle> facturaDetalleSelc;
    private Integer bodega;
    
    public BuscarPendientesCompra() {
        this.facturaDetalle = new ArrayList<>();
        this.facturaDetalleSelc = new ArrayList<>();
        this.bodega = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bodega");
    }
    
    @PostConstruct
    public void init()
    {
        PendientesCompra pendientesCompra = this.documentosServicios.buscarPendientesCompra(this.bodega);
        if(pendientesCompra != null)
        {
            for(FacturaDetalle obj : pendientesCompra.getFacturaDetalleList())
            {
                this.facturaDetalle.add(obj);
            }
        }
    }
    
    public void seleccionarLista() {
        PrimeFaces.current().dialog().closeDynamic(this.facturaDetalleSelc);
    }
    
    public void eliminar(FacturaDetalle parametro) {
        try {
            this.documentosServicios.eliminar(parametro);
            this.facturaDetalle.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }

    public List<FacturaDetalle> getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(List<FacturaDetalle> facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }

    public List<FacturaDetalle> getFacturaDetalleSelc() {
        return facturaDetalleSelc;
    }

    public void setFacturaDetalleSelc(List<FacturaDetalle> facturaDetalleSelc) {
        this.facturaDetalleSelc = facturaDetalleSelc;
    }
}
