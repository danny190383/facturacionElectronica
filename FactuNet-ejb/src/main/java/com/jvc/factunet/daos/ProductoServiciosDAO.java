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
    
    public ProductoServicio buscarBarras(String barras, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select o from ProductoServicio o Where "
                    + "o.estado = true "
                    + "and o.codigoBarras = ?1 ";
            }else{
                sql = "select o from ProductoServicio o Where "
                    + "o.codigoBarras = ?1 ";
            }
            Query q = em.createQuery(sql);
            q.setParameter(1, barras);
            return (ProductoServicio)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ProductoServicio> listar(Integer empresa, Integer grupo, Boolean estado, int maxResults, int firstResult) {
        String sql;
        if(estado){
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.estado = true "
                + "and o.grupo.codigo = ?2";
        }else{
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.grupo.codigo = ?2";
        }
        Query q = em.createQuery(sql);
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(Integer empresa, Integer grupo, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.estado = true "
                    + "and o.grupo.codigo = ?2";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo = ?2";
            }
            Query q = em.createQuery(sql);
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarBuscar(String nombre, Integer empresa, Integer grupo, Boolean estado, int maxResults, int firstResult) {
        String sql;
        if(estado){
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2 "
                + "and upper(o.nombre) like ?3 "
                + "and o.estado = true "
                + "order by o.nombre";
        }else{
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2 "
                + "and upper(o.nombre) like ?3 "
                + "order by o.nombre";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, empresa);
        q.setParameter(2, grupo);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, Integer empresa, Integer grupo, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and o.estado = true "
                    + "and o.grupo.codigo = ?2";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo = ?2";
            }
            Query q = em.createQuery(sql);
            q.setParameter(1, empresa);
            q.setParameter(2, grupo);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarBuscar(String nombre, Integer empresa, String grupo, Boolean estado, int maxResults, int firstResult) {
        String sql;
        if(estado){
            sql = "select o from ProductoServicio o Where "
            + "o.empresa.codigo = ?1 "
            + "and upper(o.grupo.nombre) like ?2 "
            + "and upper(o.nombre) like ?3 "
            + "and o.estado = true "
            + "order by o.nombre";
        }else{
            sql = "select o from ProductoServicio o Where "
            + "o.empresa.codigo = ?1 "
            + "and upper(o.grupo.nombre) like ?2 "
            + "and upper(o.nombre) like ?3 "
            + "order by o.nombre";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoServicio> listarBuscarBarras(String barras, Integer empresa, Boolean estado, int maxResults, int firstResult) {
        String sql;
        if(estado){
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.codigoBarras like ?2 "
                + "and o.estado = true "
                + "order by o.nombre";
        }else{
            sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.codigoBarras like ?2 "
                + "order by o.nombre";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(barras) ? "%%" : "%" + barras.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, Integer empresa, String grupo, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and o.estado = true "
                    + "and upper(o.grupo.nombre) like ?2";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and o.empresa.codigo = ?1 "
                    + "and upper(o.grupo.nombre) like ?2";
            }
            Query q = em.createQuery(sql);
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.codigoBarras like ?2 "
                    + "and o.estado = true "
                    + "and o.empresa.codigo = ?1 ";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.codigoBarras like ?2 "
                    + "and o.empresa.codigo = ?1 ";
            }
            Query q = em.createQuery(sql);
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(codigoBarras) ? "%%" : "%" + codigoBarras.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarPadre(Integer empresa, List<Integer> gruposBuscar, Boolean estado, int maxResults, int firstResult) {
        String sql;
            if(estado){
                sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.estado = true "
                + "and o.grupo.codigo IN ?2";
            }else{
                sql = "select o from ProductoServicio o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.grupo.codigo IN ?2";
            }
        Query q = em.createQuery(sql);
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.estado = true "
                    + "and o.grupo.codigo IN ?2";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2";
            }
            Query q = em.createQuery(sql);
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoServicio> listarBuscarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, Boolean estado, int maxResults, int firstResult) {
        try {
            String sql;
            if(estado){
                sql = "select o from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and upper(o.nombre) like ?3 "
                    + "and o.estado = true "
                    + "order by o.nombre";
            }else{
                sql = "select o from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and upper(o.nombre) like ?3 "
                    + "order by o.nombre";
            }
            Query q = em.createQuery(sql);
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
    
    public Long contarPadre(String nombre, Integer empresa, List<Integer> gruposBuscar, Boolean estado) {
        try {
            String sql;
            if(estado){
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and o.estado = true "
                    + "and upper(o.nombre) like ?3 ";
            }else{
                sql = "select count(o) from ProductoServicio o Where "
                    + "o.empresa.codigo = ?1 "
                    + "and o.grupo.codigo IN ?2 "
                    + "and upper(o.nombre) like ?3 ";
            }
            Query q = em.createQuery(sql);
            q.setParameter(1, empresa);
            q.setParameter(2, gruposBuscar);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
