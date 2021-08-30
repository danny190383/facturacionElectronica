package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Persona;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class PersonaDAO extends GenericDAO{
    
    public List<Persona> listar(int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Persona o ");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Persona buscar(Integer codigo) {
        Query q = em.createQuery("select o from Persona o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Persona)q.getSingleResult();
    }
    
    public Persona buscarCedula(String cedula) {
        try {
            Query q = em.createQuery("select o from Persona o where o.cedula like ?1 ");
            q.setParameter(1, cedula + "%%");
            return (Persona)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Persona> listarBuscar(String nombres, String apellidos, String cedula, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Persona o Where "
                + "(upper(o.nombres) like ?1 "
                + "and upper(o.apellidos) like ?2 "
                + "and o.cedula like ?3) "
                + "order by o.apellidos");
        q.setParameter(1, StringUtils.isEmpty(nombres) ? "%%" : "%%" + nombres.toUpperCase() + "%%");
        q.setParameter(2, StringUtils.isEmpty(apellidos) ? "%%" : "%%" + apellidos.toUpperCase() + "%%");
        q.setParameter(3, StringUtils.isEmpty(cedula) ? "%%" : cedula + "%%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombres, String apellidos, String cedula) {
        try {
            Query q = em.createQuery("select count(o) from Persona o Where "
                     + "(upper(o.nombres) like ?1 and "
                     + "upper(o.apellidos) like ?2 and "
                     + "upper(o.cedula) like ?3) ");
            q.setParameter(1, StringUtils.isEmpty(nombres) ? "%%" : "%" + nombres.toUpperCase() + "%");
            q.setParameter(2, StringUtils.isEmpty(apellidos) ? "%%" : "%" + apellidos.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(cedula) ? "%%" : "%" + cedula.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar() {
        try {
            Query q = em.createQuery("select count(o) from Persona o ");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
