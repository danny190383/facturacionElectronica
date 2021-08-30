package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class KardexProductosBean implements Serializable{

    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private BodegaServicio bodegaServicio;
    
    private List<Object> listaKardex;
    private List<Bodega> listaBodegas;
    private Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
    private Integer bodegaSelc;
    private ProductoBodega producto;
    private Date fechaInicio;
    private Date fechaFin;
    private String vista;
    
    public KardexProductosBean() {
        this.listaKardex = new ArrayList<>();
        this.listaBodegas = new ArrayList<>();
        this.fechaFin = new Date();
        this.fechaInicio = new Date();
    }
    
    @PostConstruct
    public void init()
    {
        this.producto = (ProductoBodega) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        this.vista = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("vista");
        this.listaBodegas.addAll(this.bodegaServicio.listar(this.empresa));
        this.bodegaSelc = this.listaBodegas.get(0).getCodigo();
        if(this.vista.equals("1")){
            this.verKardex();
        }
        else
        {
           this.verKardexFechas();
        }
    }
    
    public void filtrarKardex(){
        this.listaKardex.clear();
        if(this.vista.equals("1")){
            this.verKardex();
        }
        else
        {
           this.verKardexFechas();
        }
        if(this.listaKardex.isEmpty()){
            FacesUtils.addWarningMessage("No existen transacciones para las fechas seleccionadas");
        }
    }
    
    public void verKardex()
    {
        try {
            this.listaKardex.clear();
            this.listaKardex.addAll(this.documentosServicios.kardexProducto(this.producto.getCodigo(), this.bodegaSelc));
        } catch (Exception e) {
        }
    }
    
    public void verKardexFechas()
    {
        try {
            this.listaKardex.clear();
            this.listaKardex.addAll(this.documentosServicios.kardexProductoFechas(this.producto.getCodigo(), this.bodegaSelc, Fecha.fechaInicio(this.fechaInicio), Fecha.fechaFin(this.fechaFin)));
        } catch (Exception e) {
        }
    }

    public List<Object> getListaKardex() {
        return listaKardex;
    }

    public void setListaKardex(List<Object> listaKardex) {
        this.listaKardex = listaKardex;
    }

    public List<Bodega> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<Bodega> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public Integer getBodegaSelc() {
        return bodegaSelc;
    }

    public void setBodegaSelc(Integer bodegaSelc) {
        this.bodegaSelc = bodegaSelc;
    }

    public ProductoBodega getProducto() {
        return producto;
    }

    public void setProducto(ProductoBodega producto) {
        this.producto = producto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }
}
