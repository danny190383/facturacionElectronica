package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ProductoServicio;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class ProductoServiciosDAO extends GenericDAO{
    
    public ProductoServicio buscarBarras(String barras) {
        try {
            Query q = em.createQuery("select o from ProductoServicio o Where o.codigoBarras = ?1 ");
            q.setParameter(1, barras);
            return (ProductoServicio)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ProductoServicio> listar(Integer empresa, Integer grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(Integer empresa, Integer grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarBuscar(String nombre, Integer empresa, Integer grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2 "
                + "and upper(o.nombre) like ?3 "
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, grupo);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoServicio> listarBuscar(String nombre, Integer empresa, String grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and upper(o.grupo.nombre) like ?2 "
                + "and upper(o.nombre) like ?3 "
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoServicio> listarBuscarBarras(String barras, Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.codigoBarras like ?2 "
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(barras) ? "%%" : "%" + barras.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, Integer empresa, Integer grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo = ?2");
            q.setParameter(1, empresa);
            q.setParameter(2, grupo);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(String nombre, Integer empresa, String grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and upper(o.grupo.nombre) like ?2");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                    + "o.codigoBarras like ?2 "
                    + "and o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(codigoBarras) ? "%%" : "%" + codigoBarras.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarPadre(Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo IN ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo IN ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarBuscarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select o from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and upper(o.nombre) like ?3 "
                    + "order by o.nombre");
            q.setParameter(1, empresa);
            q.setParameter(2, gruposBuscar);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public Long contarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar) {
        try {
            Query q = em.createQuery("select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and upper(o.nombre) like ?3 ");
            q.setParameter(1, empresa);
            q.setParameter(2, gruposBuscar);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
