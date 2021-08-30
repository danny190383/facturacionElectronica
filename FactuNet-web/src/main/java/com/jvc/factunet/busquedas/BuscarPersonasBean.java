package com.jvc.factunet.busquedas;

import com.jvc.factunet.entidades.Persona;
import com.jvc.factunet.servicios.PersonaServicio;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class BuscarPersonasBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(BuscarPersonasBean.class.getName());

    @EJB
    private PersonaServicio personaServicio;

    private LazyDataModel<Persona> lazyModel;
    
    public BuscarPersonasBean() {
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<Persona>()
            {
                @Override
                public List<Persona> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String apellidos = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {       
                            if(meta.getFilterField().equals("cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("nombres"))
                            {nombres=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("apellidos"))
                            {apellidos=(String)meta.getFilterValue();}
                        }
                        List<Persona> result = personaServicio.listarBuscar(nombres,apellidos,cedula, pageSize, first);
                        lazyModel.setRowCount(personaServicio.contar(nombres,apellidos,cedula).intValue());
                        return result;
                    }
                    else
                    {
                        List<Persona> result = personaServicio.listar(pageSize, first);
                        lazyModel.setRowCount(personaServicio.contar().intValue());
                        return result;
                    }
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public void seleccionar(Persona event) {
        PrimeFaces.current().dialog().closeDynamic(event);
    }
    
    public LazyDataModel<Persona> getLazyModel() {
        return lazyModel;
    }
}
