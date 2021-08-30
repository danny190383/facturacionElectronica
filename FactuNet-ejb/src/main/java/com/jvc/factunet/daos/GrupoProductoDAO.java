package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.GrupoProducto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class GrupoProductoDAO extends GenericDAO{
    
    public List<GrupoProducto> listar() {
        Query q = em.createQuery("select o from GrupoProducto o ");
        return q.getResultList();
    }
    
    public GrupoProducto buscar(Integer codigo) {
        Query q = em.createQuery("select o from GrupoProducto o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (GrupoProducto)q.getSingleResult();
    }
    
    public List<GrupoProducto> listarSinHijos(Integer empresa) {
        Query q = em.createQuery("select o from GrupoProducto o where o.grupoProductoList IS EMPTY and o.empresa.codigo = ?1 order by o.nombre ");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarSinHijosTipo(Integer tipo, Integer empresa) {
        Query q = em.createQuery("select o from GrupoProducto o where o.grupoProductoList IS EMPTY and o.tipo = ?1 and o.empresa.codigo = ?2 order by o.nombre ");
        q.setParameter(1, tipo);
        q.setParameter(2, empresa);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarPorNivel(Integer nivel, Integer empresa) {
        Query q = em.createQuery("select o from GrupoProducto o where o.nivel = ?1 and o.empresa.codigo = ?2 order by o.nombre");
        q.setParameter(1, nivel);
        q.setParameter(2, empresa);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarPorNivelEstado(Integer nivel,Integer estado) {
        Query q = em.createQuery("select o from GrupoProducto o where o.nivel = ?1 and o.estado = ?2 order by o.nombre");
        q.setParameter(1, nivel);
        q.setParameter(2, estado);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarPorNivelBodega(Integer nivel, Integer empresa) {
        Query q = em.createQuery("select o from GrupoProducto o where o.nivel = ?1 and o.tipo = ?2 and o.empresa.codigo = ?3 order by o.nombre");
        q.setParameter(1, nivel);
        q.setParameter(2, 1);
        q.setParameter(3, empresa);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarPorPadre(Integer padre) {
        Query q = em.createQuery("select o from GrupoProducto o where o.padre.codigo = ?1 order by o.nombre");
        q.setParameter(1, padre);
        return q.getResultList();
    }
    
    public List<GrupoProducto> listarPorPuntoVenta(Integer punto) {
        Query q = em.createQuery("select o.grupo from PuntoRestriccion o where o.puntoVenta.codigo = ?1 ");
        q.setParameter(1, punto);
        return q.getResultList();
    }
}
