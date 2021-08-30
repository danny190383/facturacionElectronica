package com.jvc.factunet.daos;

import com.jvc.factunet.dao.GenericDAO;
import com.jvc.factunet.entidades.CuentaFactura;
import com.jvc.factunet.entidades.DocumentoRetencion;
import com.jvc.factunet.entidades.Empresa;
import com.jvc.factunet.entidades.Factura;
import com.jvc.factunet.entidades.FacturaCompra;
import com.jvc.factunet.entidades.FacturaDetalle;
import com.jvc.factunet.entidades.FacturaDetalleSeries;
import com.jvc.factunet.entidades.FacturaPago;
import com.jvc.factunet.entidades.FacturaVenta;
import com.jvc.factunet.entidades.GarantiaDetalle;
import com.jvc.factunet.entidades.GuiaRemision;
import com.jvc.factunet.entidades.NotaCredito;
import com.jvc.factunet.entidades.NotaDebito;
import com.jvc.factunet.entidades.PedidoCompra;
import com.jvc.factunet.entidades.PedidoVenta;
import com.jvc.factunet.entidades.PendientesCompra;
import com.jvc.factunet.entidades.Proforma;
import com.jvc.factunet.entidades.TransferenciaProductos;
import com.jvc.factunet.utilitarios.Fecha;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class DocumentosDAO extends GenericDAO{
    
    public PendientesCompra buscar(Integer bodega) {
        try {
            Query q = em.createQuery("select o from PendientesCompra o where o.bodega.codigo = ?1 ").setMaxResults(1);
            q.setParameter(1, bodega);
            return (PendientesCompra)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public FacturaDetalle buscarDetalle(Integer codigo) {
        try {
            Query q = em.createQuery("select o from FacturaDetalle o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (FacturaDetalle)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Factura buscarDocumento(Integer codigo) {
        try {
            Query q = em.createQuery("select o from Factura o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (Factura)q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaDetalle> buscarLotesCompra(Integer producto, Integer bodega) {
        try {
            Query q = em.createQuery("select o from FacturaDetalle o where "
                    + "o.productoServicio.codigo = ?1 and "
                    + "o.bodega.codigo = ?2  and "
                    + "o.stockFecha > 0 and "
                    + "o.factura.proveedor != null "
                    + "order by o.fecha desc").setMaxResults(5);
            q.setParameter(1, producto);
            q.setParameter(2, bodega);
            return q.getResultList(); 
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoCompra> listarPedidosCompra(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from PedidoCompra o where o.empresa.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("-1") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoVenta> listarPedidosVenta(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from PedidoVenta o where o.empresa.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("-1") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoVenta> listarPedidosVentaCliente(Integer cliente, String estado) {
        try {
            Query q = em.createQuery("select o from PedidoVenta o where o.cliente.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, cliente);
            q.setParameter(2, estado.equals("-1") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Empresa> listarPedidosVentaEmpresasCliente(Integer cliente, String estado) {
        try {
            Query q = em.createQuery("select DISTINCT(o.empresa) from PedidoVenta o where o.cliente.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, cliente);
            q.setParameter(2, estado.equals("-1") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public PedidoCompra pedidoCompra(Integer codigo) {
        try {
            Query q = em.createQuery("select o from PedidoCompra o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (PedidoCompra) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public PedidoVenta pedidoVenta(Integer codigo) {
        try {
            Query q = em.createQuery("select o from PedidoVenta o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (PedidoVenta) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public GarantiaDetalle ingresoProducto(Integer codigo) {
        try {
            Query q = em.createQuery("select o from GarantiaDetalle o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (GarantiaDetalle) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Proforma buscarProforma(Integer codigo) {
        try {
            Query q = em.createQuery("select o from Proforma o where o.codigo = ?1 ");
            q.setParameter(1, codigo);
            return (Proforma) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaPago> listarCreditosCliente(Integer empresa, Integer cliente) {
        try {
            Query q = em.createQuery("select o from FacturaPago o where o.factura.empresa.codigo = ?1  and o.factura.cliente.codigo = ?2 and o.factura.estado = ?3 and o.formaPago.codigo = ?4 order by o.factura.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, cliente);
            q.setParameter(3, "2");
            q.setParameter(4, 149);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CuentaFactura> listarCuentasCliente(Integer empresa) {
        try {
            Query q = em.createQuery("select o from CuentaFactura o where o.facturaPago.factura.empresa.codigo = ?1  and o.facturaPago.factura.estado = ?2 and o.facturaPago.factura.cliente != null and o.formaPago.codigo != 153 order by o.saldo desc, o.fechaVencimiento asc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, "2");
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CuentaFactura> listarCuentasProveedor(Integer empresa) {
        try {
            Query q = em.createQuery("select o from CuentaFactura o where "
                    + "o.facturaPago.factura.empresa.codigo = ?1  and "
                    + "(o.facturaPago.factura.estado = ?2 or o.facturaPago.factura.estado = ?3) and "
                    + "o.facturaPago.factura.proveedor != null "
                    + "order by o.saldo desc, o.fechaVencimiento asc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, "3");
            q.setParameter(3, "2");
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaPago> listarCreditosProveedor(Integer empresa, Integer proveedor) {
        try {
            Query q = em.createQuery("select o from FacturaPago o where o.factura.empresa.codigo = ?1  and o.factura.proveedor.codigo = ?2 and o.factura.estado = ?3 and o.formaPago.codigo = ?4 order by o.factura.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, proveedor);
            q.setParameter(3, "3");
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaCompra> listarFacturasCompra(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from FacturaCompra o where o.empresa.codigo = ?1 and o.estado like ?2 and o.numero != -1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaVenta> listarFacturasVenta(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from FacturaVenta o where o.empresa.codigo = ?1 and o.estado like ?2 and o.numero != -1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<GuiaRemision> listarGuiasRemision(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from GuiaRemision o where o.factura.empresa.codigo = ?1 and o.estado like ?2 order by o.factura.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<DocumentoRetencion> listarDocumentosRetencion(Integer empresa) {
        try {
            Query q = em.createQuery("select o from DocumentoRetencion o where o.factura.empresa.codigo = ?1 and o.factura.proveedor != null order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoVenta> listarPedidoVenta(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from PedidoVenta o where o.empresa.codigo = ?1 and o.estado like ?2 and o.numero != -1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoVenta> listarPedidoVentaCliente(Integer cliente, String estado) {
        try {
            Query q = em.createQuery("select o from PedidoVenta o where o.cliente.persona.codigo = ?1 and o.estado like ?2 and o.numero != -1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, cliente);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Proforma> listarProformas(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from Proforma o where o.empresa.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<TransferenciaProductos> listarTransferenciaProductos(Integer empresa, String estado) {
        try {
            Query q = em.createQuery("select o from TransferenciaProductos o where o.empresa.codigo = ?1 and o.estado like ?2 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            q.setParameter(2, estado.equals("0") ? "%%" : estado);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Object> kardexProducto(Integer producto, Integer bodega) {
        try {
            Query q = em.createNativeQuery("select * from kardex_producto(?,?) ").setMaxResults(150);
            q.setParameter(1, producto);
            q.setParameter(2, bodega);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Object> kardexServicios(Integer producto) {
        try {
            Query q = em.createNativeQuery("select * from kardex_servicio(?) ").setMaxResults(150);
            q.setParameter(1, producto);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Object> kardexProductoFechas(Integer producto, Integer bodega, Date fechaInicion, Date fechaFin) {
        try {
            Query q = em.createNativeQuery("select * from kardex_producto(?,?,?,?) ").setMaxResults(500);
            q.setParameter(1, producto);
            q.setParameter(2, bodega);
            q.setParameter(3, fechaInicion);
            q.setParameter(4, Fecha.fechaFin(fechaFin));
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoCompra> listarPedidosCompraFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.proveedor.persona.apellidos) like :proveedor or UPPER(o.proveedor.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.proveedor.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from PedidoCompra o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaCompra> listarFacturaCompraFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.proveedor.persona.apellidos) like :proveedor or UPPER(o.proveedor.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.proveedor.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from FacturaCompra o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Proforma> listarProformasFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.cliente.persona.apellidos) like :proveedor or UPPER(o.cliente.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from Proforma o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaVenta> listarFacturasVentaFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.cliente.persona HAVING CONCAT(o.cliente.persona.nombres,' ',o.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from FacturaVenta o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PedidoVenta> listarPedidoVentaFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula, String mascota, String mesa) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((mesa != null) && (!mesa.isEmpty()))
            {
                sql = sql + " and o.mesa.nombre like :mesa";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                sql = sql + " and o.mascota.nombre like :mascota";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.cliente.persona HAVING CONCAT(o.cliente.persona.nombres,' ',o.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from PedidoVenta o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                q.setParameter("mascota","%" + mascota.toUpperCase() + "%%");
            }
            if((mesa != null) && (!mesa.isEmpty()))
            {
                 q.setParameter("mesa","%" + mesa.toUpperCase() + "%%");
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<TransferenciaProductos> listarTransferenciaProductosFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.estado like :estado";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.cliente.persona.apellidos) like :proveedor or UPPER(o.cliente.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from TransferenciaProductos o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaPago> listarCreditosClienteFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula) {
        try {
            String sql = "o.factura.empresa.codigo = :empresa and o.formaPago.codigo = :forma ";
            if(numero != null)
            {
                sql = sql + " and o.factura.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.factura.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                sql = sql + " and (UPPER(o.factura.cliente.persona.apellidos) like :cliente or UPPER(o.factura.cliente.persona.nombres) like :cliente)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.factura.cliente.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from FacturaPago o where "+ sql +" order by o.factura.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("forma", 149);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                q.setParameter("cliente","%" + cliente.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CuentaFactura> listarCuentasClienteFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula, Date fechaV) {
        try {
            String sql = "o.facturaPago.factura.empresa.codigo = :empresa and o.facturaPago.factura.estado = :estado and o.facturaPago.factura.cliente != null and o.formaPago.codigo != 153 ";
            if(numero != null)
            {
                sql = sql + " and o.facturaPago.factura.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.facturaPago.factura.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if(fechaV != null)
            {
                sql = sql + " and (o.fechaVencimiento BETWEEN :fechaInicioV and :fechaFinV)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.facturaPago.factura.cliente.persona.cedula = :cedula";
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.facturaPago,o.facturaPago.factura,o.facturaPago.factura.cliente.persona HAVING CONCAT(o.facturaPago.factura.cliente.persona.nombres,' ',o.facturaPago.factura.cliente.persona.apellidos) like :cliente ";
            }
            Query q = em.createQuery("select o from CuentaFactura o where "+ sql +" order by o.saldo desc, o.fechaVencimiento desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", "2");
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                cliente = cliente.trim();
                StringTokenizer st = new StringTokenizer(cliente);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               cliente = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("cliente",cliente);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            if(fechaV != null)
            {
                q.setParameter("fechaInicioV", Fecha.fechaInicio(fechaV));
                q.setParameter("fechaFinV", Fecha.fechaFin(fechaV));
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CuentaFactura> listarCuentasProveedorFiltro(Integer empresa,Integer numero, Date fecha, String cliente, String cedula, Date fechaV) {
        try {
            String sql = "o.facturaPago.factura.empresa.codigo = :empresa and o.facturaPago.factura.estado = :estado and o.facturaPago.factura.proveedor != null ";
            if(numero != null)
            {
                sql = sql + " and o.facturaPago.factura.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.facturaPago.factura.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if(fechaV != null)
            {
                sql = sql + " and (o.fechaVencimiento BETWEEN :fechaInicioV and :fechaFinV)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.facturaPago.factura.proveedor.persona.cedula = :cedula";
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.facturaPago,o.facturaPago.factura,o.facturaPago.factura.proveedor.persona HAVING CONCAT(o.facturaPago.factura.proveedor.persona.nombres,' ',o.facturaPago.factura.proveedor.persona.apellidos) like :cliente ";
            }
            Query q = em.createQuery("select o from CuentaFactura o where "+ sql +" order by o.saldo desc, o.fechaVencimiento desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", "3");
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((cliente != null) && (!cliente.isEmpty()))
            {
                cliente = cliente.trim();
                StringTokenizer st = new StringTokenizer(cliente);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               cliente = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("cliente",cliente);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            if(fechaV != null)
            {
                q.setParameter("fechaInicioV", Fecha.fechaInicio(fechaV));
                q.setParameter("fechaFinV", Fecha.fechaFin(fechaV));
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacturaPago> listarCreditosProveedorFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula) {
        try {
            String sql = "o.factura.empresa.codigo = :empresa and o.formaPago.codigo = :forma ";
            if(numero != null)
            {
                sql = sql + " and o.factura.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.factura.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " and (UPPER(o.factura.proveedor.persona.apellidos) like :proveedor or UPPER(o.factura.proveedor.persona.nombres) like :proveedor)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.factura.proveedor.persona.cedula = :cedula";
            }
            Query q = em.createQuery("select o from FacturaPago o where "+ sql +" order by o.factura.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("forma", 149);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                q.setParameter("proveedor","%" + proveedor.toUpperCase() + "%%");
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<DocumentoRetencion> listarFacturasRetencionCliente(Integer cliente) {
        try {
            Query q = em.createQuery("select o from DocumentoRetencion o where o.factura.cliente.codigo = ?1 and o.factura.estado != 3 order by o.fecha ");
            q.setParameter(1, cliente);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<DocumentoRetencion> listarFacturasRetencionProveedor(Integer proveedor) {
        try {
            Query q = em.createQuery("select o from DocumentoRetencion o where o.factura.proveedor.codigo = ?1 order by o.fecha");
            q.setParameter(1, proveedor);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public FacturaDetalleSeries serie(String barras, Integer bodega ,String estado) {
        try {
            Query q = em.createQuery("select o from FacturaDetalleSeries o where "
                    + "o.facturaDetalleSeriesPK.codigoBarras = ?1 "
                    + "and o.bodegaActual.codigo = ?2 "
                    + "and o.estado = ?3   ").setMaxResults(1);
            q.setParameter(1, barras);
            q.setParameter(2, bodega);
            q.setParameter(3, estado);
            return (FacturaDetalleSeries) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public FacturaDetalleSeries serie(Integer detalle ,String barras) {
        try {
            Query q = em.createQuery("select o from FacturaDetalleSeries o where "
                    + "o.facturaDetalleSeriesPK.facturaDetalle = ?1 and "
                    + "o.facturaDetalleSeriesPK.codigoBarras = ?2 ");
            q.setParameter(1, detalle);
            q.setParameter(2, barras);
            return (FacturaDetalleSeries) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String generarXML(Integer codigoDocumento) {
        try {
            Query q = em.createNativeQuery("select generarxml(?)");
            q.setParameter(1, codigoDocumento);
            return q.getSingleResult().toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Date maxFactura(Integer empresa) {
        try {
            Query q = em.createQuery("select max(o.fecha) from FacturaDetalle o where o.factura.empresa.codigo = ?1");
            q.setParameter(1, empresa);
            if((Date)q.getSingleResult() == null)
                return new Date();
            else
                return (Date)q.getSingleResult();
        } catch (Exception e) {
            return new Date();
        }
    }
    
    public List<PedidoVenta> listarPedidoVentaFiltroCliente(Integer cliente,Integer numero, Date fecha, String proveedor, String estado, String cedula, String mascota, String mesa) {
        try {
            String sql = "o.cliente.persona.codigo = :empresa and o.estado like :estado and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((mesa != null) && (!mesa.isEmpty()))
            {
                sql = sql + " and o.mesa.nombre like :mesa";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                sql = sql + " and o.mascota.nombre like :mascota";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.cliente.persona HAVING CONCAT(o.cliente.persona.nombres,' ',o.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from PedidoVenta o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", cliente);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if((mascota != null) && (!mascota.isEmpty()))
            {
                q.setParameter("mascota","%" + mascota.toUpperCase() + "%%");
            }
            if((mesa != null) && (!mesa.isEmpty()))
            {
                 q.setParameter("mesa","%" + mesa.toUpperCase() + "%%");
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<NotaCredito> listarNotasCredito(Integer empresa) {
        try {
            Query q = em.createQuery("select o from NotaCredito o where o.empresa.codigo = ?1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<NotaDebito> listarNotasDebito(Integer empresa) {
        try {
            Query q = em.createQuery("select o from NotaDebito o where o.empresa.codigo = ?1 order by o.fecha desc").setMaxResults(100);
            q.setParameter(1, empresa);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<NotaCredito> listarNotasCreditoFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.cliente.persona HAVING CONCAT(o.cliente.persona.nombres,' ',o.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from NotaCredito o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<NotaDebito> listarNotasDebitoFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula) {
        try {
            String sql = "o.empresa.codigo = :empresa and o.numero != -1 ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.cliente.persona.cedula = :cedula";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.cliente.persona HAVING CONCAT(o.cliente.persona.nombres,' ',o.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from NotaDebito o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<GuiaRemision> listarGuiaRemisionFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String estado, String cedula) {
        try {
            String sql = "o.factura.empresa.codigo = :empresa and o.estado like :estado ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.factura.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.factura.cliente.persona.cedula = :cedula";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.factura.cliente.persona HAVING CONCAT(o.factura.cliente.persona.nombres,' ',o.factura.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from GuiaRemision o where "+ sql +" order by o.fechaEnvio desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            q.setParameter("estado", estado.equals("0") ? "%%" : estado);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<DocumentoRetencion> listarDocumentoRetencionFiltro(Integer empresa,Integer numero, Date fecha, String proveedor, String cedula) {
        try {
            String sql = "o.factura.empresa.codigo = :empresa and o.factura.proveedor != null ";
            if(numero != null)
            {
                sql = sql + " and o.numero = :numero";
            }
            if(fecha != null)
            {
                sql = sql + " and (o.fecha BETWEEN :fechaInicio and :fechaFin)";
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                sql = sql + " and o.factura.cliente.persona.cedula = :cedula";
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                sql = sql + " GROUP BY o,o.factura.cliente.persona HAVING CONCAT(o.factura.cliente.persona.nombres,' ',o.factura.cliente.persona.apellidos) like :proveedor ";
            }
            Query q = em.createQuery("select o from DocumentoRetencion o where "+ sql +" order by o.fecha desc").setMaxResults(100);
            q.setParameter("empresa", empresa);
            if(numero != null)
            {
                q.setParameter("numero", numero);
            }
            if(fecha != null)
            {
                q.setParameter("fechaInicio", Fecha.fechaInicio(fecha));
                q.setParameter("fechaFin", Fecha.fechaFin(fecha));
            }
            if((proveedor != null) && (!proveedor.isEmpty()))
            {
                proveedor = proveedor.trim();
                StringTokenizer st = new StringTokenizer(proveedor);
                String texto="";
                while (st.hasMoreTokens()) {
                    if(!texto.equals("")){
                        texto+="%";
                    }
                    texto += st.nextToken();

               }
               proveedor = "%" +texto.toUpperCase().trim() + "%";
               q.setParameter("proveedor",proveedor);
            }
            if((cedula != null) && (!cedula.isEmpty()))
            {
                q.setParameter("cedula", cedula);
            }
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
