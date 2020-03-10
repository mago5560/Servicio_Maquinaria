package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicioDetalleList {
    @SerializedName("pdr")
    private List<ServicioDetalle> servicioDetalleList =  null ;

    public List<ServicioDetalle> getServicioDetalleList() {
        return servicioDetalleList;
    }

    public void setServicioDetalleList(List<ServicioDetalle> servicioDetalleList) {
        this.servicioDetalleList = servicioDetalleList;
    }
}
