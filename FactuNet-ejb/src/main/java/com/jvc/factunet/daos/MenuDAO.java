package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Menu;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class MenuDAO extends GenericDAO{
    
    public List<Menu> listarPorNivel(Integer nivel) {
        Query q = em.createQuery("select o from Menu o where o.nivel = ?1 order by o.nombre");
        q.setParameter(1, nivel);
        return q.getResultList();
    }
    
    public List<Menu> listarPorPadre(Integer padre) {
        Query q = em.createQuery("select o from Menu o where o.padre.codigo = ?1 order by o.nombre");
        q.setParameter(1, padre);
        return q.getResultList();
    }
    
    
}
