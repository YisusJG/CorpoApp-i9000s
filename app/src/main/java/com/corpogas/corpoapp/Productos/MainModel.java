package com.corpogas.corpoapp.Productos;

public class MainModel {
    Integer langLogo;
    String langName;
    public MainModel(Integer langLogo, String langName){
        this.langLogo = langLogo;
        this.langName = langName;
    }

    public Integer getlangLogo(){
        return langLogo;
    }

    public String getLangName(){
        return langName;
    }

}
