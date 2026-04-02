package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Impresora;
import com.jvc.factunet.entidades.ReporteImpresora;
import com.jvc.factunet.entidades.ReporteTablet;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.ImpresoraServicio;
import com.jvc.factunet.servicios.ReporteTabletServicio;
import com.jvc.factunet.session.Login;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoReporteImpresoraBean {
    
    @EJB
    private ImpresoraServicio impresoraServicio;
    @EJB
    private ReporteTabletServicio reporteTabletServicio;
    
    private ReporteImpresora reporteImpresora;
    private List<ReporteTablet>  reporteTabletList;
    private List<Impresora> impresoraList;

    public NuevoReporteImpresoraBean() {
    }
    
    
    @PostConstruct
    public void init()
    {
        this.reporteImpresora = (ReporteImpresora) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("reporteImpresora");
        this.impresoraList = impresoraServicio.listar(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo());
        this.reporteTabletList = reporteTabletServicio.listar();
        if(this.reporteImpresora == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.reporteImpresora = new ReporteImpresora();
        this.reporteImpresora.setImpresora(new Impresora());
        this.reporteImpresora.setReporte(new ReporteTablet());
        this.reporteImpresora.setRestriccion(Boolean.TRUE); 
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        PrimeFaces.current().dialog().closeDynamic(this.reporteImpresora);
    }

    public ReporteImpresora getReporteImpresora() {
        return reporteImpresora;
    }

    public void setReporteImpresora(ReporteImpresora reporteImpresora) {
        this.reporteImpresora = reporteImpresora;
    }

    public List<ReporteTablet> getReporteTabletList() {
        return reporteTabletList;
    }

    public void setReporteTabletList(List<ReporteTablet> reporteTabletList) {
        this.reporteTabletList = reporteTabletList;
    }

    public List<Impresora> getImpresoraList() {
        return impresoraList;
    }

    public void setImpresoraList(List<Impresora> impresoraList) {
        this.impresoraList = impresoraList;
    }
    
    
}
