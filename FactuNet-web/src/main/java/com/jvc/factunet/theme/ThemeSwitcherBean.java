package com.jvc.factunet.theme;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "themeSwitcherBean")
@SessionScoped
public class ThemeSwitcherBean implements Serializable{
    
    private static final long serialVersionUID = 7448888248791054139L;
    private String theme= "home";
    
    public ThemeSwitcherBean() {
    }
    
    public String getTheme() {
        return theme;
    }   

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
