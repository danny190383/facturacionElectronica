package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.CuentasFacturaServicio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class BuscarClientesPedidosBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(BuscarClientesPedidosBean.class.getName());

    @EJB
    private ClienteServicio clienteServicio;
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;

    private LazyDataModel<Persona> lazyModel;
    private Cliente cliente;
   
    public BuscarClientesPedidosBean() {
        this.cliente = new Cliente();
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Persona>()
            {
                @Override
                public List<Persona> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
                {
                    if (filters != null && filters.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {                       
                            String filterProperty = it.next();
                            if(filterProperty.equals("cedula"))
                            {cedula=(String)filters.get(filterProperty);}
                            if(filterProperty.equals("nombres"))
                            {nombres=(String)filters.get(filterProperty);}
                        }
                        List<Persona> result = clienteServicio.listarBuscar(nombres,cedula,pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar(nombres,cedula).intValue());
                        return result;
                    }
                    else
                    {
                        List<Persona> result = clienteServicio.listar(pageSize, first);
                        lazyModel.setRowCount(clienteServicio.contar().intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Persona event) {
        this.cliente = this.clienteServicio.buscarCliente(event.getCodigo());
        List<CuentaFactura> listaCuentas = this.cuentasFacturaServicio.buscarPendientesCliente(this.cliente.getCodigo());
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
    
    public void eliminar(Persona parametro) {
        try {
            this.clienteServicio.eliminar(this.clienteServicio.buscarCliente(parametro.getCodigo()));
            this.init();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
