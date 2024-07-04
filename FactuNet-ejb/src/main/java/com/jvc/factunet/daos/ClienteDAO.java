package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.Persona;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ClienteDAO extends GenericDAO{
    
    public List<Cliente> listar(Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Cliente o Where o.empresa.codigo = ?1 order by o.persona.nombres,o.persona.apellidos");
        q.setParameter(1, empresa);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Cliente buscarCliente(Integer persona, Integer empresa) {
        Query q = em.createQuery("select o from Cliente o where o.persona.codigo = ?1 and o.empresa.codigo = ?2");
        q.setParameter(1, persona);
        q.setParameter(2, empresa);
        return (Cliente)q.getSingleResult();
    }
    
    public Cliente buscar(Integer codigo) {
        Query q = em.createQuery("select o from Cliente o where o.codigo = ?1");
        q.setParameter(1, codigo);
        return (Cliente)q.getSingleResult();
    }
    
    public Cliente buscarCedula(String cedula, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Cliente o where o.persona.cedula like ?1 and o.empresa.codigo = ?2");
            q.setParameter(1, cedula + "%%");
            q.setParameter(2, empresa);
            return (Cliente)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Cliente buscarConsumidorFinal(Integer empresa) {
        try {
            Query q = em.createQuery("select o from Cliente o where UPPER(o.persona.nombres) = ?1 AND o.empresa.codigo = ?2 ").setMaxResults(1);
            q.setParameter(1, "CONSUMIDOR FINAL");
            q.setParameter(2, empresa);
            return (Cliente)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Cliente> listarBuscar(String nombres, String cedula, Integer empresa, int maxResults, int firstResult) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "GROUP BY o, o.persona HAVING CONCAT(o.persona.nombres,' ',o.persona.apellidos) like :apellido ";
            }
            Query q = em.createQuery("select o from Cliente o Where o.empresa.codigo = :empresa "
                    + sql
                    + "order by o.persona.nombres, o.persona.apellidos");
            q.setParameter("empresa", empresa);
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
                q.setParameter("cedula", cedula.toUpperCase() + "%");
            }
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long contar(String nombres, String cedula, Integer empresa) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "and CONCAT(o.persona.nombres,' ',o.persona.apellidos) like :apellido ";
            }
            Query q = em.createQuery("select count(o) from Cliente o Where o.empresa.codigo = :empresa "
                    + sql );
            q.setParameter("empresa", empresa);
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
                q.setParameter("cedula", cedula.toUpperCase() + "%");
            }
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
           return Long.MIN_VALUE;
        }
    }
    
    public Long contar(Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Cliente o Where o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<Mascota> listarBuscarMascota(String numero, String nombres, String cedula,String mascota, Integer empresa, int maxResults, int firstResult) {
        try {
            String sql = " ";
            if((numero != null) && (!numero.isEmpty()))
            {
                sql = sql + "and upper(o.persona.numero) like :numero ";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "GROUP BY o.persona, o HAVING CONCAT(o.persona.nombres,' ',o.persona.apellidos) like :apellido ";
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                sql = sql + "and upper(o.nombre) like :mascota ";
            }
            Query q = em.createQuery("select o from Mascota o Where o.codigo <> null "
                    + sql
                    + "order by o.persona.nombres");
//            q.setParameter("empresa", empresa);
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
            if((numero != null) && (!numero.isEmpty())){
                q.setParameter("numero", numero.toUpperCase() + "%");
            }
            if((cedula != null) && (!cedula.isEmpty())){
                q.setParameter("cedula", cedula.toUpperCase() + "%");
            }
            if((mascota != null) && (!mascota.isEmpty())){
                q.setParameter("mascota", mascota.toUpperCase() + "%");
            }
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long contarMascota(String numero, String nombres, String cedula, String mascota, Integer empresa) {
        try {
            String sql = " ";
            if((numero != null) && (!numero.isEmpty()))
            {
                sql = sql + " and upper(persona.numero) like '"
                          + numero.toUpperCase()
                          + "%'";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and upper(persona.cedula) like '"
                          + cedula.toUpperCase()
                          + "%'";
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                sql = sql + " and upper(mascota.nombre) like '"
                          + mascota.toUpperCase()
                          + "%'";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
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
               sql = sql + " GROUP BY mascota.codigo,persona.numero,persona.cedula,persona.nombres,persona.apellidos HAVING CONCAT(persona.nombres,' ',persona.apellidos) like '"
                          + nombres
                          + "'";
            }
            Query q = em.createNativeQuery("select sum(uno) from "
                                          + "(select count(mascota.codigo) as uno from mascota inner join persona on mascota.persona = persona.codigo  Where (not persona.codigo isnull)  " 
                                          + sql 
                                          + ") as suma");
            
            return ((BigDecimal)q.getSingleResult()).longValue(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<Mascota> listarMascota(Integer empresa, int maxResults, int firstResult) {
        Query q = em.createQuery("select o from Mascota o ");
//        q.setParameter(1, empresa);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contarMascota(Integer empresa) {
        try {
            Query q = em.createQuery("select count(o) from Mascota o ");
//            q.setParameter(1, empresa);
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public String maxNumeroCliente(Integer empresa) {
        Query q = em.createQuery("select MAX(o.persona.numero) from Cliente o where o.empresa.codigo = ?1");
        q.setParameter(1, empresa);
        return (String)q.getSingleResult();
    }
    
    public List<Persona> listarBuscar(String nombres, String cedula, int maxResults, int firstResult) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + "and upper(o.persona.cedula) like :cedula ";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
                sql = sql + "GROUP BY o.persona HAVING CONCAT(o.persona.nombres,' ',o.persona.apellidos) like :apellido ";
            }
            Query q = em.createQuery("select o.persona from Cliente o Where "
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
                q.setParameter("cedula", cedula.toUpperCase() + "%");
            }
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long contar(String nombres, String cedula) {
        try {
            String sql = " ";
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and upper(cedula) like '"
                          + cedula.toUpperCase()
                          + "%'";
            }
            if((nombres != null) && (!nombres.isEmpty()))
            {
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
               sql = sql + " GROUP BY codigo,cedula HAVING CONCAT(nombres,' ',apellidos) like '"
                          + nombres
                          + "'";
            }
            Query q = em.createNativeQuery("select sum(uno) from "
                                          + "(select count(codigo) as uno from persona Where " 
                                          + sql 
                                          + ") as suma");
            
            return ((BigDecimal)q.getSingleResult()).longValue(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
    
    public List<Persona> listar(int maxResults, int firstResult) {
        Query q = em.createQuery("select o.persona from Cliente o ");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
    
    public Long contar() {
        try {
            Query q = em.createQuery("select count(o) from Cliente o ");
            return (Long)q.getSingleResult(); 
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }
}
