package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CuentaFactura;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class CuentasFacturaDAO extends GenericDAO{
    
    public List<CuentaFactura> buscarPendientesCliente(Integer persona, Integer empresa) {
        try {
            Query q = em.createQuery("select o from CuentaFactura o where o.saldo > 0 and o.facturaPago.factura.cliente.codigo = ?1 and o.facturaPago.factura.estado = 2 and o.formaPago.codigo != 153 and o.facturaPago.factura.empresa.codigo = ?2 order by o.fechaVencimiento ");
            q.setParameter(1, persona);
            q.setParameter(2, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<CuentaFactura> buscarPendientesProveedor(Integer persona, Integer empresa) {
        try {
            Query q = em.createQuery("select o from CuentaFactura o where o.saldo > 0 and o.facturaPago.factura.proveedor.codigo = ?1 and (o.facturaPago.factura.estado = 3 or o.facturaPago.factura.estado = 2) and o.facturaPago.factura.empresa.codigo = ?2 order by o.fechaVencimiento ");
            q.setParameter(1, persona);
            q.setParameter(2, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
