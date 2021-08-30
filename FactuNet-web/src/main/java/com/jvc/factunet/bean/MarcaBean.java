package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Marca;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.MarcaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class MarcaBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(SexoBean.class.getName());
    
    @EJB
    private MarcaServicio marcaServicio;
    
    private List<Marca> lista;
    private Marca marca;
    private String buscarMarca;
    
    public MarcaBean() {
       this.lista=new ArrayList<>();
       this.marca=new Marca();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.marcaServicio.listar());
        this.buscarMarca = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.marca = new Marca();
        this.marca.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa()); 
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.marcaServicio.listarNombre(this.buscarMarca));
        this.buscarMarca = StringUtils.EMPTY;
    }
    
    
    public void seleccionar(Marca parametro) {
        this.marca = parametro;
    }
    
    
    public void eliminar(Marca parametro) {
        try {
            this.marcaServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.marca.setNombre(this.marca.getNombre().trim().toUpperCase());
            if (this.marca.getCodigo() != null) {
                this.marcaServicio.actualizar(this.marca);
            } else {
                this.marcaServicio.insertar(this.marca);
                this.lista.add(marca);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog005').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            this.init();
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_MARCAS,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Marca> getLista() {
        return lista;
    }

    public void setLista(List<Marca> lista) {
        this.lista = lista;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getBuscarMarca() {
        return buscarMarca;
    }

    public void setBuscarMarca(String buscarMarca) {
        this.buscarMarca = buscarMarca;
    }
}
