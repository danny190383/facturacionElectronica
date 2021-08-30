package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Banco;
import com.jvc.factunet.entidades.BancoPersona;
import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.entidades.TipoCuenta;
import com.jvc.factunet.entidades.TipoPersona;
import com.jvc.factunet.servicios.BancoServicio;
import com.jvc.factunet.servicios.TipoCuentaServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class NuevoBancoPersonaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoBancoPersonaBean.class.getName());

    @EJB
    private BancoServicio bancoServicio;
    @EJB
    private TipoCuentaServicio tipoCuentaServicio;
    
    private BancoPersona bancoPersona;
    private List<Banco> listaBancos;
    private List<TipoCuenta> listaTipoCuenta;
    private TipoPersona persona;
    
    public NuevoBancoPersonaBean() {
        this.listaBancos = new ArrayList<>();
        this.listaTipoCuenta = new ArrayList<>();
        this.persona = new TipoPersona();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaBancos.addAll(this.bancoServicio.listar());
        this.listaTipoCuenta.addAll(this.tipoCuentaServicio.listar());
        this.persona = (TipoPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("persona");
        this.bancoPersona = (BancoPersona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bancoPersona");
        if(this.bancoPersona == null)
        {
            this.inicializar();
        }
    }
    
    private void inicializar()
    {
       this.bancoPersona = new BancoPersona();
       this.bancoPersona.setNumeroCuenta(StringUtils.EMPTY);
       this.bancoPersona.setTipoPersona(this.persona);
       this.bancoPersona.setBanco(this.listaBancos.get(0));
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        this.guardarBanco();
        PrimeFaces.current().dialog().closeDynamic(this.bancoPersona);
    }
    
    public void refreshBanco()
    {
        this.listaBancos.clear();
        this.listaBancos.addAll(this.bancoServicio.listar());
    }
    
    public void guardarBanco()
    {
        try {
            if(!verificarBanco())
            {
                Banco banco = new Banco();
                banco.setNombre(this.bancoPersona.getBanco().getNombre().trim().toUpperCase());
                this.bancoServicio.insertar(banco);
                this.bancoPersona.setBanco(banco);
                this.listaBancos.add(banco);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
        }
    }
    
    public Boolean verificarBanco()
    {
        this.refreshBanco();
        for(Banco obg : this.listaBancos)
        {
            if(this.bancoPersona.getBanco().getNombre().equals(obg.getNombre()))
            {
                this.bancoPersona.getBanco().setCodigo(obg.getCodigo());
                return true;
            }
        }
        return false;
    }

    public List<Banco> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Banco> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<TipoCuenta> getListaTipoCuenta() {
        return listaTipoCuenta;
    }

    public void setListaTipoCuenta(List<TipoCuenta> listaTipoCuenta) {
        this.listaTipoCuenta = listaTipoCuenta;
    }

    public BancoPersona getBancoPersona() {
        return bancoPersona;
    }

    public void setBancoPersona(BancoPersona bancoPersona) {
        this.bancoPersona = bancoPersona;
    }
}
