package com.jvc.factunet.bean;

import com.jvc.factunet.entidades.Bodega;
import com.jvc.factunet.entidades.CabeceraFacturaImpuestoTarifa;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import com.jvc.factunet.entidades.PedidoCompra;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.icefacesUtil.JasperReportUtil;
import com.jvc.factunet.servicios.DocumentosServicios;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.session.Login;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class FacturaCompraBean extends PedidoCompraBean implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(FacturaCompraBean.class.getName());
    
    @EJB
    private DocumentosServicios documentosServicios;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;

    private FacturaCompra facturaCompra;
    private FacturaDetalle facturaDetalleSelect;
    private StreamedContent file;
    
    @Override
    public void inicializar()
    {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/files/productos.xlsx");
        this.file = new DefaultStreamedContent(stream, "files/xlsx", "archivo.xlsx");
        this.facturaCompra = new FacturaCompra();
        this.facturaCompra.setFecha(new Date());
        this.facturaCompra.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.facturaCompra.setFacturaDetalleList(new ArrayList<>());
        this.facturaCompra.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa());
        this.facturaCompra.setEmpleado(((Login)FacesUtils.getManagedBean("login")).getEmpleado());
        this.facturaCompra.setGuiaRemisionList(new ArrayList<>());
        this.facturaCompra.setTotal(BigDecimal.ZERO);
        this.facturaCompra.setFacturaPagoList(new ArrayList<>());
        if(!super.getListaProveedores().isEmpty()){
            this.facturaCompra.setProveedor(super.getListaProveedores().get(0));
        }
        this.facturaCompra.setEstado("1");
        this.facturaCompra.setFacturaDetalleList(new ArrayList<>());
        this.facturaCompra.setDescripcion(StringUtils.EMPTY);
        super.setTotalTransporte(BigDecimal.ZERO);
        super.setTotalProrrateo(BigDecimal.ZERO);
        if(!super.getListaProveedores().isEmpty()){
            this.onProveedorSelect();
        }
    }
    
    
    @Override
    public void onProveedorSelect()
    {
        super.setDescuento(this.facturaCompra.getProveedor().getDescuento());
        if(this.facturaCompra.getFacturaDetalleList().size() > 0)
        {
            this.generalDescuento();
        }
    }
    
    @Override
    public void eliminar(int parametro) {
        try {
            this.facturaCompra.getFacturaDetalleList().remove(parametro);
            this.calcularTotales();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void agregarCalcular(List<Producto> listaProductos)
    {
        this.agregarProductos(listaProductos, this.facturaCompra);
        this.calcularTotales();
    }
    
    @Override
    public void setStockPrecio()
    {
        for(FacturaDetalle detalle : this.facturaCompra.getFacturaDetalleList())
        {
            detalle = this.crearDetalle(detalle);
        }
        this.calcularTotales();
    }
     
    @Override
    public void verBusquedaPedidos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("estado", "2");
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarPedidosCompraDialog", options, null);
    }
    
    public void verBusquedaFacturas() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1000);
        options.put("height", 500);
        options.put("contentWidth", 1000);
        options.put("contentHeight", 500);
        PrimeFaces.current().dialog().openDynamic("/busquedas/buscarFacturasCompraDialog", options, null);
    }
    
    @Override
    public void calcularTotales()
    {
        this.facturaCompra = (FacturaCompra) this.calcularTotalPago(this.facturaCompra);
    }
    
    public void onFacturaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.facturaCompra = (FacturaCompra) event.getObject();                 
        }
    }
     
    @Override
    public void onPedidoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            this.inicializar();
            PedidoCompra pedido = (PedidoCompra) event.getObject();
            this.facturaCompra.setCodigo(null);
            this.facturaCompra.setBodega(pedido.getBodega());
            this.facturaCompra.setProveedor(pedido.getProveedor());
            this.facturaCompra.setContactoProveedor(pedido.getContactoProveedor());
            this.facturaCompra.setPedidoCompra(pedido.getCodigo());
            this.facturaCompra.setCabeceraFacturaImpuestoTarifaList(pedido.getCabeceraFacturaImpuestoTarifaList());
            for(FacturaPago pago : pedido.getFacturaPagoList())
            {
                pago.setCodigo(null);
                pago.setFactura(this.facturaCompra);
                if(pago.getCuentaFacturaList() != null)
                {
                    for(CuentaFactura cuenta : pago.getCuentaFacturaList())
                    {
                        cuenta.setCodigo(null);
                    }
                }
                this.facturaCompra.getFacturaPagoList().add(pago);
            }
            for(GuiaRemision guia : pedido.getGuiaRemisionList())
            {
                guia.setCodigo(null);
                guia.setFactura(this.facturaCompra);
                this.facturaCompra.getGuiaRemisionList().add(guia);
            }
            this.calcularTransporte();
            this.facturaCompra.setSubtotal(pedido.getSubtotal());
            this.facturaCompra.setDescuento(pedido.getDescuento());
            this.facturaCompra.setTotal(pedido.getTotal());
            this.facturaCompra.setTotalPagar(pedido.getTotal());
            this.facturaCompra.setIva(pedido.getIva());
            this.facturaCompra.setEstado("2");
            for(FacturaDetalle detalle : pedido.getFacturaDetalleList())
            {
                detalle.setCodigo(null);
                detalle.setFactura(this.facturaCompra);
                detalle.setFecha(new Date());
                detalle.setEmpleado(null);
                ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                detalle.setStock(this.setStockBodega(productoBodega, super.getBodegaSelect()));
                detalle.setUtilidad(productoBodega.getUtilidad());
                detalle.setDescuentoVentas(productoBodega.getDescuentoVenta()); 
                detalle.setValorProrrateo(BigDecimal.ZERO);
                detalle.setPrecioVentaUnitarioTransporte(detalle.getPrecioVentaUnitarioDescuento()); 
                detalle.setPvp(detalle.getPrecioVentaUnitario().add(detalle.getPrecioVentaUnitario().multiply(productoBodega.getUtilidad().divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));
                detalle.setPvpIva(detalle.getPvp().add(detalle.getPvp().multiply(new BigDecimal(detalle.getImpuestoTarifa().getPorcentaje()).divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP))));
                this.facturaCompra.getFacturaDetalleList().add(detalle);
            }
        }
    }
    
    @Override
    public void verGuia(GuiaRemision guia) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guia", guia);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("factura", this.facturaCompra);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 700);
        options.put("contentWidth", 700);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/nuevaGuiaDialog", options, null);
    }
    
    @Override
    public void onGuiaSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            GuiaRemision guia = (GuiaRemision) event.getObject();
            if(this.facturaCompra.getGuiaRemisionList() == null)
            {
                this.facturaCompra.setGuiaRemisionList(new ArrayList<GuiaRemision>());
            }
            if((!this.facturaCompra.getGuiaRemisionList().contains(guia)) && (guia != null))
            {
                this.facturaCompra.getGuiaRemisionList().add(guia);
            }
            this.calcularTransporte();
        }
    }
    
    @Override
    public void eliminarGuia(GuiaRemision parametro) {
        try {
            this.facturaCompra.getGuiaRemisionList().remove(parametro);
            this.calcularTransporte();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    @Override
    public void calcularTransporte()
    {
        super.setTotalTransporte(BigDecimal.ZERO);
        for(GuiaRemision obj : this.facturaCompra.getGuiaRemisionList())
        {
            super.setTotalTransporte(super.getTotalTransporte().add(obj.getValor()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }
    
    @Override
    public void guardar()
    {
        try {
            if(this.facturaCompra.getFacturaDetalleList().size()>0)
            {
                if(this.facturaCompra.getCodigo() == null)
                {
                    this.facturaCompra.setBodega(new Bodega(super.getBodegaSelect()));
                    this.facturaCompra.setEstado("3");
                    this.documentosServicios.insertar(this.facturaCompra);
                }
                else
                {
                    this.documentosServicios.actualizar(this.facturaCompra);
                }
                FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("debeingresarproductos"));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            this.facturaCompra.setEstado("1");
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    public void onPagoRetencionSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            this.facturaCompra.getFacturaPagoList().clear();
            this.facturaCompra.getFacturaPagoList().addAll(listaPagosFin);
            super.actualizar(this.facturaCompra); 
        }
    }
    
    public void actualizar()
    {
        try {
            this.documentosServicios.actualizar(this.facturaCompra);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede guardar.", ex);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registroNoGuardado"));
        }
    }
    
    @Override
    public void verPago() {
        if(super.getTotalTransporte().floatValue() == super.getTotalProrrateo().floatValue())
        {
            if(this.facturaCompra.getTotal() != null)
            {
                if(this.facturaCompra.getTotal().doubleValue() > 0)
                {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lugar", 2);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaPagos", this.facturaCompra.getFacturaPagoList());
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pago", this.facturaCompra.getTotal());
                    Map<String, Object> options = new HashMap<>();
                    options.put("resizable", true);
                    options.put("draggable", true);
                    options.put("modal", true);
                    options.put("width", 800);
                    options.put("height", 500);
                    options.put("contentWidth", 800);
                    options.put("contentHeight", 500);
                    options.put("includeViewParams", true);
                    PrimeFaces.current().dialog().openDynamic("/transacciones/extras/cuentaCobroDialog", options, null);
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoMayorCero"));
                }
            }
            else
            {
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("montoMayorCero"));
            }
        }
        else
        {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("prorrateoIgual"));
        }
    }
    
    @Override
    public void onPagoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaPago> listaPagosFin = new ArrayList<>();
            listaPagosFin.addAll((List) event.getObject());
            this.facturaCompra.getFacturaPagoList().clear();
            for(FacturaPago pago : listaPagosFin){
                pago.setFactura(this.facturaCompra);
                this.facturaCompra.getFacturaPagoList().add(pago);    
            }
            this.guardar();
        }
    }
    
    public void calcularTodo(){
        this.calcularTotales();
    }
    
    public void verRegistroSeries(FacturaDetalle detalle) {
        this.facturaDetalleSelect = detalle;
        if(detalle.getFacturaDetalleSeriesList() == null)
        {
            detalle.setFacturaDetalleSeriesList(new ArrayList<>());
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("series", detalle.getFacturaDetalleSeriesList());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cantidad", detalle.getCantidad());
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("width", 900);
        options.put("height", 450);
        options.put("contentHeight", 450);
        options.put("contentWidth", 900);
        options.put("includeViewParams", true);
        PrimeFaces.current().dialog().openDynamic("/transacciones/extras/codigosProductosDialog", options, null);
    }
    
    public void onRegistroSeriesSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<FacturaDetalleSeries> lista = (List) event.getObject();
            this.setBodegaSeries(lista);
            this.facturaDetalleSelect.setFacturaDetalleSeriesList(lista);
        }
    }
    
    public void setBodegaSeries(List<FacturaDetalleSeries> lista)
    {
        for(FacturaDetalleSeries serie : lista)
        {
            serie.setBodegaActual(new Bodega(super.getBodegaSelect()));
            if(this.facturaCompra.getCodigo() != null){
                serie.setFacturaDetalle(this.facturaDetalleSelect);
                serie.getFacturaDetalleSeriesPK().setFacturaDetalle(this.facturaDetalleSelect.getCodigo()); 
            }
        }
    }
    
    @Override
    public void setBodegaDetalle()
    {
        for(FacturaDetalle detalle : this.facturaCompra.getFacturaDetalleList())
        {
            detalle.setBodega(new Bodega(super.getBodegaSelect()));
            this.setBodegaSeries(detalle.getFacturaDetalleSeriesList());
        }
    }
    
    @Override
    public void generarReporte(String tipoReporte) {
        if(this.facturaCompra.getCodigo() != null)
        {
            try {
                super.getParametros().put("factura", this.facturaCompra.getCodigo());
                super.getParametros().put("nombreReporte", "Factura o Ingreso de Compra");
                super.getParametros().put("SUBREPORT_DIR", JasperReportUtil.PATH);
                JasperReportUtil jasperBean = (JasperReportUtil) FacesUtils.getManagedBean(JasperReportUtil.NOMBRE_BEAN);
                jasperBean.jasperReport(JasperReportUtil.PATH_REPORTE_DOCUMENTO_TRANSACCION,tipoReporte, null, this.getParametros());
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "NO se puede crear el reporte.", ex);
            }
        }
    }
    
    public void onCellEditValorProrrateo(FacturaDetalle event) {
        event.setPrecioVentaUnitarioTransporte((event.getPrecioVentaUnitarioDescuento().add((event.getValorProrrateo().divide(event.getCantidad(),4, 1)))).setScale(7));
        this.onCellEditProrrateo(event);
    }
    
    public void onCellEditProrrateo(FacturaDetalle event) {
        this.calcularTotalProrrateo(this.facturaCompra);
        this.onCellEditUtilidad(event);
    }
    
    public BigDecimal valorBace(){
        BigDecimal valorBace = BigDecimal.ZERO;
        for(CabeceraFacturaImpuestoTarifa subtotalFactura : this.facturaCompra.getCabeceraFacturaImpuestoTarifaList()){
            valorBace = valorBace.add(subtotalFactura.getBaseImponible());
        }
        for(FacturaDetalle detalle : this.facturaCompra.getFacturaDetalleList())
        {
            if(detalle.getPrecioVentaUnitarioDescuento().floatValue() != detalle.getPrecioVentaUnitarioTransporte().floatValue()){
                valorBace = valorBace.subtract(detalle.getSubtotalSinDescuento());
            }
        }
        return valorBace;
    }
    
    public void prorratear() {
        if(super.getTotalTransporte().floatValue()>0){
            BigDecimal valorBace = this.valorBace();
            BigDecimal valorProrratear = super.getTotalTransporte().subtract(super.getTotalProrrateo());
            super.setTotalProrrateo(BigDecimal.ZERO);
            for(FacturaDetalle detalle : this.facturaCompra.getFacturaDetalleList())
            {
                if(detalle.getPrecioVentaUnitarioDescuento().floatValue() > 0)
                {
                    if(detalle.getPrecioVentaUnitarioDescuento().floatValue() == detalle.getPrecioVentaUnitarioTransporte().floatValue()){
                        detalle.setValorProrrateo(((detalle.getPrecioVentaUnitarioDescuento().multiply(valorProrratear)).divide(valorBace,RoundingMode.HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        detalle.setPrecioVentaUnitarioTransporte(detalle.getPrecioVentaUnitarioDescuento().add(detalle.getValorProrrateo()));
                        this.onCellEditUtilidad(detalle);
                    }
                }
            }
            this.calcularTotalProrrateo(this.facturaCompra);
        }
    }
    
    @Override
    public void generalDescuento()
    {
        for(FacturaDetalle detalle : this.facturaCompra.getFacturaDetalleList()){
            detalle.setDescuento(super.getDescuento());
            super.onCellEditDescuento(detalle, false);
        }
        this.calcularTotalProrrateo(this.facturaCompra);
        this.calcularTodo();
    }

    public FacturaCompra getFacturaCompra() {
        return facturaCompra;
    }

    public void setFacturaCompra(FacturaCompra facturaCompra) {
        this.facturaCompra = facturaCompra;
    }
    
    public void subirLogo(FileUploadEvent event) {
        InputStream in = null;
        try {
            List cellDataList = new ArrayList();
            in = event.getFile().getInputStream();
            XSSFWorkbook workBook = new XSSFWorkbook(in);
            XSSFSheet hssfSheet = workBook.getSheetAt(0);
            Iterator rowIterator = hssfSheet.rowIterator();
            while (rowIterator.hasNext()) {
                XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                Iterator iterator = hssfRow.cellIterator();
                List cellTempList = new ArrayList();
                while (iterator.hasNext()) {
                    XSSFCell hssfCell = (XSSFCell) iterator.next();
                    cellTempList.add(hssfCell);
                }
                cellDataList.add(cellTempList);
            }
            this.leer(cellDataList);
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir la imagen");
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }
    
    private void leer(List cellDataList) {
        for (int i = 0; i < cellDataList.size(); i++) {
            if(i==330){
                String d = "danny";
            }
            List cellTempList = (List) cellDataList.get(i);
            if(!(((XSSFCell) cellTempList.get(1)).getStringCellValue().trim().isEmpty()))
            {
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setFactura(this.facturaCompra);
                if(((XSSFCell) cellTempList.get(0)).getStringCellValue().trim().equals("s/c"))
                {
                    detalle.setProductoServicio(this.productoBodegaServicio.buscarNombreBodega(((XSSFCell) cellTempList.get(1)).getStringCellValue().trim(), super.getEmpresa().getCodigo()));
                }
                else
                {
                    detalle.setProductoServicio(this.productoBodegaServicio.buscarCodigoBarrasBodega(((XSSFCell) cellTempList.get(0)).getStringCellValue().trim(), super.getEmpresa().getCodigo()));
                }
                if(detalle.getProductoServicio() != null)
                {
                    Integer impuesto = (int)((XSSFCell) cellTempList.get(8)).getNumericCellValue();
                    for(ImpuestoTarifa tarifa : this.getListaTarifas()){
                        if(impuesto == 1){//no grava iva
                            if(tarifa.getId() == 1){
                                detalle.setImpuestoTarifa(tarifa);
                                break;
                            }
                        }
                        else//grava iva
                        {
                            if(tarifa.getPorcentaje().floatValue()>0){
                                detalle.setImpuestoTarifa(tarifa);
                                break;
                            }
                        }
                    }
//                    detalle.setImpuestoTarifa(new ImpuestoTarifa((int)((XSSFCell) cellTempList.get(8)).getNumericCellValue()));
                    detalle.setFecha(new Date());
                    detalle.setCantidad((new BigDecimal((((XSSFCell) cellTempList.get(2)).getNumericCellValue()))).setScale(2, RoundingMode.DOWN));
                    detalle.setPrecioVentaUnitario((new BigDecimal(((XSSFCell) cellTempList.get(3)).getNumericCellValue())).setScale(2, RoundingMode.DOWN));
                    detalle.setPrecioVentaUnitarioDescuento(detalle.getPrecioVentaUnitario());
                    detalle.setPrecioVentaUnitarioTransporte(detalle.getPrecioVentaUnitario());
                    detalle.setDescuento(BigDecimal.ZERO);
                    detalle.setValorDescuento(BigDecimal.ZERO);
                    detalle.setComision(BigDecimal.ZERO);
                    detalle.setValorComision(BigDecimal.ZERO);
                    detalle.setValorProrrateo(BigDecimal.ZERO);
                    try {
                        detalle.setFechaCaducidad(((XSSFCell) cellTempList.get(6)).getDateCellValue()); 
                    } catch (Exception e) {
                        
                    }
                    detalle.setDescuentoVentas((new BigDecimal(((XSSFCell) cellTempList.get(7)).getNumericCellValue())).setScale(2, RoundingMode.DOWN));
                    detalle.setSubtotalSinDescuento((detalle.getPrecioVentaUnitario().multiply(detalle.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                    detalle.setSubtotalConDescuento(detalle.getSubtotalSinDescuento());
                    ProductoBodega productoBodega = (ProductoBodega) detalle.getProductoServicio();
                    detalle.setStock(this.setStockBodega(productoBodega, super.getBodegaSelect()));
                    detalle.setUtilidad((new BigDecimal(((XSSFCell) cellTempList.get(4)).getNumericCellValue())).setScale(2, RoundingMode.DOWN));
                    detalle.setPvpIva((new BigDecimal(((XSSFCell) cellTempList.get(5)).getNumericCellValue())).setScale(2, RoundingMode.DOWN));
                    detalle.setPvp(detalle.getPvpIva().divide((new BigDecimal(detalle.getImpuestoTarifa().getPorcentaje()).divide(new BigDecimal("100")).add(BigDecimal.ONE)), RoundingMode.HALF_UP));
                    detalle.setBodega(new Bodega(super.getBodegaSelect()));
                    if(detalle.getUtilidad().floatValue() == 0){
                        if(detalle.getPrecioVentaUnitario().floatValue()>0){
                            detalle.setUtilidad(((detalle.getPvp().subtract(detalle.getPrecioVentaUnitarioTransporte())).divide(detalle.getPrecioVentaUnitarioTransporte(),4, 1)).multiply(new BigDecimal("100")).setScale(2));
                        }
                    }
                    this.facturaCompra.getFacturaDetalleList().add(detalle);
                }
            }
        }
        this.calcularTotales();
    }
    
    public StreamedContent getFile() {
        return file;
    }
    
    public void generarReportePOIXLS() {
        List<ProductoBodega> lista = this.productoBodegaServicio.listarTodo(super.getEmpresa().getCodigo());
        FacesContext contex = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) contex.getExternalContext().getResponse();
        response.setContentType(JasperReportUtil.TIPO_XLS);
        response.setHeader("Content-Disposition", "inline;filename=\"productos.xlsx\"");
        try {
             XSSFWorkbook libro = new XSSFWorkbook();
             XSSFFont font = libro.createFont();
             font.setFontHeight((short) 150);
             // Se crea una hoja dentro del libro
             XSSFSheet hoja = libro.createSheet("PRODUCTOS");
             XSSFRow fila0 = hoja.createRow(0);
             XSSFCell celda = fila0.createCell(0);
             Integer fila = 0;
             for (ProductoBodega producto : lista) {
                // Se crea una fila dentro de la hoja
                XSSFRow filaDatos = hoja.createRow(fila);
                
                celda = filaDatos.createCell(0);
                if(producto.getCodigoBarras() == null)
                {
                    celda.setCellValue("s/c");
                }
                else
                {
                    celda.setCellValue(producto.getCodigoBarras());
                }

                celda = filaDatos.createCell(1);
                celda.setCellValue(producto.getNombre());

                celda = filaDatos.createCell(2);
                celda.setCellValue(0);

                celda = filaDatos.createCell(3);
                celda.setCellValue(0);
                
                celda = filaDatos.createCell(4);
                celda.setCellValue(0);
                
                celda = filaDatos.createCell(5);
                celda.setCellValue(0);
                
                celda = filaDatos.createCell(6);
                celda.setCellValue("s/c");
                
                celda = filaDatos.createCell(7);
                celda.setCellValue(0);
                
                celda = filaDatos.createCell(8);
                celda.setCellValue(1);
                
                fila ++;
            }
            ServletOutputStream out = response.getOutputStream();
            libro.write(out);
            out.flush();
            out.close();       
        } catch (Exception e) {
            
        }
        contex.responseComplete();
    }
}
