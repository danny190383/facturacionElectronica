package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Provincia;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;

@Stateless
public class ProvinciaDAO extends GenericDAO {

    public List<Provincia> listar() {
        Query q = em.createQuery("select o from Provincia as o WHERE o.estado= 1 order by o.descripcion");
        return q.getResultList();
    }
}
