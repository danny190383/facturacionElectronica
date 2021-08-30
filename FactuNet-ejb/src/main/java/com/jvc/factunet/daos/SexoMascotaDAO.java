package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.SexoMascota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class SexoMascotaDAO extends GenericDAO{
    
    public List<SexoMascota> listar() {
        Query q = em.createQuery("select o from SexoMascota o ORDER BY o.orden");
        return q.getResultList();
    }
    
     public List<SexoMascota> listarNombre(String nombre) {
        Query q = em.createQuery("select o from SexoMascota o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
