package com.pantaleon.serviciomaquinaria.Customs;

public class Globals {

    private static Globals instance;

    private Globals() {
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }


    private String WS_URL= "http://www.cybersoftgt.com/logistik/logistik/services/";

    private String INFO_FILE = "";

    private String LINK_APP = "https://dl.dropbox.com/s/qoo0tyaqeili9fh/ServicioMaquinaria.apk";

    private String NameApi = "ServicioMaquinaria.apk";
    private String Version = "";
    private String PATH_FILE_API = "/ServicioMaquinaria";

    public String getINFO_FILE() {
        return INFO_FILE;
    }

    public String getLINK_APP() {
        return LINK_APP;
    }

    public String getNameApi() {
        return NameApi;
    }


    public String getVersion() {
        return Version;
    }

    public String getPATH_FILE_API() {
        return PATH_FILE_API;
    }

    public String getWS_URL() {
        return WS_URL;
    }
}
