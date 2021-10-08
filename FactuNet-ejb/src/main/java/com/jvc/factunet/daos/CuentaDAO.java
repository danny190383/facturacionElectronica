package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.security.TripleDES;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CuentaDAO extends GenericDAO{
    
    public List<Cuenta> listarPerfil(Integer perfil, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Cuenta o where o.rol.codigo = ?1 and "
                    + "o.empleado.empresa.codigo = ?2 ");
            q.setParameter(1, perfil);
            q.setParameter(2, empresa);
            return q.getResultList(); 
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Empleado> listarPuntoVenta(Integer punto, Integer empresa) {
        try {
            Query q = em.createQuery("select o from Empleado o where o.puntoVenta.codigo = ?1 and "
                    + "o.empresa.codigo = ?2 ");
            q.setParameter(1, punto);
            q.setParameter(2, empresa);
            return q.getResultList(); 
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Empleado> listarSinPuntoVenta(Integer empresa) {
        try {
            Query q = em.createQuery("select o from Empleado o where o.puntoVenta = null and "
                    + "o.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return q.getResultList(); 
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Cuenta> listarConPuntoVenta(Integer empresa) {
        try {
            Query q = em.createQuery("select o from Cuenta o where o.empleado.puntoVenta != null and "
                    + "o.empleado.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return q.getResultList(); 
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Cuenta> listar() {
        try {
            Query q = em.createQuery("select o from Cuenta o ");
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<Cuenta> listarSinPerfil(Integer empresa) {
        try {
            Query q = em.createQuery("select o from Cuenta o where o.rol = null and "
                     + "o.empleado.empresa.codigo = ?1 ");
            q.setParameter(1, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public Cuenta validar(String usuario, String clave) {
        try {
            Query q = em.createQuery("select o from Cuenta o where o.identificador = ?1 and o.clave = ?2");
            q.setParameter(1, usuario);
            q.setParameter(2, encriptaPassword(clave));
            return (Cuenta)q.getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
    
    public Cuenta buscar(String identificador) {
        try {
            Query q = em.createQuery("select o from Cuenta o where o.identificador = ?1");
            q.setParameter(1, identificador);
            return (Cuenta)q.getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
    
    public String encriptaPassword(String parametro) throws Exception {
        TripleDES tdes = new TripleDES();
        return tdes.encrypt(parametro);
    }
}
