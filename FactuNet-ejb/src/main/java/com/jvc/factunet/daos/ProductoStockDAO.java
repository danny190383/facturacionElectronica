package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ProductoStock;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class ProductoStockDAO extends GenericDAO{
    
    public ProductoStock buscarBarras(String barras) {
        try {
            Query q = em.createQuery("select o from ProductoStock o Where o.productoBodega.codigoBarras = ?1 ");
            q.setParameter(1, barras);
            return (ProductoStock)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public ProductoStock buscarProducto(int producto, int bodega) {
        try {
            Query q = em.createQuery("select o from ProductoStock o Where (o.productoBodega.codigo = ?1 and o.bodega.codigo = ?2)");
            q.setParameter(1, producto);
            q.setParameter(2, bodega);
            return (ProductoStock)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ProductoStock> listar(Integer bodega, Integer grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo = ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, grupo);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoStock> listar(Integer bodega, String grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and upper(o.productoBodega.grupo.nombre) like ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(Integer bodega, Integer grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo = ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, grupo);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(Integer bodega, String grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and upper(o.productoBodega.grupo.nombre) like ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoStock> listarBuscar(String nombre, String modelo, String marca, Integer bodega, Integer grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo = ?2 "
                + "and upper(o.productoBodega.nombre) like ?3 "
                + "and upper(o.productoBodega.modelo) like ?4 "
                + "and upper(o.productoBodega.marca.nombre) like ?5 "
                + "order by o.productoBodega.nombre");
        q.setParameter(1, bodega);
        q.setParameter(2, grupo);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoStock> listarBuscar(String nombre, String modelo, String marca, Integer bodega, String grupo, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.bodega.codigo = ?1 "
                + "and upper(o.productoBodega.grupo.nombre) like ?2 "
                + "and upper(o.productoBodega.nombre) like ?3 "
                + "and upper(o.productoBodega.modelo) like ?4 "
                + "and upper(o.productoBodega.marca.nombre) like ?5 "
                + "order by o.productoBodega.nombre");
        q.setParameter(1, bodega);
        q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer bodega, Integer grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                    + "(upper(o.productoBodega.nombre) like ?3 "
                    + "and upper(o.productoBodega.modelo) like ?4 "
                    + "and upper(o.productoBodega.marca.nombre) like ?5) "
                    + "and (o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo = ?2)");
            q.setParameter(1, bodega);
            q.setParameter(2, grupo);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer bodega, String grupo) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                    + "(upper(o.productoBodega.nombre) like ?3 "
                    + "and upper(o.productoBodega.modelo) like ?4 "
                    + "and upper(o.productoBodega.marca.nombre) like ?5) "
                    + "and o.bodega.codigo = ?1 "
                    + "and upper(o.productoBodega.grupo.nombre) like ?2");
            q.setParameter(1, bodega);
            q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoStock> listarPadre(Integer bodega, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo IN ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, gruposBuscar);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(Integer bodega, List<Integer> gruposBuscar) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                + "o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo IN ?2");
              q.setParameter(1, bodega);
              q.setParameter(2, gruposBuscar);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoStock> listarBuscarPadre(String nombre, String modelo, String marca, Integer bodega, List<Integer> gruposBuscar, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "(o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo IN ?2) "
                + "and (upper(o.productoBodega.nombre) like ?3 "
                + "and upper(o.productoBodega.modelo) like ?4 "
                + "and upper(o.productoBodega.marca.nombre) like ?5) "
                + "order by o.productoBodega.nombre");
        q.setParameter(1, bodega);
        q.setParameter(2, gruposBuscar);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(String nombre, String modelo, String marca, Integer bodega, List<Integer> gruposBuscar) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                    + "(o.bodega.codigo = ?1 and o.productoBodega.grupo.codigo IN ?2) "
                    + "and (upper(o.productoBodega.nombre) like ?3 "
                    + "and upper(o.productoBodega.modelo) like ?4 "
                    + "and upper(o.productoBodega.marca.nombre) like ?5)");
            q.setParameter(1, bodega);
            q.setParameter(2, gruposBuscar);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoStock> listarBuscarBarras(String barras, Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ProductoStock o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.productoBodega.codigoBarras like ?2 "
                + "order by o.productoBodega.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(barras) ? "%%" : "%" + barras.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from ProductoStock o Where "
                    + "o.productoBodega.codigoBarras like ?2 "
                    + "and o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(codigoBarras) ? "%%" : "%" + codigoBarras.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
