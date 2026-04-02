package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ReporteTablet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ReporteTabletDAO extends GenericDAO{
    public List<ReporteTablet> listar() {
        Query q = em.createQuery("select o from ReporteTablet o ORDER BY o.nombre");
        return q.getResultList();
    }
}
