package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.PersonaDAO;
import com.jvc.factunet.entidades.Persona;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class PersonaServicio {
    private static final Logger LOG = Logger.getLogger(PersonaServicio.class.getName());
    
    @EJB
    private PersonaDAO personaDAO;
    
     public List<Persona> listar(int maxResults, int firstResult) {
        return personaDAO.listar(maxResults, firstResult);
    }
    
    public Persona buscar(Integer codigo) {
        return personaDAO.buscar(codigo);
    }
    
    public Persona buscarCedula(String cedula) {
        return personaDAO.buscarCedula(cedula);
    }
    
    public List<Persona> listarBuscar(String nombres, String apellidos, String cedula, int maxResults, int firstResult) {
        return personaDAO.listarBuscar(nombres, apellidos,cedula, maxResults, firstResult);
    }
    
    public Long contar(String nombres, String apellidos, String cedula){
        return personaDAO.contar(nombres, apellidos, cedula);
    }
    
    public Long contar(){
        return personaDAO.contar();
    }
}
