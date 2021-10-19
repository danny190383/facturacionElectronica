package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CantonPK;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.ParroquiaPK;
import com.jvc.factunet.entidades.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class EmpresaDAO extends GenericDAO{
    
    public List<Empresa> listar(String tipo) {
        Query q = em.createQuery("select o from Empresa o where "
                + "o.tipoEmpresaWeb.nombre like ?1 ");
        q.setParameter(1, tipo == null ? "%%" : tipo);
        return q.getResultList();
    }
    
    public Empresa buscar(Integer codigo) {
        Query q = em.createQuery("select o from Empresa o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Empresa)q.getSingleResult();
    }
    
    public List<Empresa> listarPorProvincia(Integer provincia, String tipo) {
        Query q = em.createQuery("select o from Empresa o where "
                + "o.parroquia.parroquiaPK.idProvincia = ?1 and "
                + "o.tipoEmpresaWeb.nombre like ?2 ");
        q.setParameter(1, provincia);
        q.setParameter(2, tipo == null ? "%%" : tipo);
        return q.getResultList();
    }
    
    public List<Empresa> listarPorCanton(CantonPK canton, String tipo) {
        Query q = em.createQuery("select o from Empresa o where "
                + "o.parroquia.parroquiaPK.idProvincia = ?1 and "
                + "o.parroquia.parroquiaPK.idCanton = ?2 and "
                + "o.tipoEmpresaWeb.nombre like ?3 ");
        q.setParameter(1, canton.getIdProvincia());
        q.setParameter(2, canton.getId());
        q.setParameter(3, tipo == null ? "%%" : tipo);
        return q.getResultList();
    }
    
    public List<Empresa> listarPorParroquia(ParroquiaPK parroquia, String tipo) {
        Query q = em.createQuery("select o from Empresa o where "
                + "o.parroquia.parroquiaPK.idProvincia = ?1 and "
                + "o.parroquia.parroquiaPK.idCanton = ?2 and "
                + "o.parroquia.parroquiaPK.id = ?3 and "
                + "o.tipoEmpresaWeb.nombre like ?4 ");
        q.setParameter(1, parroquia.getIdProvincia());
        q.setParameter(2, parroquia.getIdCanton());
        q.setParameter(3, parroquia.getId());
        q.setParameter(4, tipo == null ? "%%" : tipo);
        return q.getResultList();
    }
    
    public List<Empresa> listar() {
        Query q = em.createQuery("select o from Empresa o");
        return q.getResultList();
    }
    
    public List<Producto> listarProductosEmpresa(Integer empresa) {
        Query q = em.createQuery("select o from Producto o where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        return q.getResultList();
    }
}
