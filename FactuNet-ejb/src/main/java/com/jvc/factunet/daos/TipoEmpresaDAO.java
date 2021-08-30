package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.TipoEmpresa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class TipoEmpresaDAO extends GenericDAO{
    public List<TipoEmpresa> listar() {
        Query q = em.createQuery("select o from TipoEmpresa o ");
        return q.getResultList();
    }
    
    public TipoEmpresa buscar(Integer codigo) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (TipoEmpresa)q.getSingleResult();
    }
    
    public TipoEmpresa buscarNombre(String nombre) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.nombre = ?1").setMaxResults(1); 
        q.setParameter(1, nombre);
        return (TipoEmpresa)q.getSingleResult();
    }
    
    public List<TipoEmpresa> listarSinHijos() {
        Query q = em.createQuery("select o from TipoEmpresa o where o.tipoEmpresaList IS EMPTY order by o.nombre ");
        return q.getResultList();
    }
    
    public List<TipoEmpresa> listarSinHijosTipo(Integer tipo) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.tipoEmpresaList IS EMPTY and o.tipo = ?1 order by o.nombre ");
        q.setParameter(1, tipo);
        return q.getResultList();
    }
    
    public List<TipoEmpresa> listarPorNivel(Integer nivel) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.nivel = ?1 order by o.nombre");
        q.setParameter(1, nivel);
        return q.getResultList();
    }
    
    public List<TipoEmpresa> listarPorNivelEstado(Integer nivel,Integer estado) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.nivel = ?1 and o.estado = ?2 order by o.nombre");
        q.setParameter(1, nivel);
        q.setParameter(2, estado);
        return q.getResultList();
    }
    
    public List<TipoEmpresa> listarPorPadre(Integer padre) {
        Query q = em.createQuery("select o from TipoEmpresa o where o.padre.codigo = ?1 order by o.nombre");
        q.setParameter(1, padre);
        return q.getResultList();
    }
}
