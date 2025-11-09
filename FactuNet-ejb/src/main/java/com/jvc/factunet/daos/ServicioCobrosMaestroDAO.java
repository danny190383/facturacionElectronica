package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CobrosServicio;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ServicioCobrosMaestroDAO extends GenericDAO{
    
    public List<CobrosServicio> listar(Integer servicio, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from CobrosServicio o where o.servicioPersona.codigo = ?1");
        q.setParameter(1, servicio);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar(Integer servicio) {
        try {
            Query q = em.createQuery("select count(o) from CobrosServicio o where o.servicioPersona.codigo = ?1");
            q.setParameter(1, servicio);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<CobrosServicio> listarPendintes(Integer cliente) {
        Query q = em.createQuery("select o from CobrosServicio o where o.servicioPersona.persona.codigo = ?1 and o.estado = '1' ");
        q.setParameter(1, cliente);
        return q.getResultList();
    }
    
    public List<CobrosServicio> listar(int maxResults, int firstResult) {
        Query q = em.createQuery("select o from CobrosServicio o ");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar() {
        try {
            Query q = em.createQuery("select count(o) from CobrosServicio o ");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<CobrosServicio> listarBuscar(String nombres, String cedula,String servicio,String servicioCodigo, String mes, String estado, int maxResults, int firstResult) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.servicioPersona.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "GROUP BY o.servicioPersona.persona, o HAVING CONCAT(o.servicioPersona.persona.nombres,' ',o.servicioPersona.persona.apellidos) like :apellido ";
            }
            if((servicio != null) && (!servicio.isEmpty()))
            {
                sql = sql + "and upper(o.servicioPersona.servicio.nombre) like :servicio ";
            }
            if((servicioCodigo != null) && (!servicioCodigo.isEmpty()))
            {
                sql = sql + "and upper(o.servicioPersona.servicio.codigoBarras) like :servicioCodigo ";
            }
            if((mes != null) && (!mes.isEmpty()))
            {
                sql = sql + "and upper(o.mes) like :mes ";
            }
            if((estado != null) && (!estado.isEmpty()))
            {
                sql = sql + "and upper(o.estado) like :estado ";
            }
            Query q = em.createQuery("select o from CobrosServicio o Where 1=1 "
                    + sql
                    + "order by o.servicioPersona.persona.nombres");
            if((nombres != null) && (!nombres.isEmpty())){
                nombres = nombres.trim();
                StringTokenizer st = new StringTokenizer(nombres);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               nombres = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("apellido", nombres);
            }
            if((cedula != null) && (!cedula.isEmpty())){
                q.setParameter("cedula", cedula.trim().toUpperCase() + "%");
            }
            if((servicio != null) && (!servicio.isEmpty())){
                q.setParameter("servicio", servicio.trim().toUpperCase() + "%");
            }
            if((servicioCodigo != null) && (!servicioCodigo.isEmpty())){
                q.setParameter("servicioCodigo", servicioCodigo.trim().toUpperCase() + "%");
            }
            if((mes != null) && (!mes.isEmpty())){
                q.setParameter("mes", mes.trim().toUpperCase() + "%");
            }
            if((estado != null) && (!estado.isEmpty())){
                q.setParameter("estado", estado.trim().toUpperCase() + "%");
            }
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long contar(String nombres, String cedula, String servicio, String servicioCodigo, String mes, String estado) {
        try {
            String sql = " FROM CobrosServicio o WHERE 1=1 ";

            if ((cedula != null) && (!cedula.isEmpty())) {
                sql = sql + "AND upper(o.servicioPersona.persona.cedula) LIKE :cedula ";
            }
            if ((nombres != null) && (!nombres.isEmpty())) {
                sql = sql + "AND upper(CONCAT(o.servicioPersona.persona.nombres, ' ', o.servicioPersona.persona.apellidos)) LIKE :nombres ";
            }
            if ((servicio != null) && (!servicio.isEmpty())) {
                sql = sql + "AND upper(o.servicioPersona.servicio.nombre) LIKE :servicio ";
            }
            if ((servicioCodigo != null) && (!servicioCodigo.isEmpty())) {
                sql = sql + "AND upper(o.servicioPersona.servicio.codigoBarras) LIKE :servicioCodigo ";
            }
            if((mes != null) && (!mes.isEmpty()))
            {
                sql = sql + "and upper(o.mes) like :mes ";
            }
            if((estado != null) && (!estado.isEmpty()))
            {
                sql = sql + "and upper(o.estado) like :estado ";
            }
            Query q = em.createQuery("SELECT COUNT(o)" + sql);

            if ((nombres != null) && (!nombres.isEmpty())) {
                nombres = nombres.trim();
                StringTokenizer st = new StringTokenizer(nombres);
                String texto = "";
                while (st.hasMoreTokens()) {
                    if (!texto.equals("")) {
                        texto += "%";
                    }
                    texto += st.nextToken();
                }
                nombres = "%" + texto.toUpperCase().trim() + "%";
                q.setParameter("nombres", nombres);
            }
            if ((cedula != null) && (!cedula.isEmpty())) {
                q.setParameter("cedula", cedula.trim().toUpperCase() + "%");
            }
            if ((servicio != null) && (!servicio.isEmpty())) {
                q.setParameter("servicio", servicio.trim().toUpperCase() + "%");
            }
            if ((servicioCodigo != null) && (!servicioCodigo.isEmpty())) {
                q.setParameter("servicioCodigo", servicioCodigo.trim().toUpperCase() + "%");
            }
            if((mes != null) && (!mes.isEmpty())){
                q.setParameter("mes", mes.trim().toUpperCase() + "%");
            }
            if((estado != null) && (!estado.isEmpty())){
                q.setParameter("estado", estado.trim().toUpperCase() + "%");
            }
            return ((Long) q.getSingleResult());

        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
