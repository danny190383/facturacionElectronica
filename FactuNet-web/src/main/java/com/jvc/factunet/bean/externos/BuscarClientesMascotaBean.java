package com.jvc.factunet.bean.externos;

import com.jvc.factunet.busquedas.BuscarClientesBean;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Mascota;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarClientesMascotaBean extends ImprimirReportesBean implements Serializable{
private static final Logger LOG = Logger.getLogger(BuscarClientesBean.class.getName());

    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;

    private LazyDataModel<Mascota> lazyModel;
    private Cliente cliente;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
   
    public BuscarClientesMascotaBean() {
        this.cliente = new Cliente();
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Mascota>()
            {
                @Override
                public List<Mascota> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        String mascota = StringUtils.EMPTY;
                        String numero = StringUtils.EMPTY;
                        String raza = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {    
                            if(meta.getFilterField().equals("persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("nombre"))
                            {mascota=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("persona.numero"))
                            {numero=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("raza.nombre"))
                            {raza=(String)meta.getFilterValue();}
                        }
                        List<Mascota> result = clienteServicio.listarBuscarMascota(numero, nombres,cedula,mascota,empresa.getCodigo(), pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contarMascota(numero,nombres,cedula,mascota,empresa.getCodigo()).intValue());
                        return result;
                    }
                    else
                    {
                        List<Mascota> result = clienteServicio.listarMascota(empresa.getCodigo(), pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contarMascota(empresa.getCodigo()).intValue());
                        return result;
                    }
                }
                
                @Override
                public Mascota getRowData(String rowKey) {
                    List<Mascota> lista = (List<Mascota>) getWrappedData();
                    for(Mascota car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(Mascota car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Mascota event) {
        this.cliente = this.clienteServicio.buscarCliente(event.getPersona().getCodigo(), this.empresa.getCodigo());
        this.cliente.setMascota(event);
        List<CuentaFactura> listaCuentas = this.cuentasFacturaServicio.buscarPendientesCliente(this.cliente.getCodigo(), this.empresa.getCodigo());
        if(!listaCuentas.isEmpty()){
            this.cliente.setTotalDeuda(BigDecimal.ZERO);
            for(CuentaFactura cuenta : listaCuentas){
                this.cliente.setTotalDeuda(this.cliente.getTotalDeuda().add(cuenta.getSaldo()));
            }
            PrimeFaces.current().executeScript("PF('dlgRestriccion').show();");
        }
        else
        {
            PrimeFaces.current().dialog().closeDynamic(this.cliente);
        }
    }
    
    public void cerrarSeleccionar(){
        PrimeFaces.current().dialog().closeDynamic(this.cliente);
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
    
    public void verNuevoCliente(Mascota mascota) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", mascota.getPersona().getCodigo());
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 550);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 550);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoClienteDialog", options, null);
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            if((Cliente) event.getObject() != null)
            {
                this.init();
            }
        }
    }
    
    public LazyDataModel<Mascota> getLazyModel() {
        return lazyModel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
