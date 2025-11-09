package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ServicioCobrosMaestroDAO;
import com.jvc.factunet.entidades.CobrosServicio;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ServicioCobrosMaestroServicio {
    private static final Logger LOG = Logger.getLogger(ClienteServicio.class.getName());
    
    @EJB
    private ServicioCobrosMaestroDAO servicioCobrosMaestroDAO;
    
    public List<CobrosServicio> listar(Integer servicio,int maxResults, int firstResult) {
        return servicioCobrosMaestroDAO.listar(servicio, maxResults, firstResult);
    }
    
    public List<CobrosServicio> listarPendintes(Integer cliente) {
        return servicioCobrosMaestroDAO.listarPendintes(cliente);
    }
    
    public Long contar(Integer servicio){
        return servicioCobrosMaestroDAO.contar(servicio);
    }
    
    public void insertar(CobrosServicio parametro) throws Exception {
        this.servicioCobrosMaestroDAO.insertar(parametro);
    }

    public CobrosServicio actualizar(CobrosServicio parametro) throws Exception {
        return (CobrosServicio) this.servicioCobrosMaestroDAO.actualizar(parametro);
    }
    
    public List<CobrosServicio> listar(int maxResults, int firstResult) {
        return servicioCobrosMaestroDAO.listar(maxResults, firstResult);
    }
    
    public Long contar(){
        return servicioCobrosMaestroDAO.contar();
    }
    
    public List<CobrosServicio> listarBuscar(String nombres, String cedula, String servicio, String servicioCodigo, String mes, String estado, int maxResults, int firstResult) {
        return servicioCobrosMaestroDAO.listarBuscar(nombres, cedula, servicio, servicioCodigo, mes, estado, maxResults, firstResult);
    }
    
    public Long contar(String nombres, String cedula, String servicio, String servicioCodigo, String mes, String estado){
        return servicioCobrosMaestroDAO.contar(nombres, cedula, servicio, servicioCodigo, mes, estado);
    }
    
    public void eliminar(CobrosServicio parametro) throws Exception {
        this.servicioCobrosMaestroDAO.eliminar(parametro);
    }
    
}
