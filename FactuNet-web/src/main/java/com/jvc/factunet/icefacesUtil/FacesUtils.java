package com.jvc.factunet.icefacesUtil;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * JSF utilities.
 */
public class FacesUtils {

    
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }

    public static Object getManagedBean(String beanName) {
        return getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());
    }

    public static void resetManagedBean(String beanName) {
        getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
    }

    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static Object getRequestParameterObjeto(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    public static void addWarningMessage(String msg) {
        addWarnigMessage(null, msg);
    }

    public static void addErrorMessage(Exception ex) {
        addErrorMessage(null, "Mensaje: Informe al Administrador del Sistema.");
        ex.printStackTrace();
    }

    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    public static void addWarnigMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    public static void AddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    private static Application getApplication() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        return appFactory.getApplication();
    }

    private static ValueBinding getValueBinding(String el) {
        return getApplication().createValueBinding(el);
    }

    public static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    private static Object getElValue(String el) {
        return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }

    public static String usuarioLogueado() {

        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getUserPrincipal().getName();
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(".", "--Seleccione--");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("com.jvc.factunet.recursos.messages");
    }

    public static void redireccionar(final String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    getServletRequest().getContextPath() + url);
        } catch (IOException ex) {
            Logger.getLogger(FacesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
