package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

public class Piloto {

    int Id;



    @SerializedName("nombre_piloto")
    String NombrePiloto;
    @SerializedName("edad_piloto")
    String EdadPiloto;
    @SerializedName("licencia_piloto")
    String LicenciaPiloto;
    @SerializedName("fecha_vence_licencia")
    String FechaVenceLicencia;
    @SerializedName("cod_piloto")
    String CodPiloto;

    @SerializedName("no_placa")
    String Placa;
    @SerializedName("cod_prove")
    String CodProveedor;
    @SerializedName("nom_prove")
    String NombreProveedor;
    @SerializedName("vehiculo")
    String Vehiculo;
    @SerializedName("propietario")
    String Propietario;
    @SerializedName("modelo")
    String Modelo;
    @SerializedName("tc")
    String TC;
    @SerializedName("tipo_equipo")
    String TipoEquipo;
    @SerializedName("des_tipo_equipo")
    String DescripcionTipoEquipo;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombrePiloto() {
        return NombrePiloto;
    }

    public void setNombrePiloto(String nombrePiloto) {
        NombrePiloto = nombrePiloto;
    }

    public String getEdadPiloto() {
        return EdadPiloto;
    }

    public void setEdadPiloto(String edadPiloto) {
        EdadPiloto = edadPiloto;
    }

    public String getLicenciaPiloto() {
        return LicenciaPiloto;
    }

    public void setLicenciaPiloto(String licenciaPiloto) {
        LicenciaPiloto = licenciaPiloto;
    }

    public String getFechaVenceLicencia() {
        return FechaVenceLicencia;
    }

    public void setFechaVenceLicencia(String fechaVenceLicencia) {
        FechaVenceLicencia = fechaVenceLicencia;
    }

    public String getCodPiloto() {
        return CodPiloto;
    }

    public void setCodPiloto(String codPiloto) {
        CodPiloto = codPiloto;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public String getCodProveedor() {
        return CodProveedor;
    }

    public void setCodProveedor(String codProveedor) {
        CodProveedor = codProveedor;
    }

    public String getNombreProveedor() {
        return NombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        NombreProveedor = nombreProveedor;
    }

    public String getVehiculo() {
        return Vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        Vehiculo = vehiculo;
    }

    public String getPropietario() {
        return Propietario;
    }

    public void setPropietario(String propietario) {
        Propietario = propietario;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getTipoEquipo() {
        return TipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        TipoEquipo = tipoEquipo;
    }

    public String getDescripcionTipoEquipo() {
        return DescripcionTipoEquipo;
    }

    public void setDescripcionTipoEquipo(String descripcionTipoEquipo) {
        DescripcionTipoEquipo = descripcionTipoEquipo;
    }

    @Override
    public String toString() {
        return CodPiloto + " " + NombrePiloto + " " + Vehiculo;
    }
}
