package com.jvc.factunet.converter;

import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.icefacesUtil.FacesUtils;
import com.jvc.factunet.servicios.EmpleadoServicio;
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

@FacesConverter(value = "empleadoConverter")
public class EmpleadoConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(EmpleadoConverter.class.getName());
    private final EmpleadoServicio empleadoServicio = this.lookupSeguridadServicioBean();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (StringUtils.isNotBlank(value)) {
                return this.empleadoServicio.buscar(Integer.valueOf(value));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede buscar el usuario.-->", e);

        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value instanceof Empleado) {
                return ((Empleado) value).getCodigo().toString();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se puede retornar la identificacion del usuario.-->", e);

        }
        return null;
    }

    /**
     * Obtiene la referencia a un ejb.
     */
    private EmpleadoServicio lookupSeguridadServicioBean() {
        try {
            Context c = new InitialContext();
            return (EmpleadoServicio) c.lookup("java:global/" + FacesUtils.getResourceBundle().getString("nombreAplicacion") + "/EmpleadoServicio!com.jvc.factunet.servicios.EmpleadoServicio");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
