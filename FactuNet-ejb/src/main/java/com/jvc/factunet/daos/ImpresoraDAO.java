package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Impresora;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ImpresoraDAO extends GenericDAO{
    public List<Impresora> listar() {
        Query q = em.createQuery("select o from Impresora o ORDER BY o.nombre");
        return q.getResultList();
    }
    
    public List<Impresora> listar(Integer empresa) {
        Query q = em.createQuery("select o from Impresora o where o.empresa.codigo = ?1 ORDER BY o.nombre");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
}
