package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Parroquia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ParroquiaDAO extends GenericDAO {

    public List<Parroquia> listarParroquiasCanton(Integer provinciaCodigo, Integer cantonCodigo) {
       
        Query q = em.createQuery("select a from Parroquia a where a.parroquiaPK.idCanton = :cantonCodigo and a.parroquiaPK.idProvincia = :provinciaCodigo and a.estado= 1  order by a.descripcion");
        q.setParameter("cantonCodigo", cantonCodigo);
        q.setParameter("provinciaCodigo", provinciaCodigo);
        return (List<Parroquia>) q.getResultList();
    }
}
