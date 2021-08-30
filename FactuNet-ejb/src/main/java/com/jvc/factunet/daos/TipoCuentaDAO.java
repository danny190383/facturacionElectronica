package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.TipoCuenta;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class TipoCuentaDAO extends GenericDAO{
    
    public List<TipoCuenta> listar() {
        Query q = em.createQuery("select o from TipoCuenta o ORDER BY o.nombre");
        return q.getResultList();
    }
    
    public List<TipoCuenta> listarNombre(String nombre) {
        Query q = em.createQuery("select o from TipoCuenta o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.nombre");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
