package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaDetalleSeriesPK;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class CodigosProductosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(CodigosProductosBean.class.getName());

    private String codigoBarras;
    private List<FacturaDetalleSeries> listaSeries;
    private List<FacturaDetalleSeries> listaSeriesDelete;
    private BigDecimal cantidad;
    
    public CodigosProductosBean() {
        this.listaSeriesDelete = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaSeries = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("series");
        this.cantidad = (BigDecimal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cantidad");
        if(this.listaSeries == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.listaSeries = new ArrayList<>();
    }
    
    public void agregarSerie()
    {
        if(this.listaSeries.size() < this.cantidad.floatValue())
        {
            Boolean ban = Boolean.TRUE;
            for(FacturaDetalleSeries obj: this.listaSeries)
            {
                if(Objects.equals(obj.getFacturaDetalleSeriesPK().getCodigoBarras(), this.codigoBarras))
                {
                    ban = Boolean.FALSE;
                    break;
                }
            }
            if(ban && (!this.codigoBarras.trim().isEmpty()))
            {
                FacturaDetalleSeries serie = new FacturaDetalleSeries();
                serie.setEstado("1");
                serie.setFacturaDetalleSeriesPK(new FacturaDetalleSeriesPK(0, this.codigoBarras));
                this.listaSeries.add(serie);
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("productoAgregado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoExistente"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cantidadLimite"));
        }
        this.codigoBarras = StringUtils.EMPTY;
    }
    
    public void eliminarSerie(FacturaDetalleSeries parametro) {
        try {
            this.listaSeries.remove(parametro);
            this.listaSeriesDelete.add(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("seriesDelete", this.listaSeriesDelete);
        PrimeFaces.current().dialog().closeDynamic(this.listaSeries);
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<FacturaDetalleSeries> getListaSeries() {
        return listaSeries;
    }

    public void setListaSeries(List<FacturaDetalleSeries> listaSeries) {
        this.listaSeries = listaSeries;
    }
}
