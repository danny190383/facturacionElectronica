package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.BancoDAO;
import com.jvc.factunet.entidades.Banco;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class BancoServicio {
    private static final Logger LOG = Logger.getLogger(BancoServicio.class.getName());
    
    @EJB
    private BancoDAO bancoDAO;
    
    public List<Banco> listar() {
        return bancoDAO.listar();
    }
    
    public List<Banco> listarNombre(String nombre) {
        return bancoDAO.listarNombre(nombre);
    }
    
    public void eliminar(Banco parametro) throws Exception {
        this.bancoDAO.eliminar(parametro);
    }
    
    public void insertar(Banco parametro) throws Exception {
        this.bancoDAO.insertar(parametro);
    }

    public Banco actualizar(Banco parametro) throws Exception {
        return (Banco) this.bancoDAO.actualizar(parametro);
    }
}
