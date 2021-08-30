package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.EmpleadoDAO;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empleado;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class EmpleadoServicio {
    private static final Logger LOG = Logger.getLogger(EmpleadoServicio.class.getName());
    
    @EJB
    private EmpleadoDAO empleadoDAO;
    @EJB
    private CuentaServicio cuentaServicio;
    
    public List<Empleado> listar(Integer empresa, int maxResults, int firstResult) {
        return empleadoDAO.listar(empresa, maxResults, firstResult);
    }
    
    public List<Empleado> listarTodos(int maxResults, int firstResult) {
        return empleadoDAO.listarTodos( maxResults, firstResult);
    }
    
    public Empleado buscar(Integer codigo) {
        return empleadoDAO.buscar(codigo);
    }
    
    public Empleado buscarCedula(String cedula, Integer empresa) {
        return empleadoDAO.buscarCedula(cedula, empresa);
    }
    
    public List<Empleado> listarBuscar(String nombres, String apellidos, String cedula, Integer empresa, int maxResults, int firstResult) {
        return empleadoDAO.listarBuscar(nombres, apellidos,cedula, empresa, maxResults, firstResult);
    }
    
    public List<Empleado> listarBuscarTodos(String nombres, String apellidos, String cedula, int maxResults, int firstResult) {
        return empleadoDAO.listarBuscarTodos(nombres, apellidos,cedula, maxResults, firstResult);
    }
    
    public Long contar(String nombres, String apellidos, String cedula, Integer empresa){
        return empleadoDAO.contar(nombres, apellidos, cedula, empresa);
    }
    
    public Long contarTodos(String nombres, String apellidos, String cedula){
        return empleadoDAO.contarTodos(nombres, apellidos, cedula);
    }
    
    public Long contar(Integer empresa){
        return empleadoDAO.contar(empresa);
    }
    
    public Long contarTodos(){
        return empleadoDAO.contarTodos();
    }
    
    public void eliminar(Empleado parametro) throws Exception {
        this.empleadoDAO.eliminar(parametro);
    }
    
    public void insertar(Empleado parametro) throws Exception {
        this.empleadoDAO.insertar(parametro);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Empleado actualizar(Empleado parametro) throws Exception {
        return (Empleado) this.empleadoDAO.actualizar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void crearInsertar(Empleado parametro) throws Exception
    {
        this.insertar(parametro);
        this.crearCuenta(parametro);
    }
    
    public void crearCuenta(Empleado empleado) throws Exception
    {
       Cuenta cuenta = new Cuenta();
       cuenta.setEmpleado(empleado);
       cuenta.setIdentificador(empleado.getPersona().getCedula());
       cuenta.setClave(this.cuentaServicio.encriptaPassword(empleado.getPersona().getCedula()));
       cuentaServicio.insertar(cuenta); 
    }
    
    public List<Empleado> listarEmpleadoEmpresas(String identificador) {
        return empleadoDAO.listarEmpleadoEmpresas(identificador);
    }
}
