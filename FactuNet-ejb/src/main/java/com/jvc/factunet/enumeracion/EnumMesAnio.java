package com.jvc.factunet.enumeracion;

public enum EnumMesAnio {

    ENE("enero"),
    FEB("febrero"),
    MAR("marzo"),
    ABR("abril"),
    MAY("mayo"),
    JUN("junio"),
    JUL("julio"),
    AGO("agosto"),
    SEP("septiembre"),
    OCT("octubre"),
    NOV("noviembre"),
    DIC("diciembre");

    private final String etiqueta;

    EnumMesAnio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }
}
