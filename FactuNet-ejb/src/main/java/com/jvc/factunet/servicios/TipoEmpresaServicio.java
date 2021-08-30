package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.TipoEmpresaDAO;
import com.jvc.factunet.entidades.TipoEmpresa;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TipoEmpresaServicio {
    private static final Logger LOG = Logger.getLogger(TipoEmpresaServicio.class.getName());
    
    @EJB
    private TipoEmpresaDAO tipoEmpresaDAO;
    
    public List<TipoEmpresa> listar() {
        return tipoEmpresaDAO.listar();
    }
    
    public TipoEmpresa buscar(Integer codigo) {
        return tipoEmpresaDAO.buscar(codigo);
    }
    
    public TipoEmpresa buscarNombre(String nombre) {
        return tipoEmpresaDAO.buscarNombre(nombre);
    }
    
    public List<TipoEmpresa> listarSinHijos() {
        return tipoEmpresaDAO.listarSinHijos();
    }
    
    public List<TipoEmpresa> listarSinHijosTipo(Integer tipo) {
        return tipoEmpresaDAO.listarSinHijosTipo(tipo);
    }
    
    public List<TipoEmpresa> listarPorNivel(Integer nivel) {
        return this.tipoEmpresaDAO.listarPorNivel(nivel);
    }
    
    public List<TipoEmpresa> listarPorNivelEstado(Integer nivel, Integer estado) {
        return this.tipoEmpresaDAO.listarPorNivelEstado(nivel,estado);
    }
    
    public List<TipoEmpresa> listarPorPadre(Integer padre) {
        return this.tipoEmpresaDAO.listarPorPadre(padre);
    }
    
    public void insertar(TipoEmpresa parametro) throws Exception {
        this.tipoEmpresaDAO.insertar(parametro);
    }

    public TipoEmpresa actualizar(TipoEmpresa parametro) throws Exception {
        return (TipoEmpresa) this.tipoEmpresaDAO.actualizar(parametro);
    }
    
    public void eliminar(TipoEmpresa parametro) throws Exception {
        this.tipoEmpresaDAO.eliminar(parametro);
    }
}
