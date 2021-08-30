package com.jvc.factunet.bean.reportes;

import com.jvc.factunet.entidades.Ciudad;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Sexo;
import com.jvc.factunet.entidades.TipoCliente;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.CiudadServicio;
import com.jvc.factunet.servicios.ReportesServicio;
import com.jvc.factunet.servicios.SexoServicio;
import com.jvc.factunet.servicios.TipoClienteServicio;
import com.jvc.factunet.session.Login;
import com.jvc.factunet.utilitarios.Fecha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ReportesClientesBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ReportesClientesBean.class.getName());
    
    @EJB
    private TipoClienteServicio tipoClienteServicio;
    @EJB
    private CiudadServicio ciudadServicio;
    @EJB
    private SexoServicio sexoServicio;
    @EJB
    private ReportesServicio reportesServicio;
    
    private List<TipoCliente> listaTipoCliente;
    private List<Ciudad> listaCiudades;
    private List<Sexo> listaSexo;
    private Integer tipoclienteSelc;
    private Integer ciudadSelc;
    private Integer sexoSelc;
    private Date fechaInicio;
    private Date fechaFin;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public ReportesClientesBean() {
        this.listaTipoCliente = new ArrayList<>();
        this.listaCiudades = new ArrayList<>();
        this.listaSexo = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.tipoclienteSelc = -1;
        this.ciudadSelc = -1;
        this.fechaFin = new Date();
        this.fechaInicio = new Date();
        this.listaTipoCliente.addAll(this.tipoClienteServicio.listar());
        this.listaCiudades.addAll(this.ciudadServicio.listar());
        this.listaSexo.addAll(this.sexoServicio.listar());
        super.getListaReportes().addAll(this.reportesServicio.listarPadre("4"));
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        try {
            this.getParametros().put("empresa", this.empresa.getCodigo());
            this.getParametros().put("tipo",  -1 == this.tipoclienteSelc ? "%%" : this.tipoclienteSelc);
            this.getParametros().put("ciudad", -1 == this.ciudadSelc ? "%%" : this.ciudadSelc);
            this.getParametros().put("sexo", -1 == this.sexoSelc ? "%%" : this.sexoSelc);
            this.getParametros().put("ad_fini", Fecha.fechaInicio(this.fechaInicio));
            this.getParametros().put("ad_ffin", Fecha.utilDateToSQLTimestamp(Fecha.fechaFin(this.fechaFin)));
            this.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            switch (super.getReporteSeleccionado()) {
                case "4.1":
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_CLIENTES,tipoReporte, null, this.getParametros());
                    break;
                case "4.2":
                    jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_CLIENTES_CUMPLEAÑOS,tipoReporte, null, this.getParametros());
                    break;
                default:
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("reporteNoEncontrado"));
                    break;
            }
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }

    public List<TipoCliente> getListaTipoCliente() {
        return listaTipoCliente;
    }

    public void setListaTipoCliente(List<TipoCliente> listaTipoCliente) {
        this.listaTipoCliente = listaTipoCliente;
    }

    public List<Ciudad> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Sexo> getListaSexo() {
        return listaSexo;
    }

    public void setListaSexo(List<Sexo> listaSexo) {
        this.listaSexo = listaSexo;
    }

    public Integer getTipoclienteSelc() {
        return tipoclienteSelc;
    }

    public void setTipoclienteSelc(Integer tipoclienteSelc) {
        this.tipoclienteSelc = tipoclienteSelc;
    }

    public Integer getCiudadSelc() {
        return ciudadSelc;
    }

    public void setCiudadSelc(Integer ciudadSelc) {
        this.ciudadSelc = ciudadSelc;
    }

    public Integer getSexoSelc() {
        return sexoSelc;
    }

    public void setSexoSelc(Integer sexoSelc) {
        this.sexoSelc = sexoSelc;
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
}
