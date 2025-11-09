package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Anio;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class AnioDAO extends GenericDAO{
    public List<Anio> listar() {
        Query q = em.createQuery("select o from Anio o ORDER BY o.nombre");
        return q.getResultList();
    }
}
