package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.MascotaNotaMedica;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentasFacturaServicio;
import com.jvc.factunet.servicios.MascotaServicio;
import com.jvc.factunet.servicios.NotaMedicaVeterinariaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NuevaNotaVeterinariaBean implements Serializable{
    private static final Logger LOG = Logger.getLogger(NuevaNotaVeterinariaBean.class.getName());
    
    @EJB
    private NotaMedicaVeterinariaServicio medicaVeterinariaServicio;
    @EJB
    private MascotaServicio mascotaServicio;
    @EJB
    private CuentasFacturaServicio cuentasFacturaServicio;
    
    private MascotaNotaMedica mascotaNotaMedica;
    private PedidoVenta pedido;
    private Cliente cliente;
    private Mascota mascotaSelc;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();

    public NuevaNotaVeterinariaBean() {
        this.mascotaSelc = new Mascota();
    }
    
    @PostConstruct
    public void init(){
        this.pedido = (PedidoVenta) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pedido");
        this.cliente = (Cliente) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente");
        this.mascotaSelc = this.cliente.getPersona().getMascotaPersonaList().get(0);
        this.verificarDeudas();
        this.inicializar();
    }
    
    public void verificarDeudas(){
        this.cliente.setTotalDeuda(BigDecimal.ZERO);
        List<CuentaFactura> listaCuentas = this.cuentasFacturaServicio.buscarPendientesCliente(this.cliente.getCodigo(), this.empresa.getCodigo());
        if(!listaCuentas.isEmpty()){
            for(CuentaFactura cuenta : listaCuentas){
                this.cliente.setTotalDeuda(this.cliente.getTotalDeuda().add(cuenta.getSaldo()));
            }
        }
    }
    
    public void onMascotaSelec(){
        if(this.mascotaSelc.getMascotaNotaMedicaList() != null && !this.mascotaSelc.getMascotaNotaMedicaList().isEmpty()){
            if(this.mascotaSelc.getMascotaNotaMedicaList().size() > 10){
                List<MascotaNotaMedica> listaTmp = new ArrayList<>();
                listaTmp.addAll(this.mascotaSelc.getMascotaNotaMedicaList());
                this.mascotaSelc.getMascotaNotaMedicaList().clear();
                for(int i = 0;i<=10;i++){
                    this.mascotaSelc.getMascotaNotaMedicaList().add(listaTmp.get(i));
                }
            }
            this.inicializar();
        }
    }
    
    public void inicializar(){
        this.mascotaNotaMedica = new MascotaNotaMedica();
        this.mascotaNotaMedica.setFecha(new Date());
        this.mascotaNotaMedica.setPedido(this.pedido);
        this.mascotaNotaMedica.setMascota(this.mascotaSelc);
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregarNotaPedido() {
        try {
            this.mascotaNotaMedica.setMascota(this.mascotaSelc);
            if(this.mascotaNotaMedica.getCodigo() == null){
                this.medicaVeterinariaServicio.insertar(this.mascotaNotaMedica);
                this.mascotaSelc.getMascotaNotaMedicaList().add(this.mascotaNotaMedica);
            }
            else
            {
                this.medicaVeterinariaServicio.actualizar(this.mascotaNotaMedica);
            }
            PrimeFaces.current().executeScript("PF('esDialogClaseFicha').hide();");
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroAgregado"));
        } catch (Exception e) {
           LOG.log(Level.SEVERE, "No se puede eliminar.", e);
           FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("errorCargarDatos"));
        }
    }
    
    public void seleccionar(MascotaNotaMedica parametro) {
        this.mascotaNotaMedica = parametro;
    }
    
    public void eliminar(MascotaNotaMedica parametro) {
        try {
            this.medicaVeterinariaServicio.eliminar(parametro);
            this.mascotaSelc.getMascotaNotaMedicaList().remove(parametro);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void verMascota(Mascota mascota) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mascota", mascota);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persona", this.cliente.getPersona());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("contentWidth", 1000);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/faces/ventas/extras/nuevaMascotaDialog", options, null);
    }
    
    public void onMascotaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            Mascota mascota = (Mascota) event.getObject();
            if(this.cliente.getPersona().getMascotaPersonaList() == null)
            {
                this.cliente.getPersona().setMascotaPersonaList(new ArrayList<Mascota>());
            }
            if((!this.cliente.getPersona().getMascotaPersonaList().contains(mascota)) && (mascota != null))
            {
                this.cliente.getPersona().getMascotaPersonaList().add(mascota);
            }
            else
            {
                this.cliente.getPersona().getMascotaPersonaList().clear();
                this.cliente.getPersona().getMascotaPersonaList().addAll(this.mascotaServicio.listar(this.cliente.getPersona().getCodigo()));
            }
        }
    }

    public MascotaNotaMedica getMascotaNotaMedica() {
        return mascotaNotaMedica;
    }

    public void setMascotaNotaMedica(MascotaNotaMedica mascotaNotaMedica) {
        this.mascotaNotaMedica = mascotaNotaMedica;
    }

    public PedidoVenta getPedido() {
        return pedido;
    }

    public void setPedido(PedidoVenta pedido) {
        this.pedido = pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mascota getMascotaSelc() {
        return mascotaSelc;
    }

    public void setMascotaSelc(Mascota mascotaSelc) {
        this.mascotaSelc = mascotaSelc;
    }
}
