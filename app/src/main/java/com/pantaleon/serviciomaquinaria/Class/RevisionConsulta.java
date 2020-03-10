package com.pantaleon.serviciomaquinaria.Class;
import com.google.gson.annotations.SerializedName;

public class RevisionConsulta {

    @SerializedName("fecha_revision")
    String FechaRevision;
    @SerializedName("descripcion")
    String Descripcion;
    @SerializedName("punteo_sistema")
    String PunteoSistema;
    @SerializedName("punteo_real")
    String PunteoReal;
    @SerializedName("ponderacion")
    String Ponderacion;
    @SerializedName("estado")
    String Estado;
    @SerializedName("cod_registro")
    String CodRegistro;
    @SerializedName("cod_revision")
    String CodRevision;
    @SerializedName("Observacion")
    String Observacion;

    public String getFechaRevision() {
        return FechaRevision;
    }


    public String getDescripcion() {
        return Descripcion;
    }


    public String getPunteoSistema() {
        return PunteoSistema;
    }


    public String getPunteoReal() {
        return PunteoReal;
    }


    public String getPonderacion() {
        return Ponderacion;
    }


    public String getEstado() {
        return Estado;
    }


    public String getCodRegistro() {
        return CodRegistro;
    }


    public String getObservacion() {
        return Observacion;
    }


    public String getCodRevision() {
        return CodRevision;
    }

}
