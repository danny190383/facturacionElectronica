package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.FormaPago;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class FormaPagoDAO extends GenericDAO{
    
    public FormaPago buscar(Integer codigo) {
        Query q = em.createQuery("select o from FormaPago o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (FormaPago)q.getSingleResult();
    }
    
    public List<FormaPago> listar() {
        Query q = em.createQuery("select o from FormaPago o ORDER BY o.orden");
        return q.getResultList();
    }
    
    public List<FormaPago> listarTipo(Integer tipo) {
        Query q = em.createQuery("select o from FormaPago o where o.tipo = ?1 and o.estado = 1 ORDER BY o.orden");
        q.setParameter(1, tipo);
        return q.getResultList();
    }
    
    public List<FormaPago> listarNombre(String nombre) {
        Query q = em.createQuery("select o from FormaPago o "
                + "where upper(o.nombre) like ?1 "
                + "order by o.orden");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
}
