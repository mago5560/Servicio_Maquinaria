package com.pantaleon.serviciomaquinaria.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.pantaleon.serviciomaquinaria.Class.Piloto;
import com.pantaleon.serviciomaquinaria.Class.PilotoList;
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

public class PilotoControler extends Functions {

    Context context;
    ArrayList<Piloto> arrayList;
    ProgressDialog pDialog;
    Globals vars;
    TextView lblNombrePiloto, lblLicencia, lblEdad, lblFechaVenceLiciencia, lblPlaca;
    EditText txtCodPiloto, txtTC;
    DbHandler dbHandler;

    public PilotoControler(Context context) {
        this.context = context;
        init();
    }

    public PilotoControler(Context context, TextView lblNombrePiloto, TextView lblLicencia, TextView lblEdad, TextView lblFechaVenceLiciencia, TextView lblPlaca, EditText txtCodPiloto, EditText txtTC) {
        this.context = context;
        this.lblNombrePiloto = lblNombrePiloto;
        this.lblLicencia = lblLicencia;
        this.lblEdad = lblEdad;
        this.lblFechaVenceLiciencia = lblFechaVenceLiciencia;
        this.lblPlaca = lblPlaca;
        this.txtCodPiloto = txtCodPiloto;
        this.txtTC = txtTC;
        init();
    }

    private void init() {
        arrayList = new ArrayList<>();
        vars = Globals.getInstance();
        dbHandler = new DbHandler(context,null,null,1);
        createRetrofit();
    }


    private void createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getVehiculo() {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<PilotoList> call = postService.selectVehiculo();
        call.enqueue(new Callback<PilotoList>() {
            @Override
            public void onResponse(Call<PilotoList> call, Response<PilotoList> response) {
                PilotoList models = response.body();
                if (models.getPilotoList().size() > 0) {
                    dbHandler.deleteVehiculo();
                    for (Piloto post : models.getPilotoList()) {
                        dbHandler.insertVehiculo(post);
                    }
                    msgToast("Vehiculos actualizados correctamente",context);
                } else {
                    mensaje("No se encontro informacion para el codigo ingresado, favor de verificar", ((Activity) context)).show();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PilotoList> call, Throwable t) {
                mensajeError(t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }



    public void getVehiculo(int Id) {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<PilotoList> call = postService.selectVehiculo(Id);
        call.enqueue(new Callback<PilotoList>() {
            @Override
            public void onResponse(Call<PilotoList> call, Response<PilotoList> response) {
                PilotoList models = response.body();
                if (models.getPilotoList().size() > 0) {
                    Piloto post = models.getPilotoList().get(0);
                    lblPlaca.setText(post.getPlaca());
                    lblNombrePiloto.setText(post.getNombrePiloto());
                    txtCodPiloto.setText(post.getCodPiloto());
                    lblEdad.setText(post.getEdadPiloto());
                    lblLicencia.setText(post.getLicenciaPiloto());
                    lblFechaVenceLiciencia.setText(post.getFechaVenceLicencia());
                    txtTC.setText(post.getTC());
                    txtCodPiloto.requestFocus();
                } else {
                    LimpiarVehiculo();
                    mensaje("No se encontro informacion para el codigo ingresado, favor de verificar", ((Activity) context)).show();
                }


                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PilotoList> call, Throwable t) {
                mensajeError(t.getMessage(), ((Activity) context)).show();
                LimpiarVehiculo();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }


    public void getPiloto() {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<PilotoList> call = postService.selectPiloto();
        call.enqueue(new Callback<PilotoList>() {
            @Override
            public void onResponse(Call<PilotoList> call, Response<PilotoList> response) {
                PilotoList models = response.body();
                if (models.getPilotoList().size() > 0) {
                    dbHandler.deletePiloto();
                    for (Piloto post : models.getPilotoList()) {
                        dbHandler.insertPiloto(post);
                    }
                    msgToast("Pilotos actualizados correctamente",context);
                } else {

                    mensaje("No se encontro informacion para el codigo ingresado, favor de verificar", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<PilotoList> call, Throwable t) {
                mensajeError(t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public void getPiloto(int Id) {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<PilotoList> call = postService.selectPiloto(Id);
        call.enqueue(new Callback<PilotoList>() {
            @Override
            public void onResponse(Call<PilotoList> call, Response<PilotoList> response) {
                PilotoList models = response.body();
                if (models.getPilotoList().size() > 0) {
                    Piloto post = models.getPilotoList().get(0);
                    lblNombrePiloto.setText(post.getNombrePiloto());
                    lblEdad.setText(post.getEdadPiloto());
                    lblLicencia.setText(post.getLicenciaPiloto());
                    lblFechaVenceLiciencia.setText(post.getFechaVenceLicencia());
                    txtTC.requestFocus();
                } else {
                    LimpiarPiloto();
                    mensaje("No se encontro informacion para el codigo ingresado, favor de verificar", ((Activity) context)).show();
                    txtCodPiloto.requestFocus();
                }


                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PilotoList> call, Throwable t) {
                mensajeError(t.getMessage(), ((Activity) context)).show();
                LimpiarPiloto();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }


    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    AsyncBuscar asyncBuscar;
    boolean Vehiculo;
    public void buscarVehiculoPiloto(boolean vehiculo,String codigo) {
        this.Vehiculo = vehiculo;
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute(codigo);
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList list;
            if(Vehiculo){
                list = dbHandler.selectVehiculo(strings[0]);
            }else{
                list = dbHandler.selectPiloto(strings[0]);
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
        if (arrayList.isEmpty()) {
            mensaje("No se encontraron registros, favor de descargar o ingresar la infromacion", ((Activity) context)).show();
            if(this.Vehiculo){
                LimpiarVehiculo();
            }else{
                LimpiarPiloto();
                txtCodPiloto.requestFocus();
            }
        }else {
            Piloto post = ((Piloto) arrayList.get(0));
            if (this.Vehiculo) {
                lblPlaca.setText(post.getPlaca());
                lblNombrePiloto.setText(post.getNombrePiloto());
                txtCodPiloto.setText(post.getCodPiloto());
                lblEdad.setText(post.getEdadPiloto());
                lblLicencia.setText(post.getLicenciaPiloto());
                lblFechaVenceLiciencia.setText(post.getFechaVenceLicencia());
                txtTC.setText(post.getTC());
                txtCodPiloto.requestFocus();
            } else {
                lblNombrePiloto.setText(post.getNombrePiloto());
                lblEdad.setText(post.getEdadPiloto());
                lblLicencia.setText(post.getLicenciaPiloto());
                lblFechaVenceLiciencia.setText(post.getFechaVenceLicencia());
                txtTC.requestFocus();
            }
        }
    }

    // </editor-fold>

    private void LimpiarPiloto() {
        lblNombrePiloto.setText("---");
        lblEdad.setText("---");
        txtCodPiloto.setText("");
        lblLicencia.setText("---");
        lblFechaVenceLiciencia.setText("---");

    }

    private void LimpiarVehiculo() {
        LimpiarPiloto();
        lblPlaca.setText("---");
        txtTC.setText("");
    }

}
