package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.SerieProductoDAO;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SerieProductoServicios {
    private static final Logger LOG = Logger.getLogger(SerieProductoServicios.class.getName());
     
    @EJB
    private SerieProductoDAO serieProductoDAO;
    
    public FacturaDetalleSeries buscarProductoSerie(String barras, Integer empresa, Integer producto){
        return this.serieProductoDAO.buscarProductoSerie(barras, empresa, producto);
    }
    
    public void eliminar(FacturaDetalleSeries parametro) throws Exception {
        this.serieProductoDAO.eliminar(parametro);
    }
    
    public void insertar(FacturaDetalleSeries parametro) throws Exception {
        this.serieProductoDAO.insertar(parametro);
    }

    public FacturaDetalleSeries actualizar(FacturaDetalleSeries parametro) throws Exception {
        return (FacturaDetalleSeries) this.serieProductoDAO.actualizar(parametro);
    }
}
