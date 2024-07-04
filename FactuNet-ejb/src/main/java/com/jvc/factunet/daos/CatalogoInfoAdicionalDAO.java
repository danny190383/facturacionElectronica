package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CatalogoInfoAdicional;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CatalogoInfoAdicionalDAO extends GenericDAO{
    public List<CatalogoInfoAdicional> listar() {
        Query q = em.createQuery("select o from CatalogoInfoAdicional o ORDER BY o.id");
        return q.getResultList();
    }
}
