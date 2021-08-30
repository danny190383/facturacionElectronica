package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.CargoDAO;
import com.jvc.factunet.entidades.Cargo;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class CargoServicio {
    
    private static final Logger LOG = Logger.getLogger(CargoServicio.class.getName());
    
    @EJB
    private CargoDAO cargoDAO;
    
    public List<Cargo> listar() {
        return cargoDAO.listar();
    }
    
    public List<Cargo> listarNombre(String nombre) {
        return cargoDAO.listarNombre(nombre);
    }
    
    public void eliminar(Cargo parametro) throws Exception {
        this.cargoDAO.eliminar(parametro);
    }
    
    public void insertar(Cargo parametro) throws Exception {
        this.cargoDAO.insertar(parametro);
    }

    public Cargo actualizar(Cargo parametro) throws Exception {
        return (Cargo) this.cargoDAO.actualizar(parametro);
    }
}
