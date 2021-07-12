package com.corpogas.corpoapp.Entities.Cortes;

public class MenuCorte {
    private int image;
    private String text;

    public MenuCorte(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }
    public String getText() {
        return text;
    }
}
