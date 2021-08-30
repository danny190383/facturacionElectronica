package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.OpcionesMenu;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class OpcionesMenuDAO extends GenericDAO{
    
    public List<OpcionesMenu> listarPerfil(Integer perfil) {
        Query q = em.createQuery("select o from OpcionesMenu o where o.opcionesMenuPK.rol = ?1 ORDER BY o.menu.orden ");
        q.setParameter(1, perfil);
        return q.getResultList();
    }
     
    public void eliminarRol(Integer rol)
    {
        Query q = em.createQuery("DELETE FROM OpcionesMenu o where o.opcionesMenuPK.rol = ?1");
        q.setParameter(1, rol).executeUpdate();
    }
    
}
