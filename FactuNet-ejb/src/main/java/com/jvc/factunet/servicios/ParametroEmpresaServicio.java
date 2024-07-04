package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.ParametroEmpresaDAO;
import com.jvc.factunet.entidades.CatalogoParametrosEmpresa;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class ParametroEmpresaServicio {
    private static final Logger LOG = Logger.getLogger(ParametroEmpresaServicio.class.getName());
    
    @EJB
    private ParametroEmpresaDAO parametroEmpresaDAO;
    
    public List<CatalogoParametrosEmpresa> listar() {
        return parametroEmpresaDAO.listar();
    }
    
    public CatalogoParametrosEmpresa buscar(Integer codigo) {
        return parametroEmpresaDAO.buscar(codigo);
    }
    
    public void eliminar(CatalogoParametrosEmpresa parametro) throws Exception {
        this.parametroEmpresaDAO.eliminar(parametro);
    }
    
    public void insertar(CatalogoParametrosEmpresa parametro) throws Exception {
        this.parametroEmpresaDAO.insertar(parametro);
    }

    public CatalogoParametrosEmpresa actualizar(CatalogoParametrosEmpresa parametro) throws Exception {
        return (CatalogoParametrosEmpresa) this.parametroEmpresaDAO.actualizar(parametro);
    }
}
