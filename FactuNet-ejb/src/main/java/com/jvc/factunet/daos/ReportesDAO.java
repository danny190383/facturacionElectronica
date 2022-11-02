package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Reportes;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ReportesDAO extends GenericDAO{
    
    public List<Reportes> listar(String padre) {
        Query q = em.createQuery("select o from Reportes o Where o.codigoPadre.codigo = ?1 and o.estado = '1'");
        q.setParameter(1, padre);
        return q.getResultList();
    }
}
