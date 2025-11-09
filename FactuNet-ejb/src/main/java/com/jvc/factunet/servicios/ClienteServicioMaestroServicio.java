package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ClienteServicioMaestroDAO;
import com.jvc.factunet.entidades.ServicioPersona;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ClienteServicioMaestroServicio {
    private static final Logger LOG = Logger.getLogger(ClienteServicio.class.getName());
    
    @EJB
    private ClienteServicioMaestroDAO clienteServicioMaestroDAO;
    
    public ServicioPersona buscar(Integer codigo) {
        return clienteServicioMaestroDAO.buscar(codigo);
    }
    
    public List<ServicioPersona> listarBuscar(String nombres, String cedula, String servicio, String servicioCodigo, int maxResults, int firstResult) {
        return clienteServicioMaestroDAO.listarBuscar(nombres, cedula, servicio, servicioCodigo, maxResults, firstResult);
    }
    
    public Long contar(String nombres, String cedula, String servicio, String servicioCodigo){
        return clienteServicioMaestroDAO.contar(nombres, cedula, servicio, servicioCodigo);
    }
    
    public List<ServicioPersona> listar(int maxResults, int firstResult) {
        return clienteServicioMaestroDAO.listar(maxResults, firstResult);
    }
    
    public Long contar(){
        return clienteServicioMaestroDAO.contar();
    }
    
    public List<ServicioPersona> listarAutomaticosActios() {
        return clienteServicioMaestroDAO.listarAutomaticosActios();
    }
}
