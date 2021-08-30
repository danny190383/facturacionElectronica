package com.jvc.factunet.bean.externos;

import com.jvc.factunet.bean.ProveedorBean;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoProveedorBean extends ProveedorBean implements Serializable{

    public NuevoProveedorBean() {
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        if(super.guardar()){
            PrimeFaces.current().dialog().closeDynamic(super.getProveedor());
        }
    }
}
