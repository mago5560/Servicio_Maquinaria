package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicioList {

    @SerializedName("per")
    private List<Servicio> servicioList =  null ;

    public List<Servicio> getServicioList() {
        return servicioList;
    }

}
