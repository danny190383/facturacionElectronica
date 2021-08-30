package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Controles;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ControlesDAO extends GenericDAO{
    
    public Controles buscarControl(Integer control, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Controles o where o.codigo = ?1 and o.empresa.codigo = ?2 ");
            q.setParameter(1, control);
            q.setParameter(2, empresa);
            return (Controles)q.getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
}
