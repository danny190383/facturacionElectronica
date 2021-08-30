package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Impuesto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class TipoImpuestoDAO extends GenericDAO{
    
    public List<Impuesto> listar() {
        Query q = em.createQuery("select o from Impuesto o ORDER BY o.nombre");
        return q.getResultList();
    }
    
    public List<Impuesto> listarNombre(String nombre) {
        Query q = em.createQuery("select o from Impuesto o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
