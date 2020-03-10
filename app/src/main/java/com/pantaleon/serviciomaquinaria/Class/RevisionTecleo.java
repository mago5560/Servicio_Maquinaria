package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

public class RevisionTecleo {

    @SerializedName("cod_boleta_revision")
    int Id;
    @SerializedName("cod_vehiculo")
    String IdVehiculo;
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
    @SerializedName("fecha_revision")
    String Fecha;
    @SerializedName("tc")
    String TC;
    @SerializedName("usuario")
    String Usuario;

    String Placa;

    int Transferido;

    String IdentificadorDB;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        IdVehiculo = idVehiculo;
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

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public int getTransferido() {
        return Transferido;
    }

    public void setTransferido(int transferido) {
        Transferido = transferido;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public String getIdentificadorDB() {
        return IdentificadorDB;
    }

    public void setIdentificadorDB(String identificadorDB) {
        IdentificadorDB = identificadorDB;
    }

    @Override
    public String toString() {
        return  NombrePiloto+" " + IdVehiculo  ;
    }
}
