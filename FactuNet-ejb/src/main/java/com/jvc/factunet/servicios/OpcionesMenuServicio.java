package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.OpcionesMenuDAO;
import com.jvc.factunet.entidades.Menu;
import com.jvc.factunet.entidades.OpcionesMenu;
import com.jvc.factunet.entidades.OpcionesMenuPK;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class OpcionesMenuServicio {
    
    private static final Logger LOG = Logger.getLogger(OpcionesMenuServicio.class.getName());
    
    @EJB
    private OpcionesMenuDAO opcionesMenuDAO;
    
    public List<OpcionesMenu> listar(Integer rol) {
        return opcionesMenuDAO.listarPerfil(rol);
    }
    
    public void eliminar(OpcionesMenu parametro) throws Exception {
        this.opcionesMenuDAO.eliminar(parametro);
    }
    
    public void insertar(OpcionesMenu parametro) throws Exception {
        this.opcionesMenuDAO.insertar(parametro);
    }

    public OpcionesMenu actualizar(OpcionesMenu parametro) throws Exception {
        return (OpcionesMenu) this.opcionesMenuDAO.actualizar(parametro);
    }
    
    public void eliminarRol(Integer rol) {
        this.opcionesMenuDAO.eliminarRol(rol);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(List<Menu> lista, Integer rol) throws Exception{
        this.eliminarRol(rol);
        for(Menu op:lista)
        {
            OpcionesMenu opcion = new OpcionesMenu();
            opcion.setFecha(new Date());
            opcion.setOpcionesMenuPK(new OpcionesMenuPK(rol, op.getCodigo()));
            this.insertar(opcion);
        }
    }
}
