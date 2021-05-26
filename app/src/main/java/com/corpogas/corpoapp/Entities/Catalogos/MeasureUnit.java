package com.corpogas.corpoapp.Entities.Catalogos;

import java.io.Serializable;

public class MeasureUnit implements Serializable {

    public String Code;
    public String Description;

    public String getCode() { return Code; }

    public void setCode(String code) { Code = code; }

    public String getDescription() { return Description; }

    public void setDescription(String description) { Description = description; }
}
