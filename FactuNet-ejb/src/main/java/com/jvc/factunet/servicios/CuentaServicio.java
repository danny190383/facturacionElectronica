package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CuentaDAO;
import com.jvc.factunet.entidades.Cliente;
import com.jvc.factunet.entidades.Cuenta;
import com.jvc.factunet.entidades.Empleado;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.security.TripleDES;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class CuentaServicio {
    private static final Logger LOG = Logger.getLogger(CuentaServicio.class.getName());
    
    @EJB
    private CuentaDAO cuentaDAO;
    
    public Cuenta validar(String usuario, String clave) {
        return cuentaDAO.validar(usuario, clave);
    }
    
    public Cuenta buscar(String identificador) {
        return cuentaDAO.buscar(identificador); 
    }
    
    public List<Empleado> listarPuntoVenta(Integer punto, Integer empresa) {
        return cuentaDAO.listarPuntoVenta(punto, empresa);
    }
    
    public List<Empleado> listarSinPuntoVenta(Integer empresa) {
        return cuentaDAO.listarSinPuntoVenta(empresa);
    }
    
    public List<Cuenta> listarConPuntoVenta(Integer empresa) {
        return cuentaDAO.listarConPuntoVenta(empresa);
    }
    
    public List<Cuenta> listar(Integer rol, Integer empresa) {
        return cuentaDAO.listarPerfil(rol, empresa);
    }
    
    public List<Cuenta> listarSinRol(Integer empresa) {
        return cuentaDAO.listarSinPerfil(empresa);
    }
    
    public List<Cuenta> listar() {
        return cuentaDAO.listar();
    }
    
    public void eliminar(Cuenta parametro) throws Exception {
        this.cuentaDAO.eliminar(parametro);
    }
    
    public void insertar(Cuenta parametro) throws Exception {
        this.cuentaDAO.insertar(parametro);
    }

    public Cuenta actualizar(Cuenta parametro) throws Exception {
        return (Cuenta) this.cuentaDAO.actualizar(parametro);
    }
    
    public String encriptaPassword(String parametro) throws Exception {
        TripleDES tdes = new TripleDES();
        return tdes.encrypt(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(List<Cuenta> lista) throws Exception
    {
        for(Cuenta cuenta:lista)
        {
            this.actualizar(cuenta);
        }
    }
    
    public void crearCuenta(Empresa empleado) throws Exception
    {
       Cuenta cuenta = new Cuenta();
       cuenta.setIdentificador(empleado.getRuc());
       cuenta.setClave(this.encriptaPassword(empleado.getRuc()));
       this.insertar(cuenta); 
    }
    
    public void crearCuenta(Cliente empleado) throws Exception
    {
       Cuenta cuenta = new Cuenta();
       cuenta.setCliente(empleado);
       cuenta.setIdentificador(empleado.getPersona().getCedula());
       cuenta.setClave(this.encriptaPassword(empleado.getPersona().getCedula()));
       this.insertar(cuenta); 
    }
}
