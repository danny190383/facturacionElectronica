package com.jvc.factunet.converter;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "convertidorEdadMeses")
public class ConvertidorEdadMeses implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        double edadMeses = 0;
        StringTokenizer st = new StringTokenizer(arg2, " ");
        if (st.countTokens() == 6) {
            try {
                double anios = Double.parseDouble(st.nextToken());
                st.nextToken();
                double meses = Double.parseDouble(st.nextToken());
                st.nextToken();
                double dias = Double.parseDouble(st.nextToken());
                if (anios > 0) {
                    edadMeses = anios * 12;
                }
                if (meses > 0) {
                    edadMeses += meses;
                }
                if (dias > 0) {
                    edadMeses += dias / 30.4375;
                }
                edadMeses = Math.round(edadMeses * Math.pow(10, 2)) / Math.pow(10, 2);

            } catch (NumberFormatException nfe) {
                throw new ConverterException(
                        "Debe ingresar: x años y meses z días. x,y,z deben ser números. " + nfe.getMessage());
            }
        } else {
            throw new ConverterException("Debe ingresar: x años y meses z días. x,y,z deben ser números");
        }
        return new BigDecimal(edadMeses);
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String retorno = "";
        try {
            String dato = arg2.toString();
            double meses = Double.parseDouble(dato);

            if (meses > 0) {
                System.out.println();
            }
            double v1 = 0.00;
            double v2 = 0.00;
            double v3 = 0.00;
            double v4 = 0.00;

            int anio = 0;
            int mes = 0;
            int dia = 0;

            v1 = meses / 12;
            anio = (int) v1;
            v2 = v1 - anio;
            v3 = v2 * 12;
            v4 = v3 - (int) v3;

            mes = (int) v3;
            dia = (int) (v4 * 30.4375);

            if (dia >= 30) {
                mes = mes + (int) (dia / 30.4375);
                dia = (int) ((dia / 30.4375) - (int) (dia / 30.4375));
            }
//            retorno = "" + anio + " años " + mes + " meses " + dia + " días";
            retorno = "" + anio + " años " + mes + " meses ";

        } catch (NumberFormatException nfe) {
            throw new ConverterException("No se pudo transformas los meses a: x años y meses z días. "
                    + nfe.getMessage());
        } catch (NullPointerException npe) {
            retorno = "";
        }
        return retorno;
    }
}