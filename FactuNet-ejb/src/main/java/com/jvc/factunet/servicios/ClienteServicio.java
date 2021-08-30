package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ClienteDAO;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Mascota;
import com.jvc.factunet.entidades.Persona;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class ClienteServicio {
    
    private static final Logger LOG = Logger.getLogger(ClienteServicio.class.getName());
    
    @EJB
    private ClienteDAO clienteDAO;
    
    public List<Cliente> listar(Integer empresa, int maxResults, int firstResult) {
        return clienteDAO.listar(empresa, maxResults, firstResult);
    }
    
    public List<Persona> listar(int maxResults, int firstResult) {
        return clienteDAO.listar(maxResults, firstResult);
    }
    
    public List<Mascota> listarMascota(Integer empresa, int maxResults, int firstResult) {
        return clienteDAO.listarMascota(empresa, maxResults, firstResult);
    }
    
    public Cliente buscar(Integer codigo) {
        return clienteDAO.buscar(codigo);
    }
    
    public Cliente buscarCliente(Integer persona, Integer empresa) {
        return clienteDAO.buscarCliente(persona, empresa);
    }
    
    public Cliente buscarCedula(String cedula, Integer empresa) {
        return clienteDAO.buscarCedula(cedula, empresa);
    }
    
    public Cliente buscarConsumidorFinal(Integer empresa) {
        return clienteDAO.buscarConsumidorFinal(empresa);
    }
    
    public List<Cliente> listarBuscar(String nombres, String cedula, Integer empresa, int maxResults, int firstResult) {
        return clienteDAO.listarBuscar(nombres, cedula, empresa, maxResults, firstResult);
    }
    
    public List<Persona> listarBuscar(String nombres, String cedula, int maxResults, int firstResult) {
        return clienteDAO.listarBuscar(nombres, cedula, maxResults, firstResult);
    }
    
    public List<Mascota> listarBuscarMascota(String numero, String nombres, String cedula, String mascota, Integer empresa, int maxResults, int firstResult) {
        return clienteDAO.listarBuscarMascota(numero, nombres, cedula, mascota, empresa, maxResults, firstResult);
    }
    
    public Long contar(String nombres, String cedula, Integer empresa){
        return clienteDAO.contar(nombres, cedula, empresa);
    }
    
    public Long contar(String nombres, String cedula){
        return clienteDAO.contar(nombres, cedula);
    }
    
    public Long contarMascota(String numero, String nombres, String cedula, String mascota, Integer empresa){
        return clienteDAO.contarMascota(numero, nombres, cedula, mascota, empresa);
    }
    
    public Long contar(Integer empresa){
        return clienteDAO.contar(empresa);
    }
    
    public Long contar(){
        return clienteDAO.contar();
    }
    
    public Long contarMascota(Integer empresa){
        return clienteDAO.contarMascota(empresa);
    }
    
    public void eliminar(Cliente parametro) throws Exception {
        this.clienteDAO.eliminar(parametro);
    }
    
    public void insertar(Mascota parametro) throws Exception {
        this.clienteDAO.insertar(parametro);
    }
    
    public void insertar(Cliente parametro) throws Exception {
        this.clienteDAO.insertar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarListaClientes(List<Cliente> lista)throws Exception{
        for(Cliente cliente : lista){
            this.insertar(cliente);
        }
    }

    public Cliente actualizar(Cliente parametro) throws Exception {
        return (Cliente) this.clienteDAO.actualizar(parametro);
    }
    
    public String maxNumeroCliente(Integer empresa) throws Exception {
        return this.clienteDAO.maxNumeroCliente(empresa); 
    }
}
