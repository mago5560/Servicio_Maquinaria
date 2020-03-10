package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

public class Servicio {

    int Id;

    @SerializedName("id")
    String IdServicio;
    @SerializedName("descripcion")
    String Descripcion;

    String TotalRegistros;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getIdServicio() {
        return IdServicio;
    }

    public String getDescripcion() {
        return Descripcion;
    }


    public String getTotalRegistros() {
        return TotalRegistros;
    }

    public void setTotalRegistros(String totalRegistros) {
        TotalRegistros = totalRegistros;
    }

    @Override
    public String toString() {
        return  Descripcion + " " + IdServicio ;
    }
}
