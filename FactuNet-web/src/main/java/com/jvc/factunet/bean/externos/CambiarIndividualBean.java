package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.ProductoStockServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class CambiarIndividualBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(CambiarIndividualBean.class.getName());
    
    @EJB
    private ProductoStockServicio productoStockServicio;
    @EJB
    private DocumentosServicios documentosServicios;

    private ProductoStock productoStock;
    private BigDecimal cantidad;
    private Integer productoIndividualSlc;
    private FacturaDetalle loteSlc;
    private List<FacturaDetalle> listaLotesCompra;
    
    public CambiarIndividualBean() {
        this.productoStock = new ProductoStock();
        this.cantidad = BigDecimal.ZERO;
        this.listaLotesCompra = new ArrayList<>();
    }
 
    @PostConstruct
    public void init(){
        this.productoStock = (ProductoStock) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        this.listaLotesCompra.addAll(this.documentosServicios.buscarLotesCompraMayorCero(productoStock.getProductoBodega().getCodigo() ,productoStock.getBodega().getCodigo()));
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(this.cantidad.floatValue() < 1){
            FacesUtils.addErrorMessage("La cantidad debe ser mayor a cero.");
            return;
        }
        if(this.loteSlc == null){
            if(this.cantidad.floatValue() <= this.productoStock.getStock().floatValue())
            {
                try {
                    ProductoStock productoStockIndividual = this.productoStockServicio.buscarProducto(this.buscarIndividual().getCodigo(), this.productoStock.getBodega().getCodigo());
                    this.productoStock.setStock(this.productoStock.getStock().subtract(this.cantidad));
                    this.productoStock.setCantidad(this.cantidad);
                    productoStockIndividual.setStock(productoStockIndividual.getStock().add(this.cantidad.multiply(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque()))));
                    BigDecimal costoIndividual = this.productoStock.getProductoBodega().getPrecioUltimaCompra().divide(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque()), RoundingMode.HALF_UP);
                    productoStockIndividual.getProductoBodega().setPrecioUltimaCompra(costoIndividual);
                    productoStockIndividual.setCantidad(this.cantidad.multiply(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque())));
                    this.productoStockServicio.actualizarIndividualizacion(((Login)FacesUtils.getManagedBean("login")).getEmpleado() ,this.productoStock, productoStockIndividual, null);
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                    this.cerrar();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                }
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cantidadMenor"));
            }
        }
        else
        {
            if(this.cantidad.floatValue() <= this.loteSlc.getStockActual().floatValue())
            {
                try {
                    ProductoStock productoStockIndividual = this.productoStockServicio.buscarProducto(this.buscarIndividual().getCodigo(), this.productoStock.getBodega().getCodigo());
                    this.productoStock.setStock(this.productoStock.getStock().subtract(this.cantidad));
                    this.productoStock.setCantidad(this.cantidad);
                    productoStockIndividual.setStock(productoStockIndividual.getStock().add(this.cantidad.multiply(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque()))));
                    BigDecimal costoIndividual = this.productoStock.getProductoBodega().getPrecioUltimaCompra().divide(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque()), RoundingMode.HALF_UP);
                    productoStockIndividual.getProductoBodega().setPrecioUltimaCompra(costoIndividual);
                    productoStockIndividual.setCantidad(this.cantidad.multiply(new BigDecimal(this.productoStock.getProductoBodega().getCantidadEmpaque())));
                    this.productoStockServicio.actualizarIndividualizacion(((Login)FacesUtils.getManagedBean("login")).getEmpleado() ,this.productoStock, productoStockIndividual, this.loteSlc);
                    FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
                    this.cerrar();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "No se puede guardar.", ex);
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
                }
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cantidadMenor"));
            }
        }
    }
    
    public ProductoBodega buscarIndividual()
    {
        for(ProductoBodega proTmp : this.productoStock.getProductoBodega().getPacaProductoList())
        {
            if(Objects.equals(this.productoIndividualSlc, proTmp.getCodigo()))
            {
                return proTmp;
            }
        }
        return null;
    }

    public ProductoStock getProductoStock() {
        return productoStock;
    }

    public void setProductoStock(ProductoStock productoStock) {
        this.productoStock = productoStock;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getProductoIndividualSlc() {
        return productoIndividualSlc;
    }

    public void setProductoIndividualSlc(Integer productoIndividualSlc) {
        this.productoIndividualSlc = productoIndividualSlc;
    }

    public List<FacturaDetalle> getListaLotesCompra() {
        return listaLotesCompra;
    }

    public void setListaLotesCompra(List<FacturaDetalle> listaLotesCompra) {
        this.listaLotesCompra = listaLotesCompra;
    }

    public FacturaDetalle getLoteSlc() {
        return loteSlc;
    }

    public void setLoteSlc(FacturaDetalle loteSlc) {
        this.loteSlc = loteSlc;
    }
}
