package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Cargo;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.CargoServicio;
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
public class CargoBean extends ImprimirReportesBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(CargoBean.class.getName());
    
    @EJB
    private CargoServicio cargoServicio;
    
    private List<Cargo> lista;
    private Cargo cargo;
    private String buscarCargo;
    
    public CargoBean() {
        this.lista = new ArrayList<>();
        this.cargo = new Cargo();
    }
    
    @PostConstruct
    public void init()
    {
        this.lista.clear();
        this.lista.addAll(this.cargoServicio.listar());
        this.buscarCargo = StringUtils.EMPTY;
    }
    
    public void nuevo() {
        this.cargo = new Cargo();
    }
 
    public void buscar() {
        this.lista.clear();
        this.lista.addAll(this.cargoServicio.listarNombre(this.buscarCargo));
        this.buscarCargo = StringUtils.EMPTY;
    }
    
    public void seleccionar(Cargo parametro) {
        this.cargo = parametro;
    }
  
    public void eliminar(Cargo parametro) {
        try {
            this.cargoServicio.eliminar(parametro);
            this.lista.remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
   
    public void guardar() {
        try {
            this.cargo.setNombre(this.cargo.getNombre().trim().toUpperCase());
            if (this.cargo.getCodigo() != null) {
                this.cargoServicio.actualizar(this.cargo);
            } else {
                this.cargoServicio.insertar(this.cargo);
                this.lista.add(cargo);
            }
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoCargo').hide();");
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
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_LISTA_CARGOS,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<Cargo> getLista() {
        return lista;
    }

    public void setLista(List<Cargo> lista) {
        this.lista = lista;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getBuscarCargo() {
        return buscarCargo;
    }

    public void setBuscarCargo(String buscarCargo) {
        this.buscarCargo = buscarCargo;
    }

    
}
