package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.TarjetaEmpresa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class TarjetaEmpresaDAO extends GenericDAO{
    
    public List<TarjetaEmpresa> listar(Integer empresa) {
        Query q = em.createQuery("select o from TarjetaEmpresa o where o.empresa.codigo = ?1 ");
        q.setParameter(1, empresa);
        return q.getResultList();
    } 
}
