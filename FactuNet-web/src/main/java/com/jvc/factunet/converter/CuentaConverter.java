package com.jvc.factunet.converter;

import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.CuentaServicio;
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

@FacesConverter(value = "cuentaConverter")
public class CuentaConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(CuentaConverter.class.getName());
    private CuentaServicio cuenta = this.lookupSeguridadServicioBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (StringUtils.isNotBlank(value)) {
                return this.cuenta.buscar(value);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede buscar el usuario.-->", e);

        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value instanceof Cuenta) {
                return ((Cuenta) value).getIdentificador();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede retornar la identificacion del usuario.-->", e);

        }
        return null;
    }

    /**
     * Obtiene la referencia a un ejb.
     */
    private CuentaServicio lookupSeguridadServicioBean() {
        try {
            Context c = new InitialContext();
            return (CuentaServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/CuentaServicio!com.jvc.factunet.servicios.CuentaServicio");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
