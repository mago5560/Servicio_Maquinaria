package com.pantaleon.serviciomaquinaria.Model;
import com.pantaleon.serviciomaquinaria.Class.LoginList;
import com.pantaleon.serviciomaquinaria.Class.PilotoList;
import com.pantaleon.serviciomaquinaria.Class.RevisionConsultaList;
import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalleList;
import com.pantaleon.serviciomaquinaria.Class.ServicioList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PantaleonWS {

    String API_ROUTE_SEGURIDAD="seguridad";
    String API_ROUTE_REVISION="revision";
    String API_ROUTE_PILOTO="piloto";
    String API_ROUTE_VEHICULO="vehiculo";
    String API_ROUTE_SERVICIO="per";
    String API_ROUTE_SERVICIO_DETALLE="pdr";
    String API_ROUTE_INSERT_ENCABEZADO = "creaencabezado";
    String API_ROUTE_INSERT_DETALLE = "creadetalle";

    @GET(API_ROUTE_SEGURIDAD)
    Call<LoginList> selectSeguridad();

    @GET(API_ROUTE_REVISION + "/{id}")
    Call<RevisionConsultaList> selectRevision(@Path("id") int Id);

    @GET(API_ROUTE_PILOTO + "/{id}")
    Call<PilotoList> selectPiloto(@Path("id") int Id);

    @GET(API_ROUTE_PILOTO)
    Call<PilotoList> selectPiloto();

    @GET(API_ROUTE_VEHICULO + "/{id}")
    Call<PilotoList> selectVehiculo(@Path("id") int Id);

    @GET(API_ROUTE_VEHICULO )
    Call<PilotoList> selectVehiculo();

    @GET(API_ROUTE_SERVICIO)
    Call<ServicioList> selectServicio();

    @GET(API_ROUTE_SERVICIO_DETALLE)
    Call<ServicioDetalleList> selectServicioDetalle();

    @POST(API_ROUTE_INSERT_ENCABEZADO)
    Call<RevisionTecleo> insertEncabezado(@Body RevisionTecleo revisionTecleo);

    @POST(API_ROUTE_INSERT_DETALLE)
    Call<ServicioDetalleList> insertDetalle(@Body ServicioDetalleList servicioDetalleList);

}
