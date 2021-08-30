package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Canton;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CantonDAO extends GenericDAO{

	public List<Canton> listarCantonesProvincia(Integer pvc_codigo) {
            Query q = em.createQuery("select a from Canton a where a.cantonPK.idProvincia = :codigoProvincia and  a.estado= 1  order by a.descripcion");
            q.setParameter("codigoProvincia", pvc_codigo);
            return (List<Canton>) q.getResultList();	
	}
}
