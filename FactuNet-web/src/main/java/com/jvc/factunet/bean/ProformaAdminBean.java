package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.entidades.Proforma;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.ClienteServicio;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.session.Login;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class ProformaAdminBean extends FacturaVentaBean {
    
    private static final Logger LOG = Logger.getLogger(ProformaAdminBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private ClienteServicio clienteServicio;
    
    private Proforma proforma;

    public ProformaAdminBean() {
        this.proforma = new Proforma();
    }
    
    @Override
    public void inicializar()
    {
        this.proforma = new Proforma();
        this.proforma.setFecha(new Date());
        this.proforma.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.proforma.setFacturaDetalleList(new ArrayList<FacturaDetalle>());
        this.proforma.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.proforma.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.proforma.setGuiaRemisionList(new ArrayList<GuiaRemision>());
        this.proforma.setTotal(BigDecimal.ZERO);
        this.proforma.setFacturaPagoList(new ArrayList<FacturaPago>());
        this.proforma.setEstado("1");
        this.proforma.setCliente(new Cliente());
        this.proforma.setNumero(0);
        super.setDescuento(BigDecimal.ZERO);
        this.iniciarCliente();
        this.calcularTotales();
    }
    
    @Override
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.proforma);
        this.calcularTotales();
    }
    
    @Override
    public void calcularTotales()
    {
        this.proforma = (Proforma) this.calcularTotalPago(this.proforma);
    }
    
    @Override
    public void eliminar(int parametro) {
        try {
            this.proforma.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void buscarCliente()
    {
        try {
            Cliente clienteTmp = this.clienteServicio.buscarCedula(super.getCliente().getPersona().getCedula(), super.getEmpresa().getCodigo());
            if(clienteTmp == null)
            {
                this.iniciarCliente();
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
            }
            else
            {
                super.setCliente(clienteTmp);
                this.proforma.setCliente(super.getCliente());
                super.setDescuento(super.getCliente().getTipoCliente().getDescuento());
                this.setStockPrecio();
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("clienteEncontrado"));
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("cedulaNoReg"));
        }
    }
    
    @Override
    public void setStockPrecio()
    {
        for(FacturaDetalle detalle : this.proforma.getFacturaDetalleList())
        {
            detalle.setStock(BigDecimal.ZERO);
            if(detalle.getProductoServicio() instanceof ProductoBodega)
            {
                ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                detalle.setStock(this.setStockBodega(productoBodega,detalle.getBodega().getCodigo()));
            }
            if(detalle.getProductoServicio() instanceof ProductoPaquete)
            {
                detalle.setIsPaquete(Boolean.TRUE);
            }
        }
        this.calcularTotales();
    }
    
    @Override
    public void guardar()
    {
        try {
            if(this.proforma.getFacturaDetalleList().size()>0)
            {
                if(this.proforma.getCodigo() == null)
                {
                    this.proforma.setBodega(new Bodega(super.getBodegaSelect()));
                    this.proforma.setEstado("2");
                    this.documentosServicios.insertar(this.proforma);
                }
                else
                {
                    this.documentosServicios.actualizar(this.proforma);
                }
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("debeingresarproductos"));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    @Override
    public void onProformaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.proforma = (Proforma) event.getObject();
            super.setCliente(this.proforma.getCliente());
            this.setStockPrecio();
        }
    }
    
    @Override
    public void onClienteSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            super.setCliente((Cliente) event.getObject());
            if(super.getCliente() != null)
            {
                this.proforma.setCliente(super.getCliente());
                super.setDescuento(super.getCliente().getTipoCliente().getDescuento());
                this.generalDescuento();
                this.setStockPrecio();
            }
            else
            {
                this.iniciarCliente();
            }
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        if(this.proforma.getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", this.proforma.getCodigo());
                super.getParametros().put("nombreReporte", "Proforma Cliente");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
            }
        }
    }
    
    @Override
    public void generalDescuento()
    {
        for(FacturaDetalle detalle : this.proforma.getFacturaDetalleList()){
            detalle.setDescuento(super.getDescuento());
            super.onCellEditDescuento(detalle, false);
        }
        this.calcularTotales();
    }

    public Proforma getProforma() {
        return proforma;
    }

    public void setProforma(Proforma proforma) {
        this.proforma = proforma;
    }
}
