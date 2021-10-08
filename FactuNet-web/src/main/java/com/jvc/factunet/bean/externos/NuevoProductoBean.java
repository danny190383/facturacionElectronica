package com.jvc.factunet.bean.externos;

import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.entidades.Impuesto;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import com.jvc.factunet.entidades.Marca;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import com.jvc.factunet.entidades.ProductoImpuestoTarifa;
import com.jvc.factunet.entidades.ProductoStock;
import com.jvc.factunet.icefacesUtil.CatalogosProducto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import static com.jvc.factunet.icefacesUtil.FacesUtils.getServletContext;
import com.jvc.factunet.servicios.BodegaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
import com.jvc.factunet.servicios.MarcaServicio;
import com.jvc.factunet.servicios.ProductoBodegaServicio;
import com.jvc.factunet.servicios.TipoImpuestoServicio;
import com.jvc.factunet.session.Login;
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
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@ViewScoped
public class NuevoProductoBean extends CatalogosProducto implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(NuevoProductoBean.class.getName());
    
    @EJB
    private BodegaServicio bodegaServicio;
    @EJB
    private ProductoBodegaServicio productoBodegaServicio;
    @EJB
    private GrupoProductoServicio grupoProductoServicio;
    @EJB
    private TipoImpuestoServicio tipoImpuestoServicio;
    @EJB
    public MarcaServicio marcaServicio;

    private ProductoBodega productoBodega;
    private String urlLogo;
    private String nombreLogo;
    private String pathLogo;
    private final Empresa empresa = ((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa();
    private Integer tipo;
    private List<GrupoProducto> listaGrupos;
    private boolean autogenerar = false;
    private boolean autogenerarEmpaque = false;
    private String codigoBarras;
    private List<Impuesto> listaImpuestos;
    private ProductoImpuestoTarifa impuestoTarifa;
    private Impuesto impuestoSlc;
    private Marca marca;
    private List<Marca> listaMarca;
    
    public NuevoProductoBean() {
        this.listaGrupos = new ArrayList<>();
        this.listaImpuestos = new ArrayList<>();
        this.impuestoTarifa = new ProductoImpuestoTarifa();
        this.impuestoSlc = new Impuesto();
        this.listaMarca = new ArrayList<>();
    }
    
    @PostConstruct
    public void init()
    {
        this.listaMarca.addAll(this.marcaServicio.listarPorEmpresa(empresa.getCodigo())); 
        this.listaImpuestos.addAll(this.tipoImpuestoServicio.listar());
        this.productoBodega = (ProductoBodega) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("producto");
        //1 desde productos
        //2 desde transacciones
        this.tipo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipo");
        if(this.tipo != null && this.tipo == 2)
        {
            this.listaGrupos.addAll(this.grupoProductoServicio.listarSinHijosTipo(1, this.empresa.getCodigo()));
        }
        if(this.productoBodega == null)
        {
            this.inicializar();
        }
        else
        {
            this.codigoBarras = this.productoBodega.getCodigoBarras();
        }
        this.cargarFoto();
    }
    
    public void buscarNombreGrupo()
    {
        if("1".equals(this.empresa.getGenerarNombreProducto()))
        {
            if(this.tipo != null && this.tipo == 2)
            {
                this.productoBodega.setGrupo(this.listaGrupos.get(0)); 
                this.productoBodega.setNombre(this.productoBodega.getGrupo().getNombre());
            }
            else
            {
                this.productoBodega.setNombre((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nombregrupo"));
            }
        }
    }
    
    public void inicializar()
    {
        this.productoBodega = new ProductoBodega();
        this.productoBodega.setEmpresa(this.empresa);
        this.productoBodega.setUnidadMedida(super.getListaUnidadMedida().get(0));
        this.productoBodega.setCantidadEmpaque(1);
        this.productoBodega.setProductoStockList(new ArrayList<>());
        this.productoBodega.setPrecioUltimaCompra(BigDecimal.ZERO);
        this.productoBodega.setPvp(BigDecimal.ZERO);
        this.productoBodega.setUtilidad(BigDecimal.ZERO);
        this.productoBodega.setDescuentoVenta(BigDecimal.ZERO);
        this.productoBodega.setStock(BigDecimal.ZERO);
        this.productoBodega.setCodigoBarras(StringUtils.EMPTY);
        this.buscarNombreGrupo();
        this.cargarBodega();
    }
    
    public void cargarBodega()
    {
        ProductoStock productoStock = new ProductoStock();
        productoStock.setBodega(this.bodegaServicio.bodegaPrincipal(this.empresa.getCodigo()));
        productoStock.setEmpresa(this.empresa);
        productoStock.setProductoBodega(this.productoBodega);
        productoStock.setStock(BigDecimal.ZERO);
        productoStock.setStockMin(BigDecimal.ZERO);
        productoStock.setStockMax(BigDecimal.ZERO);
        this.productoBodega.getProductoStockList().add(productoStock);
    }
    
    public void subirLogo(FileUploadEvent event) {
        OutputStream os = null;
        InputStream is = null;

        try {
            this.nombreLogo = (new Date()).getTime() + ".jpg";
            this.pathLogo = getServletContext().getRealPath("/temp");
            this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            is = event.getFile().getInputStream();
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
            if (this.productoBodega.getFoto() != null && this.productoBodega.getFoto().length > 0) {
                this.pathLogo = getServletContext().getRealPath("/") + File.separator + "temp";
                this.nombreLogo = (new Date()).getTime() + ".jpg";
                fos = new FileOutputStream(pathLogo + File.separator + this.nombreLogo);
                fos.write(this.productoBodega.getFoto());
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
            if(this.productoBodega.getProductoImpuestoTarifaList() == null || this.productoBodega.getProductoImpuestoTarifaList().isEmpty()){
                FacesUtils.addErrorMessage("Debe seleccionaro al menos un impuesto");
                return;
            }
            this.productoBodega.setNombre(this.productoBodega.getNombre().trim().toUpperCase());
            if(this.codigoBarras != null){
                this.productoBodega.setCodigoBarras(this.codigoBarras.trim());
            }
            if(this.productoBodega.getModelo() != null){
                this.productoBodega.setModelo(this.productoBodega.getModelo().trim().toUpperCase());
            }
            this.productoBodega.setModelo(this.productoBodega.getModelo().trim().toUpperCase());
            if("1".equals(this.empresa.getGenerarNombreProducto()))
            {
                if(this.productoBodega.getCodigo() == null)
                {
                    this.productoBodega.setNombre(this.productoBodega.getNombre() + (this.productoBodega.getMarca().getNombre().equals("SIN MARCA") ? " " : (" " +this.productoBodega.getMarca().getNombre() + " ")) + this.productoBodega.getModelo());
                    this.productoBodega.setNombre(this.productoBodega.getNombre().trim());
                }
            }
            if(this.autogenerar)
            {
                this.productoBodega.setCodigoBarras(StringUtils.EMPTY);
                this.productoBodega.setCodigoBarras(this.productoBodega.getNombre().trim());
            }
            Path path = Paths.get(this.pathLogo + File.separator + this.nombreLogo);
            byte[] foto = Files.readAllBytes(path);
            this.productoBodega.setFoto(foto);
            if(this.productoBodega.getPresentacion().equals("2") && this.autogenerarEmpaque)
            {
                ProductoBodega productoPaca = new ProductoBodega();
                productoPaca.setNombre(this.productoBodega.getNombre() + " " + "X" + " " + this.productoBodega.getUnidadMedida().getNombre());
                productoPaca.setPadrePaca(this.productoBodega);
                productoPaca.setMarca(this.productoBodega.getMarca());
                productoPaca.setModelo(this.productoBodega.getModelo());
//                productoPaca.setIva(this.productoBodega.getIva());
                productoPaca.setUtilidad(BigDecimal.ZERO);
                productoPaca.setUnidadMedida(this.productoBodega.getUnidadMedida());
                productoPaca.setFoto(foto);
                productoPaca.setGrupo(this.productoBodega.getGrupo());
                productoPaca.setCantidadEmpaque(1);
                productoPaca.setPresentacion("1");
                productoPaca.setEmpresa(this.empresa);
                productoPaca.setPrecioUltimaCompra(BigDecimal.ZERO);
                productoPaca.setPvp(BigDecimal.ZERO);
                
                ProductoStock productoStock = new ProductoStock();
                productoStock.setBodega(this.bodegaServicio.bodegaPrincipal(this.empresa.getCodigo()));
                productoStock.setEmpresa(this.empresa);
                productoStock.setProductoBodega(productoPaca);
                productoStock.setStock(BigDecimal.ZERO);
                productoStock.setStockMin(BigDecimal.ZERO);
                productoStock.setStockMax(BigDecimal.ZERO);
                productoPaca.setProductoStockList(new ArrayList<ProductoStock>());
                productoPaca.getProductoStockList().add(productoStock);
                
                this.productoBodega.setPacaProductoList(new ArrayList<ProductoBodega>());
                this.productoBodega.getPacaProductoList().add(productoPaca);
            }
            if(this.tipo == 2)
            {
                if(this.guardarProducto()){
                    PrimeFaces.current().dialog().closeDynamic(this.productoBodega);
                }
                else
                {
                    FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoDuplicado"));
                }
            }
            else
            {
                PrimeFaces.current().dialog().closeDynamic(this.productoBodega);
            }
        }catch (EJBException e) {
            FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoDuplicado"));
        }catch (IOException ex) {
            Logger.getLogger(NuevoProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean guardarProducto()
    {
        try {
            if((this.productoBodega.getCodigoBarras() != null) && (this.productoBodega.getCodigoBarras().trim().isEmpty()))
            {
                this.productoBodega.setCodigoBarras(null);
            }
            this.productoBodegaServicio.insertar(this.productoBodega);
            return Boolean.TRUE;
        } catch (Exception ex) {
            Logger.getLogger(NuevoProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
    }
    
    public Boolean buscarProductoBarras()
    {
        if(!this.codigoBarras.trim().isEmpty())
        {
            Producto proTMP;
            proTMP = this.productoBodegaServicio.buscarCodigoBarras(this.codigoBarras, this.empresa.getCodigo(), null);
            if(proTMP != null)
            {
                this.codigoBarras = StringUtils.EMPTY;
                FacesUtils.addErrorMessage(FacesUtils.getResourceBundle().getString("productoExistente"));
                return true;
            }
        }
        return false;
    }
    
    public void agregarImpuesto() {
        if(this.productoBodega.getProductoImpuestoTarifaList() == null){
            this.productoBodega.setProductoImpuestoTarifaList(new ArrayList<>());
        }
        Boolean ban = Boolean.TRUE;
        for(ProductoImpuestoTarifa impProducto : this.productoBodega.getProductoImpuestoTarifaList()){
            if(Objects.equals(impProducto.getImpuestoTarifa().getId(), this.impuestoTarifa.getImpuestoTarifa().getId()))
            {
                ban = Boolean.FALSE;
            }
            if(Objects.equals(impProducto.getImpuestoTarifa().getImpuesto().getId(), this.impuestoTarifa.getImpuestoTarifa().getImpuesto().getId()))
            {
                ban = Boolean.FALSE;
            }
        }
        if(ban){
            this.productoBodega.getProductoImpuestoTarifaList().add(this.impuestoTarifa);
            FacesUtils.addInfoMessage(FacesUtils.getResourceBundle().getString("registroGrabado"));
            PrimeFaces.current().executeScript("PF('dialogoImpuestoTarifa').hide();");
        }
        else
        {
            FacesUtils.addErrorMessage("EL impuesto o tipo de impuesto ya esta asignado");
        }
    }
    
    public void nuevoImpuesto() {
        this.impuestoSlc = new Impuesto();
        this.impuestoTarifa = new ProductoImpuestoTarifa();
        this.impuestoTarifa.setEstado(Boolean.TRUE);
        this.impuestoTarifa.setProducto(this.productoBodega);
        this.impuestoTarifa.setImpuestoTarifa(new ImpuestoTarifa()); 
    }
    
    public void nuevo() {
        this.marca = new Marca();
        this.marca.setEmpresa(((Login)FacesUtils.getManagedBean("login")).getEmpleado().getEmpresa()); 
    }
    
    public void guardar() {
        try {
            this.marca.setNombre(this.marca.getNombre().trim().toUpperCase());
            if (this.marca.getCodigo() != null) {
                this.marcaServicio.actualizar(this.marca);
            } else {
                this.marcaServicio.insertar(this.marca);
                this.listaMarca.add(marca);
            }
            PrimeFaces.current().executeScript("PF('dialog005').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede guardar.", e);
            FacesUtils.addErrorMessage("La marca ya esta registrada.");
        }
    }
    
    public void eliminar(ProductoImpuestoTarifa parametro) {
        this.productoBodega.getProductoImpuestoTarifaList().remove(parametro);
    }

    public ProductoBodega getProductoBodega() {
        return productoBodega;
    }

    public void setProductoBodega(ProductoBodega productoBodega) {
        this.productoBodega = productoBodega;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public List<GrupoProducto> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GrupoProducto> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    public boolean isAutogenerar() {
        return autogenerar;
    }

    public void setAutogenerar(boolean autogenerar) {
        this.autogenerar = autogenerar;
    }

    public boolean isAutogenerarEmpaque() {
        return autogenerarEmpaque;
    }

    public void setAutogenerarEmpaque(boolean autogenerarEmpaque) {
        this.autogenerarEmpaque = autogenerarEmpaque;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public List<Impuesto> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<Impuesto> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public ProductoImpuestoTarifa getImpuestoTarifa() {
        return impuestoTarifa;
    }

    public void setImpuestoTarifa(ProductoImpuestoTarifa impuestoTarifa) {
        this.impuestoTarifa = impuestoTarifa;
    }

    public Impuesto getImpuestoSlc() {
        return impuestoSlc;
    }

    public void setImpuestoSlc(Impuesto impuestoSlc) {
        this.impuestoSlc = impuestoSlc;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public List<Marca> getListaMarca() {
        return listaMarca;
    }

    public void setListaMarca(List<Marca> listaMarca) {
        this.listaMarca = listaMarca;
    }
}
