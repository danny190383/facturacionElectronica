package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.GarantiaCabecera;
import com.jvc.factunet.utilitarios.Fecha;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class IngresoDAO extends GenericDAO{
    
    public List<GarantiaCabecera> listarIngresosFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fechaIngreso BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.cliente.persona.apellidos) like :proveedor or UPPER(o.cliente.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from GarantiaCabecera o where "+ sql +" order by o.fechaIngreso desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
