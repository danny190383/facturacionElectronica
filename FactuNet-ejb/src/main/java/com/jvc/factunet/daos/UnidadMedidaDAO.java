package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.UnidadMedida;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class UnidadMedidaDAO extends GenericDAO{
   
    
    public List<UnidadMedida> listar() {
        Query q = em.createQuery("select o from UnidadMedida o ORDER BY o.orden");
        return q.getResultList();
    }
    
     public List<UnidadMedida> listarNombre(String nombre) {
        Query q = em.createQuery("select o from UnidadMedida o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.orden");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
