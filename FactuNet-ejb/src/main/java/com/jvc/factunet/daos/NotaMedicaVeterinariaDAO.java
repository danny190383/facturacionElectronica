package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.MascotaNotaMedica;
import com.jvc.factunet.utilitarios.Fecha;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class NotaMedicaVeterinariaDAO extends GenericDAO{
    
    public List<MascotaNotaMedica> listarTurnosManiana() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set( Calendar.AM_PM, Calendar.AM );
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        Date date1 = cal.getTime();
        cal.set( Calendar.AM_PM, Calendar.PM );
        cal.set(Calendar.HOUR, 11);
        cal.set(Calendar.MINUTE, 59);
        Date date2 = cal.getTime();
        Query query = super.em.createQuery("select o from MascotaNotaMedica as o where o.fechaProxima between :fechaInicio and :fechaFin");
        query.setParameter("fechaInicio", date1);
        query.setParameter("fechaFin", date2);
        return query.getResultList();
    }
    
    public List<MascotaNotaMedica> listarNotasMedicas(Integer empresa) {
        try {
            Query q = em.createQuery("select o from MascotaNotaMedica o where o.pedido.empresa.codigo = ?1 order by o.fecha desc").setMaxResults(20);
            q.setParameter(1, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<MascotaNotaMedica> listarNotasMedicasMascota(Integer mascota) {
        try {
            Query q = em.createQuery("select o from MascotaNotaMedica o where o.mascota.codigo = ?1 order by o.fecha desc").setMaxResults(20);
            q.setParameter(1, mascota);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<MascotaNotaMedica> listarNotasMedicasFiltro(Integer empresa, Date fecha, String proveedor, String cedula, String mascota, Date fechaC) {
        try {
            String sql = "o.pedido.empresa.codigo = :empresa ";
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if(fechaC != null)
            {
                sql = sql + " and (o.fechaProxima BETWEEN :fechaInicioC and :fechaFinC)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.mascota.persona.cedula = :cedula";
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                sql = sql + " and o.mascota.nombre like :mascota ";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + "GROUP BY o,o.mascota,o.mascota.persona HAVING CONCAT(o.mascota.persona.nombres,' ',o.mascota.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from MascotaNotaMedica o where "+ sql +" order by o.fecha desc").setMaxResults(20);
            q.setParameter("empresa", empresa);
            if((mascota != null) && (!mascota.isEmpty()))
            {
                q.setParameter("mascota","%" + mascota.toUpperCase() + "%%");
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if(fechaC != null)
            {
                q.setParameter("fechaInicioC", Fecha.fechaInicio(fechaC));
                q.setParameter("fechaFinC", Fecha.fechaFin(fechaC));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
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
