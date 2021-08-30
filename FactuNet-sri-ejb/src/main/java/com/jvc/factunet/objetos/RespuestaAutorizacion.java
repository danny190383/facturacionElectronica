package com.jvc.factunet.objetos;

public class RespuestaAutorizacion {
    
    private String estado;
    private String fechaAutorizacion;
    private String ambiente;
    private String identificadorError;
    private String mensajeError;
    private String descripciónAdicionalError;
    private String tipoMensaje;

    public RespuestaAutorizacion() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getIdentificadorError() {
        return identificadorError;
    }

    public void setIdentificadorError(String identificadorError) {
        this.identificadorError = identificadorError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getDescripciónAdicionalError() {
        return descripciónAdicionalError;
    }

    public void setDescripciónAdicionalError(String descripciónAdicionalError) {
        this.descripciónAdicionalError = descripciónAdicionalError;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }
}
