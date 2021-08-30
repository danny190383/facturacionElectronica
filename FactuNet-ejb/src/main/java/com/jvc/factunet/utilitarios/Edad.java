package com.jvc.medisys.bussines.utilitario;

import java.util.Calendar;
import java.util.Date;

public class Edad {
    
    public int anios;
    public int meses;
    public int dias;
    
    public Edad(int anios, int meses, int dias) {
        super();
        this.anios = anios;
        this.meses = meses;
        this.dias = dias;
    }
    
    public Edad() {
        super();
    }
    
    public int getAnios() {
        return anios;
    }
    
    public void setAnios(int anios) {
        this.anios = anios;
    }
    
    public int getMeses() {
        return this.meses;
    }
    
    public void setMeses(int meses) {
        this.meses = meses;
    }
    
    public int getDias() {
        return this.dias;
    }
    
    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
     * Calcula la edad dada la fecha de nacimiento y la fecha hasta
     * Si la fecha hasta es null, se toma hasta la fecha actual
     * @param fecha_nac
     * @param fecha_hasta 
     */
    public void calculoEdad(Date fecha_nac, Date fecha_hasta) {
        
        java.util.Calendar c = java.util.Calendar.getInstance();
        
        c.setTime(fecha_nac);
        int ageMonths, ageDays;
        
        Calendar cd = Calendar.getInstance();
        if (fecha_hasta != null) {
            cd.setTime(fecha_hasta);
        }
        
        Calendar bd = Calendar.getInstance();
        bd.setTime(fecha_nac);
        
        int ageYears = cd.get(Calendar.YEAR) - bd.get(Calendar.YEAR);
        
        if (bd.get(Calendar.MONTH) <= cd.get(Calendar.MONTH)) {
            ageMonths = (cd.get(Calendar.MONTH) + 1) - (bd.get(Calendar.MONTH) + 1);
        } else {
            ageMonths = (cd.get(Calendar.MONTH) + 1) + 12 - (bd.get(Calendar.MONTH) + 1);
            ageYears--;
        }
        
        if (bd.get(Calendar.DAY_OF_MONTH) <= cd.get(Calendar.DAY_OF_MONTH)) {
            ageDays = cd.get(Calendar.DAY_OF_MONTH) - bd.get(Calendar.DAY_OF_MONTH);
        } else {
            ageDays = cd.get(Calendar.DAY_OF_MONTH) + cd.getActualMaximum(Calendar.DAY_OF_MONTH)
                    - bd.get(Calendar.DAY_OF_MONTH);
            ageMonths--;
        }
        
        if (ageMonths < 0) {
            ageYears--;
            ageMonths += 12;
        }
        
        this.anios = ageYears;
        this.meses = ageMonths;
        this.dias = ageDays;
        
    }

    /**
     * Calcula la edad en años, dada la fecha de nacimiento y la fecha hasta
     * Si la fecha hasta es null, se toma hasta la fecha actual
     *
     * @param fechaNacimiento
     * @return
     */
    public double edadEnAnios(Date fechaNacimiento, Date fechaHasta) {
        if (fechaNacimiento != null) {
            calculoEdad(fechaNacimiento, fechaHasta);
            return this.getAnios() + this.getMeses() / 12 + this.getDias() / 365;
        } else {
            return 0;
        }
    }
    
    public float edadEnAnios2(Date fechaNacimiento, Date fechaHasta) {
        if (fechaNacimiento != null) {
            calculoEdad(fechaNacimiento, fechaHasta);
            float anios = new Float(getAnios());
            float meses = new Float(getMeses());
            float dias = new Float(getDias());
            return anios + (anios / 12) + (dias / 365);
        } else {
            return 0;
        }
    }

    /**
     * Devuelve la edad calculada en meses
     *
     * @param fechaNacimiento
     * @return
     */
    public double edadEnMeses(Date fechaNacimiento, Date fechaHasta) {
        if (fechaNacimiento != null) {
            calculoEdad(fechaNacimiento, fechaHasta);
            return this.getAnios() * 12 + this.getMeses() + this.getDias() / 30.4375;
//            return this.getAnios() * 12 + this.getMeses();
        } else {
            return 0;
        }
    }

    /**
     * Devuelve la edad calculada en dias
     *
     * @param fechaNacimiento
     * @return
     */
    public double edadEnDias(Date fechaNacimiento, Date fechaHasta) {
        if (fechaNacimiento != null) {
            calculoEdad(fechaNacimiento, fechaHasta);
            return this.getAnios() * 365 + this.getMeses() * 30 + this.getDias();
        } else {
            return 0;
        }
    }
    
    public String getEdadAniosMeses(Date fechaNacimiento) {
        calculoEdad(fechaNacimiento, new Date());
        return (anios + " años " + meses + " meses");
    }
    
    @Override
    /**
     * Mestra la edad en modo texto , años meses dias
     */
    public String toString() {
        return "" + this.anios + " años " + this.meses + " meses " + this.dias + "  días ";
    }
}
