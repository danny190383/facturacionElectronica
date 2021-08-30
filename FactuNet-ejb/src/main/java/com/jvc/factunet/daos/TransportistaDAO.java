package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Transportista;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class TransportistaDAO extends GenericDAO{
    
    public List<Transportista> listar(Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Transportista o Where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<Transportista> listar(Integer empresa) {
        Query q = em.createQuery("select o from Transportista o Where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
    
    public Transportista buscar(Integer codigo) {
        Query q = em.createQuery("select o from Transportista o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Transportista)q.getSingleResult();
    }
    
    public Transportista buscarCedula(String cedula, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Transportista o where o.persona.cedula like ?1 and o.empresa.codigo = ?2");
            q.setParameter(1, cedula + "%%");
            q.setParameter(2, empresa);
            return (Transportista)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Transportista> listarBuscar(String nombre, String ciudad, String ruc, Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Transportista o Where o.empresa.codigo = ?1 and"
                + "(upper(o.persona.nombres) like ?2 "
                + "and upper(o.persona.ciudad.nombre) like ?3 "
                + "and upper(o.persona.cedula) like ?4) "
                + "order by o.persona.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(ciudad) ? "%%" : "%" + ciudad.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(ruc) ? "%%" : "%" + ruc.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, String ciudad, String ruc, Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Transportista o Where o.empresa.codigo = ?1 "
                     + "and "
                     + "(upper(o.persona.nombres) like ?2 and "
                     + "upper(o.persona.ciudad.nombre) like ?3 and "
                     + "upper(o.persona.cedula) like ?4) ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(ciudad) ? "%%" : "%" + ciudad.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(ruc) ? "%%" : "%" + ruc.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Transportista o Where o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
