package com.jvc.factunet.objetos;

public class RespuestaRecepcion {
    
    private String estado;
    private String claveAccceso;
    private String identificadorError;
    private String descripciónError;
    private String descripciónAdicionalError;
    private String tipoMensaje;

    public RespuestaRecepcion() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClaveAccceso() {
        return claveAccceso;
    }

    public void setClaveAccceso(String claveAccceso) {
        this.claveAccceso = claveAccceso;
    }

    public String getIdentificadorError() {
        return identificadorError;
    }

    public void setIdentificadorError(String identificadorError) {
        this.identificadorError = identificadorError;
    }

    public String getDescripciónError() {
        return descripciónError;
    }

    public void setDescripciónError(String descripciónError) {
        this.descripciónError = descripciónError;
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
