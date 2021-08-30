package com.jvc.factunet.bean.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ParametrosApplicationPedidos {
    
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
    private Boolean iniciarConteoManual;
    private List<String> pacientesAlta;
    private BigDecimal coordenadaLatitudEcuador;
    private BigDecimal coordenadaLongitudEcuador;
    private Integer zoomPais;
    private Integer zoomProvincia;
    private Integer zoomCanton;
    private Integer zoomParroquia;
    private Integer zoomDireccion;
    public ParametrosApplicationPedidos() {
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
        this.iniciarConteoManual=false;
        this.pacientesAlta = new ArrayList<>();
        coordenadaLatitudEcuador = new BigDecimal(-1.4646083870);
        coordenadaLongitudEcuador = new BigDecimal(-78.4310099309);
        zoomPais=7;
        zoomProvincia=10;
        zoomCanton=12;
        zoomParroquia=15;
        zoomDireccion=17;
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

    public Boolean getIniciarConteoManual() {
        return iniciarConteoManual;
    }

    public void setIniciarConteoManual(Boolean iniciarConteoManual) {
        this.iniciarConteoManual = iniciarConteoManual;
    }

    public List<String> getPacientesAlta() {
        return pacientesAlta;
    }

    public void setPacientesAlta(List<String> pacientesAlta) {
        this.pacientesAlta = pacientesAlta;
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

    public BigDecimal getCoordenadaLatitudEcuador() {
        return coordenadaLatitudEcuador;
    }

    public void setCoordenadaLatitudEcuador(BigDecimal coordenadaLatitudEcuador) {
        this.coordenadaLatitudEcuador = coordenadaLatitudEcuador;
    }

    public BigDecimal getCoordenadaLongitudEcuador() {
        return coordenadaLongitudEcuador;
    }

    public void setCoordenadaLongitudEcuador(BigDecimal coordenadaLongitudEcuador) {
        this.coordenadaLongitudEcuador = coordenadaLongitudEcuador;
    }

    public Integer getZoomPais() {
        return zoomPais;
    }

    public void setZoomPais(Integer zoomPais) {
        this.zoomPais = zoomPais;
    }

    public Integer getZoomProvincia() {
        return zoomProvincia;
    }

    public void setZoomProvincia(Integer zoomProvincia) {
        this.zoomProvincia = zoomProvincia;
    }

    public Integer getZoomCanton() {
        return zoomCanton;
    }

    public void setZoomCanton(Integer zoomCanton) {
        this.zoomCanton = zoomCanton;
    }

    public Integer getZoomParroquia() {
        return zoomParroquia;
    }

    public void setZoomParroquia(Integer zoomParroquia) {
        this.zoomParroquia = zoomParroquia;
    }

    public Integer getZoomDireccion() {
        return zoomDireccion;
    }

    public void setZoomDireccion(Integer zoomDireccion) {
        this.zoomDireccion = zoomDireccion;
    }
    
    
    
}
