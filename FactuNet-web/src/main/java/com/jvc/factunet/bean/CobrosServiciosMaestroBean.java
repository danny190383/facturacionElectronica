package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.CobrosServicio;
import com.jvc.factunet.enumeracion.EnumMesAnio;
import com.jvc.factunet.servicios.ServicioCobrosMaestroServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean
@ViewScoped
public class CobrosServiciosMaestroBean {
    private static final Logger LOG = Logger.getLogger(CobrosServiciosMaestroBean.class.getName());
    
    @EJB
    private ServicioCobrosMaestroServicio servicioCobrosMaestroServicio;
    
    private LazyDataModel<CobrosServicio> lazyModel;
    private List<SelectItem> mesesAnio;

    public CobrosServiciosMaestroBean() {
    }
    
    @PostConstruct
    private void init()
    {
        try {
            this.lazyModel = new LazyDataModel<CobrosServicio>()
            {
                @Override
                public List<CobrosServicio> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filterMeta) 
                {
                    if (filterMeta != null && filterMeta.size()>0) {
                        String nombres = StringUtils.EMPTY;
                        String cedula = StringUtils.EMPTY;
                        String servicio = StringUtils.EMPTY;
                        String servicioCodigo = StringUtils.EMPTY;
                        String mes = StringUtils.EMPTY;
                        String estado = StringUtils.EMPTY;
                        for (FilterMeta meta : filterMeta.values()) {        
                            if(meta.getFilterField().equals("servicioPersona.persona.cedula"))
                            {cedula=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("servicioPersona.persona.nombres"))
                            {nombres=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("servicioPersona.servicio.nombre"))
                            {servicio=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("servicioPersona.servicio.codigoBarras"))
                            {servicioCodigo=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("mes"))
                            {mes=(String)meta.getFilterValue();}
                            if(meta.getFilterField().equals("estado"))
                            {estado=(String)meta.getFilterValue();}
                        }
                        List<CobrosServicio> result = servicioCobrosMaestroServicio.listarBuscar(nombres,cedula,servicio,servicioCodigo, mes, estado, pageSize, first);
                        lazyModel.setRowCount(servicioCobrosMaestroServicio.contar(nombres,cedula,servicio,servicioCodigo, mes, estado).intValue());
                        return result;
                    }
                    else
                    {
                        List<CobrosServicio> result = servicioCobrosMaestroServicio.listar(pageSize, first);
                        lazyModel.setRowCount(servicioCobrosMaestroServicio.contar().intValue());
                        return result;
                    }
                }
                
                @Override
                public CobrosServicio getRowData(String rowKey) {
                    List<CobrosServicio> lista = (List<CobrosServicio>) getWrappedData();
                    for(CobrosServicio car : lista) {
                        if(car.getCodigo().equals(Integer.parseInt(rowKey)))
                            return car;
                    }

                    return null;
                }

                @Override
                public Object getRowKey(CobrosServicio car) {
                    return car.getCodigo();
                }
            };
        } catch (Exception e) {
            
        }
    }
    
    public List<SelectItem> getMesesAnio() {
        if (mesesAnio == null) {
            mesesAnio = new ArrayList<>();
            for (EnumMesAnio mes : EnumMesAnio.values()) {
                mesesAnio.add(new SelectItem(mes.name(), mes.getEtiqueta()));
            }
        }
        return mesesAnio;
    }
            
    public LazyDataModel<CobrosServicio> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<CobrosServicio> lazyModel) {
        this.lazyModel = lazyModel;
    }
}
