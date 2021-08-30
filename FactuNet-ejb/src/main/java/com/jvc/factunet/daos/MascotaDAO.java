package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Mascota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class MascotaDAO extends GenericDAO{
    
    public List<Mascota> listar(Integer persona) {
        Query q = em.createQuery("select o from Mascota o where o.persona.codigo = ?1 ");
        q.setParameter(1, persona);
        return q.getResultList();
    }
}
