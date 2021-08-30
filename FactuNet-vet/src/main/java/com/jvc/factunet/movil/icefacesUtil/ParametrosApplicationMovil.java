package com.jvc.factunet.movil.icefacesUtil;

import java.util.Locale;
import java.util.TimeZone;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class ParametrosApplicationMovil {
    
    private TimeZone timeZone;
    private Locale locale;
    private String formatoFecha;
    private String formatoFechaHora;
    private String formatoFechaHoraSMS;
    private String formatoMesAnio;
    private String formatoHora;
    private String formatoHoraSMS;
    private String formatoNumeroDecimal;
    private String formatoNumeroEntero;
    private String local;
   
    public ParametrosApplicationMovil() {
        this.timeZone = TimeZone.getTimeZone("GMT-5");
        this.locale = new Locale("ISO-8859-1", "es_EC");
        this.local = "es";
        this.formatoHora = "HH:mm:ss";
        this.formatoHoraSMS = "HH:mm";
        this.formatoFecha = "dd/MM/yyyy";
        this.formatoFechaHora = "dd/MM/yyyy HH:mm:ss";
        this.formatoFechaHoraSMS = "dd/MM/yyyy HH:mm";
        this.formatoMesAnio = "MM/yyyy";
        this.formatoNumeroDecimal = "###,###,##0.00";
        this.formatoNumeroEntero = "###,###,##0";
    }
    
    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    public String getFormatoFechaHora() {
        return formatoFechaHora;
    }

    public void setFormatoFechaHora(String formatoFechaHora) {
        this.formatoFechaHora = formatoFechaHora;
    }

    public String getFormatoFechaHoraSMS() {
        return formatoFechaHoraSMS;
    }

    public void setFormatoFechaHoraSMS(String formatoFechaHoraSMS) {
        this.formatoFechaHoraSMS = formatoFechaHoraSMS;
    }

    public String getFormatoMesAnio() {
        return formatoMesAnio;
    }

    public void setFormatoMesAnio(String formatoMesAnio) {
        this.formatoMesAnio = formatoMesAnio;
    }

    public String getFormatoHora() {
        return formatoHora;
    }

    public void setFormatoHora(String formatoHora) {
        this.formatoHora = formatoHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFormatoHoraSMS() {
        return formatoHoraSMS;
    }

    public void setFormatoHoraSMS(String formatoHoraSMS) {
        this.formatoHoraSMS = formatoHoraSMS;
    }

    public String getFormatoNumeroDecimal() {
        return formatoNumeroDecimal;
    }

    public void setFormatoNumeroDecimal(String formatoNumeroDecimal) {
        this.formatoNumeroDecimal = formatoNumeroDecimal;
    }

    public String getFormatoNumeroEntero() {
        return formatoNumeroEntero;
    }

    public void setFormatoNumeroEntero(String formatoNumeroEntero) {
        this.formatoNumeroEntero = formatoNumeroEntero;
    }
}
