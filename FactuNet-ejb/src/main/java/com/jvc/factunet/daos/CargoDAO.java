package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Cargo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class CargoDAO extends GenericDAO{
    public List<Cargo> listar() {
        Query q = em.createQuery("select o from Cargo o ORDER BY o.nombre");
        return q.getResultList();
    }
    
     public List<Cargo> listarNombre(String nombre) {
        Query q = em.createQuery("select o from Cargo o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
