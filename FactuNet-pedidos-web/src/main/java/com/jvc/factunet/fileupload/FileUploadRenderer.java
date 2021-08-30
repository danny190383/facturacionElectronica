package com.jvc.factunet.fileupload;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FileUploadRenderer extends org.primefaces.component.fileupload.FileUploadRenderer{
    
    public void decode(FacesContext context, UIComponent component) {
        if (context.getExternalContext().getRequestContentType().toLowerCase().startsWith("multipart/")) {
            super.decode(context, component);
        }
    }
   
}
