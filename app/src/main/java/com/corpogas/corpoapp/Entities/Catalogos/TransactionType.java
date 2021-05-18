package com.corpogas.corpoapp.Entities.Catalogos;

import java.io.Serializable;

public class TransactionType implements Serializable {

    public String Description;

    /// <summary>
    /// Valor usado para mapearlo en la base de datos anterior
    /// </summary>
    public String MappingValue;

    public String getDescription() { return Description; }

    public void setDescription(String description) { Description = description; }

    public String getMappingValue() { return MappingValue; }

    public void setMappingValue(String mappingValue) { MappingValue = mappingValue; }
}
