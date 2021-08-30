package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Banco;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.BancoServicio;
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
public class BancoBean extends ImprimirReportesBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(BancoBean.class.getName());
    
    @EJB
    private BancoServicio bancoServicio;
    
    private List<Banco> lista;
    private Banco marca;
    private String buscarBanco;
    
    public BancoBean() {
       this.lista=new ArrayList<>();
       this.marca=new Banco();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.bancoServicio.listar());
        this.buscarBanco = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.marca = new Banco();
    }
    
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.bancoServicio.listarNombre(this.buscarBanco));
        this.buscarBanco = StringUtils.EMPTY;
    }
    
    public void seleccionar(Banco parametro) {
        this.marca = parametro;
    }
    
    public void eliminar(Banco parametro) {
        try {
            this.bancoServicio.eliminar(parametro);
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
                this.bancoServicio.actualizar(this.marca);
            } else {
                this.bancoServicio.insertar(this.marca);
                this.lista.add(marca);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialog006').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
            this.init();
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            super.getParametros().put("empresa", ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getPersona().getCodigo());
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_BANCOS,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Banco> getLista() {
        return lista;
    }

    public void setLista(List<Banco> lista) {
        this.lista = lista;
    }

    public Banco getBanco() {
        return marca;
    }

    public void setBanco(Banco marca) {
        this.marca = marca;
    }

    public String getBuscarBanco() {
        return buscarBanco;
    }

    public void setBuscarBanco(String buscarBanco) {
        this.buscarBanco = buscarBanco;
    }
}
