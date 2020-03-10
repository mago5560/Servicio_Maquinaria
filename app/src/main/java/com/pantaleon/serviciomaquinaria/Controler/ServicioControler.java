package com.pantaleon.serviciomaquinaria.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pantaleon.serviciomaquinaria.Adapter.ServicioAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.ServicioDetalleAdapter;
import com.pantaleon.serviciomaquinaria.Class.Servicio;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalleList;
import com.pantaleon.serviciomaquinaria.Class.ServicioList;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.Customs.Globals;
import com.pantaleon.serviciomaquinaria.Model.DbHandler;
import com.pantaleon.serviciomaquinaria.Model.PantaleonWS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioControler {

    Context context;
    ServicioAdapter mAdapter;
    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Servicio> arrayList;
    ProgressDialog pDialog;

    ServicioDetalleAdapter mDetalleAdapter;
    ArrayList<ServicioDetalle> arrayDetalleList;

    Functions util;
    Globals vars;
    DbHandler dbHandler;

    public ServicioControler(Context context) {
        this.context = context;
        init();
    }

    public ServicioControler(Context context, ServicioAdapter mAdapter, RecyclerView grdDatos, SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.mAdapter = mAdapter;
        this.grdDatos = grdDatos;
        this.refreshLayout = refreshLayout;
        init();
    }

    public ServicioControler(Context context, RecyclerView grdDatos, SwipeRefreshLayout refreshLayout, ServicioDetalleAdapter mDetalleAdapter) {
        this.context = context;
        this.grdDatos = grdDatos;
        this.refreshLayout = refreshLayout;
        this.mDetalleAdapter = mDetalleAdapter;
        init();
    }

    private void init(){
        arrayList  = new ArrayList<>();
        arrayDetalleList = new ArrayList<>();
        util = new Functions();
        vars = Globals.getInstance();
        dbHandler = new DbHandler(context,null,null,1);
        createRetrofit();
    }


    private void createRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getServicios() {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<ServicioList> call = postService.selectServicio();
        call.enqueue(new Callback<ServicioList>() {
            @Override
            public void onResponse(Call<ServicioList> call, Response<ServicioList> response) {
                ServicioList models = response.body();
                dbHandler.deleteServicio();
                for (Servicio post : models.getServicioList()) {
                    dbHandler.insertServicio(post);
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                getServiciosDetalle();
            }

            @Override
            public void onFailure(Call<ServicioList> call, Throwable t) {
                util.mensajeError(t.getMessage(), ((Activity)context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }


    private void getServiciosDetalle() {
        pDialog = ProgressDialog.show(context, "Consultado...", "Obteniendo el detalle.\nEspere....", true, true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<ServicioDetalleList> call = postService.selectServicioDetalle();
        call.enqueue(new Callback<ServicioDetalleList>() {
            @Override
            public void onResponse(Call<ServicioDetalleList> call, Response<ServicioDetalleList> response) {
                ServicioDetalleList models = response.body();
                for (ServicioDetalle post : models.getServicioDetalleList()) {
                    dbHandler.insertServicioDetalle(post);
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                buscar(1,"","");
            }

            @Override
            public void onFailure(Call<ServicioDetalleList> call, Throwable t) {
                util.mensajeError(t.getMessage(), ((Activity)context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }


    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    AsyncBuscar asyncBuscar;
    int TipoBusqueda;
    public void buscar(int TipoBusqueda,String IdServicio,String IdRevision) {
        this.TipoBusqueda = TipoBusqueda;
        grdDatos.setScrollingTouchSlop(0);
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute(IdServicio,IdRevision);
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList list;
            switch (TipoBusqueda){
                case 2://Detalle Servicios
                    list = dbHandler.selectServicioDetalle(strings[0],strings[1]);
                    break;
                default: //Servicios
                    list = dbHandler.selectServicio();
                    break;
            }

            return list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            viewConsulta(arrayList);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private void viewConsulta(ArrayList arrayList) {
        switch (TipoBusqueda){
            case 2://Detalle Servicios
                mDetalleAdapter.setInfo(arrayList);
                grdDatos.setAdapter(mDetalleAdapter);
                break;
            default: //Servicios
                mAdapter.setInfo(arrayList);
                grdDatos.setAdapter(mAdapter);
                break;
        }

        refreshLayout.setRefreshing(false);
        if (arrayList.isEmpty()) {
            util.mensaje("No se encontraron registros, favor de descargar o ingresar la infromacion", ((Activity) context)).show();
        }
    }

    // </editor-fold>


}
