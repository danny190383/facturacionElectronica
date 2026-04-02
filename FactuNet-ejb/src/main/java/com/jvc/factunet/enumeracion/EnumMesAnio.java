package com.jvc.factunet.enumeracion;

public enum EnumMesAnio {
    ENE(1, "enero"),
    FEB(2, "febrero"),
    MAR(3, "marzo"),
    ABR(4, "abril"),
    MAY(5, "mayo"),
    JUN(6, "junio"),
    JUL(7, "julio"),
    AGO(8, "agosto"),
    SEP(9, "septiembre"),
    OCT(10, "octubre"),
    NOV(11, "noviembre"),
    DIC(12, "diciembre");
    
    private final int numero;
    private final String etiqueta;
    
    EnumMesAnio(int numero, String etiqueta) {
        this.numero = numero;
        this.etiqueta = etiqueta;
    }
    
    public int getNumero() {
        return this.numero;
    }
    
    public String getEtiqueta() {
        return this.etiqueta;
    }
}
