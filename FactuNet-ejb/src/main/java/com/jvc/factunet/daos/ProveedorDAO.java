package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Proveedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class ProveedorDAO extends GenericDAO{
    
    public List<Proveedor> listar(Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Proveedor o Where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<Proveedor> listar(Integer empresa) {
        Query q = em.createQuery("select o from Proveedor o Where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
    
    public Proveedor buscar(Integer codigo) {
        Query q = em.createQuery("select o from Proveedor o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Proveedor)q.getSingleResult();
    }
    
    public Proveedor buscarCedula(String cedula, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Proveedor o where o.persona.cedula like ?1 and o.empresa.codigo = ?2 ");
            q.setParameter(1, cedula + "%%");
            q.setParameter(2, empresa);
            return (Proveedor)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Proveedor> listarBuscar(String nombre, String ciudad, String ruc, Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Proveedor o Where o.empresa.codigo = ?1 and "
                + "(upper(o.persona.nombres) like ?2 "
                + "and upper(o.persona.ciudad.nombre) like ?3 "
                + "and upper(o.persona.cedula) like ?4) "
                + "order by o.persona.nombres");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(nombre) ? "%%" : "%%" + nombre.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(ciudad) ? "%%" : "%%" + ciudad.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(ruc) ? "%%" : ruc + "%%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, String ciudad, String ruc, Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Proveedor o Where o.empresa.codigo = ?1 and "
                     + "(upper(o.persona.nombres) like ?2 and "
                     + "upper(o.persona.ciudad.nombre) like ?3 and "
                     + "upper(o.persona.cedula) like ?4) ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(ciudad) ? "%%" : "%" + ciudad.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(ruc) ? "%%" : ruc + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Proveedor o Where o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
