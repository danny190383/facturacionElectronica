package com.jvc.factunet.converter;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.GrupoProducto;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentaServicio;
import com.jvc.factunet.servicios.GrupoProductoServicio;
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

@FacesConverter(value = "grupoConverter")
public class GrupoConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(GrupoConverter.class.getName());
    private GrupoProductoServicio grupo = this.lookupSeguridadServicioBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (StringUtils.isNotBlank(value)) {
                return this.grupo.buscar(Integer.parseInt(value));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede buscar el usuario.-->", e);

        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value instanceof GrupoProducto) {
                return ((GrupoProducto) value).getCodigo().toString();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede retornar la identificacion del usuario.-->", e);

        }
        return null;
    }

    /**
     * Obtiene la referencia a un ejb.
     */
    private GrupoProductoServicio lookupSeguridadServicioBean() {
        try {
            Context c = new InitialContext();
            return (GrupoProductoServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/GrupoProductoServicio!com.jvc.factunet.servicios.GrupoProductoServicio");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
