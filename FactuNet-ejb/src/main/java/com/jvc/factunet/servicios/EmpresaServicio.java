package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.EmpresaDAO;
import com.jvc.factunet.entidades.CantonPK;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.ParroquiaPK;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class EmpresaServicio {
    private static final Logger LOG = Logger.getLogger(EmpresaServicio.class.getName());
    
    @EJB
    private EmpresaDAO empresaDAO;
    
    public List<Empresa> listar() {
        return this.empresaDAO.listar();
    }
    
    public List<Empresa> listar(String tipo) {
        return this.empresaDAO.listar(tipo);
    }
    
    public List<Empresa> listarPorProvincia(Integer provincia, String tipo) {
        return this.empresaDAO.listarPorProvincia(provincia, tipo);
    }
    
    public List<Empresa> listarPorCanton(CantonPK canton, String tipo) {
        return this.empresaDAO.listarPorCanton(canton, tipo);
    }
    
    public List<Empresa> listarPorParroquia(ParroquiaPK parroquia, String tipo) {
        return this.empresaDAO.listarPorParroquia(parroquia, tipo);
    }
    
    public Empresa buscar(Integer codigo) {
        return this.empresaDAO.buscar(codigo);
    }
    
     public void eliminar(Empresa parametro) throws Exception {
        this.empresaDAO.eliminar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(Empresa parametro) throws Exception {
        this.empresaDAO.insertar(parametro);
    }

    public Empresa actualizar(Empresa parametro) throws Exception {
        return (Empresa) this.empresaDAO.actualizar(parametro);
    }
}
