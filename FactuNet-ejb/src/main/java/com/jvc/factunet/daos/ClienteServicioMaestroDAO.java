package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.ServicioPersona;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ClienteServicioMaestroDAO extends GenericDAO{
    
    public List<ServicioPersona> listarBuscar(String nombres, String cedula,String servicio,String servicioCodigo, int maxResults, int firstResult) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "GROUP BY o.persona, o HAVING CONCAT(o.persona.nombres,' ',o.persona.apellidos) like :apellido ";
            }
            if((servicio != null) && (!servicio.isEmpty()))
            {
                sql = sql + "and upper(o.servicio.nombre) like :servicio ";
            }
            if((servicioCodigo != null) && (!servicioCodigo.isEmpty()))
            {
                sql = sql + "and upper(o.servicio.codigoBarras) like :servicioCodigo ";
            }
            Query q = em.createQuery("select o from ServicioPersona o Where 1=1 "
                    + sql
                    + "order by o.persona.nombres");
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
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long contar(String nombres, String cedula, String servicio, String servicioCodigo) {
        try {
            String sql = " FROM ServicioPersona o WHERE 1=1 ";

            if ((cedula != null) && (!cedula.isEmpty())) {
                sql = sql + "AND upper(o.persona.cedula) LIKE :cedula ";
            }
            if ((nombres != null) && (!nombres.isEmpty())) {
                sql = sql + "AND upper(CONCAT(o.persona.nombres, ' ', o.persona.apellidos)) LIKE :nombres ";
            }
            if ((servicio != null) && (!servicio.isEmpty())) {
                sql = sql + "AND upper(o.servicio.nombre) LIKE :servicio ";
            }
            if ((servicioCodigo != null) && (!servicioCodigo.isEmpty())) {
                sql = sql + "AND upper(o.servicio.codigoBarras) LIKE :servicioCodigo ";
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
            return ((Long) q.getSingleResult());

        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<ServicioPersona> listar(int maxResults, int firstResult) {
        Query q = em.createQuery("select o from ServicioPersona o ");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar() {
        try {
            Query q = em.createQuery("select count(o) from ServicioPersona o ");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public ServicioPersona buscar(Integer codigo) {
        Query q = em.createQuery("select o from ServicioPersona o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (ServicioPersona) q.getSingleResult();
    }
    
    public List<ServicioPersona> listarAutomaticosActios() {
        Query q = em.createQuery("select o from ServicioPersona o where o.estado = true and o.cobroAutomatico = true ");
        return q.getResultList();
    }
}
