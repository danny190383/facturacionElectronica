package com.jvc.factunet.movil.validador;

import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;

@FacesValidator("com.jvc.factunet.movil.validador.ValidarEmailMovil")
public class ValidarEmailMovil implements Validator, ClientValidator{
    
    private Pattern pattern;
  
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  
    public ValidarEmailMovil() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
 
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null || value.equals("")) {
            ((UIInput) component).setValid(true);
            return;
        }
         
        if(!pattern.matcher(value.toString()).matches()) {
            ((UIInput) component).setValid(false);
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", 
                        value + " is not a valid email;"));
        }
        else
        {
            ((UIInput) component).setValid(true);
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "custom.emailValidator";
    }
    
}
