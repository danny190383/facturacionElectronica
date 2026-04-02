package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ReporteImpresora;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ReporteImpresoraDAO extends GenericDAO{
    public List<ReporteImpresora> listar(Integer empresa, Integer reporte) {
        Query q = em.createQuery("select o from ReporteImpresora o where o.empresa.codigo = ?1 and o.reporte.id = ?2 ");
        q.setParameter(1, empresa);
        q.setParameter(2, reporte);
        return q.getResultList();
    }
}
