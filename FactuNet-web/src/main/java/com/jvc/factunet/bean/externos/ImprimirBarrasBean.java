package com.jvc.factunet.bean.externos;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ImprimirBarrasBean {

    private String barras;
    private List<String> listaFilas;
    private List<String> listaColumnas;
    private Integer filas;
    private Integer columnas;
    
    public ImprimirBarrasBean() {
        this.listaFilas = new ArrayList<>();
        this.listaColumnas = new ArrayList<>();
    }
    
    public void cargarListas(){
        this.listaColumnas.clear();
        this.listaFilas.clear();
        for(int i=0 ; i<filas ; i++  ){
            this.listaFilas.add(barras);
        }
        for(int j=0 ; j<columnas ; j++  ){
            this.listaColumnas.add(barras);
        }
    }
    
    @PostConstruct
    public void init(){
        this.barras = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("barras");
        this.filas = 5;
        this.columnas = 4;
        this.cargarListas();
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public List<String> getListaFilas() {
        return listaFilas;
    }

    public void setListaFilas(List<String> listaFilas) {
        this.listaFilas = listaFilas;
    }

    public List<String> getListaColumnas() {
        return listaColumnas;
    }

    public void setListaColumnas(List<String> listaColumnas) {
        this.listaColumnas = listaColumnas;
    }

    public Integer getFilas() {
        return filas;
    }

    public void setFilas(Integer filas) {
        this.filas = filas;
    }

    public Integer getColumnas() {
        return columnas;
    }

    public void setColumnas(Integer columnas) {
        this.columnas = columnas;
    }
}
