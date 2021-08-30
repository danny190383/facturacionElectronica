package com.jvc.factunet.servicios;
import com.jvc.factunet.daos.MarcaDAO;
import com.jvc.factunet.entidades.Marca;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class MarcaServicio {
    
    private static final Logger LOG = Logger.getLogger(MarcaServicio.class.getName());
    
    @EJB
    private MarcaDAO marcaDAO;

    public List<Marca> listar() {
        return marcaDAO.listar();
    }
    
    public List<Marca> listarPorEmpresa(Integer empresa) {
        return marcaDAO.listarPorEmpresa(empresa);
    }
    
    public List<Marca> listarNombre(String nombre) {
        return marcaDAO.listarMarca(nombre);
    }
    
    public void eliminar(Marca parametro) throws Exception {
        this.marcaDAO.eliminar(parametro);
    }
    
    public void insertar(Marca parametro) throws Exception {
        this.marcaDAO.insertar(parametro);
    }

    public Marca actualizar(Marca parametro) throws Exception {
        return (Marca) this.marcaDAO.actualizar(parametro);
    }
    
        
}
