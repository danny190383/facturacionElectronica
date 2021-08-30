package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NotaEntregaBean extends FacturaVentaBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NotaEntregaBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;

    public NotaEntregaBean() {
    }
    
    @Override
    public void guardar()
    {
        try {
            this.documentosServicios.actualizar(super.getFacturaVenta());
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
        
    @Override
    public void onRegistroSeriesSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            super.getFacturaDetalleSelect().setListaSeriesDelete((List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("seriesDelete")); 
            List<FacturaDetalleSeries> lista = (List) event.getObject();
            super.getFacturaDetalleSelect().setFacturaDetalleSeriesList(lista);
        }
    }    
      
    @Override
    public void generarReporte(String tipoReporte) {
        if(super.getFacturaVenta().getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", super.getFacturaVenta().getCodigo());
                super.getParametros().put("nombreReporte", "Nota de Entrega");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_NOTA_ENTREGA,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
            }
        }
    }
}
