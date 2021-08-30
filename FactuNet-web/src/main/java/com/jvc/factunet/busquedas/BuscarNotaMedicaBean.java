package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.MascotaNotaMedica;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.NotaMedicaVeterinariaServicio;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
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

@ManagedBean
@ViewScoped
public class BuscarNotaMedicaBean implements Serializable{

    @EJB
    private NotaMedicaVeterinariaServicio medicaVeterinariaServicio;
    
    private List<MascotaNotaMedica> listaNotasMedicas;
    private List<MascotaNotaMedica> listaNotasMedicasSelect;
    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private Date fecha;
    private Date fechaCita;
    private String nombre;
    private String mascota;
    private String ruc;
    
    public BuscarNotaMedicaBean() {
        this.listaNotasMedicas = new ArrayList<>();
        this.listaNotasMedicasSelect = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaNotasMedicas.addAll(this.medicaVeterinariaServicio.listarNotasMedicas(this.empresa.getCodigo()));
    }
    
    public void filtrar()
    {
        this.listaNotasMedicas.clear();
        this.listaNotasMedicas.addAll(this.medicaVeterinariaServicio.listarNotasMedicasFiltro(empresa.getCodigo(), fecha, nombre, ruc, mascota, fechaCita));
    }
    
//    public void envioNotificacionCita() {
//        if(this.listaNotasMedicasSelect.size()>0){
//            String asunto = "Recordatorio Cita Medica ";
//            SimpleDateFormat sd1 = new SimpleDateFormat("EEEE, dd 'de' MMMM yyyy", new Locale("es", "ES"));
//            for (MascotaNotaMedica nota : listaNotasMedicasSelect) {
//                String usuario = this.empresa.getEmail().trim();
//                String password = this.empresa.getEmailClave().trim();
//                String destinatario = nota.getPedido().getCliente().getPersona().getEmail();
//                String mensaje = this.empresa.getNombre() + "\n" + "LE RECORDAMOS QUE TIENE UNA CITA MEDICA PARA" + "\n" +
//                                 nota.getPedido().getMascota().getNombre() + "\n" +
//                                 "PARA LA FECHA : " + sd1.format(nota.getFechaProxima());
//                EmailSenderThread emailSenderThread = new EmailSenderThread(usuario, password, destinatario, asunto, mensaje);
//                emailSenderThread.start();
//            }
//            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("correoEnviado"));
//        }
//    }
    
    public void verNotaMedica(PedidoVenta pedido) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pedido", pedido);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cliente", pedido.getCliente());
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1100);
        options.put("contentWidth", 1100);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaNotaVeterinariaDialog", options, null);
    }

    public List<MascotaNotaMedica> getListaNotasMedicas() {
        return listaNotasMedicas;
    }

    public void setListaNotasMedicas(List<MascotaNotaMedica> listaNotasMedicas) {
        this.listaNotasMedicas = listaNotasMedicas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public List<MascotaNotaMedica> getListaNotasMedicasSelect() {
        return listaNotasMedicasSelect;
    }

    public void setListaNotasMedicasSelect(List<MascotaNotaMedica> listaNotasMedicasSelect) {
        this.listaNotasMedicasSelect = listaNotasMedicasSelect;
    }
}
