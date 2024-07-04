package com.jvc.factunet.converter;

import com.jvc.factunet.entidades.CatalogoParametrosEmpresa;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.ParametroEmpresaServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;

@FacesConverter(value = "parametroEmpresaConverter")
public class ParametroEmpresaConverter implements Converter{
    
    private static final Logger LOG = Logger.getLogger(ParametroEmpresaConverter.class.getName());
    private final ParametroEmpresaServicio parametroEmpresaServicio = this.lookupSeguridadServicioBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (StringUtils.isNotBlank(value)) {
                return this.parametroEmpresaServicio.buscar(Integer.valueOf(value));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede buscar el usuario.-->", e);

        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value instanceof CatalogoParametrosEmpresa) {
                return ((CatalogoParametrosEmpresa) value).getId().toString();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede retornar la identificacion del usuario.-->", e);

        }
        return null;
    }

    /**
     * Obtiene la referencia a un ejb.
     */
    private ParametroEmpresaServicio lookupSeguridadServicioBean() {
        try {
            Context c = new InitialContext();
            return (ParametroEmpresaServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/ParametroEmpresaServicio!com.jvc.factunet.servicios.ParametroEmpresaServicio");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
