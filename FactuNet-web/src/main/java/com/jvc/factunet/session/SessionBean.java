package com.jvc.factunet.session;

import com.jvc.factunet.entidades.Cuenta;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionBean {

    public static HttpSession getSession() {
            return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
            return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static Cuenta getUser() {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            return (Cuenta)session.getAttribute("cuenta");
    }
}
