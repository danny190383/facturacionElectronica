package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.MenuDAO;
import com.jvc.factunet.entidades.Menu;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class MenuServicio {
    
    private static final Logger LOG = Logger.getLogger(MenuServicio.class.getName());
    
    @EJB
    private MenuDAO menuDAO;
    
    public List<Menu> listarPorNivel(Integer nivel) {
        return this.menuDAO.listarPorNivel(nivel);
    }
    
    public List<Menu> listarPorPadre(Integer padre) {
        return this.menuDAO.listarPorPadre(padre);
    }
    
    public void eliminar(Menu parametro) throws Exception {
        this.menuDAO.eliminar(parametro);
    }
    
    public void insertar(Menu parametro) throws Exception {
        this.menuDAO.insertar(parametro);
    }

    public Menu actualizar(Menu parametro) throws Exception {
        return (Menu) this.menuDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminar(List<Menu> lista) throws Exception {
        try {
            for(Menu opcion : lista)
            {
                this.eliminar(opcion);
            }
        } catch (Exception ex) {
             LOG.log(Level.SEVERE, "No se puede eliminar el menú.", ex);
        }
    }
}
