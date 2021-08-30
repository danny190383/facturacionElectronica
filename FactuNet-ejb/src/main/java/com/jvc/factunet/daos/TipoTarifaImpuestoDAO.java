package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ImpuestoTarifa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class TipoTarifaImpuestoDAO extends GenericDAO{
    
    public List<ImpuestoTarifa> listar() {
        Query q = em.createQuery("select o from ImpuestoTarifa o ORDER BY o.descripcion");
        return q.getResultList();
    }
    
    public List<ImpuestoTarifa> listarNombre(String nombre) {
        Query q = em.createQuery("select o from ImpuestoTarifa o "
                + "where upper(o.descripcion) like ?1 "
                + "order by o.descripcion");
        
        q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
    }
    
    public List<ImpuestoTarifa> listarImpuesto(Integer impuesto) {
        Query q = em.createQuery("select o from ImpuestoTarifa o where "
                + "o.impuesto.id = ?1 and "
                + "o.estado = true "
                + "order by o.descripcion");
        
        q.setParameter(1, impuesto);
        return q.getResultList();
    }
}
