package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class SerieProductoDAO extends GenericDAO{
    
    public FacturaDetalleSeries buscarProductoSerie(String barras, Integer empresa, Integer producto) {
        try {
            Query q = em.createQuery("select o from FacturaDetalleSeries o where  "
                                     + "o.facturaDetalleSeriesPK.codigoBarras = ?1 "
                                     + "and o.estado = ?2 "
                                     + "and o.facturaDetalle.factura.empresa.codigo = ?3 "
                                     + "and o.facturaDetalle.productoServicio.codigo = ?4 ");
            q.setParameter(1, barras);
            q.setParameter(2, "1");
            q.setParameter(3, empresa);
            q.setParameter(4, producto);
            return (FacturaDetalleSeries)q.getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
}
