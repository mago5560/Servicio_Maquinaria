package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PilotoList {

    @SerializedName("vehiculo1")
    private List<Piloto> pilotoList =  null ;

    public List<Piloto> getPilotoList() {
        return pilotoList;
    }

    public void setPilotoList(List<Piloto> pilotoList) {
        this.pilotoList = pilotoList;
    }
}
