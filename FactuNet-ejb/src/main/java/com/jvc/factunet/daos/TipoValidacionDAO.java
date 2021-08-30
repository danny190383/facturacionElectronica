package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.TipoValidacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class TipoValidacionDAO extends GenericDAO{
    
    public List<TipoValidacion> listar() {
        Query q = em.createQuery("select o from TipoValidacion o ORDER BY o.nombre");
        return q.getResultList();
    }
    
     public List<TipoValidacion> listarNombre(String nombre) {
        Query q = em.createQuery("select o from TipoValidacion o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
     }
}
