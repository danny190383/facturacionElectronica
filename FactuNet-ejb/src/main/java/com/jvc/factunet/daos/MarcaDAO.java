package com.jvc.factunet.daos;


import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Marca;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

@Stateless
public class MarcaDAO extends GenericDAO {
    
     public List<Marca> listar() {
         Query q = em.createQuery("select o from Marca o ORDER BY o.orden");
         return q.getResultList();
     }    
         
     public List<Marca> listarMarca(String nombre){
         Query q = em.createQuery("select o from Marca o "
         + "where upper(o.nombre) like ?1 "
         + "order by o.orden");     
         q.setParameter(1, StringUtils.isEmpty(nombre) ? "%%" : "%" + nombre.toUpperCase() + "%");
        return q.getResultList();
     }
             
     public List<Marca> listarPorEmpresa(Integer empresa) {
         Query q = em.createQuery("select o from Marca o where o.empresa.codigo = ?1 ORDER BY o.nombre");
         q.setParameter(1, empresa);
         return q.getResultList();
     }
}
