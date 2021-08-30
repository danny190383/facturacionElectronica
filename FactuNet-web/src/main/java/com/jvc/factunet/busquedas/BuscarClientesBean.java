package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.ImprimirReportesBean;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.CuentasFacturaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarClientesBean extends ImprimirReportesBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarClientesBean.class.getName());

    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;

    private LazyDataModel<Cliente> lazyModel;
    private Cliente cliente;
    private final Integer empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo();
   
    public BuscarClientesBean() {
        this.cliente = new Cliente();
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Cliente>()
            {
                @Override
                public List<Cliente> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {        
                            if(meta.getFilterField().equals("persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                        }
                        List<Cliente> result = clienteServicio.listarBuscar(nombres,cedula,empresa, pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar(nombres,cedula,empresa).intValue());
                        return result;
                    }
                    else
                    {
                        List<Cliente> result = clienteServicio.listar(empresa, pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar(empresa).intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Cliente event) {
        List<CuentaFactura> listaCuentas = this.cuentasFacturaServicio.buscarPendientesCliente(event.getCodigo(), this.empresa);
        this.cliente = event;
        if(!listaCuentas.isEmpty()){
            this.cliente.setTotalDeuda(BigDecimal.ZERO);
            for(CuentaFactura cuenta : listaCuentas){
                this.cliente.setTotalDeuda(this.cliente.getTotalDeuda().add(cuenta.getSaldo()));
            }
            PrimeFaces.current().executeScript("PF('dlgRestriccion').show();");
        }
        else
        {
            PrimeFaces.current().dialog().closeDynamic(event);
        }
    }
    
    public void cerrarSeleccionar(){
        PrimeFaces.current().dialog().closeDynamic(this.cliente);
    }
    
    public void refrescarLista(){
        this.init();
    }
    
    public void eliminar(Cliente parametro) {
        try {
            this.clienteServicio.eliminar(parametro);
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte, Integer persona) {
        try {
            super.getParametros().put("empresa", this.empresa);
            super.getParametros().put("persona", persona);
            super.getParametros().put("tipo", 1);
            super.getParametros().put("nombreReporte", "Ficha Cliente");
            super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
            JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
            jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_FICHA_GENERAL_PERSONA,tipoReporte, null, this.getParametros());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
        }
    }
    
    public LazyDataModel<Cliente> getLazyModel() {
        return lazyModel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
