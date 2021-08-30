package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Bodega;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class BodegaDAO extends GenericDAO{
    
    public List<Bodega> listar(Integer empresa) {
        Query q = em.createQuery("select o from Bodega o Where o.empresa.codigo = ?1");
              q.setParameter(1, empresa);
        return q.getResultList();
    }
    
    public List<Bodega> listarPorNivel(Integer nivel) {
        Query q = em.createQuery("select o from Bodega o where o.nivel = ?1 order by o.nombre");
        q.setParameter(1, nivel);
        return q.getResultList();
    }
    
    public Bodega bodegaPrincipal(Integer empresa) {
        try {
            Query q = em.createQuery("select o from Bodega o where o.empresa.codigo = ?1 and o.nivel = ?2 ");
            q.setParameter(1, empresa);
            q.setParameter(2, 1);
            return (Bodega)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
