package com.jvc.factunet.validador;

import com.jvc.factunet.icefacesUtil.FacesUtils;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;

@FacesValidator("com.jvc.factunet.validador.ValidarCedulaRucNulo")
public class ValidarCedulaRucNulo implements Validator, ClientValidator
{

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
         
         
        if(value == null || value.equals("")) {
            ((UIInput) component).setValid(true);
            return;
        }
        else
        {
            if(this.validarCedulaRuc((String)value))
            {
                 ((UIInput) component).setValid(true);
                 return;
            }
            ((UIInput) component).setValid(false);
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "custom.emailValidator";
    }
    
    public static boolean validarCedulaRuc(String cedula_ruc) 
    {
        boolean valor = true;
        try 
        {
            int suma = 0;
            int residuo = 0;
            boolean privada = false;
            boolean publica = false;
            boolean natural = false;
            /**
             * Cambia el valor de 22 a 24 por las 2 nuevas provincias  de Ecuador
             * DVSP
             * 14/02/2011
             * */
            int numeroProvincias = 24;
            int digitoVerificador = 0;
            int modulo = 11;

            int d1, d2, d3, d4, d5, d6, d7, d8, d9, d10;
            int p1, p2, p3, p4, p5, p6, p7, p8, p9;

            d1 = d2 = d3 = d4 = d5 = d6 = d7 = d8 = d9 = d10 = 0;
            p1 = p2 = p3 = p4 = p5 = p6 = p7 = p8 = p9 = 0;

            if (cedula_ruc.length() < 10) 
            {
                    FacesUtils.addErrorMessage("El número de documento ingresado no es valido (Cédula o RUC).");
                    cedula_ruc = "";
                    valor = false;
            }

            // Los primeros dos digitos corresponden al codigo de la provincia
            int provincia = Integer.parseInt(cedula_ruc.substring(0, 2));

            /*
             *  LOS MENSAJES DE ERROR DE LA CEDULA SE CAMBIARON A PEDIDO DE ADRIANA  EL 19-ENE-2011
             *  CAMBIO REALIZADO POR ALBERTO GARCIA
             */

            if (provincia <= 0 || provincia > numeroProvincias) 
            {
                    //FacesUtils.addErrorMessage("El codigo de la provincia (dos primeros digitos) es invalido");
                    FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                    cedula_ruc ="";
                    valor = false;
            }

            // Aqui almacenamos los digitos de la documento en variables.
            d1 = Integer.parseInt(cedula_ruc.substring(0, 1));
            d2 = Integer.parseInt(cedula_ruc.substring(1, 2));
            d3 = Integer.parseInt(cedula_ruc.substring(2, 3));
            d4 = Integer.parseInt(cedula_ruc.substring(3, 4));
            d5 = Integer.parseInt(cedula_ruc.substring(4, 5));
            d6 = Integer.parseInt(cedula_ruc.substring(5, 6));
            d7 = Integer.parseInt(cedula_ruc.substring(6, 7));
            d8 = Integer.parseInt(cedula_ruc.substring(7, 8));
            d9 = Integer.parseInt(cedula_ruc.substring(8, 9));
            d10 = Integer.parseInt(cedula_ruc.substring(9, 10));

            // El tercer digito es:
            // 9 para sociedades privadas y extranjeros
            // 6 para sociedades publicas
            // menor que 6 (0,1,2,3,4,5) para personas naturales
            if (d3 == 7 || d3 == 8) 
            {
                //FacesUtils.addErrorMessage("El tercer digito ingresado es invalido");
                FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                cedula_ruc ="";
                valor = false;
            }

            // Solo para personas naturales (modulo 10)
            if (d3 < 6) 
            {
                natural = true;
                modulo = 10;
                p1 = d1 * 2;
                if (p1 >= 10)
                        p1 -= 9;
                p2 = d2 * 1;
                if (p2 >= 10)
                        p2 -= 9;
                p3 = d3 * 2;
                if (p3 >= 10)
                        p3 -= 9;
                p4 = d4 * 1;
                if (p4 >= 10)
                        p4 -= 9;
                p5 = d5 * 2;
                if (p5 >= 10)
                        p5 -= 9;
                p6 = d6 * 1;
                if (p6 >= 10)
                        p6 -= 9;
                p7 = d7 * 2;
                if (p7 >= 10)
                        p7 -= 9;
                p8 = d8 * 1;
                if (p8 >= 10)
                        p8 -= 9;
                p9 = d9 * 2;
                if (p9 >= 10)
                        p9 -= 9;
            }

            // Solo para sociedades publicas (modulo 11)
            // Aqui el digito verficador esta en la posicion 9, en las otras 2
            // en la pos. 10
//            if (d3 == 6) 
//            {
//                publica = true;
//                p1 = d1 * 3;
//                p2 = d2 * 2;
//                p3 = d3 * 7;
//                p4 = d4 * 6;
//                p5 = d5 * 5;
//                p6 = d6 * 4;
//                p7 = d7 * 3;
//                p8 = d8 * 2;
//                p9 = 0;
//            }

            /* Solo para entidades privadas (modulo 11) */
            if (d3 == 9) 
            {
                privada = true;
                p1 = d1 * 4;
                p2 = d2 * 3;
                p3 = d3 * 2;
                p4 = d4 * 7;
                p5 = d5 * 6;
                p6 = d6 * 5;
                p7 = d7 * 4;
                p8 = d8 * 3;
                p9 = d9 * 2;
            }

            suma = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
            residuo = suma % modulo;

            // Si residuo=0, dig.ver.=0, caso contrario 10 - residuo
            digitoVerificador = residuo == 0 ? 0 : modulo - residuo;
            int longitud = cedula_ruc.length(); // Longitud del string

            // ahora comparamos el elemento de la posicion 10 con el dig. ver.
            if (publica == true) 
            {
                    if (digitoVerificador != d9) 
                    {
                        //FacesUtils.addErrorMessage("El RUC de la empresa del sector publico es incorrecto.");
                        FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                        cedula_ruc="";
                        valor = false;
                    }
                    /* El ruc de las empresas del sector publico terminan con 0001 */
                    if (!cedula_ruc.substring(9, longitud).equals("0001")) 
                    {

                        //FacesUtils.addErrorMessage("El RUC de la empresa del sector publico debe terminar con 0001");
                        FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                        cedula_ruc ="";
                        valor = false;
                    }
            }

            if (privada == true) 
            {
                if (digitoVerificador != d10)
                {
                    //FacesUtils.addErrorMessage("El ruc de la empresa del sector privado es incorrecto.");
                    FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                    cedula_ruc ="";
                    valor = false;
                }
                if (!cedula_ruc.substring(10, longitud).equals("001")) 
                {
                    //FacesUtils.addErrorMessage("El ruc de la empresa del sector privado debe terminar con 001");
                    FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                    cedula_ruc ="";
                    valor = false;
                }
            }

            if (natural == true) 
            {
                if (digitoVerificador != d10) 
                {					
                        //FacesUtils.addErrorMessage("El numero de documento de la persona natural es incorrecto.");
                        FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                        cedula_ruc ="";
                        valor = false;
                }
                if (cedula_ruc.length() > 10 && !cedula_ruc.substring(10, longitud).equals("001"))
                {
                        //FacesUtils.addErrorMessage("El ruc de la persona natural debe terminar con 001");
                        FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                        cedula_ruc ="";
                        valor = false;
                }
            }
        } 
        catch (Exception e) 
        {
                cedula_ruc ="";
                valor = false;
        }
        return valor;
    }
        
        
    public static boolean validarCedulaRucSinMsg(String cedula_ruc) 
    {
        boolean valor = true;
        try 
        {
            int suma = 0;
            int residuo = 0;
            boolean privada = false;
            boolean publica = false;
            boolean natural = false;
            /**
             * Cambia el valor de 22 a 24 por las 2 nuevas provincias  de Ecuador
             * DVSP
             * 14/02/2011
             * */
            int numeroProvincias = 24;
            int digitoVerificador = 0;
            int modulo = 11;

            int d1, d2, d3, d4, d5, d6, d7, d8, d9, d10;
            int p1, p2, p3, p4, p5, p6, p7, p8, p9;

            d1 = d2 = d3 = d4 = d5 = d6 = d7 = d8 = d9 = d10 = 0;
            p1 = p2 = p3 = p4 = p5 = p6 = p7 = p8 = p9 = 0;

            if (cedula_ruc.length() < 10) 
            {
                    //FacesUtils.addErrorMessage("El número de documento ingresado no es valido (Cédula o RUC).");
                    cedula_ruc = "";
                    valor = false;
            }

            // Los primeros dos digitos corresponden al codigo de la provincia
            int provincia = Integer.parseInt(cedula_ruc.substring(0, 2));

            /*
             *  LOS MENSAJES DE ERROR DE LA CEDULA SE CAMBIARON A PEDIDO DE ADRIANA  EL 19-ENE-2011
             *  CAMBIO REALIZADO POR ALBERTO GARCIA
             */

            if (provincia <= 0 || provincia > numeroProvincias) 
            {
                    //FacesUtils.addErrorMessage("El codigo de la provincia (dos primeros digitos) es invalido");
                    //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                    cedula_ruc ="";
                    valor = false;
            }

            // Aqui almacenamos los digitos de la documento en variables.
            d1 = Integer.parseInt(cedula_ruc.substring(0, 1));
            d2 = Integer.parseInt(cedula_ruc.substring(1, 2));
            d3 = Integer.parseInt(cedula_ruc.substring(2, 3));
            d4 = Integer.parseInt(cedula_ruc.substring(3, 4));
            d5 = Integer.parseInt(cedula_ruc.substring(4, 5));
            d6 = Integer.parseInt(cedula_ruc.substring(5, 6));
            d7 = Integer.parseInt(cedula_ruc.substring(6, 7));
            d8 = Integer.parseInt(cedula_ruc.substring(7, 8));
            d9 = Integer.parseInt(cedula_ruc.substring(8, 9));
            d10 = Integer.parseInt(cedula_ruc.substring(9, 10));

            // El tercer digito es:
            // 9 para sociedades privadas y extranjeros
            // 6 para sociedades publicas
            // menor que 6 (0,1,2,3,4,5) para personas naturales
            if (d3 == 7 || d3 == 8) 
            {
                    //FacesUtils.addErrorMessage("El tercer digito ingresado es invalido");
                    //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                    cedula_ruc ="";
                    valor = false;
            }

            // Solo para personas naturales (modulo 10)
            if (d3 < 6) 
            {
                    natural = true;
                    modulo = 10;
                    p1 = d1 * 2;
                    if (p1 >= 10)
                            p1 -= 9;
                    p2 = d2 * 1;
                    if (p2 >= 10)
                            p2 -= 9;
                    p3 = d3 * 2;
                    if (p3 >= 10)
                            p3 -= 9;
                    p4 = d4 * 1;
                    if (p4 >= 10)
                            p4 -= 9;
                    p5 = d5 * 2;
                    if (p5 >= 10)
                            p5 -= 9;
                    p6 = d6 * 1;
                    if (p6 >= 10)
                            p6 -= 9;
                    p7 = d7 * 2;
                    if (p7 >= 10)
                            p7 -= 9;
                    p8 = d8 * 1;
                    if (p8 >= 10)
                            p8 -= 9;
                    p9 = d9 * 2;
                    if (p9 >= 10)
                            p9 -= 9;
            }

            // Solo para sociedades publicas (modulo 11)
            // Aqui el digito verficador esta en la posicion 9, en las otras 2
            // en la pos. 10
            if (d3 == 6) 
            {
                    publica = true;
                    p1 = d1 * 3;
                    p2 = d2 * 2;
                    p3 = d3 * 7;
                    p4 = d4 * 6;
                    p5 = d5 * 5;
                    p6 = d6 * 4;
                    p7 = d7 * 3;
                    p8 = d8 * 2;
                    p9 = 0;
            }

            /* Solo para entidades privadas (modulo 11) */
            if (d3 == 9) 
            {
                    privada = true;
                    p1 = d1 * 4;
                    p2 = d2 * 3;
                    p3 = d3 * 2;
                    p4 = d4 * 7;
                    p5 = d5 * 6;
                    p6 = d6 * 5;
                    p7 = d7 * 4;
                    p8 = d8 * 3;
                    p9 = d9 * 2;
            }

            suma = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
            residuo = suma % modulo;

            // Si residuo=0, dig.ver.=0, caso contrario 10 - residuo
            digitoVerificador = residuo == 0 ? 0 : modulo - residuo;
            int longitud = cedula_ruc.length(); // Longitud del string

            // ahora comparamos el elemento de la posicion 10 con el dig. ver.
            if (publica == true) 
            {
                    if (digitoVerificador != d9) 
                    {
                            //FacesUtils.addErrorMessage("El RUC de la empresa del sector publico es incorrecto.");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc="";
                            valor = false;
                    }
                    /* El ruc de las empresas del sector publico terminan con 0001 */
                    if (!cedula_ruc.substring(9, longitud).equals("0001")) 
                    {

                            //FacesUtils.addErrorMessage("El RUC de la empresa del sector publico debe terminar con 0001");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc ="";
                            valor = false;
                    }
            }

            if (privada == true) 
            {
                    if (digitoVerificador != d10)
                    {
                            //FacesUtils.addErrorMessage("El ruc de la empresa del sector privado es incorrecto.");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc ="";
                            valor = false;
                    }
                    if (!cedula_ruc.substring(10, longitud).equals("001")) 
                    {
                            //FacesUtils.addErrorMessage("El ruc de la empresa del sector privado debe terminar con 001");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc ="";
                            valor = false;
                    }
            }

            if (natural == true) 
            {
                    if (digitoVerificador != d10) 
                    {					
                            //FacesUtils.addErrorMessage("El numero de documento de la persona natural es incorrecto.");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc ="";
                            valor = false;
                    }
                    if (cedula_ruc.length() > 10 && !cedula_ruc.substring(10, longitud).equals("001"))
                    {
                            //FacesUtils.addErrorMessage("El ruc de la persona natural debe terminar con 001");
                            //FacesUtils.addErrorMessage("Cédula/Ruc incorrecto");
                            cedula_ruc ="";
                            valor = false;
                    }
            }
        } 
        catch (Exception e) 
        {
                cedula_ruc ="";
                valor = false;
        }
        return valor;
    }
}