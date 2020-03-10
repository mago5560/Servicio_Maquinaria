package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

public class ServicioDetalle {

    int Id;
    int IdRevisionDetalle;
    int IdRevision;

    @SerializedName("c1")
    String IdServicio;//CodCategoriaRevision
    @SerializedName("c2")
    String CodRevision;
    @SerializedName("c3")
    String DescripiconRevision;
    @SerializedName("c4")
    String PunteoSistema;
    @SerializedName("c5")
    String PunteoReal;
    @SerializedName("c6")
    String Ponderacion;

    String Observaciones;




    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }

    public String getCodRevision() {
        return CodRevision;
    }

    public void setCodRevision(String codRevision) {
        CodRevision = codRevision;
    }

    public String getDescripiconRevision() {
        return DescripiconRevision;
    }

    public void setDescripiconRevision(String descripiconRevision) {
        DescripiconRevision = descripiconRevision;
    }

    public String getPunteoSistema() {
        return PunteoSistema;
    }

    public void setPunteoSistema(String punteoSistema) {
        PunteoSistema = punteoSistema;
    }

    public String getPunteoReal() {
        return PunteoReal;
    }

    public void setPunteoReal(String punteoReal) {
        PunteoReal = punteoReal;
    }

    public String getPonderacion() {
        return Ponderacion;
    }

    public void setPonderacion(String ponderacion) {
        Ponderacion = ponderacion;
    }

    public int getIdRevisionDetalle() {
        return IdRevisionDetalle;
    }

    public void setIdRevisionDetalle(int idRevisionDetalle) {
        IdRevisionDetalle = idRevisionDetalle;
    }

    public int getIdRevision() {
        return IdRevision;
    }

    public void setIdRevision(int idRevision) {
        IdRevision = idRevision;
    }


    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    @Override
    public String toString() {
        return DescripiconRevision + " " + CodRevision ;
    }


}
