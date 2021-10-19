package com.corpogas.corpoapp.Entregas.Entities;

import java.io.Serializable;

public class PaperVoucherType implements Serializable {
    public int image;
    public String Description;
    public long Id;

    public PaperVoucherType(String description, long id) {
        this.Description = description;
        this.Id = id;
    }

    public PaperVoucherType(int image, String description, long id) {
        this.image = image;
        this.Description = description;
        this.Id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
