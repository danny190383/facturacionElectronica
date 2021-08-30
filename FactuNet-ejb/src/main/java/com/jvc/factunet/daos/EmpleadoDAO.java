package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Empleado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class EmpleadoDAO extends GenericDAO{
    
    public List<Empleado> listar(Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Empleado o Where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<Empleado> listarTodos(int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Empleado o order by o.persona.apellidos");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Empleado buscar(Integer codigo) {
        Query q = em.createQuery("select o from Empleado o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Empleado)q.getSingleResult();
    }
    
    public Empleado buscarCedula(String cedula, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Empleado o where o.persona.cedula like ?1 and o.empresa.codigo = ?2");
            q.setParameter(1, cedula + "%%");
            q.setParameter(2, empresa);
            return (Empleado)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Empleado> listarBuscar(String nombres, String apellidos, String cedula, Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Empleado o Where o.empresa.codigo = ?1 and"
                + "(upper(o.persona.nombres) like ?2 "
                + "and upper(o.persona.apellidos) like ?3 "
                + "and upper(o.persona.cedula) like ?4) "
                + "order by o.persona.apellidos");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(nombres) ? "%%" : "%" + nombres.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(apellidos) ? "%%" : "%" + apellidos.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(cedula) ? "%%" : "%" + cedula.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<Empleado> listarBuscarTodos(String nombres, String apellidos, String cedula, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Empleado o Where "
                + "(upper(o.persona.nombres) like ?1 "
                + "and upper(o.persona.apellidos) like ?2 "
                + "and upper(o.persona.cedula) like ?3) "
                + "order by o.persona.apellidos");
        q.setParameter(1, StringUtils.isEmpty(nombres) ? "%%" : "%" + nombres.toUpperCase() + "%");
        q.setParameter(2, StringUtils.isEmpty(apellidos) ? "%%" : "%" + apellidos.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(cedula) ? "%%" : "%" + cedula.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombres, String apellidos, String cedula, Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Empleado o Where o.empresa.codigo = ?1 "
                     + "and "
                     + "(upper(o.persona.nombres) like ?2 and "
                     + "upper(o.persona.apellidos) like ?3 and "
                     + "upper(o.persona.cedula) like ?4) ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(nombres) ? "%%" : "%" + nombres.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(apellidos) ? "%%" : "%" + apellidos.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(cedula) ? "%%" : "%" + cedula.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contarTodos(String nombres, String apellidos, String cedula) {
        try {
            Query q = em.createQuery("select count(o) from Empleado o Where "
                     + "(upper(o.persona.nombres) like ?1 and "
                     + "upper(o.persona.apellidos) like ?2 and "
                     + "upper(o.persona.cedula) like ?3) ");
            q.setParameter(1, StringUtils.isEmpty(nombres) ? "%%" : "%" + nombres.toUpperCase() + "%");
            q.setParameter(2, StringUtils.isEmpty(apellidos) ? "%%" : "%" + apellidos.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(cedula) ? "%%" : "%" + cedula.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Empleado o Where o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contarTodos() {
        try {
            Query q = em.createQuery("select count(o) from Empleado o ");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<Empleado> listarEmpleadoEmpresas(String identificador) {
        Query q = em.createQuery("select o from Empleado o Where o.persona.cedula = ?1 and o.estado = true");
        q.setParameter(1, identificador);
        return q.getResultList();
    }
}
