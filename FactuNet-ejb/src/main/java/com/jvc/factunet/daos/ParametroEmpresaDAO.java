package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CatalogoParametrosEmpresa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ParametroEmpresaDAO extends GenericDAO{
    
    public List<CatalogoParametrosEmpresa> listar() {
        Query q = em.createQuery("select o from CatalogoParametrosEmpresa o ORDER BY o.nombre");
        return q.getResultList();
    }
    
    public CatalogoParametrosEmpresa buscar(Integer codigo) {
        Query q = em.createQuery("select o from CatalogoParametrosEmpresa o where o.id = ?1");
        q.setParameter(1, codigo);
        return (CatalogoParametrosEmpresa)q.getSingleResult();
    }
}
