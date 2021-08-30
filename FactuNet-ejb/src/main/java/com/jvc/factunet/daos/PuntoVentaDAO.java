package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.PuntoVenta;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class PuntoVentaDAO extends GenericDAO{
    
    public SecuenciaDocumento secuencia(Integer codigo) {
        Query q = em.createQuery("select o from SecuenciaDocumento o where o.codigo = ?1 ");
        q.setParameter(1, codigo);
        return (SecuenciaDocumento) q.getSingleResult();
    }
    
    public SecuenciaDocumento secuencia(Integer documento, Integer empresa, Integer punto) {
        try {
            Query q = em.createQuery("select o from SecuenciaDocumento o where o.tipoDocumento.codigo = ?1 and o.puntoVenta.codigo =  ?3 and o.puntoVenta.empresa.codigo = ?2 ");
            q.setParameter(1, documento);
            q.setParameter(2, empresa);
            q.setParameter(3, punto);
            return (SecuenciaDocumento) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PuntoVenta> listarPuntoVenta(Integer empresa){
        Query q = em.createQuery("select o from PuntoVenta o where o.empresa.codigo = ?1 ");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
}
