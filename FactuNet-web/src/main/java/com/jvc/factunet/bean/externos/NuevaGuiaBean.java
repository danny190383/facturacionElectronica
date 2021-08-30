package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.Motivo;
import com.jvc.factunet.entidades.Transportista;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.MotivoServicio;
import com.jvc.factunet.servicios.TransportistaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NuevaGuiaBean implements Serializable{

    @EJB
    private TransportistaServicio transportistaServicio;
    @EJB
    private MotivoServicio motivoServicio;
    
    private GuiaRemision guiaRemision;
    private Factura factura;
    private List<Transportista> listaTransportistas;
    private List<Motivo> listaMotivo;
    
    public NuevaGuiaBean() {
        this.listaTransportistas = new ArrayList<>();
        this.listaMotivo = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaTransportistas.addAll(this.transportistaServicio.listar(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa().getCodigo()));
        this.listaMotivo.addAll(this.motivoServicio.listar());
        this.factura = (Factura) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("factura");
        this.guiaRemision = (GuiaRemision) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("guia");
        if(this.guiaRemision == null)
        {
            this.inicializar();
        }
    }
    
    public void inicializar()
    {
        this.guiaRemision = new GuiaRemision();
        this.guiaRemision.setFactura(this.factura);
        this.guiaRemision.setFechaEnvio(new Date());
        this.guiaRemision.setFechaRecepcion(new Date());
        this.guiaRemision.setValor(BigDecimal.ZERO);
        this.guiaRemision.setEstado("1");
        this.guiaRemision.setCartones(1);
        this.guiaRemision.setMotivo(new Motivo());
        if(this.factura instanceof FacturaVenta){
            this.guiaRemision.setDestinatario(this.factura.getCliente()); 
        }
    }
    
    public void cambiarTransportista(){
        this.guiaRemision.setDireccionOrigen(this.guiaRemision.getTransportista().getPersona().getDireccion()); 
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(this.guiaRemision.getDestinatario() == null){
            FacesUtils.addErrorMessage("Debe seleccionar un destinatario.");
            return;
        }
        PrimeFaces.current().dialog().closeDynamic(this.guiaRemision);
    }
    
    public void verBusquedaClientes() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 750);
        options.put("height", 500);
        options.put("contentWidth", 750);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarClientesDialog", options, null);
    }
    
    public void verNuevoCliente() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", null);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevoClienteDialog", options, null);
    }
    
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.guiaRemision.setDestinatario((Cliente) event.getObject());
        }
    }

    public GuiaRemision getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(GuiaRemision guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public List<Transportista> getListaTransportistas() {
        return listaTransportistas;
    }

    public void setListaTransportistas(List<Transportista> listaTransportistas) {
        this.listaTransportistas = listaTransportistas;
    }

    public List<Motivo> getListaMotivo() {
        return listaMotivo;
    }

    public void setListaMotivo(List<Motivo> listaMotivo) {
        this.listaMotivo = listaMotivo;
    }
}
