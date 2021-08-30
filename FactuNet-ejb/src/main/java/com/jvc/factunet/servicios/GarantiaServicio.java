package com.jvc.factunet.servicios;

import com.jvc.factunet.daos.GarantiasDAO;
import com.jvc.factunet.entidades.GarantiaCabecera;
import com.jvc.factunet.entidades.SecuenciaDocumento;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@LocalBean
public class GarantiaServicio {
    private static final Logger LOG = Logger.getLogger(GarantiaServicio.class.getName());
    
    @EJB
    private GarantiasDAO garantiasDAO;
    @EJB
    private PuntoVentaServicio puntoVentaServicio;
    
    public void eliminar(GarantiaCabecera parametro) throws Exception {
        this.garantiasDAO.eliminar(parametro);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(GarantiaCabecera parametro, Integer punto) throws Exception {
        SecuenciaDocumento secuencia = this.puntoVentaServicio.numeroActual(327,parametro.getEmpresa().getCodigo(), punto);
        if(secuencia!= null)
        {
            parametro.setNumero(secuencia.getNumeroActual()+1);
            secuencia.setNumeroActual(parametro.getNumero());
            this.puntoVentaServicio.actualizar(secuencia);
        }
        else
        {
            parametro.setNumero(001);
        }
        this.garantiasDAO.insertar(parametro);
    }

    public GarantiaCabecera actualizar(GarantiaCabecera parametro) throws Exception {
        return (GarantiaCabecera) this.garantiasDAO.actualizar(parametro);
    }
}
