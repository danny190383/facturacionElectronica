package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.GrupoProductoDAO;
import com.jvc.factunet.entidades.GrupoProducto;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class GrupoProductoServicio {
    
    private static final Logger LOG = Logger.getLogger(GrupoProductoServicio.class.getName());
    
    @EJB
    private GrupoProductoDAO grupoProductoDAO;
    
    public List<GrupoProducto> listar() {
        return grupoProductoDAO.listar();
    }
    
    public GrupoProducto buscar(Integer codigo) {
        return grupoProductoDAO.buscar(codigo);
    }
    
    public List<GrupoProducto> listarSinHijos(Integer empresa) {
        return grupoProductoDAO.listarSinHijos(empresa);
    }
    
    public List<GrupoProducto> listarSinHijosTipo(Integer tipo, Integer empresa) {
        return grupoProductoDAO.listarSinHijosTipo(tipo, empresa);
    }
    
    public List<GrupoProducto> listarPorNivel(Integer nivel, Integer empresa) {
        return this.grupoProductoDAO.listarPorNivel(nivel, empresa);
    }
    
    public List<GrupoProducto> listarPorNivelEstado(Integer nivel, Integer estado) {
        return this.grupoProductoDAO.listarPorNivelEstado(nivel,estado);
    }
    
    public List<GrupoProducto> listarPorNivelBodega(Integer nivel, Integer empresa) {
        return this.grupoProductoDAO.listarPorNivelBodega(nivel, empresa);
    }
    
    public List<GrupoProducto> listarPorPadre(Integer padre) {
        return this.grupoProductoDAO.listarPorPadre(padre);
    }
    
    public void insertar(GrupoProducto parametro) throws Exception {
        this.grupoProductoDAO.insertar(parametro);
    }

    public GrupoProducto actualizar(GrupoProducto parametro) throws Exception {
        return (GrupoProducto) this.grupoProductoDAO.actualizar(parametro);
    }
    
    public void eliminar(GrupoProducto parametro) throws Exception {
        this.grupoProductoDAO.eliminar(parametro);
    }
    
    public List<GrupoProducto> listarPorPuntoVenta(Integer punto) {
        return grupoProductoDAO.listarPorPuntoVenta(punto);
    }
}
