package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Rol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class RolDAO extends GenericDAO{
    
    public List<Rol> listar() {
        Query q = em.createQuery("select o from Rol o ORDER BY o.nombre");
        return q.getResultList();
    }
    
    public List<Rol> listarNombre(String nombre) {
        Query q = em.createQuery("select o from Rol o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
