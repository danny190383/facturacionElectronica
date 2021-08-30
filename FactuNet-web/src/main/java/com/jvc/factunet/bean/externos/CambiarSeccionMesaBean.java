package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Mesa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.session.Login;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class CambiarSeccionMesaBean implements Serializable{

    private Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    
    public void agregar(Mesa mesa) {
        PrimeFaces.current().dialog().closeDynamic(mesa);
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
