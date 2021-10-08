package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.Producto;
import com.jvc.factunet.entidades.ProductoBodega;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class ProductoBodegaDAO extends GenericDAO{
    
    public Producto buscarBarras(String barras, Integer empresa, Integer bodega) {
        try {
            Query q = em.createQuery("select o from Producto o Where UPPER(o.codigoBarras) = ?1 and o.empresa.codigo = ?2 ").setMaxResults(1);
            q.setParameter(1, barras.toUpperCase());
            q.setParameter(2, empresa);
            Producto producto = (Producto)q.getSingleResult();
            return producto;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Producto buscarSerie(String serie, Integer empresa) {
        try {
            Query q = em.createQuery("select o from FacturaDetalleSeries o Where "
                                    + "UPPER(o.facturaDetalleSeriesPK.codigoBarras) = ?1 "
                                    + "and o.facturaDetalle.factura.empresa.codigo = ?2 and "
                                    + "o.facturaDetalle.factura.estado = ?3 ").setMaxResults(1);
            q.setParameter(1, serie.toUpperCase());
            q.setParameter(2, empresa);
            q.setParameter(3, "3");
            FacturaDetalleSeries serieP = (FacturaDetalleSeries) q.getSingleResult();
            Producto producto = serieP.getFacturaDetalle().getProductoServicio();
            producto.setBodega(serieP.getBodegaActual());
            producto.setSerie(serieP);
            return producto;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Producto buscarSerieFactura(String serie, Integer empresa, String estadoSerie, String estadoFactura) {
        try {
            Query q = em.createQuery("select o from FacturaDetalleSeries o Where "
                                    + "o.facturaDetalleSeriesPK.codigoBarras = ?1 "
                                    + "and o.facturaDetalle.factura.empresa.codigo = ?2 "
                                    + "and o.estado = ?3 "
                                    + "and o.facturaDetalle.factura.estado = ?4 ").setMaxResults(1);
            q.setParameter(1, serie);
            q.setParameter(2, empresa);
            q.setParameter(3, estadoSerie);
            q.setParameter(4, estadoFactura);
            FacturaDetalleSeries serieP = (FacturaDetalleSeries) q.getSingleResult();
            Producto producto = serieP.getFacturaDetalle().getProductoServicio();
            producto.setBodega(serieP.getBodegaActual());
            producto.setSerie(serieP);
            return producto;
        } catch (Exception e) {
            return null;
        }
    }
    
    public ProductoBodega buscarBarrasBodega(String barras, Integer empresa) {
        try {
            Query q = em.createQuery("select o from ProductoBodega o Where o.codigoBarras = ?1 and o.empresa.codigo = ?2 ").setMaxResults(1);
            q.setParameter(1, barras);
            q.setParameter(2, empresa);
            return (ProductoBodega)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public ProductoBodega buscarNombreBodega(String nombre, Integer empresa) {
        try {
            Query q = em.createQuery("select o from ProductoBodega o Where o.nombre = ?1 and o.empresa.codigo = ?2 ").setMaxResults(1);
            q.setParameter(1, nombre);
            q.setParameter(2, empresa);
            return (ProductoBodega)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ProductoBodega> listar(Integer empresa, Integer grupo, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "o.empresa.codigo = ?1 "
                + sql
                + "and o.grupo.codigo = ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(Integer empresa, Integer grupo, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "o.empresa.codigo = ?1 "
                    + sql
                    + "and o.grupo.codigo = ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, grupo);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoBodega> listarBuscar(String nombre, String modelo, String marca, Integer empresa, Integer grupo, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "o.empresa.codigo = ?1 and o.grupo.codigo = ?2 "
                + "and upper(o.nombre) like ?3 "
                + "and upper(o.modelo) like ?4 "
                + "and upper(o.marca.nombre) like ?5 "
                + sql
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, grupo);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoBodega> listarBuscar(String nombre, String modelo, String marca, Integer empresa, String grupo, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "o.empresa.codigo = ?1 "
                + "and upper(o.grupo.nombre) like ?2 "
                + "and upper(o.nombre) like ?3 "
                + "and upper(o.modelo) like ?4 "
                + "and upper(o.marca.nombre) like ?5 "
                + sql
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public List<ProductoBodega> listarBuscarBarras(String barras, Integer empresa, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "o.empresa.codigo = ?1 "
                + "and o.codigoBarras like ?2 "
                + sql
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, StringUtils.isEmpty(barras) ? "%%" : "%" + barras.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer empresa, Integer grupo, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "(upper(o.nombre) like ?3 "
                    + "and upper(o.modelo) like ?4 "
                    + "and upper(o.marca.nombre) like ?5) "
                    + sql
                    + "and (o.empresa.codigo = ?1 and o.grupo.codigo = ?2)");
            q.setParameter(1, empresa);
            q.setParameter(2, grupo);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contar(String nombre, String modelo, String marca, Integer empresa, String grupo, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "upper(o.nombre) like ?3 "
                    + "and upper(o.grupo.nombre) like ?2 "
                    + "and upper(o.modelo) like ?4 "
                    + "and upper(o.marca.nombre) like ?5 "
                    + sql
                    + "and o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(grupo) ? "%%" : "%" + grupo.toUpperCase() + "%");
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public Long contarBarras(String codigoBarras, Integer empresa, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "o.codigoBarras like ?2 "
                    + sql
                    + "and o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            q.setParameter(2, StringUtils.isEmpty(codigoBarras) ? "%%" : "%" + codigoBarras.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoBodega> listarPadre(Integer empresa, List<Integer> gruposBuscar, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "o.empresa.codigo = ?1 "
                + sql
                + "and o.grupo.codigo IN ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
              q.setMaxResults(maxResults);
              q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(Integer empresa, List<Integer> gruposBuscar, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "o.empresa.codigo = ?1 "
                    + sql
                    + "and o.grupo.codigo IN ?2");
              q.setParameter(1, empresa);
              q.setParameter(2, gruposBuscar);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoBodega> listarBuscarPadre(String nombre, String modelo, String marca, Integer empresa, List<Integer> gruposBuscar, String lugar, int maxResults, int firstResult) {
        String sql = " ";
        if(lugar.equals("1")){
            sql = "and o.padrePaca = null ";
        }
        Query q = em.createQuery("select o from ProductoBodega o Where "
                + "(o.empresa.codigo = ?1 and o.grupo.codigo IN ?2) "
                + "and (upper(o.nombre) like ?3 "
                + "and upper(o.modelo) like ?4 "
                + "and upper(o.marca.nombre) like ?5) "
                + sql
                + "order by o.nombre");
        q.setParameter(1, empresa);
        q.setParameter(2, gruposBuscar);
        q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
        q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarPadre(String nombre, String modelo, String marca, Integer empresa, List<Integer> gruposBuscar, String lugar) {
        try {
            String sql = " ";
            if(lugar.equals("1")){
                sql = "and o.padrePaca = null ";
            }
            Query q = em.createQuery("select count(o) from ProductoBodega o Where "
                    + "(o.empresa.codigo = ?1 and o.grupo.codigo IN ?2) "
                    + "and (upper(o.nombre) like ?3 "
                    + "and upper(o.modelo) like ?4 "
                    + sql
                    + "and upper(o.marca.nombre) like ?5)");
            q.setParameter(1, empresa);
            q.setParameter(2, gruposBuscar);
            q.setParameter(3, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
            q.setParameter(4, StringUtils.isEmpty(modelo) ? "%%" : "%" + modelo.toUpperCase() + "%");
            q.setParameter(5, StringUtils.isEmpty(marca) ? "%%" : "%" + marca.toUpperCase() + "%");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ProductoBodega> listarTodo(Integer empresa) {
        Query q = em.createQuery("select o from ProductoBodega o Where o.empresa.codigo = ?1 order by o.nombre");
              q.setParameter(1, empresa);
        return q.getResultList();
    }
}
