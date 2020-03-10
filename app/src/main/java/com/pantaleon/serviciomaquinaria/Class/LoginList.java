package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginList {

    @SerializedName("seguridad")
    private List<Login> loginList =  null ;

    public List<Login> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<Login> loginList) {
        this.loginList = loginList;
    }
}
