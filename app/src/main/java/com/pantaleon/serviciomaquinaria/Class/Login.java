package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

public class Login {

    int Id;

    @SerializedName("USUARIO")
    String Usuario;
    @SerializedName("PASSWORD")
    String Contraseña;

    String FechaIngreso;

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getFechaIngreso() {
        return FechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        FechaIngreso = fechaIngreso;
    }
}
