package com.jvc.factunet.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericDAO {
    @PersistenceContext(unitName = "factunetPU")
    protected EntityManager em;

    
    public void insertar(Object object){
        em.persist(object);
        em.flush();
    }

    
    public void eliminar(Object object) {
        em.remove(em.merge(object));
    }

    
    public Object actualizar(Object object) {
        object = em.merge(object);
        return object;
    }
}
