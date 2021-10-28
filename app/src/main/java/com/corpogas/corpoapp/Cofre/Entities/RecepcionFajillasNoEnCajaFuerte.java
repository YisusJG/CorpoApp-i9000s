package com.corpogas.corpoapp.Cofre.Entities;

import java.io.Serializable;
import java.util.List;

public class RecepcionFajillasNoEnCajaFuerte<T> implements Serializable {

    public String Responsable;
    public List<TotalFajillaCajaFuerte> FajillasNoEntregadas;
}
