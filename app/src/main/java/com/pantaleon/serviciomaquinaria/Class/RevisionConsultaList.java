package com.pantaleon.serviciomaquinaria.Class;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RevisionConsultaList {

    @SerializedName("revision")
    private List<RevisionConsulta> revisionConsultaList =  null ;

    public List<RevisionConsulta> getRevisionConsultaList() {
        return revisionConsultaList;
    }

    public void setRevisionConsultaList(List<RevisionConsulta> revisionConsultaList) {
        this.revisionConsultaList = revisionConsultaList;
    }
}
