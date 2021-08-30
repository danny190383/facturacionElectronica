package com.jvc.factunet.bean.externos;

import com.jvc.factunet.bean.ClienteBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoClienteBean extends ClienteBean implements Serializable{
    
    public NuevoClienteBean() {

    }
    
    @PostConstruct
    public void initNuevo(){
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente") != null){
            super.setCliente(super.clienteServicio.buscarCliente((Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cliente"), super.getEmpresa().getCodigo()));
            if(super.getCliente() == null){
                super.nuevoCliente();
            }
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(super.guardar()){
            if(super.getCliente().getPersona().getMascotaPersonaList() != null && !super.getCliente().getPersona().getMascotaPersonaList().isEmpty()){
                super.getCliente().setMascota(super.getCliente().getPersona().getMascotaPersonaList().get(0));
            }
            PrimeFaces.current().dialog().closeDynamic(super.getCliente());
        }
    }
}
