package com.corpogas.corpoapp.Entities.Sucursales;

import java.io.Serializable;

public class Application implements Serializable {
    public String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
