package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.PaqueteVenta;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoPaquete;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.session.LoginPedidos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NuevoPaquetePedidosBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(NuevoProductoPedidosBean.class.getName());

    private ProductoPaquete productoPaquete;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private Empresa empresa;
    private BigDecimal pvpNormal;
    
    private boolean autogenerar = false;
    
    public NuevoPaquetePedidosBean() {
        this.pvpNormal = BigDecimal.ZERO;
    }
    
    @PostConstruct
    public void init()
    {
        this.empresa = ((LoginPedidos)FacesUtils.getManagedBean("login")).getCuenta().getEmpresa();
        this.productoPaquete = (ProductoPaquete) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        if(this.productoPaquete == null)
        {
            this.inicializar();
        }
        else
        {
            this.calcularTotalProductos();
        }
        this.cargarFoto();
    }
    
    public void inicializar()
    {
        this.productoPaquete = new ProductoPaquete();
        this.productoPaquete.setNombre((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nombregrupo"));
        this.productoPaquete.setEmpresa(this.empresa);
        this.productoPaquete.setPvp(BigDecimal.ZERO);
        this.productoPaquete.setPaqueteVentaList(new ArrayList<PaqueteVenta>());
    }
    
    public void subirLogo(FileUploadEvent event) {
        OutputStream os = null;
        InputStream is = null;

        try {
            this.nombreLogo = (new Date()).getTime() + ".jpg";
            this.pathLogo = getServletContext().getRealPath("/temp");
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            is = event.getFile().getInputstream();
            os = Files.newOutputStream(path);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            this.urlLogo = "/temp/" + this.nombreLogo;
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage();
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Error al subir la imagen");
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (Exception e) {
            }

        }
    }
    
    public void onCapture(CaptureEvent captureEvent) {
        this.nombreLogo = (new Date()).getTime() + ".jpg";
        byte[] data = captureEvent.getData();
        this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
        this.urlLogo = "/temp/" + this.nombreLogo;
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(this.pathLogo + File.separator + this.nombreLogo));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }
    
    public void cargarFoto()
    {
        FileOutputStream fos = null;
        try {
            
            
            if (this.productoPaquete.getFoto() != null && this.productoPaquete.getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.productoPaquete.getFoto());
                this.urlLogo = "/temp/" + this.nombreLogo;
            } else {
                this.nombreLogo = "foto_hombre.jpg";
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "resources" + File.separator + "imagenes";
                this.urlLogo = "/resources/imagenes/foto_hombre.jpg";
            }
            
        } catch (Exception e) {
            } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void cerrar() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    public void agregar() {
        try {
            this.productoPaquete.setNombre(this.productoPaquete.getNombre().trim().toUpperCase());
            if(this.autogenerar)
            {
                this.productoPaquete.setCodigoBarras(this.productoPaquete.getNombre());
            }
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.productoPaquete.setFoto(foto);
            PrimeFaces.current().dialog().closeDynamic(this.productoPaquete);
        } catch (IOException ex) {
            Logger.getLogger(NuevoProductoPedidosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void verBusquedaProductosStock() {
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 600);
        options.put("contentWidth", 1200);
        options.put("contentHeight", 600);
        PrimeFaces.current().dialog().openDynamic("/faces/busquedas/buscarProductosStockDialog", options, null);
    }
    
    public void onProductoSelect(SelectEvent event) {
        if(event.getObject() != null)
        {
            List<Producto> listaProductos = (List) event.getObject();
            for(Producto producto : listaProductos)
            {
                if(producto instanceof ProductoPaquete)
                {
                    FacesUtils.addErrorMessage("Debe ingresar servicios o productos de bodega al paquete");
                }
                else
                {
                    PaqueteVenta paquete = new PaqueteVenta();
                    paquete.setProducto(producto);
                    paquete.setCantidad(BigDecimal.ONE);
                    paquete.setPvp(producto.getPvp());
                    paquete.setTotal(producto.getPvp());
                    paquete.setDescuento(BigDecimal.ZERO);
                    paquete.setComision(BigDecimal.ZERO);
                    paquete.setProductoPadre(this.productoPaquete);
                    this.productoPaquete.getPaqueteVentaList().add(paquete);
                }
            }
            this.calcularTotalProductos();
        }
    }
    
    public void eliminarProducto(PaqueteVenta parametro) {
        try {
            this.productoPaquete.getPaqueteVentaList().remove(parametro);
            this.calcularTotalProductos();
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroEliminado"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede eliminar.", e);
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("registronoEliminado"));
        }
    }
    
    public void calcularTotalProductos()
    {
        this.productoPaquete.setPvp(BigDecimal.ZERO);
        this.pvpNormal = BigDecimal.ZERO;
        for(PaqueteVenta obj : this.productoPaquete.getPaqueteVentaList())
        {
            this.productoPaquete.setPvp(this.productoPaquete.getPvp().add((obj.getCantidad()).multiply(obj.getPvp())).setScale(2, BigDecimal.ROUND_HALF_UP));
            this.pvpNormal = this.pvpNormal.add(((obj.getCantidad()).multiply(obj.getProducto().getPvp())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }
    
    public void onCellEdit(PaqueteVenta event) {
        event.setTotal(event.getPvp().multiply(event.getCantidad()));
        this.calcularTotalProductos();
    }
    
    public void onCellEditDescuento(PaqueteVenta event) {
        event.setComision(BigDecimal.ZERO);
        event.setPvp(event.getProducto().getPvp().subtract((event.getProducto().getPvp().multiply(event.getDescuento())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
        this.onCellEdit(event);
    }
    
    public void onCellEditComision(PaqueteVenta event) {
        event.setDescuento(BigDecimal.ZERO);
        event.setPvp(event.getProducto().getPvp().add((event.getProducto().getPvp().multiply(event.getComision())).divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP));
        this.onCellEdit(event);
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }
    
    public boolean isAutogenerar() {
        return autogenerar;
    }

    public void setAutogenerar(boolean autogenerar) {
        this.autogenerar = autogenerar;
    }

    public ProductoPaquete getProductoPaquete() {
        return productoPaquete;
    }

    public void setProductoPaquete(ProductoPaquete productoPaquete) {
        this.productoPaquete = productoPaquete;
    }

    public BigDecimal getPvpNormal() {
        return pvpNormal;
    }

    public void setPvpNormal(BigDecimal pvpNormal) {
        this.pvpNormal = pvpNormal;
    }
}
