package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.EspecieMascota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class EspecieMascotaDAO extends GenericDAO{
    
    public List<EspecieMascota> listar() {
        Query q = em.createQuery("select o from EspecieMascota o ORDER BY o.orden");
        return q.getResultList();
    }
    
     public List<EspecieMascota> listarNombre(String nombre) {
        Query q = em.createQuery("select o from EspecieMascota o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
